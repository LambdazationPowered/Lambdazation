package org.lambdazation.common.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lambdazation.common.util.GeneralizedBuilder;
import org.lamcalcj.ast.Lambda.Abs;
import org.lamcalcj.ast.Lambda.App;
import org.lamcalcj.ast.Lambda.Identifier;
import org.lamcalcj.ast.Lambda.Term;
import org.lamcalcj.ast.Lambda.Var;
import org.lamcalcj.compiler.Compiler;
import org.lamcalcj.parser.Text;
import org.lamcalcj.parser.Text$;
import org.lamcalcj.reducer.BetaReducer;
import org.lamcalcj.reducer.EtaConverter;
import org.lamcalcj.reducer.Result;
import org.lamcalcj.reducer.Strategy;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

public final class LambdazationTermFactory {
	public final Lambdazation lambdazation;

	public final TermCache termCache;
	public final PredefTerm predefTermId;
	public final PredefTerm predefTermFix;

	public LambdazationTermFactory(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		this.termCache = new TermCache();
		this.predefTermId = PredefTerm
			.builder()
			.name("id")
			.term(parseTerm("λx.x", true).get())
			.build();
		this.predefTermFix = PredefTerm
			.builder()
			.name("fix")
			.term(parseTerm("λf.(λx.f (x x)) (λx.f (x x))", true).get())
			.build();
	}

	public TermMetadata serializeTerm(Term term, DataOutput output) throws IOException {
		int termSize = term.size();
		int termDepth = term.depth();
		TermState termState = TermState.BETA_ETA_NORMAL_FORM;
		int termHash = 1;

		Deque<Term> pendingTerms = new ArrayDeque<>();
		Object2IntMap<Identifier> identifierMap = new Object2IntOpenHashMap<>();
		TermNaming.Builder termNamingBuilder = TermNaming.builder();
		boolean pendingBetaRedex = false;
		boolean pendingEtaRedex = false;
		Identifier pendingEtaRedexIdentifier = null;
		Object2BooleanMap<Identifier> potentialEtaRedexIdentifiers = new Object2BooleanOpenHashMap<>();

		pendingTerms.add(term);

		while (!pendingTerms.isEmpty()) {
			Term currentTerm = pendingTerms.pop();
			if (currentTerm instanceof Var) {
				Identifier varIdentifier = ((Var) currentTerm).identifier();

				output.writeByte(0);
				termHash = 31 * termHash + 0;
				termHash = 31 * termHash + serializeIdentifier(varIdentifier, output, identifierMap, termNamingBuilder);

				if (!termState.equals(TermState.REDUCIBLE_FORM)) {
					boolean identifierState = potentialEtaRedexIdentifiers.getBoolean(varIdentifier);
					if (identifierState)
						potentialEtaRedexIdentifiers.put(varIdentifier, false);
					else
						potentialEtaRedexIdentifiers.removeBoolean(varIdentifier);
					pendingBetaRedex = false;
					pendingEtaRedex = false;
					pendingEtaRedexIdentifier = null;
				}
			} else if (currentTerm instanceof Abs) {
				Identifier absBinding = ((Abs) currentTerm).binding();
				Term absTerm = ((Abs) currentTerm).term();

				output.writeByte(1);
				termHash = 31 * termHash + 1;
				termHash = 31 * termHash + serializeIdentifier(absBinding, output, identifierMap, termNamingBuilder);
				pendingTerms.push(absTerm);

				if (!termState.equals(TermState.REDUCIBLE_FORM)) {
					if (pendingBetaRedex) {
						termState = TermState.REDUCIBLE_FORM;
						pendingBetaRedex = false;
						potentialEtaRedexIdentifiers = null;
					} else if (termState.equals(TermState.BETA_ETA_NORMAL_FORM)) {
						pendingEtaRedex = true;
						pendingEtaRedexIdentifier = absBinding;
					}
				}
			} else if (currentTerm instanceof App) {
				Term appTerm = ((App) currentTerm).term();
				Term appArgument = ((App) currentTerm).argument();

				output.writeByte(2);
				termHash = 31 * termHash + 2;
				pendingTerms.push(appArgument);
				pendingTerms.push(appTerm);

				if (!termState.equals(TermState.REDUCIBLE_FORM)) {
					if (pendingEtaRedex) {
						if (appArgument instanceof Var) {
							Identifier varIdentifier = ((Var) appArgument).identifier();

							if (pendingEtaRedexIdentifier.equals(varIdentifier))
								potentialEtaRedexIdentifiers.put(varIdentifier, true);
						}
						pendingEtaRedex = false;
						pendingEtaRedexIdentifier = null;
					}
					pendingBetaRedex = true;
				}
			} else
				throw new IllegalStateException();
		}

		if (termState.equals(TermState.BETA_ETA_NORMAL_FORM) && !potentialEtaRedexIdentifiers.isEmpty())
			termState = TermState.BETA_NORMAL_FORM;

		TermStatistics termStatistics = new TermStatistics(termSize, termDepth, termState, termHash);
		TermNaming termNaming = termNamingBuilder.build();
		return new TermMetadata(termNaming, termStatistics);
	}

	private int serializeIdentifier(Identifier identifier, DataOutput output, Object2IntMap<Identifier> identifierMap,
		TermNaming.Builder termNamingBuilder) throws IOException {
		int serialId;
		if (identifierMap.containsKey(identifier))
			serialId = identifierMap.getInt(identifier);
		else
			identifierMap.put(identifier, serialId = termNamingBuilder.putIdentifier(identifier.name()));
		output.writeInt(serialId);
		return serialId;
	}

	public Term deserializeTerm(TermNamer termNamer, DataInput input) throws IOException {
		TermBuilder termBuilder = new TermBuilder();
		Int2ObjectMap<Identifier> identifierMap = new Int2ObjectOpenHashMap<>();

		while (!termBuilder.isCompleted()) {
			byte termType = input.readByte();
			switch (termType) {
			case 0:
				Identifier varIdentifier = deserializeIdentifier(termNamer, input, identifierMap);
				termBuilder.pushVar(varIdentifier);
				break;
			case 1:
				Identifier absBinding = deserializeIdentifier(termNamer, input, identifierMap);
				termBuilder.pushAbs(absBinding);
				break;
			case 2:
				termBuilder.pushApp();
				break;
			default:
				throw new IOException("Invalid term type");
			}
		}

		return termBuilder.buildTerm();
	}

	private Identifier deserializeIdentifier(TermNamer termNamer, DataInput input,
		Int2ObjectMap<Identifier> identifierMap) throws IOException {
		int serialId = input.readInt();
		if (!termNamer.isValidSerialId(serialId))
			throw new IOException("Invalid serial id");
		Identifier identifier = identifierMap.get(serialId);
		if (identifier == null) {
			String identifierName = termNamer.identifierName(serialId);
			identifier = new Identifier(identifierName);
			identifierMap.put(serialId, identifier);
		}
		return identifier;
	}

	public Optional<Term> parseTerm(String source, boolean requireClosedTerm) {
		Text text = Text$.MODULE$.apply(source, Text.apply$default$2());

		Optional<Term> resultTerm = Compiler.runLambdaParser(text, Compiler.runLambdaParser$default$2(),
			Compiler.runLambdaParser$default$3(), Compiler.runLambdaParser$default$4())
			.fold(parserError -> Optional.empty(),
				parserResult -> requireClosedTerm && parserResult._1.nonEmpty() ? Optional.empty()
					: Optional.of(parserResult._2));

		return resultTerm;
	}

	public static final class PredefTerm {
		public final String name;
		public final Term term;

		public PredefTerm(String name, Term term) {
			this.name = name;
			this.term = term;
		}

		public Term applyTerm(Term argumentTerm) {
			return new App(term, argumentTerm);
		}

		public Term acceptTerm(Term functionTerm) {
			return new App(functionTerm, term);
		}

		public ItemLambdaCrystal.Builder withCrystal(ItemLambdaCrystal.Builder builder) {
			return builder.term(term);
		}

		public static Builder builder() {
			return new Builder();
		}

		public static final class Builder implements GeneralizedBuilder<Builder, PredefTerm> {
			private String name;
			private Term term;

			Builder() {

			}

			public Builder name(String name) {
				this.name = name;
				return this;
			}

			public Builder term(Term term) {
				this.term = term;
				return this;
			}

			private void validateState() {
				if (name == null || term == null)
					throw new IllegalStateException("Property uninitialized");
			}

			@Override
			public Builder concrete() {
				return this;
			}

			@Override
			public PredefTerm build() {
				validateState();

				return new PredefTerm(name, term);
			}
		}
	}

	public enum TermState {
		REDUCIBLE_FORM("ReducibleForm"), BETA_NORMAL_FORM("BetaNormalForm"), BETA_ETA_NORMAL_FORM("BetaEtaNormalForm");

		private final String displayName;

		TermState(String displayName) {
			this.displayName = displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}

	public interface TermNamer {
		static TermNamer EMPTY_NAMER = new TermNamer() {
			@Override
			public boolean isValidSerialId(int serialId) {
				return serialId >= 0;
			}

			@Override
			public String identifierName(int serialId) {
				if (!isValidSerialId(serialId))
					throw new IndexOutOfBoundsException();
				return "";
			}
		};

		static TermNamer ALPHABET_NAMER = new TermNamer() {
			@Override
			public boolean isValidSerialId(int serialId) {
				return serialId >= 0;
			}

			@Override
			public String identifierName(int serialId) {
				if (!isValidSerialId(serialId))
					throw new IndexOutOfBoundsException();
				StringBuilder builder = new StringBuilder();
				builder.append((char) ('a' + serialId % 26));
				if (serialId >= 26)
					builder.append(serialId / 26 - 1);
				return builder.toString();
			}
		};

		boolean isValidSerialId(int serialId);

		String identifierName(int serialId);
	}

	public static final class TermNaming implements TermNamer {
		private final String[] identifierNames;

		TermNaming(String[] identifierNames) {
			this.identifierNames = identifierNames;
		}

		@Override
		public boolean isValidSerialId(int serialId) {
			return serialId >= 0 && serialId < identifierNames.length;
		}

		@Override
		public String identifierName(int serialId) {
			if (!isValidSerialId(serialId))
				throw new IndexOutOfBoundsException();
			return identifierNames[serialId];
		}

		public void serialize(DataOutput output) throws IOException {
			int identifierCount = identifierNames.length;
			output.writeInt(identifierCount);
			for (String identifierName : identifierNames)
				output.writeUTF(identifierName);
		}

		public static TermNaming deserialize(DataInput input) throws IOException {
			int identifierCount = input.readInt();
			if (identifierCount < 0)
				throw new IOException("Invalid identifier count");
			String[] identifierNames = new String[identifierCount];
			for (int i = 0; i < identifierNames.length; i++)
				identifierNames[i] = input.readUTF();
			return new TermNaming(identifierNames);
		}

		public static Builder builder() {
			return new Builder();
		}

		public static final class Builder implements GeneralizedBuilder<Builder, TermNaming> {
			private final List<String> termNaming;

			public Builder() {
				termNaming = new ArrayList<>();
			}

			public int putIdentifier(String identifierName) {
				int serialId = termNaming.size();
				termNaming.add(identifierName);
				return serialId;
			}

			@Override
			public Builder concrete() {
				return this;
			}

			@Override
			public TermNaming build() {
				return new TermNaming(termNaming.toArray(new String[termNaming.size()]));
			}
		}
	}

	public static final class TermStatistics {
		public final int termSize;
		public final int termDepth;
		public final TermState termState;
		public final int termHash;

		public TermStatistics(int termSize, int termDepth, TermState termState, int termHash) {
			this.termState = termState;
			this.termSize = termSize;
			this.termDepth = termDepth;
			this.termHash = termHash;
		}

		@Override
		public String toString() {
			return "[termSize: " + termSize + ", termDepth: " + termDepth + ", termState:" + termState + ", termHash: " + termHash + "]";
		}
	}

	public static final class TermMetadata {
		public final TermNaming termNaming;
		public final TermStatistics termStatistics;

		public TermMetadata(TermNaming termNaming, TermStatistics termStatistics) {
			this.termNaming = termNaming;
			this.termStatistics = termStatistics;
		}
	}

	public static class TermRef {
		public final byte[] serializedTerm;
		public final int termSize;
		public final int termDepth;
		public final TermState termState;
		public final int termHash;

		public TermRef(byte[] serializedTerm, int termSize, int termDepth, TermState termState, int termHash) {
			this.serializedTerm = serializedTerm;
			this.termSize = termSize;
			this.termDepth = termDepth;
			this.termState = termState;
			this.termHash = termHash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (!(obj instanceof TermRef))
				return false;
			if (termHash != ((TermRef) obj).termHash)
				return false;
			byte[] firstSerializedTerm = serializedTerm;
			byte[] secondSerializedTerm = ((TermRef) obj).serializedTerm;
			return Arrays.equals(firstSerializedTerm, secondSerializedTerm);
		}

		@Override
		public int hashCode() {
			return termHash;
		}
	}

	public final class TermCache {
		final TermPool primaryTermPool;
		final TermPool secondaryTermPool;

		TermCache() {
			primaryTermPool = new TermPool(null, 0xFFFFFF, 0x7FFFFF, 20 * 60 * 10, -1);
			secondaryTermPool = new TermPool(primaryTermPool, 0xFFFF, 0x7FFF, 20 * 10, 20 * 60);
		}

		public void purgeCache(boolean force, int time) {
			primaryTermPool.purgePool(force, time);
			secondaryTermPool.purgePool(force, time);
		}

		public TermRef reduceTerm(TermRef termRef, int maxStep, int maxSize, int time) {
			Entry entry = secondaryTermPool.acquireEntry(termRef, time);
			int currentStep = 0;
			while (currentStep < maxStep && entry.termSize <= maxSize) {
				if (entry.nextEntry == null)
					break;
				if (currentStep + entry.nextEntryStep > maxStep)
					break;
				if (entry.nextEntrySizePeak > maxSize)
					break;
				currentStep += entry.nextEntryStep;
				entry = entry.nextEntry;
			}
			if (currentStep >= maxStep || entry.termState.equals(TermState.BETA_ETA_NORMAL_FORM))
				return entry;
			return reduceEntry(entry, maxStep - currentStep, maxSize, time);
		}

		TermRef reduceEntry(Entry entry, int maxStep, int maxSize, int time) {
			Term term;
			try (DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(entry.serializedTerm))) {
				term = deserializeTerm(TermNamer.EMPTY_NAMER, dataInput);
			} catch (IOException e) {
				return entry;
			}

			boolean successful = true;
			int currentStep = 0;
			int sizePeak = 0;
			if (successful && currentStep <= maxStep && term.size() <= maxSize) {
				Result result = BetaReducer.betaReduction(term, new Strategy(scala.Option.apply(maxStep - currentStep),
					scala.Option.apply(maxSize), scala.Option.empty(), false, false));
				successful = result.abortReason().successful();
				currentStep += result.step();
				sizePeak = Math.max(sizePeak, result.sizePeak());
				term = result.term();
			}
			if (successful && currentStep <= maxStep && term.size() <= maxSize) {
				Result result = EtaConverter.etaConversion(term, new Strategy(scala.Option.apply(maxStep - currentStep),
					scala.Option.apply(maxSize), scala.Option.empty(), false, false));
				successful = result.abortReason().successful();
				currentStep += result.step();
				sizePeak = Math.max(sizePeak, result.sizePeak());
				term = result.term();
			}
			if (currentStep <= 0)
				return entry;

			TermMetadata termMetadata;
			ByteArrayOutputStream termOutput;
			try (DataOutputStream dataOutput = new DataOutputStream(termOutput = new ByteArrayOutputStream())) {
				termMetadata = serializeTerm(term, dataOutput);
			} catch (IOException e) {
				return entry;
			}
			byte[] serializedTerm = termOutput.toByteArray();
			TermRef termRef = new TermRef(serializedTerm, termMetadata.termStatistics.termSize,
				termMetadata.termStatistics.termDepth, termMetadata.termStatistics.termState,
				termMetadata.termStatistics.termHash);

			Entry reducedEntry = secondaryTermPool.acquireEntry(termRef, time);

			entry.setNextEntry(reducedEntry, currentStep, sizePeak);
			return reducedEntry;
		}

		final class TermPool {
			final TermPool precedingPool;
			int hardTermSizeLimit;
			int softTermSizeLimit;
			int entryExpireTime;
			int entryPromoteTime;
			final ObjectLinkedOpenHashSet<Entry> entries;
			int termSize;

			TermPool(TermPool precedingPool, int hardTermSizeLimit, int softTermSizeLimit, int entryExpireTime,
				int entryPromoteTime) {
				this.precedingPool = precedingPool;
				this.hardTermSizeLimit = hardTermSizeLimit;
				this.softTermSizeLimit = softTermSizeLimit;
				this.entryExpireTime = entryExpireTime;
				this.entryPromoteTime = entryPromoteTime;
				this.entries = new ObjectLinkedOpenHashSet<>();
				this.termSize = 0;
			}

			void purgePool(boolean force, int time) {
				if (force || hardTermSizeLimit >= 0 && termSize > hardTermSizeLimit) {
					ObjectListIterator<Entry> iterator = entries.iterator();
					if (softTermSizeLimit >= 0) {
						while (iterator.hasNext() && termSize > softTermSizeLimit) {
							Entry entry = iterator.next();
							termSize -= entry.termSize;
							entry.removeEntry();
							iterator.remove();
						}
					}
					if (entryExpireTime >= 0) {
						while (iterator.hasNext()) {
							Entry entry = iterator.next();
							if (time - entry.accessTime < entryExpireTime)
								break;
							termSize -= entry.termSize;
							entry.removeEntry();
							iterator.remove();
						}
					}
				}
			}

			Entry lookupEntry(TermRef termRef, int time) {
				Entry entry = entries.get(termRef);
				if (entry != null) {
					entry.accessTime = time;
					if (precedingPool != null && entryPromoteTime >= 0 && time - entry.cachedTime >= entryPromoteTime) {
						termSize -= entry.termSize;
						entries.remove(entry);
						precedingPool.termSize += entry.termSize;
						precedingPool.entries.add(entry);
					} else
						entries.addAndMoveToLast(entry);
				} else if (precedingPool != null)
					entry = precedingPool.lookupEntry(termRef, time);
				return entry;
			}

			Entry acquireEntry(TermRef termRef, int time) {
				Entry entry = lookupEntry(termRef, time);
				if (entry == null) {
					entry = new Entry(termRef, time);
					termSize += entry.termSize;
					entries.add(entry);
				}
				return entry;
			}
		}

		final class Entry extends TermRef {
			final int cachedTime;
			int accessTime;
			Entry prevEntry;
			Entry nextEntry;
			int nextEntryStep;
			int nextEntrySizePeak;

			Entry(TermRef termRef, int time) {
				this(termRef.serializedTerm, termRef.termSize, termRef.termDepth, termRef.termState, termRef.termHash,
					time);
			}

			Entry(byte[] serializedTerm, int termSize, int termDepth, TermState termState, int termHash, int time) {
				super(serializedTerm, termSize, termDepth, termState, termHash);

				this.cachedTime = time;
				this.accessTime = time;
				this.prevEntry = null;
				this.nextEntry = null;
				this.nextEntryStep = -1;
				this.nextEntrySizePeak = -1;
			}

			void setNextEntry(Entry nextEntry, int nextEntryStep, int nextEntrySizePeak) {
				this.removeNextEntry();
				nextEntry.removePrevEntry();

				this.nextEntry = nextEntry;
				this.nextEntryStep = nextEntryStep;
				this.nextEntrySizePeak = nextEntrySizePeak;
				nextEntry.prevEntry = this;
			}

			void removeNextEntry() {
				if (this.nextEntry != null) {
					this.nextEntry.prevEntry = null;
					this.nextEntry = null;
					this.nextEntryStep = -1;
					this.nextEntrySizePeak = -1;
				}
			}

			void removePrevEntry() {
				if (this.prevEntry != null) {
					this.prevEntry.nextEntry = null;
					this.prevEntry.nextEntryStep = -1;
					this.prevEntry.nextEntrySizePeak = -1;
					this.prevEntry = null;
				}
			}

			void removeEntry() {
				if (this.prevEntry != null) {
					if (this.nextEntry != null) {
						this.prevEntry.nextEntry = this.nextEntry;
						this.prevEntry.nextEntryStep = this.prevEntry.nextEntryStep + this.nextEntryStep;
						this.prevEntry.nextEntrySizePeak = Math.max(this.prevEntry.nextEntrySizePeak,
							this.nextEntrySizePeak);
						this.nextEntry.prevEntry = this.prevEntry;
					} else {
						this.prevEntry.nextEntry = null;
						this.prevEntry.nextEntryStep = -1;
						this.prevEntry.nextEntrySizePeak = -1;
					}
				} else if (this.nextEntry != null) {
					this.nextEntry.prevEntry = null;
				}
				this.prevEntry = null;
				this.nextEntry = null;
				this.nextEntryStep = -1;
				this.nextEntrySizePeak = -1;
			}
		}
	}

	private static final class TermBuilder {
		private final Deque<PendingTerm> pendingTerms = new ArrayDeque<>();

		public void pushVar(Identifier varIdentifier) {
			if (isCompleted())
				throw new IllegalStateException();
			pendingTerms.push(new PendingVar(varIdentifier));
			combinePendingTerm();
		}

		public void pushAbs(Identifier absBinding) {
			if (isCompleted())
				throw new IllegalStateException();
			pendingTerms.push(new PendingAbs(absBinding));
			combinePendingTerm();
		}

		public void pushApp() {
			if (isCompleted())
				throw new IllegalStateException();
			pendingTerms.push(new PendingApp());
			combinePendingTerm();
		}

		private void combinePendingTerm() {
			while (pendingTerms.peek().isCompleted()) {
				if (pendingTerms.size() == 1)
					return;
				Term completedTerm = pendingTerms.pop().toTerm();
				PendingTerm pendingTerm = pendingTerms.peek();
				pendingTerm.pushTerm(completedTerm);
			}
		}

		public boolean isCompleted() {
			return pendingTerms.size() == 1 && pendingTerms.peek().isCompleted();
		}

		public Term buildTerm() {
			if (!isCompleted())
				throw new IllegalStateException();
			return pendingTerms.pop().toTerm();
		}

		private abstract class PendingTerm {
			public abstract void pushTerm(Term term);

			public abstract boolean isCompleted();

			public abstract Term toTerm();
		}

		private final class PendingVar extends PendingTerm {
			private final Identifier varIdentifier;

			public PendingVar(Identifier varIdentifier) {
				this.varIdentifier = varIdentifier;
			}

			@Override
			public void pushTerm(Term term) {
				throw new IllegalStateException();
			}

			@Override
			public boolean isCompleted() {
				return true;
			}

			@Override
			public Term toTerm() {
				return new Var(varIdentifier);
			}
		}

		private final class PendingAbs extends PendingTerm {
			private final Identifier absBinding;
			private Term absTerm;

			public PendingAbs(Identifier absBinding) {
				this.absBinding = absBinding;
			}

			@Override
			public void pushTerm(Term term) {
				if (absTerm == null)
					absTerm = term;
				else
					throw new IllegalStateException();
			}

			@Override
			public boolean isCompleted() {
				return absTerm != null;
			}

			@Override
			public Term toTerm() {
				if (!isCompleted())
					throw new IllegalStateException();
				return new Abs(absBinding, absTerm);
			}
		}

		private final class PendingApp extends PendingTerm {
			private Term appTerm;
			private Term appArgument;

			public PendingApp() {

			}

			@Override
			public void pushTerm(Term term) {
				if (appTerm == null)
					appTerm = term;
				else if (appArgument == null)
					appArgument = term;
				else
					throw new IllegalStateException();
			}

			@Override
			public boolean isCompleted() {
				return appTerm != null && appArgument != null;
			}

			@Override
			public Term toTerm() {
				if (!isCompleted())
					throw new IllegalStateException();
				return new App(appTerm, appArgument);
			}
		}
	}
}

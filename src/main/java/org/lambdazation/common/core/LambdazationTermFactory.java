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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.MutableInt;
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
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public final class LambdazationTermFactory {
	public final Lambdazation lambdazation;

	public final TermCache termCache;
	public final TermAsyncFactory termAsyncFactory;
	public final PredefTermLibrary predefTermLibrary;

	public LambdazationTermFactory(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		this.termCache = new TermCache();
		this.termAsyncFactory = new TermAsyncFactory();
		this.predefTermLibrary = PredefTermLibrary
			.builder()
			// function
			.with(builder -> builder
				.name("id")
				.term(parseTerm("λx.x", true).get()))
			.with(builder -> builder
				.name("const")
				.term(parseTerm("λx.λy.x", true).get()))
			.with(builder -> builder
				.name("subst")
				.term(parseTerm("λf.λg.λx.f x (g x)", true).get()))
			.with(builder -> builder
				.name("compose")
				.term(parseTerm("λf.λg.λx.f (g x)", true).get()))
			.with(builder -> builder
				.name("flip")
				.term(parseTerm("λf.λx.λy.f y x", true).get()))
			.with(builder -> builder
				.name("apply")
				.term(parseTerm("λf.λx.f x", true).get()))
			.with(builder -> builder
				.name("reverseApply")
				.term(parseTerm("λx.λf.f x", true).get()))
			.with(builder -> builder
				.name("prepose")
				.term(parseTerm("λn.λf.λg.n (λh.λi.λx.h (λu.i u x)) (λu.f (u g)) (λu.u)", true).get()))
			.with(builder -> builder
				.name("fix")
				.term(parseTerm("λf.(λx.f (x x)) (λx.f (x x))", true).get()))
			.with(builder -> builder
				.name("omega")
				.term(parseTerm("(λx.x x) (λx.x x)", true).get()))
			// nat
			.with(builder -> builder
				.name("zero")
				.term(parseTerm("λf.λx.x", true).get()))
			.with(builder -> builder
				.name("succ")
				.term(parseTerm("λn.λf.λx.f (n f x)", true).get()))
			.with(builder -> builder
				.name("plus")
				.term(parseTerm("λm.λn.λf.λx.m f (n f x)", true).get()))
			.with(builder -> builder
				.name("mult")
				.term(parseTerm("λm.λn.λf.m (n f)", true).get()))
			.with(builder -> builder
				.name("pow")
				.term(parseTerm("λm.λn.n m", true).get()))
			.with(builder -> builder
				.name("pred")
				.term(parseTerm("λn.λf.λx.n (λg.λh.h (g f)) (λu.x) (λu.u)", true).get()))
			// bool
			.with(builder -> builder
				.name("bool")
				.term(parseTerm("λt.λf.λb.b t f", true).get()))
			.with(builder -> builder
				.name("false")
				.term(parseTerm("λt.λf.f", true).get()))
			.with(builder -> builder
				.name("true")
				.term(parseTerm("λt.λf.t", true).get()))
			.with(builder -> builder
				.name("and")
				.term(parseTerm("λp.λq.p q p", true).get()))
			.with(builder -> builder
				.name("or")
				.term(parseTerm("λp.λq.p p q", true).get()))
			.with(builder -> builder
				.name("not")
				.term(parseTerm("λb.λt.λf.b f t", true).get()))
			.with(builder -> builder
				.name("if")
				.term(parseTerm("λb.λt.λf.b t f", true).get()))
			// maybe
			.with(builder -> builder
				.name("maybe")
				.term(parseTerm("λn.λj.λm.m n j", true).get()))
			.with(builder -> builder
				.name("nothing")
				.term(parseTerm("λn.λj.n", true).get()))
			.with(builder -> builder
				.name("just")
				.term(parseTerm("λx.λn.λj.j x", true).get()))
			.with(builder -> builder
				.name("isNothing")
				.term(parseTerm("(λfalse.((λtrue.(λm.m true (λx. false))) (λt.λf.t))) (λt.λf.f)", true).get()))
			.with(builder -> builder
				.name("isJust")
				.term(parseTerm("(λfalse.((λtrue.(λm.m false (λx. true))) (λt.λf.t))) (λt.λf.f)", true).get()))
			.with(builder -> builder
				.name("fromMaybe")
				.term(parseTerm("λx.λm.m x (λu.u)", true).get()))
			// list
			.with(builder -> builder
				.name("list")
				.term(parseTerm("λn.λc.λl.l n c", true).get()))
			.with(builder -> builder
				.name("nil")
				.term(parseTerm("λn.λc.n", true).get()))
			.with(builder -> builder
				.name("cons")
				.term(parseTerm("λh.λt.λn.λc.c h t", true).get()))
			.with(builder -> builder
				.name("append")
				.term(parseTerm("(λfix.((λcons.(fix (λappend.(λl.λm.l m (λh.λt.cons h (append t m)))))) (λh.λt.λn.λc.c h t))) (λf.(λx.f (x x)) (λx.f (x x)))", true).get()))
			.with(builder -> builder
				.name("head")
				.term(parseTerm("(λnothing.((λjust.(λl.l nothing (λh.λt.just h))) (λx.λn.λj.j x))) (λn.λj.n)", true).get()))
			.with(builder -> builder
				.name("last")
				.term(parseTerm("(λ2.((λprepose.((λfix.((λnothing.((λjust.(λl.l nothing (prepose 2 just (fix (λf.λh.λt.t h f))))) (λx.λn.λj.j x))) (λn.λj.n))) (λf.(λx.f (x x)) (λx.f (x x))))) (λn.λf.λg.n (λh.λi.λx.h (λu.i u x)) (λu.f (u g)) (λu.u)))) (λf.λx.f (f x))", true).get()))
			.with(builder -> builder
				.name("tail")
				.term(parseTerm("(λnothing.((λjust.(λl.l nothing (λh.λt.just t))) (λx.λn.λj.j x))) (λn.λj.n)", true).get()))
			.with(builder -> builder
				.name("init")
				.term(parseTerm("(λ2.((λprepose.((λfix.((λnothing.((λjust.((λnil.((λcons.(λl.l nothing (prepose 2 just (fix (λf.λh.λt.t nil (prepose 2 (cons h) f)))))) (λh.λt.λn.λc.c h t))) (λn.λc.n))) (λx.λn.λj.j x))) (λn.λj.n))) (λf.(λx.f (x x)) (λx.f (x x))))) (λn.λf.λg.n (λh.λi.λx.h (λu.i u x)) (λu.f (u g)) (λu.u)))) (λf.λx.f (f x))", true).get()))
			.with(builder -> builder
				.name("null")
				.term(parseTerm("(λfalse.((λtrue.(λl.l true (λh.λt.false))) (λt.λf.t))) (λt.λf.f)", true).get()))
			.with(builder -> builder
				.name("length")
				.term(parseTerm("(λfix.((λzero.((λsucc.(fix (λlength.(λl.l zero (λh.λt.succ (length t)))))) (λn.λf.λx.f (n f x)))) (λf.λx.x))) (λf.(λx.f (x x)) (λx.f (x x)))", true).get()))
			.with(builder -> builder
				.name("map")
				.term(parseTerm("(λfix.((λnil.((λcons.(fix (λmap.(λf.λl.l nil (λh.λt.cons (f h) (map f t)))))) (λh.λt.λn.λc.c h t))) (λn.λc.n))) (λf.(λx.f (x x)) (λx.f (x x)))", true).get()))
			.with(builder -> builder
				.name("reverse")
				.term(parseTerm("(λfix.((λnil.((λcons.(λl.(fix (λf.λm.λn.m n (λh.λt.f t (cons h n)))) l nil)) (λh.λt.λn.λc.c h t))) (λn.λc.n))) (λf.(λx.f (x x)) (λx.f (x x)))", true).get()))
			.with(builder -> builder
				.name("foldr")
				.term(parseTerm("(λfix.(fix (λfoldr.(λf.λx.λl.l x (λh.λt.f h (foldr f x t)))))) (λf.(λx.f (x x)) (λx.f (x x)))", true).get()))
			.with(builder -> builder
				.name("foldl")
				.term(parseTerm("(λfix.(fix (λfoldl.(λf.λx.λl.l x (λh.λt.foldl f (f x h) t))))) (λf.(λx.f (x x)) (λx.f (x x)))", true).get()))
			.build();
	}

	public void onWorldTick(ServerTickEvent e) {
		int time = LogicalSidedProvider.INSTANCE.<MinecraftServer> get(LogicalSide.SERVER).getTickCounter();
		// TODO Make purge pool interval configurable.
		int purgePoolInterval = 20 * 60;
		if (time % purgePoolInterval == 0)
			termAsyncFactory.purgePool(false, time);
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

	public final class TermReductionResult {
		public final int step;
		public final TermRef termRef;

		public TermReductionResult(int step, TermRef termRef) {
			this.step = step;
			this.termRef = termRef;
		}
	}

	public final class TermCache {
		final Lock cacheLock;
		final TermPool primaryTermPool;
		final TermPool secondaryTermPool;

		TermCache() {
			cacheLock = new ReentrantLock();
			primaryTermPool = new TermPool(null, 0xFFFFFF, 0x7FFFFF, 20 * 60 * 10, -1);
			secondaryTermPool = new TermPool(primaryTermPool, 0xFFFF, 0x7FFF, 20 * 10, 20 * 60);
		}

		public void purgeCache(boolean force, int time) {
			cacheLock.lock();
			try {
				primaryTermPool.purgePool(force, time);
				secondaryTermPool.purgePool(force, time);
			} finally {
				cacheLock.unlock();
			}
		}

		public TermReductionResult reduceTerm(TermRef termRef, int maxStep, int maxSize, int time)
			throws InterruptedException {
			MutableInt currentStep = new MutableInt();
			Entry entry;

			cacheLock.lock();
			try {
				entry = secondaryTermPool.reduceEntry(termRef, maxStep, maxSize, time, currentStep);
				entry.lock();
			} finally {
				cacheLock.unlock();
			}

			TermRef reducedTermRef = null;
			int reducedStep = 0;
			int sizePeak = 0;

			reduceEntry: try {
				if (currentStep.getValue() >= maxStep || entry.termSize >= maxSize
					|| entry.termState.equals(TermState.BETA_ETA_NORMAL_FORM))
					break reduceEntry;

				Term term;
				try (DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(entry.serializedTerm))) {
					term = deserializeTerm(TermNamer.EMPTY_NAMER, dataInput);
				} catch (IOException e) {
					break reduceEntry;
				}

				boolean successful = true;
				int actualMaxStep = maxStep - currentStep.getValue();
				if (successful && reducedStep <= actualMaxStep && term.size() <= maxSize) {
					Result result = BetaReducer.interruptibleBetaReduction(term,
						new Strategy(scala.Option.apply(actualMaxStep - reducedStep), scala.Option.apply(maxSize),
							scala.Option.empty(), false, false));
					successful = result.abortReason().successful();
					reducedStep += result.step();
					sizePeak = Math.max(sizePeak, result.sizePeak());
					term = result.term();
				}
				if (successful && reducedStep <= actualMaxStep && term.size() <= maxSize) {
					Result result = EtaConverter.interruptibleEtaConversion(term,
						new Strategy(scala.Option.apply(actualMaxStep - reducedStep), scala.Option.apply(maxSize),
							scala.Option.empty(), false, false));
					successful = result.abortReason().successful();
					reducedStep += result.step();
					sizePeak = Math.max(sizePeak, result.sizePeak());
					term = result.term();
				}
				currentStep.add(reducedStep);

				if (reducedStep <= 0)
					break reduceEntry;

				TermMetadata termMetadata;
				ByteArrayOutputStream termOutput;
				try (DataOutputStream dataOutput = new DataOutputStream(termOutput = new ByteArrayOutputStream())) {
					termMetadata = serializeTerm(term, dataOutput);
				} catch (IOException e) {
					break reduceEntry;
				}
				byte[] serializedTerm = termOutput.toByteArray();
				reducedTermRef = new TermRef(serializedTerm, termMetadata.termStatistics.termSize,
					termMetadata.termStatistics.termDepth, termMetadata.termStatistics.termState,
					termMetadata.termStatistics.termHash);
			} catch (Throwable e) {
				cacheLock.lock();
				try {
					entry.unlock();
				} finally {
					cacheLock.unlock();
				}
				throw e;
			}

			cacheLock.lock();
			try {
				Entry reducedEntry;
				if (reducedTermRef == null) {
					reducedEntry = entry;
					if (reducedEntry.accessTime != time)
						secondaryTermPool.updateEntry(reducedEntry, time);
				} else {
					reducedEntry = secondaryTermPool.acquireEntry(reducedTermRef, time);
					entry.setNextEntry(reducedEntry, reducedStep, sizePeak);
				}
				entry.unlock();
				return new TermReductionResult(currentStep.getValue(), reducedEntry);
			} finally {
				cacheLock.unlock();
			}
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
							if (!entry.locked) {
								termSize -= entry.termSize;
								entry.removeEntry();
								iterator.remove();
							}
						}
					}
					if (entryExpireTime >= 0) {
						while (iterator.hasNext()) {
							Entry entry = iterator.next();
							if (time - entry.accessTime < entryExpireTime)
								break;
							if (!entry.locked) {
								termSize -= entry.termSize;
								entry.removeEntry();
								iterator.remove();
							}
						}
					}
				}
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

			Entry lookupEntry(TermRef termRef, int time) {
				Entry entry = entries.get(termRef);
				if (entry != null)
					relocateEntry(entry, time);
				else if (precedingPool != null)
					entry = precedingPool.lookupEntry(termRef, time);
				return entry;
			}

			void relocateEntry(Entry entry, int time) {
				entry.accessTime = time;
				if (precedingPool != null && entryPromoteTime >= 0 && time - entry.cachedTime >= entryPromoteTime) {
					if (!entries.remove(entry))
						throw new IllegalStateException();
					termSize -= entry.termSize;
					precedingPool.termSize += entry.termSize;
					precedingPool.entries.add(entry);
				} else if (entries.addAndMoveToLast(entry))
					throw new IllegalStateException();
			}

			void updateEntry(Entry entry, int time) {
				if (entries.contains(entry))
					relocateEntry(entry, time);
				else if (precedingPool != null)
					precedingPool.updateEntry(entry, time);
			}

			Entry reduceEntry(TermRef termRef, int maxStep, int maxSize, int time, MutableInt currentStep) {
				Entry entry = acquireEntry(termRef, time);
				while (currentStep.getValue() < maxStep && entry.termSize <= maxSize) {
					if (entry.nextEntry == null)
						break;
					if (currentStep.getValue() + entry.nextEntryStep > maxStep)
						break;
					if (entry.nextEntrySizePeak > maxSize)
						break;
					currentStep.add(entry.nextEntryStep);
					entry = entry.nextEntry;
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
			Condition lockedCondition;
			boolean locked;

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
				this.lockedCondition = cacheLock.newCondition();
				this.locked = false;
			}

			void lock() throws InterruptedException {
				while (locked)
					lockedCondition.await();
				locked = true;
			}

			void unlock() {
				locked = false;
				lockedCondition.signal();
			}

			void setNextEntry(Entry nextEntry, int nextEntryStep, int nextEntrySizePeak) {
				this.removeNextEntry();
				nextEntry.removePrevEntry();

				this.nextEntry = nextEntry;
				this.nextEntryStep = nextEntryStep;
				this.nextEntrySizePeak = nextEntrySizePeak;
				nextEntry.prevEntry = this;
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
		}
	}

	public interface TermAsyncResult<T> {
		void discard();

		Optional<T> get();
	}

	public final class TermAsyncFactory {
		final ExecutorService executorService;
		AtomicInteger concurrentTasks;

		TermAsyncFactory() {
			// TODO Make executor service configurable.
			this.executorService = Executors.newCachedThreadPool();
			this.concurrentTasks = new AtomicInteger();
		}

		public void purgePool(boolean force, int time) {
			executorService.submit(() -> termCache.purgeCache(false, time));
		}

		public TermAsyncResult<TermReductionResult> reduceTerm(TermRef termRef, int maxStep, int maxSize, int time) {
			Future<TermReductionResult> future = executorService.submit(() -> {
				concurrentTasks.incrementAndGet();
				try {
					return termCache.reduceTerm(termRef, maxStep, maxSize, time);
				} catch (InterruptedException e) {
					return new TermReductionResult(0, termRef);
				} finally {
					concurrentTasks.decrementAndGet();
				}
			});
			return new TermAsyncResult<TermReductionResult>() {
				@Override
				public void discard() {
					// TODO Make cancellation threshold configurable.
					int cancellationThreshold = 2;
					if (concurrentTasks.get() >= cancellationThreshold)
						future.cancel(true);
				}

				@Override
				public Optional<TermReductionResult> get() {
					try {
						return Optional.of(future.get());
					} catch (CancellationException e) {
						return Optional.empty();
					} catch (InterruptedException e) {
						return Optional.empty();
					} catch (ExecutionException e) {
						return Optional.empty();
					}
				}
			};
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

	public static final class PredefTermLibrary {
		private final Map<String, PredefTerm> predefTerms;

		PredefTermLibrary(Map<String, PredefTerm> predefTerms) {
			this.predefTerms = predefTerms;
		}

		public Optional<PredefTerm> get(String name) {
			return Optional.ofNullable(predefTerms.get(name));
		}

		public Stream<PredefTerm> stream() {
			return predefTerms.values().stream();
		}

		public static Builder builder() {
			return new Builder();
		}

		public static final class Builder implements GeneralizedBuilder<Builder, PredefTermLibrary> {
			private final Map<String, PredefTerm> predefTerms;

			public Builder() {
				this.predefTerms = new LinkedHashMap<>();
			}

			public Builder with(PredefTerm predefTerm) {
				if (predefTerms.containsKey(predefTerm.name))
					throw new IllegalStateException("Duplicate predef term name");
				predefTerms.put(predefTerm.name, predefTerm);
				return this;
			}

			public Builder with(Function<PredefTerm.Builder, PredefTerm.Builder> f) {
				with(f.apply(PredefTerm.builder()).build());
				return this;
			}

			@Override
			public Builder concrete() {
				return this;
			}

			@Override
			public PredefTermLibrary build() {
				return new PredefTermLibrary(predefTerms);
			}
		}
	}
}

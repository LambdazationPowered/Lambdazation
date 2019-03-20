package org.lambdazation.common.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lambdazation.common.util.GeneralizedBuilder;
import org.lambdazation.common.util.IO;
import org.lamcalcj.ast.Lambda.Abs;
import org.lamcalcj.ast.Lambda.App;
import org.lamcalcj.ast.Lambda.Identifier;
import org.lamcalcj.ast.Lambda.Term;
import org.lamcalcj.ast.Lambda.Var;
import org.lamcalcj.compiler.Compiler;
import org.lamcalcj.parser.Text;
import org.lamcalcj.parser.Text$;

public final class LambdazationTermFactory {
	public final Lambdazation lambdazation;

	public final PredefTerm predefTermId;
	public final PredefTerm predefTermFix;

	public LambdazationTermFactory(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

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

	public TermStatistics serializeTerm(Term term, DataOutput output) throws IOException {
		int termSize = term.size();
		int termDepth = term.depth();
		TermState termState = TermState.BETA_ETA_NORMAL_FORM;
		int termHash = 1;

		Deque<Term> pendingTerms = new ArrayDeque<>();
		Map<Identifier, Integer> identifierMap = new HashMap<>();
		boolean pendingBetaRedex = false;
		boolean pendingEtaRedex = false;
		Identifier pendingEtaRedexIdentifier = null;
		Map<Identifier, Boolean> potentialEtaRedexIdentifiers = new HashMap<>();

		pendingTerms.add(term);

		while (!pendingTerms.isEmpty()) {
			Term currentTerm = pendingTerms.pop();
			if (currentTerm instanceof Var) {
				Identifier varIdentifier = ((Var) currentTerm).identifier();

				output.writeByte(0);
				termHash = 31 * termHash + 0;
				termHash = 31 * termHash + serializeIdentifier(varIdentifier, output, identifierMap);

				if (!termState.equals(TermState.REDUCIBLE_FORM)) {
					Boolean identifierState = potentialEtaRedexIdentifiers.get(varIdentifier);
					if (identifierState != null)
						if (identifierState)
							potentialEtaRedexIdentifiers.put(varIdentifier, false);
						else
							potentialEtaRedexIdentifiers.remove(varIdentifier);
					pendingBetaRedex = false;
					pendingEtaRedex = false;
					pendingEtaRedexIdentifier = null;
				}
			} else if (currentTerm instanceof Abs) {
				Identifier absBinding = ((Abs) currentTerm).binding();
				Term absTerm = ((Abs) currentTerm).term();

				output.writeByte(1);
				termHash = 31 * termHash + 1;
				termHash = 31 * termHash + serializeIdentifier(absBinding, output, identifierMap);
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

		return new TermStatistics(termSize, termDepth, termState, termHash);
	}

	private int serializeIdentifier(Identifier identifier, DataOutput output, Map<Identifier, Integer> identifierMap)
		throws IOException {
		Integer serialId = identifierMap.get(identifier);
		if (serialId == null) {
			identifierMap.put(identifier, serialId = identifierMap.size());
			output.writeBoolean(true);
			output.writeUTF(identifier.name());
		} else {
			output.writeBoolean(false);
			output.writeInt(serialId);
		}
		return serialId;
	}

	public Term deserializeTerm(DataInput input) throws IOException {
		TermBuilder termBuilder = new TermBuilder();
		Map<Integer, Identifier> identifierMap = new HashMap<>();

		while (!termBuilder.isCompleted()) {
			byte termType = input.readByte();
			switch (termType) {
			case 0:
				Identifier varIdentifier = deserializeIdentifier(input, identifierMap);
				termBuilder.pushVar(varIdentifier);
				break;
			case 1:
				Identifier absBinding = deserializeIdentifier(input, identifierMap);
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

	private Identifier deserializeIdentifier(DataInput input, Map<Integer, Identifier> identifierMap)
		throws IOException {
		boolean newIdentifier = input.readBoolean();
		if (newIdentifier) {
			String name = input.readUTF();
			Identifier identifier = new Identifier(name);
			identifierMap.put(identifierMap.size(), identifier);
			return identifier;
		} else {
			int serialId = input.readInt();
			Identifier identifier = identifierMap.get(serialId);
			if (identifier == null)
				throw new IOException("Invalid serial id");
			return identifier;
		}
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

	public boolean isAlphaEquivalent(DataInput firstInput, DataInput secondInput) {
		try {
			for (int pendingTerm = 1; pendingTerm > 0; pendingTerm--) {
				byte firstTermType = firstInput.readByte();
				byte secondTermType = secondInput.readByte();
				if (firstTermType != secondTermType)
					return false;
				byte termType = firstTermType = secondTermType;
				switch (termType) {
				case 0:
					if (!isIdentifierEquivalent(firstInput, secondInput))
						return false;
					break;
				case 1:
					if (!isIdentifierEquivalent(firstInput, secondInput))
						return false;
					pendingTerm++;
					break;
				case 2:
					pendingTerm++;
					pendingTerm++;
					break;
				default:
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private boolean isIdentifierEquivalent(DataInput firstInput, DataInput secondInput) {
		try {
			boolean firstNewIdentifier = firstInput.readBoolean();
			boolean secondNewIdentifier = secondInput.readBoolean();
			if (firstNewIdentifier != secondNewIdentifier)
				return false;
			boolean newIdentifier = firstNewIdentifier = secondNewIdentifier;
			if (newIdentifier) {
				IO.skipUTF(firstInput);
				IO.skipUTF(secondInput);
			} else {
				int firstSerialId = firstInput.readInt();
				int secondSerialId = secondInput.readInt();
				if (firstSerialId != secondSerialId)
					return false;
			}
			return true;
		} catch (IOException e) {
			return false;
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

	private final class TermBuilder {
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

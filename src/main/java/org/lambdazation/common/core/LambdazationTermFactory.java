package org.lambdazation.common.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.lambdazation.Lambdazation;
import org.lamcalcj.ast.Lambda.Abs;
import org.lamcalcj.ast.Lambda.App;
import org.lamcalcj.ast.Lambda.Identifier;
import org.lamcalcj.ast.Lambda.Term;
import org.lamcalcj.ast.Lambda.Var;

public final class LambdazationTermFactory {
	public final Lambdazation lambdazation;

	public LambdazationTermFactory(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;
	}

	public void serializeTerm(Term term, DataOutput output) throws IOException {
		Deque<Term> pendingTerms = new ArrayDeque<>();
		pendingTerms.add(term);

		Map<Identifier, Integer> identifierMap = new HashMap<>();

		for (Term currentTerm; (currentTerm = pendingTerms.pop()) != null;) {
			if (currentTerm instanceof Var) {
				Identifier varIdentifier = ((Var) currentTerm).identifier();

				output.writeByte(0);
				serializeIdentifier(varIdentifier, output, identifierMap);
			} else if (currentTerm instanceof Abs) {
				Identifier absBinding = ((Abs) currentTerm).binding();
				Term absTerm = ((Abs) currentTerm).term();

				output.writeByte(1);
				serializeIdentifier(absBinding, output, identifierMap);
				pendingTerms.push(absTerm);
			} else if (currentTerm instanceof App) {
				Term appTerm = ((App) currentTerm).term();
				Term appArgument = ((App) currentTerm).argument();

				output.writeByte(2);
				pendingTerms.push(appArgument);
				pendingTerms.push(appTerm);
			} else
				throw new IllegalStateException();
		}
	}

	private void serializeIdentifier(Identifier identifier, DataOutput output, Map<Identifier, Integer> identifierMap)
		throws IOException {
		Integer serialId = identifierMap.get(identifier);
		if (serialId == null) {
			identifierMap.put(identifier, identifierMap.size());
			output.writeBoolean(true);
			output.writeUTF(identifier.name());
		} else {
			output.writeBoolean(false);
			output.writeInt(serialId);
		}
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

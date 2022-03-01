package org.processmining.stochasticlabelledpetrinets.automaton;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;

import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNet;
import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNetsSemanticsImpl;

import gnu.trove.set.hash.THashSet;

public abstract class ConjunctiveStateSpaceTraverser<SB> {

	private static class CState<S> {

		public CState(byte[] stateA, S stateB) {
			this.stateA = null;
			this.stateB = null;
		}

		final byte[] stateA;
		final S stateB;

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(stateA);
			result = prime * result + Objects.hash(stateB);
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			CState<S> other = (CState<S>) obj;
			return Arrays.equals(stateA, other.stateA) && Objects.equals(stateB, other.stateB);
		}
	}

	public void traverse(StochasticLabelledPetriNet net) {
		StochasticLabelledPetriNetsSemanticsImpl semantics = new StochasticLabelledPetriNetsSemanticsImpl(net);

		THashSet<CState<SB>> seen = new THashSet<>();
		ArrayDeque<CState<SB>> worklist = new ArrayDeque<>();
		{
			CState<SB> state = new CState<>(semantics.getState(), getInitialState());
			worklist.add(state);
			seen.add(state);
		}

		while (!worklist.isEmpty()) {
			CState<SB> stateAB = worklist.pop();

			semantics.setState(stateAB.stateA);

			if (semantics.isFinalState()) {
				if (isFinalState(stateAB.stateB)) {
					//TODO: report final state
				}
			} else {
				BitSet enabledTransitions = semantics.getEnabledTransitions();

				for (int transition = enabledTransitions.nextSetBit(0); transition >= 0; transition = enabledTransitions
						.nextSetBit(transition + 1)) {

					semantics.setState(stateAB.stateA);
					semantics.executeTransition(transition);
					byte[] newStateA = semantics.getState();
					SB newStateB;
					if (net.isTransitionSilent(transition)) {
						//silent transition; only A takes a step
						newStateB = stateAB.stateB;
					} else {
						//labelled transition; both A and B take steps
						newStateB = takeStep(stateAB.stateB, net.getTransitionLabel(transition));
						if (newStateB == null) {
							continue;
						}
					}

					CState<SB> newStateAB = new CState<SB>(newStateA, newStateB);
					if (seen.add(newStateAB)) {
						worklist.add(newStateAB);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @return The initial state.
	 */
	public abstract SB getInitialState();

	/**
	 * 
	 * @param label
	 * @return The new state, or null if the step cannot be taken.
	 */
	public abstract SB takeStep(SB state, String label);

	public abstract boolean isFinalState(SB state);
}
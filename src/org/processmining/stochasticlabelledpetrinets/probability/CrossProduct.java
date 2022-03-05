package org.processmining.stochasticlabelledpetrinets.probability;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;

import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNet;
import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNetSemantics;
import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNetSemanticsImpl;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public abstract class CrossProduct {

	protected static class ABState<B> {

		public ABState(byte[] stateA, B stateB) {
			this.stateA = null;
			this.stateB = null;
		}

		final byte[] stateA;
		final B stateB;

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
			ABState<B> other = (ABState<B>) obj;
			return Arrays.equals(stateA, other.stateA) && Objects.equals(stateB, other.stateB);
		}
	}

	private static class Z<B> {
		int stateCounter = 0;
		StochasticLabelledPetriNetSemantics semantics;
		TObjectIntMap<ABState<B>> seen = new TObjectIntHashMap<>(10, 0.5f, -1);
		ArrayDeque<ABState<B>> worklist = new ArrayDeque<>();
	}

	private static class Y {
		TIntList outgoingStates = new TIntArrayList();
		TDoubleList outgoingStateProbabilities = new TDoubleArrayList();
	}

	public static <B> void traverse(StochasticLabelledPetriNet netA, FollowerSemantics<B> netB,
			CrossProductResult result) {
		Z<B> z = new Z<>();
		Y y = new Y();
		z.semantics = new StochasticLabelledPetriNetSemanticsImpl(netA);

		//initialise
		{
			ABState<B> state = new ABState<>(z.semantics.getState(), netB.getInitialState());
			z.worklist.add(state);
			z.seen.put(state, z.stateCounter);
			z.stateCounter++;
		}

		while (!z.worklist.isEmpty()) {
			ABState<B> stateAB = z.worklist.pop();
			int stateABindex = z.seen.get(stateAB);

			z.semantics.setState(stateAB.stateA);

			if (z.semantics.isFinalState()) {
				if (netB.isFinalState(stateAB.stateB)) {
					result.reportFinalState(stateABindex);
				}
			} else {
				BitSet enabledTransitions = z.semantics.getEnabledTransitions();
				double totalWeight = z.semantics.getTotalWeightOfEnabledTransitions();

				y.outgoingStates.clear();
				y.outgoingStateProbabilities.clear();

				for (int transition = enabledTransitions.nextSetBit(0); transition >= 0; transition = enabledTransitions
						.nextSetBit(transition + 1)) {

					z.semantics.setState(stateAB.stateA);
					z.semantics.executeTransition(transition);
					byte[] newStateA = z.semantics.getState();
					if (netA.isTransitionSilent(transition)) {
						//silent transition; only A takes a step
						B newStateB = stateAB.stateB;

						processNewState(netA, z, y, totalWeight, transition, newStateA, newStateB);
					} else {
						//labelled transition; both A and B take steps
						B[] newStatesB = netB.takeStep(stateAB.stateB, netA.getTransitionLabel(transition));
						if (newStatesB == null || newStatesB.length == 0) {
							continue;
						}

						for (B newStateB : newStatesB) {
							processNewState(netA, z, y, totalWeight, transition, newStateA, newStateB);
						}
					}
				}

				result.reportNonFinalState(stateABindex, y.outgoingStates, y.outgoingStateProbabilities);
			}
		}
	}

	private static <B> void processNewState(StochasticLabelledPetriNet net, Z<B> z, Y y, double totalWeight,
			int transition, byte[] newStateA, B newStateB) {
		ABState<B> newStateAB = new ABState<B>(newStateA, newStateB);
		int newStateIndex = z.seen.adjustOrPutValue(newStateAB, 0, z.stateCounter);
		if (newStateIndex == z.stateCounter) {
			//newStateAB was not encountered before
			z.stateCounter++;
			z.worklist.add(newStateAB);
		}

		y.outgoingStates.add(newStateIndex);
		y.outgoingStateProbabilities.add(net.getTransitionWeight(transition) / totalWeight);
	}

}
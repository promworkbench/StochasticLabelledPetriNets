package org.processmining.stochasticlabelledpetrinets;

import java.util.BitSet;

/**
 * This semantics aims to avoid traversing all transitions. After construction,
 * executing a transition will only consider the transitions whose enabledness
 * may have changed. The only all-transition operation is BitSet.clear().
 * 
 * After construction, the semantics will only allocate local non-array
 * variables.
 * 
 * @author sander
 *
 */
public class StochasticLabelledPetriNetsSemanticsImpl implements StochasticLabelledPetriNetSemantics {

	private final StochasticLabelledPetriNet net;
	private byte[] state;
	private byte[] cacheState;
	private boolean[] enabledTransitions;
	private BitSet cacheTransition;
	private int numberOfEnabledTransitions;

	public StochasticLabelledPetriNetsSemanticsImpl(StochasticLabelledPetriNet net) {
		this.net = net;
		state = new byte[net.getNumberOfPlaces()];
		cacheState = new byte[net.getNumberOfPlaces()];
		cacheTransition = new BitSet(net.getNumberOfTransitions());
		setInitialState();
	}

	public void setInitialState() {
		for (int place = 0; place < net.getNumberOfPlaces(); place++) {
			state[place] = (byte) net.isInInitialMarking(place);
		}
		computeEnabledTransitions();
	}

	public void executeTransition(int transition) {
		int[] inSet = net.getInputPlaces(transition);
		for (int place : inSet) {
			if (state[place] == 0) {
				throw new RuntimeException("non-existing token consumed");
			}
			state[place]--;

			//update the enabled transitions; some transitions might be disabled by this execution
			for (int transitionT : net.getOutputTransitions(place)) {
				computeEnabledTransition(transitionT);
			}
		}

		int[] postSet = net.getOutputPlaces(transition);
		for (int place : postSet) {
			if (state[place] == Byte.MAX_VALUE) {
				throw new RuntimeException("maximum number of tokens in a place exceeded");
			}
			state[place]++;

			//update the enabled transitions; some transitions might be enabled by this execution
			for (int transitionT : net.getInputTransitions(place)) {
				computeEnabledTransition(transitionT);
			}
		}
	}

	private boolean computeEnabledTransition(int transition) {
		//due to potential multiplicity of arcs, we have to keep track of how many tokens we would consume
		System.arraycopy(state, 0, cacheState, 0, state.length);

		int[] inSet = net.getInputPlaces(transition);
		for (int inPlace : inSet) {
			if (cacheState[inPlace] == 0) {
				if (enabledTransitions[transition]) {
					enabledTransitions[transition] = false;
					numberOfEnabledTransitions--;
				}
				return false;
			} else {
				cacheState[inPlace]--;
			}
		}

		if (!enabledTransitions[transition]) {
			enabledTransitions[transition] = true;
			numberOfEnabledTransitions++;
		}
		return true;
	}

	private void computeEnabledTransitions() {
		numberOfEnabledTransitions = 0;
		for (int transition = 0; transition < net.getNumberOfTransitions(); transition++) {
			computeEnabledTransition(transition);
		}
	}

	public boolean[] getEnabledTransitions() {
		return enabledTransitions;
	}

	public boolean isFinalState() {
		return numberOfEnabledTransitions == 0;
	}

	public byte[] getState() {
		return state;
	}

	public void setState(byte[] newState) {
		byte[] oldState = this.state;
		this.state = newState;

		cacheTransition.clear();

		//walk through all places that have changed, and update the transition enabledness
		for (int place = 0; place < net.getNumberOfPlaces(); place++) {
			if (oldState[place] != state[place]) {
				for (int transition : net.getInputTransitions(place)) {
					if (!cacheTransition.get(transition)) {
						computeEnabledTransition(transition);
						cacheTransition.set(transition);
					}
				}
				for (int transition : net.getOutputTransitions(place)) {
					if (!cacheTransition.get(transition)) {
						computeEnabledTransition(transition);
						cacheTransition.set(transition);
					}
				}
			}
		}
	}
}
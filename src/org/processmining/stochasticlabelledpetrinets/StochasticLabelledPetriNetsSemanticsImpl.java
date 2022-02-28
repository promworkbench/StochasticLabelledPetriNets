package org.processmining.stochasticlabelledpetrinets;

/**
 * This semantics aims to avoid traversing all transitions. After construction,
 * executing a transition will only consider the transitions whose enabledness
 * may have changed.
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
	private byte[] cache;
	private boolean[] enabledTransitions;
	private int numberOfEnabledTransitions;

	public StochasticLabelledPetriNetsSemanticsImpl(StochasticLabelledPetriNet net) {
		this.net = net;
		state = new byte[net.getNumberOfPlaces()];
		cache = new byte[net.getNumberOfPlaces()];
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
		System.arraycopy(state, 0, cache, 0, state.length);

		int[] inSet = net.getInputPlaces(transition);
		for (int inPlace : inSet) {
			if (cache[inPlace] == 0) {
				if (enabledTransitions[transition]) {
					enabledTransitions[transition] = false;
					numberOfEnabledTransitions--;
				}
				return false;
			} else {
				cache[inPlace]--;
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

	public void setState(byte[] state) {
		this.state = state;
		computeEnabledTransitions();
	}
}
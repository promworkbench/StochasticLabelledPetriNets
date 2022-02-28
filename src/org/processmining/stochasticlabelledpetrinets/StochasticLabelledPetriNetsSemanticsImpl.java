package org.processmining.stochasticlabelledpetrinets;

public class StochasticLabelledPetriNetsSemanticsImpl implements StochasticLabelledPetriNetSemantics {

	private final StochasticLabelledPetriNet net;
	private byte[] state;

	public StochasticLabelledPetriNetsSemanticsImpl(StochasticLabelledPetriNet net) {
		this.net = net;
		state = new byte[net.getNumberOfPlaces()];
		setInitialState();
	}

	public void setInitialState() {
		for (int place = 0; place < net.getNumberOfPlaces(); place++) {
			state[place] = (byte) net.isInInitialMarking(place);
		}
	}

	public void executeTransition(int transition) {
		// TODO Auto-generated method stub

	}

	public boolean[] getEnabledTransitions() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isFinalState() {
		// TODO Auto-generated method stub
		return false;
	}

	public byte[] getState() {
		return state;
	}

	public void setState(byte[] state) {
		this.state = state;
	}

}

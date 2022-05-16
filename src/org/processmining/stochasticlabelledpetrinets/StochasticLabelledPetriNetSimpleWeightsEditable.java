package org.processmining.stochasticlabelledpetrinets;

public interface StochasticLabelledPetriNetSimpleWeightsEditable extends StochasticLabelledPetriNet {

	public void setTransitionWeight(int transition, double weight);

}
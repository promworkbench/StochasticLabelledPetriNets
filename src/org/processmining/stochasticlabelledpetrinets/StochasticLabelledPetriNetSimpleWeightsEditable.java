package org.processmining.stochasticlabelledpetrinets;

public interface StochasticLabelledPetriNetSimpleWeightsEditable extends StochasticLabelledPetriNetSimpleWeights {

	public void setTransitionWeight(int transition, double weight);

}
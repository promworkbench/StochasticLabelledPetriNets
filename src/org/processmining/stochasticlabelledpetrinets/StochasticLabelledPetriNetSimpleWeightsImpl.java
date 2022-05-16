package org.processmining.stochasticlabelledpetrinets;

import gnu.trove.list.array.TDoubleArrayList;

public class StochasticLabelledPetriNetSimpleWeightsImpl extends StochasticLabelledPetriNetImpl
		implements StochasticLabelledPetriNetSimpleWeights {

	private TDoubleArrayList transitionWeights;

	public StochasticLabelledPetriNetSimpleWeightsImpl() {
		transitionWeights = new TDoubleArrayList();
	}

	public void setTransitionWeight(int transition, double weight) {
		transitionWeights.set(transition, weight);
	}

	@Override
	public int addTransition(String label, double weight) {
		super.addTransition(label, weight);
		transitionWeights.add(weight);
		return transitionWeights.size() - 1;
	}

	public double getTransitionWeight(int transition) {
		return transitionWeights.get(transition);
	}

}
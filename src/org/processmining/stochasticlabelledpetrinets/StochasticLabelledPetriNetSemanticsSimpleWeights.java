package org.processmining.stochasticlabelledpetrinets;

/**
 * Semantics may be implemented using a state machine, thus the underlying Petri
 * net might not be able to be changed.
 * 
 * @author sander
 *
 */
public interface StochasticLabelledPetriNetSemanticsSimpleWeights extends StochasticLabelledPetriNetSemantics {

	/**
	 * 
	 * @param transition
	 * @return the weight of the transition. This might depend on the state.
	 */
	public double getTransitionWeight(int transition);

	/**
	 * 
	 * @param enabledTransitions
	 * @return the sum of the weight of the enabled transitions
	 */
	public double getTotalWeightOfEnabledTransitions();

}
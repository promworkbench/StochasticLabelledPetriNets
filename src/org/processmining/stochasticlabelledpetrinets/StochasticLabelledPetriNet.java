package org.processmining.stochasticlabelledpetrinets;

public interface StochasticLabelledPetriNet {

	/**
	 * 
	 * @return the number of transitions. All transitions have indices starting
	 *         at 0 and ending at the returned value (exclusive);
	 */
	public int getNumberOfPlaces();

	public Iterable<Integer> getPlaces();

	/**
	 * Only call when it is certain that the transition is not a silent
	 * transition.
	 * 
	 * @param transition
	 * @return the label of the transition.
	 */
	public String getTransitionLabel(int transition);

	/**
	 * 
	 * @param transition
	 * @return whether the transition is a silent transition
	 */
	public boolean isTransitionSilent(int transition);

	/**
	 * 
	 * @param transition
	 * @return the weight of the transition.
	 */
	public double getTransitionWeight(int transition);

	/**
	 * 
	 * @param place
	 * @return the number of tokens on this place in the initial marking.
	 */
	public int isInInitialMarking(int place);

	/**
	 * 
	 * @param transition
	 * @return a list of places that have arcs to this transition. Arcs may
	 *         appear multiple times. The caller must not change the returned
	 *         array.
	 */
	public int[] getInputPlaces(int transition);

	/**
	 * 
	 * @param transition
	 * @return a list of places that have arcs from this transition. Arcs may
	 *         appear multiple times. The caller must not change the returned
	 *         array.
	 */
	public int[] getOutputPlaces(int transition);
}
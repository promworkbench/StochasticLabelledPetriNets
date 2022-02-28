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
}
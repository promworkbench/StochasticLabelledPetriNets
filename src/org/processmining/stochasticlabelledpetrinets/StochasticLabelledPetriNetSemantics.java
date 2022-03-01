package org.processmining.stochasticlabelledpetrinets;

import java.util.BitSet;

/**
 * Semantics may be implemented using a state machine, thus the underlying Petri
 * net might not be able to be changed.
 * 
 * @author sander
 *
 */
public interface StochasticLabelledPetriNetSemantics {

	/**
	 * (Re)set the semantics to the initial state.
	 */
	public void setInitialState();

	/**
	 * Update the state to reflect execution of the transition.
	 * 
	 * @param transition
	 */
	public void executeTransition(int transition);

	/**
	 * 
	 * @param state
	 * @return an array of indices of the transitions that are enabled. The
	 *         implementation might require that this array is not changed by
	 *         the caller.
	 */
	public BitSet getEnabledTransitions();

	/**
	 * 
	 * @return whether the current state is a final state.
	 */
	public boolean isFinalState();

	/**
	 * 
	 * @return the current state.
	 */
	public byte[] getState();

	/**
	 * Set the state.
	 * 
	 * @param state
	 */
	public void setState(byte[] state);
}
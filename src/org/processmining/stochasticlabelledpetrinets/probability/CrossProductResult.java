package org.processmining.stochasticlabelledpetrinets.probability;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;

public interface CrossProductResult {

	/**
	 * The initial state will be reported twice: once as initial state, and
	 * again as a final or non-final state.
	 * 
	 * @param stateIndex
	 */
	public void reportInitialState(int stateIndex);

	/**
	 * A state will be reported as either final or non-final.
	 * 
	 * @param stateIndex
	 * @param nextStateIndices
	 *            may contain duplicated values. List might be reused and
	 *            changed after this call returns, and changes by the
	 *            implementer will be overwritten.
	 * @param nextStateProbabilities
	 *            list might be reused and changed after this call returns, and
	 *            changes by the implementer will be overwritten.
	 */
	public void reportNonFinalState(int stateIndex, TIntList nextStateIndices, TDoubleList nextStateProbabilities);

	/**
	 * A state will be reported as either final or non-final.
	 * 
	 * @param stateIndex
	 */
	public void reportFinalState(int stateIndex);
}
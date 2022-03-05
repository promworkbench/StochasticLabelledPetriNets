package org.processmining.stochasticlabelledpetrinets.probability;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;

public interface CrossProductResult {
	public void reportFinalState(int stateIndex);

	/**
	 * 
	 * @param stateIndex
	 * @param nextStateIndexes
	 *            may contain double values. List might be reused and changed
	 *            after this call returns.
	 * @param probabilities
	 *            list might be reused and changed after this call returns.
	 */
	public void reportNonFinalState(int stateIndex, TIntList nextStateIndexes, TDoubleList probabilities);
}

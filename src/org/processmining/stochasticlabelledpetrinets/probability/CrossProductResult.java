package org.processmining.stochasticlabelledpetrinets.probability;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;

public interface CrossProductResult {
	public void reportFinalState(int stateIndex);

	/**
	 * 
	 * @param stateIndex
	 * @param nextStateIndexes
	 *            may contain duplicated values. List might be reused and
	 *            changed after this call returns.
	 * @param nextStateProbabilities
	 *            list might be reused and changed after this call returns.
	 */
	public void reportNonFinalState(int stateIndex, TIntList nextStateIndexes, TDoubleList nextStateProbabilities);
}

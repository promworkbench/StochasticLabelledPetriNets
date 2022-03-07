package org.processmining.stochasticlabelledpetrinets.probability;

import java.util.ArrayList;
import java.util.BitSet;

import org.apache.commons.lang.ArrayUtils;
import org.processmining.framework.plugin.ProMCanceller;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

public class CrossProductResultSolver implements CrossProductResult {

	static {
		System.loadLibrary("lpsolve55");
		System.loadLibrary("lpsolve55j");
	}

	private int initialState;
	private int deadState;
	private BitSet finalStates;
	private int maxState;
	private ArrayList<int[]> nextStates;
	private ArrayList<double[]> nextStateProbabilities;

	private BitSet deDuplicationCache = new BitSet();

	public CrossProductResultSolver() {
		initialState = -1;
		deadState = -1;
		maxState = -1;
		finalStates = new BitSet();
	}

	public void reportInitialState(int stateIndex) {
		initialState = stateIndex;
	}

	public void reportNonFinalState(int stateIndex, TIntList nextStateIndices, TDoubleList nextStateProbabilities) {
		maxState = Math.max(maxState, stateIndex);
		deDuplicate(nextStateIndices, nextStateProbabilities);

		while (nextStates.size() <= maxState) {
			this.nextStates.add(null);
			this.nextStateProbabilities.add(null);
		}

		this.nextStates.set(stateIndex, nextStateIndices.toArray());
		this.nextStateProbabilities.set(stateIndex, nextStateProbabilities.toArray());
	}

	public void reportFinalState(int stateIndex) {
		finalStates.set(stateIndex);
		maxState = Math.max(maxState, stateIndex);
	}

	public void reportDeadState(int stateIndex) {
		this.deadState = stateIndex;
	}

	private void deDuplicate(TIntList nextStateIndexes, TDoubleList nextStateProbabilities) {
		deDuplicationCache.clear();
		for (int indexA = 0; indexA < nextStateIndexes.size(); indexA++) {
			int nodeA = nextStateIndexes.get(indexA);
			if (deDuplicationCache.get(nodeA)) {
				//look for the duplicate
				for (int indexB = 0; indexB < indexA; indexB++) {
					int nodeB = nextStateIndexes.get(indexB);
					if (nodeA == nodeB) {
						nextStateProbabilities.set(indexB,
								nextStateProbabilities.get(indexB) + nextStateProbabilities.get(indexA));
						nextStateIndexes.removeAt(indexA);
						nextStateProbabilities.removeAt(indexA);
						indexA--;
						break;
					}
				}
			}

			deDuplicationCache.set(nodeA);
		}
	}

	/**
	 * Structure of the LP model:
	 * 
	 * One row per state; one column per state.
	 * 
	 * @return
	 * @throws LpSolveException
	 */
	public double solve(ProMCanceller canceller) throws LpSolveException {
		LpSolve solver = LpSolve.makeLp(0, maxState);

		solver.setDebug(false);
		solver.setVerbose(0);

		solver.setObj(initialState, -1);

		solver.setAddRowmode(true);

		for (int stateIndex = 0; stateIndex < nextStates.size(); stateIndex++) {

			if (stateIndex == deadState) {
				//a dead state has a 0 probability to end up in a final state
				int[] columns = new int[] { deadState };
				double[] probabilities = new double[] { 1 };
				solver.addConstraintex(columns.length, probabilities, columns, LpSolve.EQ, 0);
			} else if (finalStates.get(stateIndex)) {
				//a final state has a 1 probability to end up in a final state
				int[] columns = new int[] { stateIndex };
				double[] probabilities = new double[] { 1 };
				solver.addConstraintex(columns.length, probabilities, columns, LpSolve.EQ, 1.0);
			} else {
				//any other state has a probability equal to the weighted sum of its next states, to end up in a final state
				int[] columns = ArrayUtils.add(nextStates.get(stateIndex), stateIndex);
				double[] probabilities = ArrayUtils.add(nextStateProbabilities.get(stateIndex), -1);
				solver.addConstraintex(columns.length, probabilities, columns, LpSolve.EQ, 0);
			}

			if (canceller.isCancelled()) {
				return Double.NaN;
			}
		}

		solver.setAddRowmode(false);

		if (canceller.isCancelled()) {
			return Double.NaN;
		}

		solver.solve();

		return solver.getObjective();
	}

}
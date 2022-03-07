package org.processmining.stochasticlabelledpetrinets.probability;

import java.util.ArrayList;
import java.util.BitSet;

import org.processmining.framework.plugin.ProMCanceller;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

public class CrossProductResultSolver implements CrossProductResult {

	static {
		System.loadLibrary("lpsolve55");
		System.loadLibrary("lpsolve55j");
	}

	private int initialState;
	private TIntList finalStates;
	private int maxState;
	private ArrayList<int[]> nextStates;
	private ArrayList<double[]> nextStateProbabilities;

	private BitSet deDuplicationCache = new BitSet();

	public CrossProductResultSolver() {
		initialState = -1;
		maxState = -1;
		finalStates = new TIntArrayList();
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
		finalStates.add(stateIndex);
		maxState = Math.max(maxState, stateIndex);
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
		LpSolve solver = LpSolve.makeLp(maxState, maxState);

		solver.setDebug(false);
		solver.setVerbose(0);

		solver.setAddRowmode(true);

		for (int stateIndex = 0; stateIndex < nextStates.size(); stateIndex++) {
			solver.setRowex(stateIndex, nextStates.get(stateIndex).length, nextStateProbabilities.get(stateIndex),
					nextStates.get(stateIndex));

			if (canceller.isCancelled()) {
				return Double.NaN;
			}
		}

		solver.setAddRowmode(false);

		if (canceller.isCancelled()) {
			return Double.NaN;
		}

		solver.solve();
	}

}
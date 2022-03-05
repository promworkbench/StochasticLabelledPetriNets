package org.processmining.stochasticlabelledpetrinets.probability;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotNode;

import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class CrossProductResultDot implements CrossProductResult {

	private Dot dot;
	private TIntObjectMap<DotNode> index2dotNode;

	public CrossProductResultDot() {
		dot = new Dot();
		index2dotNode = new TIntObjectHashMap<DotNode>(10, 0.5f, -1);
	}

	public void reportFinalState(int stateIndex) {
		// TODO Auto-generated method stub

	}

	public void reportNonFinalState(int stateIndex, TIntList nextStateIndexes, TDoubleList nextStateProbabilities) {
		DotNode source = index2dotNode.get(stateIndex);
		if (source == null) {
			source = dot.addNode(stateIndex + "");
			index2dotNode.put(stateIndex, source);
		}

		TIntIterator itI = nextStateIndexes.iterator();
		TDoubleIterator itP = nextStateProbabilities.iterator();
		while (itI.hasNext()) {
			int targetIndex = itI.next();
			double targetProbability = itP.next();

			DotNode target = index2dotNode.get(targetIndex);
			if (target == null) {
				target = dot.addNode(targetIndex + "");
				index2dotNode.put(targetIndex, target);
			}

			dot.addEdge(source, target, targetProbability + "");
		}
	}
}
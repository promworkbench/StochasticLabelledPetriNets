package org.processmining.stochasticlabelledpetrinets.visualisation;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNet;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class StochasticLabelledPetriNet2Dot {
	public static Dot toDot(StochasticLabelledPetriNet net) {
		Dot dot = new Dot();

		TIntObjectMap<DotNode> place2dotNode = new TIntObjectHashMap<>(10, 0.5f, -1);
		TIntObjectMap<DotNode> transition2dotNode = new TIntObjectHashMap<>(10, 0.5f, -1);

		for (int place = 0; place < net.getNumberOfPlaces(); place++) {
			DotNode dotNode = dot.addNode("" + place);
			dotNode.setOption("shape", "circle");
			place2dotNode.put(place, dotNode);
		}

		for (int transition = 0; transition < net.getNumberOfTransitions(); transition++) {
			DotNode dotNode;

			if (net.isTransitionSilent(transition)) {
				dotNode = dot.addNode("" + net.getTransitionWeight(transition) + " (" + transition + ")");
			} else {
				dotNode = dot.addNode(net.getTransitionLabel(transition) + " (" + transition + ")\\n"
						+ net.getTransitionWeight(transition));
			}

			dotNode.setOption("shape", "box");
			transition2dotNode.put(transition, dotNode);
		}

		for (int transition = 0; transition < net.getNumberOfTransitions(); transition++) {
			for (int place : net.getOutputPlaces(transition)) {
				dot.addEdge(transition2dotNode.get(transition), place2dotNode.get(place));
			}

			for (int place : net.getInputPlaces(transition)) {
				dot.addEdge(place2dotNode.get(place), transition2dotNode.get(transition));
			}
		}

		return dot;
	}
}

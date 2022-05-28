package org.processmining.stochasticlabelledpetrinets.plugins;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNetSimpleWeights;
import org.processmining.stochasticlabelledpetrinets.visualisation.StochasticLabelledPetriNet2Dot;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public abstract class StochasticLabelledPetriNetVisualisationPlugin {

	public DotPanel visualise(StochasticLabelledPetriNetSimpleWeights net) {
		Dot dot = StochasticLabelledPetriNet2Dot.toDot(net);

		TIntObjectMap<DotNode> place2node = new TIntObjectHashMap<>();

		for (int place = 0; place < net.getNumberOfPlaces(); place++) {
			DotNode dotNode = dot.addNode("");

			place2node.put(place, dotNode);

			decoratePlace(net, place, dotNode);
		}

		for (int transition = 0; transition < net.getNumberOfTransitions(); transition++) {
			DotNode dotNode;
			if (net.isTransitionSilent(transition)) {
				dotNode = dot.addNode("");
			} else {
				dotNode = dot.addNode(net.getTransitionLabel(transition));
			}

			for (int place : net.getOutputPlaces(transition)) {
				dot.addEdge(dotNode, place2node.get(place));
			}

			for (int place : net.getInputPlaces(transition)) {
				dot.addEdge(place2node.get(place), dotNode);
			}

			decorateTransition(net, transition, dotNode);
		}

		return new DotPanel(dot);
	}

	public abstract void decoratePlace(StochasticLabelledPetriNetSimpleWeights net, int place, DotNode dotNode);

	public abstract void decorateTransition(StochasticLabelledPetriNetSimpleWeights net, int transition,
			DotNode dotNode);

}
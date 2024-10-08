package org.processmining.stochasticlabelledpetrinets.plugins;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNetSimpleWeights;

@Plugin(name = "Stochastic labelled Petri net exporter", returnLabels = {}, returnTypes = {}, parameterLabels = {
		"Inductive visual Miner alignment", "File" }, userAccessible = true)
@UIExportPlugin(description = "Stochastic labelled Petri net", extension = "slpn")
public class StochasticLabelledPetriNetExportPlugin {
	@PluginVariant(variantLabel = "Dfg export (Directly follows graph)", requiredParameterLabels = { 0, 1 })
	public void exportDefault(UIPluginContext context, StochasticLabelledPetriNetSimpleWeights net, File file) throws IOException {
		export(net, file);
	}

	public static void export(StochasticLabelledPetriNetSimpleWeights net, File file) throws IOException {
		PrintWriter w = null;
		try {
			w = new PrintWriter(file);
			w.println("stochastic labelled Petri net");
			w.println("# number of places");
			w.println(net.getNumberOfPlaces());

			w.println("# initial marking");
			for (int place = 0; place < net.getNumberOfPlaces(); place++) {
				w.println(net.isInInitialMarking(place));
			}

			w.println("# number of transitions");
			w.println(net.getNumberOfTransitions());
			for (int transition = 0; transition < net.getNumberOfTransitions(); transition++) {
				w.println("# transition " + transition);
				if (net.isTransitionSilent(transition)) {
					w.println("silent");
				} else {
					w.println("label " + StringEscapeUtils.escapeJava(net.getTransitionLabel(transition)));
				}
				w.println("# weight ");
				w.println(net.getTransitionWeight(transition));

				w.println("# number of input places");
				w.println(net.getInputPlaces(transition).length);
				for (int place : net.getInputPlaces(transition)) {
					w.println(place);
				}

				w.println("# number of output places");
				w.println(net.getOutputPlaces(transition).length);
				for (int place : net.getOutputPlaces(transition)) {
					w.println(place);
				}
			}
		} finally {
			if (w != null) {
				w.close();
			}
		}
	}
	
	public static StringBuilder export(StochasticLabelledPetriNetSimpleWeights net) {
		StringBuilder w = new StringBuilder();
		
		w.append("stochastic labelled Petri net\n");
		w.append("# number of places\n");
		w.append(net.getNumberOfPlaces() + "\n");

		w.append("# initial marking\n");
		for (int place = 0; place < net.getNumberOfPlaces(); place++) {
			w.append(net.isInInitialMarking(place) + "\n");
		}

		w.append("# number of transitions\n");
		w.append(net.getNumberOfTransitions() + "\n");
		for (int transition = 0; transition < net.getNumberOfTransitions(); transition++) {
			w.append("# transition " + transition + "\n");
			if (net.isTransitionSilent(transition)) {
				w.append("silent\n");
			} else {
				w.append("label " + StringEscapeUtils.escapeJava(net.getTransitionLabel(transition)) + "\n");
			}
			w.append("# weight \n");
			w.append(net.getTransitionWeight(transition) + "\n");

			w.append("# number of input places\n");
			w.append(net.getInputPlaces(transition).length + "\n");
			for (int place : net.getInputPlaces(transition)) {
				w.append(place + "\n");
			}

			w.append("# number of output places\n");
			w.append(net.getOutputPlaces(transition).length + "\n");
			for (int place : net.getOutputPlaces(transition)) {
				w.append(place + "\n");
			}
		}
		
		return w;
	}
}

package org.processmining.stochasticlabelledpetrinets.plugins;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNet;

@Plugin(name = "Stochastic labelled Petri net exporter", returnLabels = {}, returnTypes = {}, parameterLabels = {
		"Inductive visual Miner alignment", "File" }, userAccessible = true)
@UIExportPlugin(description = "Stochastic labelled Petri net", extension = "slpn")
public class StochasticLabelledPetriNetExportPlugin {
	@PluginVariant(variantLabel = "Dfg export (Directly follows graph)", requiredParameterLabels = { 0, 1 })
	public void exportDefault(UIPluginContext context, StochasticLabelledPetriNet net, File file) throws IOException {
		export(net, file);
	}

	public static void export(StochasticLabelledPetriNet net, File file) throws IOException {
		PrintWriter w = null;
		try {
			w = new PrintWriter(file);
			w.println("number of places");
			w.println(net.getNumberOfPlaces());
			w.println(" ");
		} finally {
			if (w != null) {
				w.close();
			}
		}
	}
}

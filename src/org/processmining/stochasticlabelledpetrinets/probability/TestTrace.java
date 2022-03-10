package org.processmining.stochasticlabelledpetrinets.probability;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.stochasticlabelledpetrinets.StochasticLabelledPetriNetImpl;
import org.processmining.stochasticlabelledpetrinets.plugins.StochasticLabelledPetriNetImportPlugin;

import lpsolve.LpSolveException;

public class TestTrace {
	public static void main(String[] args)
			throws NumberFormatException, FileNotFoundException, IOException, LpSolveException {
		StochasticLabelledPetriNetImpl netA = StochasticLabelledPetriNetImportPlugin.read(new FileInputStream(
				new File("/home/sander/Documents/svn/51 - hybrid stochastic models - marco/PetriNet.slpn")));

		String[] trace = new String[] { "a", "c" };
		FollowerSemanticsTrace systemB = new FollowerSemanticsTrace(trace);

		ProMCanceller canceller = new ProMCanceller() {
			public boolean isCancelled() {
				return false;
			}
		};

		{
			CrossProductResultDot resultDot = new CrossProductResultDot();
			CrossProduct.traverse(netA, systemB, resultDot, canceller);

			System.out.println(resultDot.toDot());
		}

		{
			CrossProductResultSolver resultSolver = new CrossProductResultSolver();
			CrossProduct.traverse(netA, systemB, resultSolver, canceller);

			System.out.println(resultSolver.solve(canceller));
		}

	}
}
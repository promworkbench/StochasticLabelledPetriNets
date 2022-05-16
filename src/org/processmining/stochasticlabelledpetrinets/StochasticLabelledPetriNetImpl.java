package org.processmining.stochasticlabelledpetrinets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

public class StochasticLabelledPetriNetImpl implements StochasticLabelledPetriNetEditable {

	private ArrayList<String> transitionLabels;
	private TIntIntMap initialMarking;
	private List<int[]> inputPlaces = new ArrayList<>();
	private List<int[]> outputPlaces = new ArrayList<>();
	private List<int[]> inputTransitions = new ArrayList<>();
	private List<int[]> outputTransitions = new ArrayList<>();

	public StochasticLabelledPetriNetImpl() {
		transitionLabels = new ArrayList<>();
		initialMarking = new TIntIntHashMap(10, 0.5f, -1, 0);
		inputPlaces = new ArrayList<>();
		outputPlaces = new ArrayList<>();
	}

	public int getNumberOfTransitions() {
		return transitionLabels.size();
	}

	public int getNumberOfPlaces() {
		return inputTransitions.size();
	}

	public String getTransitionLabel(int transition) {
		return transitionLabels.get(transition);
	}

	public boolean isTransitionSilent(int transition) {
		return transitionLabels.get(transition) == null;
	}

	public int isInInitialMarking(int place) {
		return initialMarking.get(place);
	}

	public int[] getInputPlaces(int transition) {
		return inputPlaces.get(transition);
	}

	public int[] getOutputPlaces(int transition) {
		return outputPlaces.get(transition);
	}

	public int[] getInputTransitions(int place) {
		return inputTransitions.get(place);
	}

	public int[] getOutputTransitions(int place) {
		return outputTransitions.get(place);
	}

	public void setTransitionLabel(int transition, String label) {
		transitionLabels.set(transition, label);
	}

	public void makeTransitionSilent(int transition) {
		transitionLabels.set(transition, null);
	}

	public int addTransition(String label, double weight) {
		inputPlaces.add(new int[0]);
		outputPlaces.add(new int[0]);
		transitionLabels.add(label);
		return transitionLabels.size() - 1;
	}

	public int addTransition(double weight) {
		return addTransition(null, weight);
	}

	public int addPlace() {
		inputTransitions.add(new int[0]);
		outputTransitions.add(new int[0]);
		return inputTransitions.size() - 1;
	}

	public void addPlaceToInitialMarking(int place, int cardinality) {
		initialMarking.adjustOrPutValue(place, cardinality, cardinality);
	}

	public void addPlaceToInitialMarking(int place) {
		addPlaceToInitialMarking(place, 1);
	}

	public void addPlaceTransitionArc(int place, int transition) {
		addPlaceTransitionArc(place, transition, 1);
	}

	public void addPlaceTransitionArc(int place, int transition, int cardinality) {
		int[] xOutputTransitions = outputTransitions.get(place);
		int[] xInputPlaces = inputPlaces.get(transition);

		if (cardinality > 0) {
			//add arcs
			xOutputTransitions = Arrays.copyOf(xOutputTransitions, xOutputTransitions.length + cardinality);
			xInputPlaces = Arrays.copyOf(xInputPlaces, xInputPlaces.length + cardinality);
			for (int i = 0; i < cardinality; i++) {
				xOutputTransitions[i + xOutputTransitions.length - cardinality] = transition;
				xInputPlaces[i + xInputPlaces.length - cardinality] = place;
			}
		} else {
			//remove arcs or no action
			for (int i = 0; i < cardinality; i++) {
				xOutputTransitions = ArrayUtils.removeElement(xOutputTransitions, transition);
			}
			for (int i = 0; i < cardinality; i++) {
				xInputPlaces = ArrayUtils.removeElement(xInputPlaces, place);
			}
		}
		outputTransitions.set(place, xOutputTransitions);
		inputPlaces.set(transition, xInputPlaces);
	}

	public void addTransitionPlaceArc(int transition, int place) {
		addTransitionPlaceArc(transition, place, 1);
	}

	public void addTransitionPlaceArc(int transition, int place, int cardinality) {
		int[] xOutputPlaces = outputPlaces.get(transition);
		int[] xInputTransitions = inputTransitions.get(place);

		if (cardinality > 0) {
			//add arcs
			xOutputPlaces = Arrays.copyOf(xOutputPlaces, xOutputPlaces.length + cardinality);
			xInputTransitions = Arrays.copyOf(xInputTransitions, xInputTransitions.length + cardinality);
			for (int i = 0; i < cardinality; i++) {
				xOutputPlaces[i + xOutputPlaces.length - cardinality] = place;
				xInputTransitions[i + xInputTransitions.length - cardinality] = transition;
			}
		} else {
			//remove arcs or no action
			for (int i = 0; i < cardinality; i++) {
				xOutputPlaces = ArrayUtils.removeElement(xOutputPlaces, place);
			}
			for (int i = 0; i < cardinality; i++) {
				xInputTransitions = ArrayUtils.removeElement(xInputTransitions, transition);
			}
		}
		inputTransitions.set(place, xInputTransitions);
		outputPlaces.set(transition, xOutputPlaces);
	}

}
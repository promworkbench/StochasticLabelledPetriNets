package org.processmining.stochasticlabelledpetrinets.probability;

public abstract class FollowerSemanticsTrace implements FollowerSemantics<Integer> {

	private final String[] trace;

	public FollowerSemanticsTrace(String[] trace) {
		this.trace = trace;
	}

	public Integer getInitialState() {
		return 0;
	}

	public Integer[] takeStep(Integer state, String label) {
		if (trace[state].equals(label)) {
			return new Integer[] { state + 1 };
		}
		return null;
	}

	public boolean isFinalState(Integer state) {
		return state == trace.length;
	}

}

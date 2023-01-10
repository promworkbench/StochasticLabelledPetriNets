package org.processmining.stochasticlabelledpetrinets.probability;

import java.util.Arrays;
import java.util.Objects;

public class ABState<B> {

	public ABState(byte[] stateA, B stateB) {
		this.stateA = stateA;
		this.stateB = stateB;
	}

	private final byte[] stateA;
	private final B stateB;

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(getStateA());
		result = prime * result + Objects.hash(getStateB());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		ABState<B> other = (ABState<B>) obj;
		return Arrays.equals(getStateA(), other.getStateA()) && Objects.equals(getStateB(), other.getStateB());
	}

	public String toString() {
		return Arrays.toString(getStateA()) + "-" + getStateB().toString();
	}

	public byte[] getStateA() {
		return stateA;
	}

	public B getStateB() {
		return stateB;
	}
}
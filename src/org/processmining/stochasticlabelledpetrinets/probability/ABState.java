package org.processmining.stochasticlabelledpetrinets.probability;

import java.util.Arrays;
import java.util.Objects;

public class ABState<B> {

	public ABState(byte[] stateA, B stateB) {
		this.stateA = stateA;
		this.stateB = stateB;
	}

	final byte[] stateA;
	final B stateB;

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(stateA);
		result = prime * result + Objects.hash(stateB);
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
		return Arrays.equals(stateA, other.stateA) && Objects.equals(stateB, other.stateB);
	}

	public String toString() {
		return Arrays.toString(stateA) + "-" + stateB.toString();
	}
}
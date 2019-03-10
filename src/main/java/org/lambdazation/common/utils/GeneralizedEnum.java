package org.lambdazation.common.utils;

public abstract class GeneralizedEnum<E extends GeneralizedEnum<E>> implements Comparable<E> {
	private final String name;
	private final int ordinal;

	protected GeneralizedEnum(String name, int ordinal) {
		this.name = name;
		this.ordinal = ordinal;
	}

	public String name() {
		return name;
	}

	public int ordinal() {
		return ordinal;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int compareTo(E o) {
		return ordinal - o.ordinal();
	}
}

package org.lambdazation.common.util.data;

import java.util.function.Function;

public final class Product<A, B> {
	public final A left;
	public final B right;

	public Product(A left, B right) {
		this.left = left;
		this.right = right;
	}

	public A left() {
		return left;
	}

	public B right() {
		return right;
	}

	public <C> C match(Function<A, Function<B, C>> f) {
		return f.apply(left).apply(right);
	}

	public static <A, B> Function<A, Function<B, Product<A, B>>> ofProduct() {
		return left -> right -> new Product<>(left, right);
	}

	public static <A, B> Function<B, Product<A, B>> ofProduct(A left) {
		return right -> new Product<>(left, right);
	}

	public static <A, B> Product<A, B> ofProduct(A left, B right) {
		return new Product<>(left, right);
	}

	public static <A, B> Function<Product<A, B>, A> projectionLeft() {
		return product -> product.left;
	}

	public static <A, B> Function<Product<A, B>, B> projectionRight() {
		return product -> product.right;
	}

	public static <A, B, C> Function<Product<A, B>, Function<Function<A, Function<B, C>>, C>> matchProduct() {
		return product -> f -> product.match(f);
	}

	public static <A, B, C> Function<Function<A, Function<B, C>>, C> matchProduct(Product<A, B> product) {
		return f -> product.match(f);
	}

	public static <A, B, C> C matchProduct(Product<A, B> product, Function<A, Function<B, C>> f) {
		return product.match(f);
	}

	public static <A, B, C> Function<Product<A, B>, C> unboxProduct(Function<A, Function<B, C>> f) {
		return product -> product.match(f);
	}
}

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

	public Product<B, A> commute() {
		return new Product<>(right, left);
	}

	public Product<B, Unit> leftShift() {
		return new Product<>(right, Unit.UNIT);
	}

	public Product<Unit, A> rightShift() {
		return new Product<>(Unit.UNIT, left);
	}

	public <C> C match(Function<A, Function<B, C>> f) {
		return f.apply(left).apply(right);
	}

	public static <A, B> Function<A, Function<B, Product<A, B>>> ofProduct() {
		return left -> right -> new Product<>(left, right);
	}

	public static <A, B> Product<A, B> ofProduct(A left, B right) {
		return new Product<>(left, right);
	}

	public static <A, B> Function<B, Product<A, B>> ofProductLeft(A left) {
		return right -> new Product<>(left, right);
	}

	public static <A, B> Function<A, Product<A, B>> ofProductRight(B right) {
		return left -> new Product<>(left, right);
	}

	public static <A> Product<A, A> ofProductBoth(A both) {
		return new Product<>(both, both);
	}

	public static <A, B> Function<Product<A, B>, A> projectionLeft() {
		return product -> product.left;
	}

	public static <A, B> Function<Product<A, B>, B> projectionRight() {
		return product -> product.right;
	}

	public static <A, B> Function<Product<A, B>, Product<B, Unit>> leftShiftProduct() {
		return product -> product.leftShift();
	}

	public static <A, B> Product<B, Unit> leftShiftProduct(Product<A, B> product) {
		return product.leftShift();
	}

	public static <A, B> Function<Product<A, B>, Product<Unit, A>> rightShiftProduct() {
		return product -> product.rightShift();
	}

	public static <A, B> Product<Unit, A> rightShiftProduct(Product<A, B> product) {
		return product.rightShift();
	}

	public static <A, B, C> Function<Product<A, Product<B, C>>, Product<Product<A, B>, C>> assocLeftProduct() {
		return product -> new Product<>(new Product<>(product.left, product.right.left), product.right.right);
	}

	public static <A, B, C> Product<Product<A, B>, C> assocLeftProduct(Product<A, Product<B, C>> product) {
		return new Product<>(new Product<>(product.left, product.right.left), product.right.right);
	}

	public static <A, B, C> Function<Product<Product<A, B>, C>, Product<A, Product<B, C>>> assocRightProduct() {
		return product -> new Product<>(product.left.left, new Product<>(product.left.right, product.right));
	}

	public static <A, B, C> Product<A, Product<B, C>> assocRightProduct(Product<Product<A, B>, C> product) {
		return new Product<>(product.left.left, new Product<>(product.left.right, product.right));
	}

	public static <A, B> Function<Product<A, B>, Product<B, A>> commuteProduct() {
		return product -> product.commute();
	}

	public static <A, B> Product<B, A> commuteProduct(Product<A, B> product) {
		return product.commute();
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

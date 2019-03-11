package org.lambdazation.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.lambdazation.Lambdazation;
import org.lamcalcj.ast.Lambda.Abs;
import org.lamcalcj.ast.Lambda.Identifier;
import org.lamcalcj.ast.Lambda.Term;
import org.lamcalcj.ast.Lambda.Var;
import org.lamcalcj.utils.Utils;

public final class ItemLambdaCrystal extends Item {
	public final Lambdazation lambdazation;

	public ItemLambdaCrystal(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (isInGroup(group)) {
			 // TODO Random value
			Identifier identifier = new Identifier("x");
			Term term = new Abs(identifier, new Var(identifier));
			TermState termState = TermState.BETA_ETA_NORMAL_FORM;

			items.add(builder()
				.capacity(1024)
				.energy(1024)
				.term(term)
				.termState(termState)
				.build());
		}
	}

	public Optional<Integer> getCapacity(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			return Optional.empty();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		if (!tag.contains("capacity", 3))
			return Optional.empty();
		int capacity = tag.getInt("capacity");
		return Optional.of(capacity);
	}

	public void setCapacity(ItemStack itemStack, int capacity) {
		if (!equals(itemStack.getItem()))
			return;

		NBTTagCompound tag = itemStack.getOrCreateTag();
		tag.setInt("capacity", capacity);
	}

	public Optional<Integer> getEnergy(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			return Optional.empty();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		if (!tag.contains("energy", 3))
			return Optional.empty();
		int energy = tag.getInt("energy");
		return Optional.of(energy);
	}

	public void setEnergy(ItemStack itemStack, int energy) {
		if (!equals(itemStack.getItem()))
			return;

		NBTTagCompound tag = itemStack.getOrCreateTag();
		tag.setInt("energy", energy);
	}

	public Optional<Term> getTerm(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			return Optional.empty();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		if (!tag.contains("term", 7))
			return Optional.empty();
		byte[] serializedTerm = tag.getByteArray("term");

		Term term;
		try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(serializedTerm))) {
			term = lambdazation.lambdazationTermFactory.deserializeTerm(dis);
		} catch (IOException e) {
			return Optional.empty();
		}
		return Optional.of(term);
	}

	public void setTerm(ItemStack itemStack, Term term) {
		if (!equals(itemStack.getItem()))
			return;

		ByteArrayOutputStream baos;
		try (DataOutputStream dos = new DataOutputStream(baos = new ByteArrayOutputStream())) {
			lambdazation.lambdazationTermFactory.serializeTerm(term, dos);
		} catch (IOException e) {
			return;
		}
		byte[] serializedTerm = baos.toByteArray();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		tag.setByteArray("term", serializedTerm);
	}

	public Optional<TermState> getTermState(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			return Optional.empty();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		if (!tag.contains("termState", 1))
			return Optional.empty();
		byte termStateOrdinal = tag.getByte("termState");

		if (termStateOrdinal < 0 || termStateOrdinal >= TermState.values().length)
			return Optional.empty();
		TermState termState = TermState.values()[termStateOrdinal];
		return Optional.of(termState);
	}

	public void setTermState(ItemStack itemStack, TermState termState) {
		if (!equals(itemStack.getItem()))
			return;

		byte termStateOrdinal = (byte) termState.ordinal();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		tag.setByte("termState", termStateOrdinal);
	}

	public boolean isAlphaEquivalent(ItemStack firstItemStack, ItemStack secondItemStack) {
		if (!equals(firstItemStack.getItem()) || !equals(secondItemStack.getItem()))
			return false;

		// GIB ME DO-NOTATION!
		return getTerm(firstItemStack)
			.flatMap(firstTerm -> getTerm(secondItemStack)
				.map(secondTerm -> Utils.isAlphaEquivalent(firstTerm, secondTerm, Utils.isAlphaEquivalent$default$3())))
			.orElse(false);
	}

	public Builder builder() {
		return new Builder();
	}

	public enum TermState {
		REDUCIBLE_FORM, BETA_NORMAL_FORM, BETA_ETA_NORMAL_FORM
	}

	public final class Builder {
		private Integer capacity;
		private Integer energy;
		private Term term;
		private TermState termState;

		public Builder capacity(int capacity) {
			this.capacity = capacity;
			return this;
		}

		public Builder energy(int energy) {
			this.energy = energy;
			return this;
		}

		public Builder term(Term term) {
			this.term = term;
			return this;
		}

		public Builder termState(TermState termState) {
			this.termState = termState;
			return this;
		}

		private void validateState() {
			if (capacity == null || energy == null || term == null || termState == null)
				throw new IllegalStateException("Property uninitialized");
		}

		public ItemStack build() {
			validateState();

			ItemStack itemStack = new ItemStack(lambdazation.lambdazationItems.itemLambdaCrystal);
			setCapacity(itemStack, capacity);
			setEnergy(itemStack, energy);
			setTerm(itemStack, term);
			setTermState(itemStack, termState);

			return itemStack;
		}
	}
}

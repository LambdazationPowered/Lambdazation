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
import java.io.IOError;
import java.io.IOException;

import org.lambdazation.Lambdazation;
import org.lamcalcj.ast.Lambda.Abs;
import org.lamcalcj.ast.Lambda.Identifier;
import org.lamcalcj.ast.Lambda.Term;
import org.lamcalcj.ast.Lambda.Var;

public final class ItemLambdaCrystal extends Item {
	public final Lambdazation lambdazation;

	public ItemLambdaCrystal(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (isInGroup(group)) {
			Identifier identifier = new Identifier("x");
			Term term = new Abs(identifier, new Var(identifier));
			TermState termState = TermState.BETA_ETA_NORMAL_FORM;

			items.add(builder()
				.capacity(0)
				.energy(0)
				.term(term)
				.termState(termState)
				.build());
		}
	}

	public int getCapacity(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			throw new IllegalStateException();

		NBTTagCompound tag = itemStack.getTag();
		if (!tag.contains("capacity", 3))
			return 0;
		int capacity = tag.getInt("capacity");
		return capacity;
	}

	public int getEnergy(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			throw new IllegalStateException();

		NBTTagCompound tag = itemStack.getTag();
		if (!tag.contains("energy", 3))
			return 0;
		int energy = tag.getInt("energy");
		return energy;
	}

	public Term getTerm(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			throw new IllegalStateException();

		NBTTagCompound tag = itemStack.getTag();
		if (!tag.contains("term", 7))
			return null;
		byte[] serializedTerm = tag.getByteArray("term");

		try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(serializedTerm))) {
			Term term = lambdazation.lambdazationTermFactory.deserializeTerm(dis);
			return term;
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	public TermState getTermState(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			throw new IllegalStateException();

		NBTTagCompound tag = itemStack.getTag();
		if (!tag.contains("termState", 1))
			return null;
		byte termStateOrdinal = tag.getByte("termState");

		if (termStateOrdinal < 0 || termStateOrdinal >= TermState.values().length)
			throw new IndexOutOfBoundsException();
		TermState termState = TermState.values()[termStateOrdinal];
		return termState;
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

			ByteArrayOutputStream baos;
			try (DataOutputStream dos = new DataOutputStream(baos = new ByteArrayOutputStream())) {
				lambdazation.lambdazationTermFactory.serializeTerm(term, dos);
			} catch (IOException e) {
				throw new IOError(e);
			}
			byte[] serializedTerm = baos.toByteArray();
			byte termStateOrdinal = (byte) termState.ordinal();

			ItemStack itemStack = new ItemStack(lambdazation.lambdazationItems.itemLambdaCrystal);
			NBTTagCompound tag = itemStack.getOrCreateTag();
			tag.setInt("capacity", capacity);
			tag.setInt("energy", energy);
			tag.setByteArray("term", serializedTerm);
			tag.setByte("termState", termStateOrdinal);

			return itemStack;
		}
	}
}

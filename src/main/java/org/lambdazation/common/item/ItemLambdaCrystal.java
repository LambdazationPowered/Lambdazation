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
import org.lambdazation.common.core.LambdazationTermFactory.TermState;
import org.lambdazation.common.core.LambdazationTermFactory.TermStatistics;
import org.lambdazation.common.utils.GeneralizedBuilder;
import org.lamcalcj.ast.Lambda.Term;

public final class ItemLambdaCrystal extends Item {
	public final Lambdazation lambdazation;

	public ItemLambdaCrystal(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (isInGroup(group)) {
			// TODO Magic value
			int capacity = 1024;
			int energy = 1024;

			items.add(builder()
				.capacity(capacity)
				.energy(energy)
				.accept(lambdazation.lambdazationTermFactory.predefTermId::withCrystal)
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

		int energy = getEnergy(itemStack).orElse(0);
		itemStack.setDamage(Integer.MAX_VALUE - (int) ((double) energy / (double) capacity * Integer.MAX_VALUE));
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

		int capacity = getCapacity(itemStack).orElse(0);
		itemStack.setDamage(Integer.MAX_VALUE - (int) ((double) energy / (double) capacity * Integer.MAX_VALUE));
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

		TermStatistics termStatistics;
		ByteArrayOutputStream baos;
		try (DataOutputStream dos = new DataOutputStream(baos = new ByteArrayOutputStream())) {
			termStatistics = lambdazation.lambdazationTermFactory.serializeTerm(term, dos);
		} catch (IOException e) {
			return;
		}
		byte[] serializedTerm = baos.toByteArray();
		int termSize = termStatistics.termSize;
		int termDepth = termStatistics.termDepth;
		byte termStateOrdinal = (byte) termStatistics.termState.ordinal();
		int termHash = termStatistics.termHash;

		NBTTagCompound tag = itemStack.getOrCreateTag();
		tag.setByteArray("term", serializedTerm);
		tag.setInt("termSize", termSize);
		tag.setInt("termDepth", termDepth);
		tag.setByte("termState", termStateOrdinal);
		tag.setInt("termHash", termHash);
	}

	public Optional<Integer> getTermSize(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			return Optional.empty();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		if (!tag.contains("termSize", 3))
			return Optional.empty();
		int termSize = tag.getInt("termSize");
		return Optional.of(termSize);
	}

	public Optional<Integer> getTermDepth(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			return Optional.empty();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		if (!tag.contains("termDepth", 3))
			return Optional.empty();
		int termDepth = tag.getInt("termDepth");
		return Optional.of(termDepth);
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

	public Optional<Integer> getTermHash(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			return Optional.empty();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		if (!tag.contains("termHash", 3))
			return Optional.empty();
		int termHash = tag.getInt("termHash");
		return Optional.of(termHash);
	}

	public boolean isAlphaEquivalent(ItemStack firstItemStack, ItemStack secondItemStack) {
		if (!equals(firstItemStack.getItem()) || !equals(secondItemStack.getItem()))
			return false;

		NBTTagCompound firstTag = firstItemStack.getOrCreateTag();
		if (!firstTag.contains("term", 7))
			return false;
		byte[] firstSerializedTerm = firstTag.getByteArray("term");
		NBTTagCompound secondTag = secondItemStack.getOrCreateTag();
		if (!secondTag.contains("term", 7))
			return false;
		byte[] secondSerializedTerm = secondTag.getByteArray("term");

		boolean result;
		try (DataInputStream firstInput = new DataInputStream(new ByteArrayInputStream(firstSerializedTerm));
			DataInputStream secondInput = new DataInputStream(new ByteArrayInputStream(secondSerializedTerm))) {
			result = lambdazation.lambdazationTermFactory.isAlphaEquivalent(firstInput, secondInput);
		} catch (IOException e) {
			return false;
		}
		return result;
	}

	public Builder builder() {
		return new Builder();
	}

	public final class Builder implements GeneralizedBuilder<Builder, ItemStack> {
		private Integer capacity;
		private Integer energy;
		private Term term;

		Builder() {

		}

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

		private void validateState() {
			if (capacity == null || energy == null || term == null)
				throw new IllegalStateException("Property uninitialized");
		}

		@Override
		public Builder concrete() {
			return this;
		}

		@Override
		public ItemStack build() {
			validateState();

			ItemStack itemStack = new ItemStack(lambdazation.lambdazationItems.itemLambdaCrystal);
			setCapacity(itemStack, capacity);
			setEnergy(itemStack, energy);
			setTerm(itemStack, term);

			return itemStack;
		}
	}
}

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
import java.util.Arrays;
import java.util.Optional;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.core.LambdazationTermFactory.TermMetadata;
import org.lambdazation.common.core.LambdazationTermFactory.TermNamer;
import org.lambdazation.common.core.LambdazationTermFactory.TermNaming;
import org.lambdazation.common.core.LambdazationTermFactory.TermRef;
import org.lambdazation.common.core.LambdazationTermFactory.TermState;
import org.lambdazation.common.core.LambdazationTermFactory.TermStatistics;
import org.lambdazation.common.util.GeneralizedBuilder;
import org.lambdazation.common.util.IO;
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
		byte[] serializedTermNaming = tag.contains("termNaming", 7) ? tag.getByteArray("termNaming") : null;

		TermNamer termNamer;
		if (serializedTermNaming == null)
			termNamer = TermNamer.ALPHABET_NAMER;
		else
			try (DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(serializedTermNaming))) {
				termNamer = TermNaming.deserialize(dataInput);
				IO.readEOF(dataInput);
			} catch (IOException e) {
				return Optional.empty();
			}
		Term term;
		try (DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(serializedTerm))) {
			term = lambdazation.lambdazationTermFactory.deserializeTerm(termNamer, dataInput);
			IO.readEOF(dataInput);
		} catch (IOException e) {
			return Optional.empty();
		}
		return Optional.of(term);
	}

	public void setTerm(ItemStack itemStack, Term term) {
		if (!equals(itemStack.getItem()))
			return;

		TermMetadata termMetadata;
		ByteArrayOutputStream termOutput;
		try (DataOutputStream dataOutput = new DataOutputStream(termOutput = new ByteArrayOutputStream())) {
			termMetadata = lambdazation.lambdazationTermFactory.serializeTerm(term, dataOutput);
		} catch (IOException e) {
			return;
		}
		byte[] serializedTerm = termOutput.toByteArray();
		TermNaming termNaming = termMetadata.termNaming;
		ByteArrayOutputStream termNamingOutput;
		try (DataOutputStream dataOutput = new DataOutputStream(termNamingOutput = new ByteArrayOutputStream())) {
			termNaming.serialize(dataOutput);
		} catch (IOException e) {
			return;
		}
		byte[] serializedTermNaming = termNamingOutput.toByteArray();
		TermStatistics termStatistics = termMetadata.termStatistics;
		int termSize = termStatistics.termSize;
		int termDepth = termStatistics.termDepth;
		byte termStateOrdinal = (byte) termStatistics.termState.ordinal();
		int termHash = termStatistics.termHash;

		NBTTagCompound tag = itemStack.getOrCreateTag();
		tag.setByteArray("term", serializedTerm);
		tag.setByteArray("termNaming", serializedTermNaming);
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

	public Optional<TermRef> getTermRef(ItemStack itemStack) {
		if (!equals(itemStack.getItem()))
			return Optional.empty();

		NBTTagCompound tag = itemStack.getOrCreateTag();
		if (!tag.contains("term", 7))
			return Optional.empty();
		if (!tag.contains("termSize", 3))
			return Optional.empty();
		if (!tag.contains("termDepth", 3))
			return Optional.empty();
		if (!tag.contains("termState", 1))
			return Optional.empty();
		if (!tag.contains("termHash", 3))
			return Optional.empty();
		byte[] serializedTerm = tag.getByteArray("term");
		int termSize = tag.getInt("termSize");
		int termDepth = tag.getInt("termDepth");
		byte termStateOrdinal = tag.getByte("termState");
		int termHash = tag.getInt("termHash");

		if (termStateOrdinal < 0 || termStateOrdinal >= TermState.values().length)
			return Optional.empty();
		TermState termState = TermState.values()[termStateOrdinal];
		TermRef termRef = new TermRef(serializedTerm, termSize, termDepth, termState, termHash);

		return Optional.ofNullable(termRef);
	}

	public void setTermRef(ItemStack itemStack, TermRef termRef) {
		if (!equals(itemStack.getItem()))
			return;

		byte[] serializedTerm = termRef.serializedTerm;
		int termSize = termRef.termSize;
		int termDepth = termRef.termDepth;
		byte termStateOrdinal = (byte) termRef.termState.ordinal();
		int termHash = termRef.termHash;

		NBTTagCompound tag = itemStack.getOrCreateTag();
		tag.setByteArray("term", serializedTerm);
		tag.removeTag("termNaming");
		tag.setInt("termSize", termSize);
		tag.setInt("termDepth", termDepth);
		tag.setByte("termState", termStateOrdinal);
		tag.setInt("termHash", termHash);
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

		return Arrays.equals(firstSerializedTerm, secondSerializedTerm);
	}

	public Builder builder() {
		return new Builder();
	}

	public final class Builder implements GeneralizedBuilder<Builder, ItemStack> {
		private Integer capacity;
		private Integer energy;
		private Term term;
		private TermRef termRef;

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
			this.termRef = null;
			return this;
		}

		public Builder termRef(TermRef termRef) {
			this.termRef = termRef;
			this.term = null;
			return this;
		}

		private void validateState() {
			if (capacity == null || energy == null || (term == null && termRef == null))
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
			if (term != null)
				setTerm(itemStack, term);
			if (termRef != null)
				setTermRef(itemStack, termRef);

			return itemStack;
		}
	}
}

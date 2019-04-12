package org.lambdazation.common.network.message;

import java.util.UUID;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.lambdazation.common.network.message.builder.BuilderGeneric;
import org.lambdazation.common.network.message.field.FieldBlockPos;
import org.lambdazation.common.network.message.field.FieldBoolean;
import org.lambdazation.common.network.message.field.FieldByte;
import org.lambdazation.common.network.message.field.FieldByteArray;
import org.lambdazation.common.network.message.field.FieldCharacter;
import org.lambdazation.common.network.message.field.FieldCompoundTag;
import org.lambdazation.common.network.message.field.FieldDouble;
import org.lambdazation.common.network.message.field.FieldEnum;
import org.lambdazation.common.network.message.field.FieldFloat;
import org.lambdazation.common.network.message.field.FieldInteger;
import org.lambdazation.common.network.message.field.FieldItemStack;
import org.lambdazation.common.network.message.field.FieldLong;
import org.lambdazation.common.network.message.field.FieldResourceLocation;
import org.lambdazation.common.network.message.field.FieldShort;
import org.lambdazation.common.network.message.field.FieldString;
import org.lambdazation.common.network.message.field.FieldTextComponent;
import org.lambdazation.common.network.message.field.FieldUniqueId;
import org.lambdazation.common.util.EnumBoolean;
import org.lambdazation.common.util.GeneralizedEnum;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public final class MessageTest implements Message<MessageTest> {
	public final BlockPos p0;
	public final boolean p1;
	public final byte p2;
	public final byte[] p3;
	public final char p4;
	public final NBTTagCompound p5;
	public final double p6;
	public final EnumBoolean p7;
	public final float p8;
	public final int p9;
	public final ItemStack p10;
	public final long p11;
	public final ResourceLocation p12;
	public final short p13;
	public final String p14;
	public final ITextComponent p15;
	public final UUID p16;

	public MessageTest(BuilderGeneric<MessageTest, FieldTest<?>>.Initializer initializer) {
		this.p0 = initializer.get(FieldTest.P0);
		this.p1 = initializer.get(FieldTest.P1);
		this.p2 = initializer.get(FieldTest.P2);
		this.p3 = initializer.get(FieldTest.P3);
		this.p4 = initializer.get(FieldTest.P4);
		this.p5 = initializer.get(FieldTest.P5);
		this.p6 = initializer.get(FieldTest.P6);
		this.p7 = initializer.get(FieldTest.P7);
		this.p8 = initializer.get(FieldTest.P8);
		this.p9 = initializer.get(FieldTest.P9);
		this.p10 = initializer.get(FieldTest.P10);
		this.p11 = initializer.get(FieldTest.P11);
		this.p12 = initializer.get(FieldTest.P12);
		this.p13 = initializer.get(FieldTest.P13);
		this.p14 = initializer.get(FieldTest.P14);
		this.p15 = initializer.get(FieldTest.P15);
		this.p16 = initializer.get(FieldTest.P16);
	}

	@Override
	public MessageTest concrete() {
		return this;
	}

	@Override
	public String toString() {
		MutableBoolean isFirst = new MutableBoolean(true);
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		FieldTest.METADATA.values().forEachOrdered(field -> {
			if (isFirst.getValue())
				isFirst.setFalse();
			else
				builder.append(", ");
			builder.append("\n  ");
			builder.append(field.name());
			builder.append(" : ");
			builder.append(field.get(this));
		});
		builder.append("\n");
		builder.append("}");
		return builder.toString();
	}

	public static abstract class FieldTest<T> extends GeneralizedEnum<FieldTest<?>>
		implements Message.Field<MessageTest, FieldTest<?>, T> {
		public static final FieldTest<BlockPos> P0;
		public static final FieldTest<Boolean> P1;
		public static final FieldTest<Byte> P2;
		public static final FieldTest<byte[]> P3;
		public static final FieldTest<Character> P4;
		public static final FieldTest<NBTTagCompound> P5;
		public static final FieldTest<Double> P6;
		public static final FieldTest<EnumBoolean> P7;
		public static final FieldTest<Float> P8;
		public static final FieldTest<Integer> P9;
		public static final FieldTest<ItemStack> P10;
		public static final FieldTest<Long> P11;
		public static final FieldTest<ResourceLocation> P12;
		public static final FieldTest<Short> P13;
		public static final FieldTest<String> P14;
		public static final FieldTest<ITextComponent> P15;
		public static final FieldTest<UUID> P16;

		public static final GeneralizedEnum.Metadata<FieldTest<?>> METADATA;

		static {
			GeneralizedEnum.Metadata.Builder<FieldTest<?>> builder = GeneralizedEnum.Metadata.builder();

			class FieldP0 extends FieldTest<BlockPos> implements FieldBlockPos<MessageTest, FieldTest<?>> {
				FieldP0(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public BlockPos get(MessageTest msg) {
					return msg.p0;
				}

			}
			P0 = builder.withValue("P0", FieldP0::new);

			class FieldP1 extends FieldTest<Boolean> implements FieldBoolean<MessageTest, FieldTest<?>> {
				FieldP1(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public Boolean get(MessageTest msg) {
					return msg.p1;
				}

			}
			P1 = builder.withValue("P1", FieldP1::new);

			class FieldP2 extends FieldTest<Byte> implements FieldByte<MessageTest, FieldTest<?>> {
				FieldP2(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public Byte get(MessageTest msg) {
					return msg.p2;
				}

			}
			P2 = builder.withValue("P2", FieldP2::new);

			class FieldP3 extends FieldTest<byte[]> implements FieldByteArray<MessageTest, FieldTest<?>> {
				FieldP3(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public byte[] get(MessageTest msg) {
					return msg.p3;
				}

			}
			P3 = builder.withValue("P3", FieldP3::new);

			class FieldP4 extends FieldTest<Character> implements FieldCharacter<MessageTest, FieldTest<?>> {
				FieldP4(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public Character get(MessageTest msg) {
					return msg.p4;
				}

			}
			P4 = builder.withValue("P4", FieldP4::new);

			class FieldP5 extends FieldTest<NBTTagCompound> implements FieldCompoundTag<MessageTest, FieldTest<?>> {
				FieldP5(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public NBTTagCompound get(MessageTest msg) {
					return msg.p5;
				}

			}
			P5 = builder.withValue("P5", FieldP5::new);

			class FieldP6 extends FieldTest<Double> implements FieldDouble<MessageTest, FieldTest<?>> {
				FieldP6(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public Double get(MessageTest msg) {
					return msg.p6;
				}

			}
			P6 = builder.withValue("P6", FieldP6::new);

			class FieldP7 extends FieldTest<EnumBoolean> implements FieldEnum<MessageTest, FieldTest<?>, EnumBoolean> {
				FieldP7(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public EnumBoolean get(MessageTest msg) {
					return msg.p7;
				}

				@Override
				public Class<EnumBoolean> enumClass() {
					return EnumBoolean.class;
				}

			}
			P7 = builder.withValue("P7", FieldP7::new);

			class FieldP8 extends FieldTest<Float> implements FieldFloat<MessageTest, FieldTest<?>> {
				FieldP8(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public Float get(MessageTest msg) {
					return msg.p8;
				}

			}
			P8 = builder.withValue("P8", FieldP8::new);

			class FieldP9 extends FieldTest<Integer> implements FieldInteger<MessageTest, FieldTest<?>> {
				FieldP9(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public Integer get(MessageTest msg) {
					return msg.p9;
				}

			}
			P9 = builder.withValue("P9", FieldP9::new);

			class FieldP10 extends FieldTest<ItemStack> implements FieldItemStack<MessageTest, FieldTest<?>> {
				FieldP10(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public ItemStack get(MessageTest msg) {
					return msg.p10;
				}

			}
			P10 = builder.withValue("P10", FieldP10::new);

			class FieldP11 extends FieldTest<Long> implements FieldLong<MessageTest, FieldTest<?>> {
				FieldP11(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public Long get(MessageTest msg) {
					return msg.p11;
				}

			}
			P11 = builder.withValue("P11", FieldP11::new);

			class FieldP12 extends FieldTest<ResourceLocation>
				implements FieldResourceLocation<MessageTest, FieldTest<?>> {
				FieldP12(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public ResourceLocation get(MessageTest msg) {
					return msg.p12;
				}

			}
			P12 = builder.withValue("P12", FieldP12::new);

			class FieldP13 extends FieldTest<Short> implements FieldShort<MessageTest, FieldTest<?>> {
				FieldP13(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public Short get(MessageTest msg) {
					return msg.p13;
				}

			}
			P13 = builder.withValue("P13", FieldP13::new);

			class FieldP14 extends FieldTest<String> implements FieldString<MessageTest, FieldTest<?>> {
				FieldP14(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public String get(MessageTest msg) {
					return msg.p14;
				}

			}
			P14 = builder.withValue("P14", FieldP14::new);

			class FieldP15 extends FieldTest<ITextComponent> implements FieldTextComponent<MessageTest, FieldTest<?>> {
				FieldP15(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public ITextComponent get(MessageTest msg) {
					return msg.p15;
				}

			}
			P15 = builder.withValue("P15", FieldP15::new);

			class FieldP16 extends FieldTest<UUID> implements FieldUniqueId<MessageTest, FieldTest<?>> {
				FieldP16(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public UUID get(MessageTest msg) {
					return msg.p16;
				}

			}
			P16 = builder.withValue("P16", FieldP16::new);

			METADATA = builder.build();
		}

		FieldTest(String name, int ordinal) {
			super(name, ordinal);
		}
	}
}

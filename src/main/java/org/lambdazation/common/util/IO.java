package org.lambdazation.common.util;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

public final class IO {
	public static final void readEOF(InputStream inputStream) throws IOException {
		if (inputStream.read() >= 0)
			throw new IOException("EOF expected");
	}

	public static final void skipUTF(DataInput dataInput) throws IOException {
		int utflen = dataInput.readUnsignedShort();
		dataInput.skipBytes(utflen);
	}
}

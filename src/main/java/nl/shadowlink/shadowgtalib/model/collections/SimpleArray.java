/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.shadowlink.shadowgtalib.model.collections;

import nl.shadowlink.file_io.ByteReader;
import nl.shadowlink.shadowgtalib.model.model.Vector4D;
import java.util.ArrayList;

/**
 * @author Kilian
 */
public class SimpleArray<T> {
	public ArrayList<T> Values;
	public int Count;
	public int type;

	private final static int ReadUInt32 = 0;
	private final static int ReadOffset = 1;
	private final static int ReadByte = 2;
	private final static int ReadShort = 3;
	private final static int ReadVector4 = 4;

	/**
	 * Creates a new simplearray from the given type and amount of objects
	 *
	 * @param br
	 *        the bytereader to read the objects from
	 * @param count
	 *        amount of objects to read
	 * @param type
	 *        type of objects to read
	 */
	public SimpleArray(ByteReader br, int count, int type) {
		this.Count = count;
		this.type = type;
		Read(br);
	}

	/**
	 * Read the objects and put them in the array
	 *
	 * @param br
	 *        the bytereader to read them from
	 */
	public void Read(ByteReader br) {
		Values = new ArrayList<T>(Count);

		for (int i = 0; i < Count; i++) {
			Values.add(ReadData(br));
		}
	}

	/**
	 * Returns the class type of objects in the current simplearray
	 *
	 * @param br
	 *        the bytereader to read the objects from
	 * @return the class type
	 */
	public T ReadData(ByteReader br) {

		switch (type) {
			case ReadUInt32:
				Object data = br.ReadUInt32();
				// Message.displayMsgLow("Data: " + data);
				return (T) data;
			case ReadOffset:
				Object offset = br.ReadOffset();
				// Message.displayMsgLow("Offset: " + offset);
				return (T) offset;
			case ReadByte:
				Object Byte = br.ReadByte();
				// Message.displayMsgLow("Byte: " + Byte);
				return (T) Byte;
			case ReadShort:
				Object Short = br.ReadUInt16();
				// Message.displayMsgLow("Short: " + Short);
				return (T) Short;
			case ReadVector4:
				Vector4D vec = Vector4D.readFromByteReader(br);
				vec.print("SimpleArray");
				return (T) vec;
			default:
				Object data2 = br.ReadUInt32();
				return (T) data2;
		}
	}

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.shadowlink.shadowgtalib.model.wdr;

import nl.shadowlink.file_io.ByteReader;
import nl.shadowlink.shadowgtalib.model.collections.PtrCollection;
import nl.shadowlink.shadowgtalib.model.model.Vector4D;
import nl.shadowlink.shadowgtalib.utils.Utils;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kilian
 */
public class DrawableModel {
	public int startOffset;

	public ShaderGroup shaderGroup;
	public Skeleton skeleton;

	public int shaderGroupOffset;
	public int skeletonOffset;

	public Vector4D Center;
	public Vector4D BoundsMin;
	public Vector4D BoundsMax;

	int[] modelOffsets;
	public int levelOfDetailCount;

	public PtrCollection<Model2>[] mModelCollection;

	public Vector4D AbsoluteMax;

	private int Unk1;     // either 1 or 9

	private int Neg1;
	private int Neg2;
	private int Neg3;

	private float Unk2;

	private int Unk3;
	private int Unk4;
	private int Unk5;

	private int Unk6;  // This should be a CSimpleCollection
	private int Unk7;

	public void readSystemMemory(ByteReader br) {

		startOffset = br.getCurrentOffset();

		// Message.displayMsgHigh("VTable: " + br.ReadUInt32());
		// Message.displayMsgHigh("BlockMapAdress: " + br.ReadOffset());

		shaderGroupOffset = br.ReadOffset();
		skeletonOffset = br.ReadOffset();

		// Message.displayMsgHigh("ShaderGroupOffset: " + shaderGroupOffset);
		// Message.displayMsgHigh("skeletonOffset: " + skeletonOffset);

		Center = Vector4D.readFromByteReader(br);
		Center.print("Center");
		BoundsMin = Vector4D.readFromByteReader(br);
		BoundsMin.print("BoundsMin");
		BoundsMax = Vector4D.readFromByteReader(br);
		BoundsMax.print("BoundsMax");

		levelOfDetailCount = 0;
		modelOffsets = new int[4];
		for (int i = 0; i < 4; i++) {
			modelOffsets[i] = br.ReadOffset();
			if (modelOffsets[i] != -1) {
				// Message.displayMsgHigh("Level " + i + " at offset " + modelOffsets[i]);
				levelOfDetailCount++;
			}
		}
		// Message.displayMsgHigh("Level of detail: " + levelOfDetailCount);

		AbsoluteMax = Vector4D.readFromByteReader(br);
		AbsoluteMax.print("AbsoluteMax");

		Unk1 = br.ReadUInt32();

		Neg1 = br.ReadUInt32();
		Neg2 = br.ReadUInt32();
		Neg3 = br.ReadUInt32();

		Unk2 = br.readFloat();

		Unk3 = br.ReadUInt32();
		Unk4 = br.ReadUInt32();
		Unk5 = br.ReadUInt32();

		// Collection<LightAttrs>
		Unk6 = br.ReadUInt32();
		Unk7 = br.ReadUInt32();

		// Message.displayMsgHigh("Unknown: " + Unk1);

		// Message.displayMsgHigh("Neg: " + Neg1 + ", " + Neg2 + ", " + Neg3);

		// Message.displayMsgHigh("Unknown float: " + Unk2);

		// Message.displayMsgHigh("Unknown: " + Unk3 + ", " + Unk4 + ", " + Unk5);

		// Message.displayMsgHigh("Unknown: " + Unk6 + ", " + Unk7);

		if (shaderGroupOffset != -1) {
			// Message.displayMsgHigh("Setting shader offset " + Utils.getHexString(shaderGroupOffset));
			br.setCurrentOffset(shaderGroupOffset);
			shaderGroup = new ShaderGroup(br);
		}

		if (skeletonOffset != -1) {
			br.setCurrentOffset(skeletonOffset);
			skeleton = new Skeleton(/* br */);
		}

		// Message.displayMsgHigh("Created new PtrCollection");
		mModelCollection = new PtrCollection[levelOfDetailCount];
		for (int i = 0; i < levelOfDetailCount; i++) {
			Logger.getGlobal().log(Level.INFO, "PointerCollectionOffset: {0}", modelOffsets[i]);
			br.setCurrentOffset(modelOffsets[i]);
			mModelCollection[i] = new PtrCollection<>(br, 1);
		}

	}

	public String[] getDataNames() {
		String[] names = new String[(17 + levelOfDetailCount)];
		int i = 0;
		names[i] = "shaderGroupOffset";
		i++;
		names[i] = "skeletonOffset";
		i++;
		names[i] = "Center";
		i++;
		names[i] = "BoundsMin";
		i++;
		names[i] = "BoundsMax";
		i++;

		names[i] = "levelOfDetailCount";
		i++;

		for (int i2 = 0; i2 < levelOfDetailCount; i2++) {
			names[i] = "  DetailOffset " + (i2 + 1);
			i++;
		}

		names[i] = "AbsoluteMax";
		i++;

		names[i] = "Unk1";     // either 1 or 9
		i++;

		names[i] = "Neg1";
		i++;
		names[i] = "Neg2";
		i++;
		names[i] = "Neg3";
		i++;

		names[i] = "Unk2";
		i++;

		names[i] = "Unk3";
		i++;
		names[i] = "Unk4";
		i++;
		names[i] = "Unk5";
		i++;

		names[i] = "Unk6";  // This should be a CSimpleCollection
		i++;
		names[i] = "Unk7";

		return names;
	}

	public String[] getDataValues() {
		String[] values = new String[(17 + levelOfDetailCount)];
		int i = 0;
		values[i] = Utils.getHexString(shaderGroupOffset);
		i++;
		values[i] = Utils.getHexString(skeletonOffset);
		i++;

		values[i] = "" + Center;
		i++;
		values[i] = "" + BoundsMin;
		i++;
		values[i] = "" + BoundsMax;
		i++;

		values[i] = "" + levelOfDetailCount;
		i++;

		for (int i2 = 0; i2 < levelOfDetailCount; i2++) {
			values[i] = Utils.getHexString(modelOffsets[i2]);
			i++;
		}

		values[i] = "" + AbsoluteMax;
		i++;

		values[i] = "" + Unk1;     // either 1 or 9
		i++;

		values[i] = "" + Neg1;
		i++;
		values[i] = "" + Neg2;
		i++;
		values[i] = "" + Neg3;
		i++;

		values[i] = "" + Unk2;
		i++;

		values[i] = "" + Unk3;
		i++;
		values[i] = "" + Unk4;
		i++;
		values[i] = "" + Unk5;
		i++;

		values[i] = "" + Unk6;  // This should be a CSimpleCollection
		i++;
		values[i] = "" + Unk7;

		return values;
	}

}

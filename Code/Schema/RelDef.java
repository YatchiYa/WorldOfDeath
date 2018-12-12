package Schema;

public class RelDef {
	private static RelDefSchema relDef;
	private static int recordSize,slotCount;
	private static int fileIdx;

	
	
	public RelDef(RelDefSchema relDef, int slotCount) {
		this.relDef = relDef;
	}

	public static RelDefSchema getRelDef() {
		return relDef;
	}

	public static void setRelDef(RelDefSchema relDef) {
		RelDef.relDef = relDef;
	}

	public static int getRecordSize() {
		return recordSize;
	}

	public static void setRecordSize(int recordSize) {
		RelDef.recordSize = recordSize;
	}

	public static int getSlotCount() {
		return slotCount;
	}

	public static void setSlotCount(int slotCount) {
		RelDef.slotCount = slotCount;
	}

	public static int getFileIdx() {
		return fileIdx;
	}

	public static void setFileIdx(int fileIdx) {
		RelDef.fileIdx = fileIdx;
	}
	
	
	public static RelDefSchema get(int i) {
		return null;
	}
	
}

package Schema;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;




public class HeaderPageInfo {
	private int  dataPageCount;
	private ArrayList<Integer> pageIdx;
	private ArrayList<Integer> freeSlot;


	public HeaderPageInfo() {
		this(0);
	}
	
	//Susceptible de produire une erreur !!! 
		public HeaderPageInfo(int dataPageCount) {
			
			this.dataPageCount =0;
			this.pageIdx =null;//new ArrayList<Integer>(dataPageCount);;
			this.freeSlot = null; //new ArrayList<Integer>(dataPageCount);;
		}
		
	
	/**
	 * @return the dataPageCount
	 */
	public int getDataPageCount() {
		return dataPageCount;
	}

	/**
	 * @param dataPageCount the dataPageCount to set
	 */
	public void setDataPageCount(int dataPageCount) {
		this.dataPageCount = dataPageCount;
	}

	/**
	 * @return the pageIdx
	 */
	public ArrayList<Integer> getPageIdx() {
		return pageIdx;
	}

	/**
	 * @param pageIdx the pageIdx to set
	 */
	public void setPageIdx(ArrayList<Integer> pageIdx) {
		this.pageIdx = pageIdx;
	}

	/**
	 * @return the freeSlot
	 */
	public ArrayList<Integer> getFreeSlot() {
		return freeSlot;
	}

	/**
	 * @param freeSlot the freeSlot to set
	 */
	public void setFreeSlot(ArrayList<Integer> freeSlot) {
		this.freeSlot = freeSlot;
	}

	public void writeToBuffer(byte[] headerPage, HeaderPageInfo headerF) throws IOException {
		 int i=0;
			ByteBuffer buffer = ByteBuffer.wrap(headerPage);
			buffer.putInt(headerF.getDataPageCount());
			ArrayList<Integer> idxTab = headerF.getPageIdx();
			ArrayList<Integer> freeSlotTab = headerF.getFreeSlot();
			
			while(i<idxTab.size()) {
				buffer.putInt(idxTab.get(i).intValue());
				buffer.putInt(freeSlotTab.get(i).intValue());	
				i++;
	}
	
	}
	
	public void readFromBuffer(byte[] headerPage, HeaderPageInfo headerF ) throws IOException {
		int i=0;
		ByteBuffer buffer = ByteBuffer.wrap(headerPage);
		int nbPage = buffer.getInt();
		headerF.setDataPageCount(nbPage);
	
		while(i<nbPage) {
			Integer idx = new Integer(buffer.getInt());
			Integer nbSlot = new Integer(buffer.getInt());
			pageIdx.add(idx);
			freeSlot.add(nbSlot);
			i++;
		}	
	}
	
	public void addIdxPage(Integer i) {
		pageIdx.add(i);
	}
	public void addNbSlotDispo(Integer i) {
		freeSlot.add(i);
	}
	
	public boolean decrementNbSlotDispo(Integer id) {
		boolean find = false;
		
		int indice = pageIdx.indexOf(id);
		
		if(indice!=-1) {
			int nb = freeSlot.get(indice).intValue();
			nb--;
			Integer newNb = new Integer(nb);
			freeSlot.set(indice, newNb);
			find = true;
		}
		
		return find;
	}
	public void incrementNbPage() {
		dataPageCount++;
	}
}


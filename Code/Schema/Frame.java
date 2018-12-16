package Schema;

import java.util.Date;


public class Frame {
	private PageId page;
	private int pinCount;
	private int dirtyFlag;
	private Date timePinCountAtZero;
	
	public Frame(PageId page) {
		this.page = page;
		this.pinCount = 0;
		this.dirtyFlag = 0;
		this.timePinCountAtZero = null;
	}
	
	public Frame() {
		this(null);
	}

	public PageId getPage() {
		return page;
	}

	public void setPage(PageId page) {
		this.page = page;
	}

	public int getPinCount() {
		return pinCount;
	}

	public void setPinCount(int pinCount) {
		this.pinCount = pinCount;
	}

	public int getDirtyFlag() {
		return dirtyFlag;
	}

	public void setDirtyFlag(int dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}
	
	public Date getTimePinCountAtZero() {
		return timePinCountAtZero;
	}

	public void setTimePinCountAtZero(Date timePinCountAtZero) {
		this.timePinCountAtZero = timePinCountAtZero;
	}
	
	/**
	 * décrémente le pin count de 1
	 */
	public void decrementPinCount() {
		this.pinCount--;
	}
	
	/**
	 * incrémente le pin count de 1
	 */
	public void incrementPinCount() {
		this.pinCount++;
	}
	
	/**
	 * affiche l'état d'une table d'information contenue dans une des frames du BufferPool:<br>
	 * - page (idFichier, idPage)
	 * - pin count
	 * - dirty flag
	 * - temps du unpin (pin count passe à 0)
	 */
	public String toString() {
		return("<page=" + this.page + ",pin=" + this.pinCount + ",dirty=" + this.dirtyFlag + ",time=" + this.timePinCountAtZero + ">\n");
	}
}

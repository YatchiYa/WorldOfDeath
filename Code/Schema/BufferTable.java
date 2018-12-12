package Schema;

import java.util.Arrays;
import constants.Constants;

public class BufferTable {
	private Frame frame;
	private byte[] buffer;

	
	public BufferTable(Frame frame, byte[] buffer) {
		this.frame = new Frame();
		this.buffer = new byte[(int)Constants.pageSize];
	}

	
	
	public Frame getframe() {
		return frame;
	}

	public void setframe(Frame frame) {
		this.frame = frame;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * 
	 * @param buffer
	 */
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}



	@Override
	public String toString() {
		return "BufferTable [frame=" + frame + ", buffer=" + Arrays.toString(buffer) + "]";
	}
	
	
	
	
	
}

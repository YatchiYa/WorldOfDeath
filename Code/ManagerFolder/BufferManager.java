package ManagerFolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import Schema.BufferTable;
import Schema.PageId;
import constants.Constants;

public class BufferManager {
	// singleton !!
	private static final BufferManager INSTANCE = new BufferManager();

	public static BufferManager getInstance(){
	      return INSTANCE;
	}
		
	private static BufferTable[] bufferPool;

	
	public static byte[] getPage(PageId iPageId) throws IOException {
		
		for(int j = 0; j<Constants.frameCount ; j++) {
					
			/** on demande une page deja presente dns le bufferPool
			 * si c'est le cas =>  frameCount++
			 */
			
			if(iPageId.equals(bufferPool[j].getframe().getPage())) {
				bufferPool[j].getframe().incr_PinCount();
				return bufferPool[j].getBuffer();
			}
			// la frame est vide donc  
			if(bufferPool[j].getframe().getPage() == null) {
							
				byte[] new_buffer = new byte[(int)Constants.pageSize];  // create a new buffer for the pages with byte encoding
				// dabord faire le diskManager
				DiskManager.ReadPage(iPageId, new_buffer);
				bufferPool[j].setBuffer(new_buffer); //o n initialisele buffer
				bufferPool[j].getframe().incr_PinCount(); // on incr le pincount
				bufferPool[j].getframe().setPage(iPageId); // on initialise avec la page ID
				return new_buffer;
			}
			
			// gerer les remplacement : avec la methode LRU
			ArrayList<BufferTable> CountFrame_zero = new ArrayList<BufferTable>(0);  // creation d'un nouveau buffer 
			for(int i = 0; i<Constants.frameCount; i++) {
							
				// on selectionne les frame avec 0 pincount et on les ajoute a notre bufferpool
				if(bufferPool[i].getframe().getPinCount() == 0) {
					CountFrame_zero.add(bufferPool[i]);
				}				
			}
			
			// check if pincount different de 0 ==> l'arrayList n'est pas vide 
			if(CountFrame_zero.size() != 0) {
				try {

					BufferTable nouvelleFrame = CountFrame_zero.get(0);  // frame a changer
				//  LRUUUUUUU to check after
					for(int i = 1; i<CountFrame_zero.size(); i++) {
						
						// il verifie la trame, son pinCountTime a l instant t
						Date currentTime = CountFrame_zero.get(i).getframe().getInitialTimeFrame();
						Date newLRUTime = nouvelleFrame.getframe().getInitialTimeFrame();
						if(currentTime.before(newLRUTime)) {
							nouvelleFrame = CountFrame_zero.get(i);
						}
					}
					
					// et en fin on verifier le dirty flag si il est a 0, on quitte normal sinon on serai obligé de l'enregistrer
					if(nouvelleFrame.getframe().getDirtyFlag() == 1) {
						DiskManager.WritePage(nouvelleFrame.getframe().getPage(), nouvelleFrame.getBuffer());
					}
					// maintenant on procede au remplacement.
					/**
					 * on recupere la frame, et on met la page dans cette frame
					 */
					nouvelleFrame.getframe().setPage(iPageId);
					// pinCount++
					nouvelleFrame.getframe().incr_PinCount();
					
					// on cree un buffer et on l'ajoute au disk manager
					byte[] field = new byte[(int)Constants.pageSize];
					DiskManager.ReadPage(iPageId, field);
					nouvelleFrame.setBuffer(field);
					
					return field;			
					
				}catch(Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
		
	}
	
	/**
	 * 
	 * @param iPageId
	 * @param iIsDirty
	 */
	public static void freePageId(PageId iPageId,int iIsDirty) {

		for(int i = 0;i< Constants.frameCount;i++) {
			
			if(iPageId.equals(bufferPool[i].getframe().getPage())) {
				
				bufferPool[i].getframe().decr_pinCount();
				if(bufferPool[i].getframe().getPinCount() == 0) {
					bufferPool[i].getframe().rezTime();
				}
				if(iIsDirty == 1) {
					bufferPool[i].getframe().setDirtyFlag(iIsDirty);
				}
			}
		}

	}
	
	
	public static void flushAll() {
		boolean razPinCount = true;
		
		// if(frame pincount == 0 )
		// si la condition precedente n'est pas realiser on quitte le programe
		
		for(int i = 0; i< Constants.frameCount; i++) {
			if(bufferPool[i].getframe().getPinCount() != 0) {
				razPinCount = false;
			}
		}
		
		// si c'est false on quitte le programe
		if(razPinCount) {
			for(int i = 0; i< Constants.frameCount; i++) {
				
				if(bufferPool[i].getframe().getDirtyFlag() == 1) {
					
					try{
						// on ecrit sur le diskmanager						
						DiskManager.WritePage(bufferPool[i].getframe().getPage(), bufferPool[i].getBuffer());
					}catch(IOException e) {
						System.out.println(e);
					}
				}
				//on marque les frames comme "inoccupées"
				bufferPool[i] = new BufferTable(null, null);
			}
		}
		else {
			System.out.println(" page occupé");
			System.exit(-99999);   // pour sortir du programe
		}
		
	}
	
	
	
	
	
	public BufferTable[] getBufferPool() {
		return bufferPool;
	}

	public void setBufferPool(BufferTable[] bufferPool) {
		this.bufferPool = bufferPool;
	}
	
	
	
		
}

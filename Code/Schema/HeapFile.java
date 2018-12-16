package Schema;

import java.io.IOException;

import ManagerFolder.BufferManager;
import ManagerFolder.DiskManager;

public class HeapFile {
	private static RelDef relation;

	public void createNewOnDisk () throws IOException {
		
		DiskManager.CreateFile(relation.getFileIdx());	

		// ajouter de la page
		DiskManager.AddPage(relation.getFileIdx());    // revenir revoir plus tard 
		
		//recuperer le buffer de la Header Page vis le BufferManger
		// new pageId
		PageId hp= new PageId(relation.getFileIdx(),0);		
		byte[] bufferHeaderPage = BufferManager.getPage(hp);
		
		HeaderPageInfo headerPageInfo = new HeaderPageInfo(0);
		
		headerPageInfo.writeToBuffer(bufferHeaderPage, headerPageInfo);  // revoir 
		
		//liberer le buffer aupres de BufferManager
		BufferManager.freePageId(hp, 1);   // 1 => modifier      0 => pas modif 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @return the relation
	 */
	public static RelDef getRelation() {
		return relation;
	}

	/**
	 * @param relation the relation to set
	 */
	public static void setRelation(RelDef relation) {
		HeapFile.relation = relation;
	}
	
	
	

}

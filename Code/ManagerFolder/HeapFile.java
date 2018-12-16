package ManagerFolder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import Schema.HeaderPageInfo;
import Schema.PageId;
import Schema.Record;
import Schema.RelDef;
import Schema.RelDefSchema;
import Schema.bytemap;




public class HeapFile {
	private static RelDef relation;

	public HeapFile(RelDef r) {
		this.relation = r;
	}

	public HeapFile() {
	}





	public void createNewOnDisk () throws IOException {
		DiskManager.CreateFile(relation.getFileIdx());	

		System.out.println("32");
		// ajouter de la page
		DiskManager.AddPage(relation.getFileIdx());    // revenir revoir plus tard 

		System.out.println("33");
		//recuperer le buffer de la Header Page vis le BufferManger
		// new pageId
		PageId hp= new PageId(relation.getFileIdx(),0);		
		System.out.println("34");

		byte[] bufferHeaderPage = BufferManager.getPage(hp);

		System.out.println("35");

		HeaderPageInfo headerPageInfo = new HeaderPageInfo(0);
		
		headerPageInfo.writeToBuffer(bufferHeaderPage, headerPageInfo);  // revoir 

		System.out.println("35");
		//liberer le buffer aupres de BufferManager
		BufferManager.freePageId(hp, 1);   // 1 => modifier      0 => pas modif 

		System.out.println("36");
	}
	
	
	
	
	
	/**********************
	 * 
	 * @return
	 * @throws IOException
	 */
	
	public PageId getFreePageId() throws IOException {
		
		int idFile = relation.getFileIdx();
		
		HeaderPageInfo hpi = new HeaderPageInfo();
		getHeaderPageInfo(hpi);
		
		ArrayList<Integer> idxList = hpi.getPageIdx();
		ArrayList<Integer> slotDispoList = hpi.getFreeSlot();
		
		for(int i = 0;i<idxList.size();i++) {
			int idx = idxList.get(i).intValue();
			int nbSlot = slotDispoList.get(i).intValue();
			
			if(nbSlot > 0) {
				return new PageId(idFile, idx); 
			}
		}
		return addDataPage();
	}
	
	public PageId addDataPage() throws IOException {
		//on rajoute une data page au heapfile
		PageId newPage = DiskManager.AddPage(relation.getFileIdx());
		
		updateHeaderNewDataPage(newPage);
		
		return newPage;
	}
	public void updateHeaderNewDataPage(PageId newpid) throws IOException {
		//on recupere la header page du heapfile
		int fileIdHP = relation.getFileIdx();
		//on construit son PageId pour faire le bon appel au BufferManager
		PageId headerPage = new PageId(fileIdHP, 0);
		
		byte[] bufferHeaderPage = BufferManager.getPage(headerPage);
		
		HeaderPageInfo hpi = new HeaderPageInfo();
		
		readHeaderPageInfo(bufferHeaderPage, hpi);
		//on rajoute dans la liste de la headerpageinfo l'indice de la nouvelle page
		Integer idx = new Integer(newpid.getPageIdx());
		hpi.addIdxPage(idx);
		//le nombre de cases dispos est égal au slotCount
		hpi.addNbSlotDispo(relation.getSlotCount());
		//on incremente le nb de page total
		hpi.incrementNbPage();
		
		writeHeaderPageInfo(bufferHeaderPage, hpi);
		//on libere la header page qui vient d'etre modifiée
		BufferManager.freePageId(headerPage, 1);
	}
	
	public void getHeaderPageInfo(HeaderPageInfo hpi) throws IOException {
		//on recupere la header page du heapfile
		int fileIdHP = relation.getFileIdx();
		//on construit son PageId pour faire le bon appel au BufferManager
		PageId headerPage = new PageId(fileIdHP, 0);
		
		byte[] bufferHeaderPage = BufferManager.getPage(headerPage);
		
		//on remplit hpi avec le contenu du buffer de la header page
		readHeaderPageInfo(bufferHeaderPage, hpi);
		
		BufferManager.freePageId(headerPage, 0);
	}
	public void writeHeaderPageInfo(byte[] headerPage, HeaderPageInfo hfi) throws IOException {
		//"sérialisation" : flux octet(headerPage) buffer <- un objet du type headerpageinfo
		ByteBuffer buffer = ByteBuffer.wrap(headerPage);
	
		//ecriture du nombre page (4 bytes)
		buffer.putInt(hfi.getDataPageCount());
		
		ArrayList<Integer> idxTab = hfi.getPageIdx();
		ArrayList<Integer> nbSlotTab = hfi.getFreeSlot();
		
		//ecriture successive du couple idx/nbslotrestant (8 bytes = 4 bytes + 4 bytes)
		for(int i = 0; i<idxTab.size(); i++) {
			buffer.putInt(idxTab.get(i).intValue());
			buffer.putInt(nbSlotTab.get(i).intValue());
		}
	}
	public void readHeaderPageInfo(byte[] headerPage, HeaderPageInfo hfi) throws IOException {
		//"désérialisation" : flux octet(headerPage) buffer -> un object de type HeaderPageInfo		
		ByteBuffer buffer = ByteBuffer.wrap(headerPage);
		
		//on récupère le nombre de page (4 bytes)
		int nbPage = buffer.getInt();
		hfi.setDataPageCount(nbPage);
		
		//lecture successive du couple idx/nbslotrestant (8 bytes = 4 bytes + 4 bytes)
		for(int i = 0; i<nbPage; i++) {
			Integer idx = new Integer(buffer.getInt());
			Integer nbSlot = new Integer(buffer.getInt());
			
			hfi.addIdxPage(idx);
			hfi.addNbSlotDispo(nbSlot);
		}
	}
	
	
	
	
	
	
	
	public void updateHeaderTakenSlot(PageId pid) throws IOException {
		//on recupere la header page du heapfile
		int fileIdHP = relation.getFileIdx();
		//on construit son PageId pour faire le bon appel au BufferManager
		PageId headerPage = new PageId(fileIdHP, 0);
				
		byte[] bufferHeaderPage = BufferManager.getPage(headerPage);
		
		HeaderPageInfo hpi = new HeaderPageInfo();
				
		readHeaderPageInfo(bufferHeaderPage, hpi);
		
		//chercher l'id de la PageId dans la liste idxPageTab pour obtenir l'indice
		//et le faire correspondre dans la liste nbSlotsRestantDisponibles
		
		Integer idChercher = new Integer(pid.getPageIdx());
		boolean find = hpi.decrementNbSlotDispo(idChercher);
		if(!find) {
			System.out.println("*** Erreur ! Cette page n'est pas présente ! ***\n");
			BufferManager.freePageId(headerPage, 0);
		}
		else {
			writeHeaderPageInfo(bufferHeaderPage, hpi);
			//on libere la header page qui vient d'etre modifiée
			BufferManager.freePageId(headerPage, 1);
		}
	}
	
	
	
	
	
	
	public void writeRecordInBuffer(Record r, byte[] bufferPage,int offset) {
		//on recupere la relschema de la reldef du heapfile courant
		RelDefSchema schema = relation.getRelDef();
		
		//la liste des types des différentes colonnes de la relation
		ArrayList<String> typeCol = schema.getType_col();
		//la liste des valeurs du record
		ArrayList<String> listVal = r.getListValues();
		
		ByteBuffer buffer = ByteBuffer.wrap(bufferPage);
		
		//on se positionne dans le buffer à l'offset indiqué pour pouvoir écrire le record
		buffer.position(offset);
		
		//lors de l'écriture du record, on convertit les valeurs des champs int ou float, qui sont
		//au format String dans le Record. Pour les types, on se base sur les types de la relschema
		//Une fois la conversion faite, il faudra écrire "les octets de la valeur"
		for(int i = 0; i<typeCol.size(); i++) {
			String type = typeCol.get(i);
			String val = listVal.get(i);
			
			int valToInt;
			float valtoFloat;
			String valToString;
			
			switch(type.toLowerCase()) {
				case "int" : 
					valToInt = Integer.parseInt(val);
					buffer.putInt(valToInt);
					break;
				case "float" : 
					valtoFloat = Float.parseFloat(val);
					buffer.putFloat(valtoFloat);
					break;
				default : 
					int longueurString = Integer.parseInt(type.substring(6));
					valToString = val;
					for(int j = 0; j<longueurString; j++) {
						buffer.putChar(valToString.charAt(j));
					}
			}
		}
	}
	
	
	public void insertRecordInPage(Record r, PageId page) throws IOException {
		byte[] bufferPage = BufferManager.getPage(page);
		
		bytemap bitMap = new bytemap();
		
		readPageBitmapInfo(bufferPage, bitMap);
		
		ArrayList<Byte> slotStatus = bitMap.getSlotIndice();
		
		//Retourne l'indice de la premiere occurence de l'element spécifié dans la list,
		//ou -1 si cette liste ne contient pas l'element
		int caseIdX = slotStatus.indexOf(new Byte((byte)0));
		
		if(caseIdX == -1) {
			BufferManager.freePageId(page, 0);
		}
		else {
			int slotCount = relation.getSlotCount();
			int recordSize = relation.getRecordSize();
			
			//slotCount + idCaseFreeBitMap*recordSize
			//le 1er "slotCount" correspond aux nb byte pour la bitmap
			//le 2e membre "idCase*recordSize" correspond au 1er byte de la idCase ieme slot
			writeRecordInBuffer(r, bufferPage, slotCount + caseIdX*recordSize);
			
			//nous actualisons la PageBitMapInfo pour marquer la case caseIdX comme "occupée"
			//(octet de valeur de 1)
			bitMap.slotOccup(caseIdX);
			
			writePageBitmapInfo(bufferPage, bitMap);
			
			BufferManager.freePageId(page, 1);
		}
	}
	
	public void readPageBitmapInfo(byte[] bufferPage, bytemap bmpi) throws IOException {
		//"désérialisation" : flux octet(bufferPage) buffer -> un objet du type PageBitmapInfo
		//Dans chaque page de données "en forme buffer" il y aura donc :
		//AU DEBUT, la bitmap avec un octet par case
		
		//nbSlot = nbSlot octets = nbSlot bytes
		//car on a la bitmap avec un octet par case
		int nbSlot = relation.getSlotCount();
		
		ByteBuffer buffer = ByteBuffer.wrap(bufferPage);
		
		//lecture de la bitmap (nbSlot bytes)
		for(int i = 0; i<nbSlot; i++) {
			Byte status = new Byte(buffer.get());
			bmpi.addSlotIndice(status);
		}
	}
	public void writePageBitmapInfo(byte[] bufferPage, bytemap bmpi) throws IOException {
		//"sérialisation" : flux octet(bufferPage) buffer <- un objet du type PageBitmapInfo
		
		//nbSlot = nbSlot octets = nbSlot bytes
		//car on a la bitmap avec un octet par case
		int nbSlot = relation.getSlotCount();
		
		ArrayList<Byte> bitMap = bmpi.getSlotIndice();
		
		ByteBuffer buffer = ByteBuffer.wrap(bufferPage);
		
		//modification du buffer de la header page passée en parametre
		for(int i = 0; i<nbSlot; i++){
			buffer.put(bitMap.get(i).byteValue());
		}
	}
	
	
	public void insertRecord(Record r) throws IOException {
		PageId pageWhereRecordSaved = getFreePageId();
		insertRecordInPage(r, pageWhereRecordSaved);
		
		updateHeaderTakenSlot(pageWhereRecordSaved);
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
		relation = relation;
	}
	
	
	

}

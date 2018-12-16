package ManagerFolder;

import java.io.IOException;
import java.util.ArrayList;

import Schema.DBDef;
import Schema.Record;
import Schema.RelDef;



//
public class FileManager {
	
	private static final FileManager INSTANCE = new FileManager();

	
	public static FileManager getInstance(){
	      return INSTANCE;
	}

	private static DBDef db = new DBDef();
	private static ArrayList<HeapFile> listeHeapFile;
	
	public static DBDef getDb() {
		return db;
	}


	public static void setDb(DBDef db) {
		FileManager.db = db;
	}

	public FileManager() {
		super();

		DBDef db = new DBDef();
		listeHeapFile = new ArrayList<HeapFile>(0);
	}
	
	public static void init() {

		DBDef db = new DBDef();
		listeHeapFile = new ArrayList<HeapFile>(0);
		for(RelDef r : db.getListRelDef()) {
			listeHeapFile.add(new HeapFile(r));
		}
		
	}

	
	public void createNewHeapFile(RelDef iRelDef) throws IOException {
		HeapFile hp = new HeapFile(iRelDef);
		listeHeapFile.add(hp);
		hp.createNewOnDisk();
		
	}
	
	
	public void insertRecordInRelation(RelDef iRelationName, Record iRecord) throws IOException {
		
		for(HeapFile l : listeHeapFile) {
			if(l.getRelation() == iRelationName) {
				l.insertRecord(iRecord);
			}
		}
	}
	
	
	public static ArrayList<HeapFile> getListeHeapFile() {
		return listeHeapFile;
	}


	public static void setListeHeapFile(ArrayList<HeapFile> listeHeapFile) {
		FileManager.listeHeapFile = listeHeapFile;
	}
		
	
	
}

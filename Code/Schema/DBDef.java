package Schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ManagerFolder.BufferManager;
import ManagerFolder.DBManager;
import ManagerFolder.FileManager;
import ManagerFolder.HeapFile;



public class DBDef {
		// faire le singleton !!!!
	private static final DBDef INSTANCE = new DBDef();

	public static DBDef getInstance(){
	      return INSTANCE;
	}
	
	private ArrayList<RelDef> listRelDef = new ArrayList<>(0);
	private int cptRel=0;

	private static ArrayList<HeapFile> listeHeapFile;
	//private static ArrayList<Index> listeIndex;
	
	
	public DBDef(int c) {
		this.cptRel = c;
		listRelDef = new ArrayList<RelDef>(c);
	}
	public DBDef() {
		this(0);
	}
	
	public void AddRelation(RelDef rel) {
		listRelDef.add(rel);
	}
	
	
	
	
	public ArrayList<RelDef> getListRelDef() {
		return listRelDef;
	}
	public void setListRelDef(ArrayList<RelDef> listRelDef) {
		this.listRelDef = listRelDef;
	}
	public int getCptRel() {
		return cptRel;
	}
	public void setCptRel(int cptRel) {
		this.cptRel = cptRel;
	}
	public void incrCompteur() {
		this.cptRel++;
	}
	
	public void raz() {
		this.cptRel = 0;
		this.listRelDef = new ArrayList<RelDef>(0);
	}
	
	public static void init() throws FileNotFoundException, IOException, ClassNotFoundException {
		DBDef db = DBManager.getDb();
		File fichier =  new File(constants.Constants.catalogRep);
		if(fichier.exists()) {
			// ouverture d'un flux sur un fichier
			try(FileInputStream fis = new FileInputStream(fichier);ObjectInputStream ois =  new ObjectInputStream(fis);){
				 // désérialization de l'objet
				db = (DBDef)ois.readObject();
			}
		}
		//refresh la liste de heapfiles
		FileManager.init();
		//initialisation de la liste d'indexs
		// listeIndex = new ArrayList<Index>(0);
		
	}
	public static void refreshHeapFiles() {
		DBDef db = DBManager.getDb();
		listeHeapFile = new ArrayList<HeapFile>(0);
		for(RelDef r : db.getListRelDef()) {
			listeHeapFile.add(new HeapFile(r));
		}
	}
	
	
	
	
	
	
	public static void finish() throws IOException {
		File fichier =  new File(constants.Constants.catalogRep);

		 // ouverture d'un flux sur un fichier
		try(FileOutputStream fos = new FileOutputStream(fichier);ObjectOutputStream oos =  new ObjectOutputStream(fos);){
			// sérialization de l'objet
			oos.writeObject(DBManager.getDb());
		}
		BufferManager.flushAll();
			
	}
	
	
	
	
	
}

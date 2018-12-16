package ManagerFolder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Schema.DBDef;
import Schema.RelDef;
import Schema.RelDefSchema;
import gestion.Commande;


public class DBManager {
	// a definir le singleton 
	
	
	private static DBDef db;
	static Commande cmd= new Commande();
	
	public static void init() throws FileNotFoundException, ClassNotFoundException, IOException {
		// the init function
		db = new DBDef();
		db.init();
	}
	

	public static void finish() throws FileNotFoundException, IOException {
		db.finish();
		BufferManager.flushAll();		
	}
	

	public static void processCommande(String commande) {
		cmd.listCommande(commande);
		
	}
	
	public static void CreateRelation(String NomRel, int nbC, ArrayList<String> typeC) {
		RelDefSchema new_Rel = new RelDefSchema(NomRel, nbC);
		new_Rel.setType_col(typeC);
		
		int recordSize =0;

		for(String s :typeC) {
			switch (s.toLowerCase()) {
			case "int" :case"float" :recordSize+=4;
			break;		
			default :recordSize+= 2*Integer.parseInt(s.substring(6)); 
			}
		}
		int slotCount= constants.Constants.pageSize/(recordSize+1);
		
		// creation de la relation avec la nouvelle rel
		RelDef relDef =new RelDef(new_Rel,db.getCptRel());
		
		//A VERIFIER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		relDef.setRecordSize(recordSize);
		relDef.setSlotCount(slotCount);
		relDef.setFileIdx(db.getCptRel());
		// ajouter a la base relation
		db.AddRelation(relDef);
		// incrementer le countRel  de la DBDef
		db.incrCompteur();		
		
		System.out.println(" relation ajouté avec succée !!");
		
		
		try {
			DiskManager.CreateFile(relDef.getFileIdx());
		}catch(IOException e) {
			System.out.println("*** Une ereur s'est produite lors de la création du fichier ! ***");
			System.out.println("Détails : " + e.getMessage());
		}
		
		//creation d'un heapfile (pointant vers la RelDef nouvellement créée) et rajout de ce heapfile
		//dans la liste des heapfiles
		HeapFile hf = new HeapFile(relDef);
		//appel de create header sur le heapfile nouvellement créé
		try {
			DiskManager.AddPage(relDef.getFileIdx());
		}catch(IOException e) {
			System.out.println("*** Une ereur s'est produite lors de la création de la header page ! ***");
			System.out.println("Détails : " + e.getMessage());
		}
		
	}
	
	
	
	
	public static DBDef getDb() {
		return db;
	}


	public static void setDb(DBDef db) {
		DBManager.db = db;
	}


	

}

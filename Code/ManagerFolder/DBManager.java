package ManagerFolder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Schema.RelDefSchema;
import gestion.Commande;
import relANDrecord.RelDef;


public class DBManager {
	// a definir le singleton 
	
	
	private static DBDef db;
	static Commande cmd= new Commande();
	
	public static void init() {
		// the init function
		db = new DBDef();
		db.init();
	}
	

	public static void finish() throws FileNotFoundException, IOException {
		db.finish();
		
	}
	

	public static void processCommande(String commande) {
		cmd.listCommande(commande);
		
	}
	
	public static void CreateRelation(String NomRel, int nbC, ArrayList<String> typeC) {
		RelDefSchema new_Rel = new RelDefSchema(NomRel, nbC);
		new_Rel.setType_col(typeC);
		// creation de la relation avec la nouvelle rel
		RelDef relDef =new RelDef(new_Rel,db.getCptRel());
		// ajouter a la base relation
		db.AddRelation(relDef);
		// incrementer le countRel  de la DBDef
		db.incrCompteur();		
		
		System.out.println(" relation ajouté avec succée !!");
		
	}
	
	
	
	
	public static DBDef getDb() {
		return db;
	}


	public static void setDb(DBDef db) {
		DBManager.db = db;
	}


	

}

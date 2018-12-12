package gestion;

import java.util.ArrayList;
import java.util.StringTokenizer;

import ManagerFolder.DBManager;
import Schema.RelDefSchema;


public class Commande {
	
	public static String listCommande(String c) {		
		String action;
		
		StringTokenizer st = new StringTokenizer(c," ");
		action = st.nextToken();
		
		switch(action) {
		
		case "create":
			System.out.println("create methode");
			createRel(c);
			break;
		default: 
			System.out.println("don t know the commande ");
			break;
		}
		
		return c;
	}
	
	
public static void createRel(String commande) {
		
		RelDefSchema n_rel = new RelDefSchema();
		try{
			n_rel = getRelationAdded(commande.substring(7));
		}catch(IllegalArgumentException e) {
			System.out.println(e);
		}
		DBManager.CreateRelation(n_rel.getNom_rel(), n_rel.getNb_col(), n_rel.getType_col());
		
	}












private static RelDefSchema getRelationAdded(String chaine) {
	String commande = chaine.trim();
	
	// on essaye de recuperer les mots separer par un espace entre les mots
	StringTokenizer st = new StringTokenizer(commande," ");
	
	RelDefSchema relation = new RelDefSchema();

	ArrayList<String> typeCol = new ArrayList<String>(0);

	// le nom de la relation
	String nom = st.nextToken();
	relation.setNom_rel(nom);
	
	//nombre de colone de la relation
	int nbCol = Integer.parseInt(st.nextToken());
	relation.setNb_col(nbCol);
	
	while (st.hasMoreTokens()) {
		String type = st.nextToken().toLowerCase();
		if(type.equals("int") || type.equals("float") || type.contains("string")) {
			typeCol.add(type);
		}
		else {
			System.out.println("doesn t much");
		}
		ArrayList<String> type2= new ArrayList<String>(0);
		type2.add(type);		
		relation.setType_col(type2);
	}

	return relation;
	
	}
	
	
}

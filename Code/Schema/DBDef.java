package Schema;

import java.util.ArrayList;


public class DBDef {
		// faire le singleton !!!!
	private static final DBDef INSTANCE = new DBDef();

	public static DBDef getInstance(){
	      return INSTANCE;
	}
	
	private ArrayList<RelDef> listRelDef = new ArrayList<>(0);
	private int cptRel=0;
	
	
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
	
	public static void init() {
		
	}
	public static void finish() {
			
	}
	
	
	
	
	
}

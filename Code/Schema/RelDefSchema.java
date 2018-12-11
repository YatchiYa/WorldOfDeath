package Schema;

import java.util.ArrayList;

public class RelDefSchema {
	private static String nom_rel;
	private static int nb_col;
	private static ArrayList<String> type_col;
	
	public RelDefSchema(String nom_rel, int nb_col) {
		this.nom_rel = nom_rel;
		this.nb_col = nb_col;
	}
	
	public RelDefSchema() {
		this(null,0);
	}
	
	public static String getNom_rel() {
		return nom_rel;
	}
	public static void setNom_rel(String nom_rel) {
		RelDefSchema.nom_rel = nom_rel;
	}
	public static int getNb_col() {
		return nb_col;
	}
	public static void setNb_col(int nb_col) {
		RelDefSchema.nb_col = nb_col;
	}
	public static ArrayList<String> getType_col() {
		return type_col;
	}
	public static void setType_col(ArrayList<String> type_col) {
		RelDefSchema.type_col = type_col;
	}
	
	
	@Override
	public String toString() {
		return "RelDefSchema [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}

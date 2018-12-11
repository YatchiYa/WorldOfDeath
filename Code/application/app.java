package application;

import java.util.Scanner;
import constants.Constants;

import ManagerFolder.DBManager;

public class app {

	public static void main(String[] args) {

		
		System.out.println("Feel welcome to mini_SGBD");
		System.out.println(" tappez votre commande ");
		System.out.println("(\\_/)");
		System.out.println("( •,•)");
		System.out.println("(\")_(\")");
		
		
		
		
		DBManager dbManager = new DBManager();
		dbManager.init();
		
		
		Scanner sc = new Scanner(System.in);
		String commande;
		
		
		do {
			System.out.println("cammande : ");
			
			commande = sc.nextLine();
			// int nbc = sc.nextInt();
			// String typeC = sc.nextLine();
			if(commande.equals(Constants.exit)) {

				System.out.println("sortie");
			}
			else {
				
				dbManager.processCommande(commande);
			}
			
		}while(!commande.equals(Constants.exit));	

		
	}

}

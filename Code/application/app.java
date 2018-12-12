package application;

import java.io.IOException;
import java.util.Scanner;
import constants.Constants;

import ManagerFolder.DBManager;
import ManagerFolder.DiskManager;
import Schema.PageId;

public class app {

	public static void main(String[] args) throws IOException {

		
		System.out.println("Feel welcome to mini_SGBD");
		System.out.println(" tappez votre commande ");
		System.out.println("(\\_/)");
		System.out.println("( �,�)");
		System.out.println("(\")_(\")");

		System.out.println(" tapez help pour voir la listCommande possible ! \n");
		
		
		
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

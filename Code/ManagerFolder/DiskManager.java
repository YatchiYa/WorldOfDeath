package ManagerFolder;

import java.io.IOException;
import java.io.RandomAccessFile;

import Schema.PageId;
import constants.Constants;

public class DiskManager {
	// singleton !!
	private static final DiskManager INSTANCE = new DiskManager();

	public static DiskManager getInstance(){
	      return INSTANCE;
	}
	
	/**
	 * 
	 * @param iFileIdx
	 * @throws IOException
	 */
	public static void  CreateFile(int iFileIdx)throws IOException{
		try {
			RandomAccessFile randomfile = new RandomAccessFile(Constants.PATH + iFileIdx + ".rf","rw");	
			randomfile.close();	
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * 
	 * @param iFileIdx
	 * @param oPageId
	 * @return
	 * @throws IOException
	 */
	public static int AddPage(int iFileIdx, int oPageId) throws IOException{
		// ouverture du fichier
		try {
			RandomAccessFile randomfile = new RandomAccessFile(Constants.PATH+iFileIdx+".rf","rw");
			// calcule de la longuer du fichier actuel
			long longueur =randomfile.length();
			// 	indice du dernier points trouver qui est égale a longeur / pageSize
			int indice =(int)(longueur/Constants.pageSize);
			// ecriture des bytes 0 
			for(int i=0;i<Constants.pageSize; i++) {
				randomfile.writeByte(0);
			}
			// fermeture du fichier
			randomfile.close();		
			
			// System.out.println(" \n commentaire : "+ iFileIdx + " " + indice);
			PageId new_page =  new PageId(iFileIdx,indice);
			// retourn l'id de la nouvelle page !!!
			oPageId = new_page.getFileIdx();
			return oPageId;
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 
	 * @param iPageId
	 * @param oBuffer
	 * @throws IOException
	 */
	public static void ReadPage(PageId iPageId, byte[]oBuffer)throws IOException{
		try {
			RandomAccessFile randomfile = new RandomAccessFile(Constants.PATH+iPageId.getFileIdx()+".rf", "rw"); 
			randomfile.seek(iPageId.getPageIdx()*Constants.pageSize);
			randomfile.write(oBuffer);
			
			//System.out.println("commentaire : "+ oBuffer);
			randomfile.close();			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param iPageId
	 * @param iBuffer
	 * @throws IOException
	 */
	public static void WritePage(PageId iPageId,byte[] iBuffer)throws IOException {
		try {
			RandomAccessFile randomfile = new RandomAccessFile(Constants.PATH + iPageId.getFileIdx()+".rf", "rw"); 
			randomfile.seek(iPageId.getPageIdx()*Constants.pageSize);
			randomfile.readFully(iBuffer);
			randomfile.close();			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}

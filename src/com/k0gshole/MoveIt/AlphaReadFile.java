package com.k0gshole.MoveIt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class AlphaReadFile {

	private String path;
	
	public AlphaReadFile(String file_path){
		path = file_path;
	}
	
	public ArrayList OpenFile() throws IOException, ClassNotFoundException{
		ArrayList tempList = new ArrayList();
		FileInputStream fr = new FileInputStream(path);
		ObjectInputStream textData = new ObjectInputStream(fr);

			tempList = (ArrayList) textData.readObject();
			
		
		
		//textReader.close();
		return tempList;
	}
	
/*
 * Version 1.1 Bukkit.
 * By K0Gs
 */
}

package com.k0gshole.MoveIt;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;


public class AlphaWriteFile{
	private String path;
	private boolean append_to_file = false;
	
	public AlphaWriteFile(String file_path){
		path = file_path;
	}
	
	public AlphaWriteFile(String file_path, boolean append_value){
		path = file_path;
		append_to_file = append_value;
	}
	
	public void writeToFile(ArrayList arrayOut) throws IOException{
		FileOutputStream write = new FileOutputStream(path, append_to_file);
		ObjectOutputStream print_line = new ObjectOutputStream(write);
		print_line.writeObject( arrayOut);
		print_line.close();
		write.close();
	}
}

/*
 * Version 1.1 Bukkit.
 * By K0Gs
 */

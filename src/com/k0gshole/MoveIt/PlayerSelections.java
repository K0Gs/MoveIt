package com.k0gshole.MoveIt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerSelections {
	
	ArrayList store;
	
	public PlayerSelections(){
		store  = new ArrayList();
	}
	
	public void addIndex(Player player, UUID animation, Integer frameInt, UUID frame, Block block){
		
		for(int a = 0; a < store.size(); a++){
			ArrayList tempList = new ArrayList((ArrayList) store.get(a));
			if(player == (Player) tempList.get(0)){
				store.remove(a);
			}
			
		}
		
		ArrayList tList = new ArrayList();
		tList.add(player);
		tList.add(animation);
		tList.add(frameInt);
		tList.add(frame);
		tList.add(block);
		this.store.add(tList);
		
	}
	
	public void removePlayer(Player player){
		for (int a = 0;a < this.store.size();a++){
			ArrayList tempList = new ArrayList((ArrayList)this.store.get(a));
			Player tempPlayer = (Player) tempList.get(0);

			if(tempPlayer == player){
				this.store.remove(a);
			}
			
		}
		
	
	}
	
	public ArrayList returnPlayer(Player player){
		ArrayList tempList = new ArrayList();
		ArrayList tempList2 = new ArrayList();
		for(int a = 0; a < store.size(); a++){
			tempList = new ArrayList((ArrayList) store.get(a));
			if(player == tempList.get(0)){
				for(int b = 0; b < tempList.size(); b++){
					tempList2.add(tempList.get(b));
				}
			}
			
		}
		
		return tempList2;
	}
	
	public void clearData(){
		this.store.clear();
		MoveItMain.instance.getServer().broadcastMessage("Clear Data");
	}
	
	public ArrayList returnL(){
		return this.store;
	}

}
/*
 * Version 1.0 
 * By K0Gs
 */
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
	
	public void addIndex(UUID player, UUID animation, Integer frameInt, UUID frame, Block block){
		
		for(int a = 0; a < store.size(); a++){
			ArrayList tempList = new ArrayList((ArrayList) store.get(a));
			if(player == (UUID) tempList.get(0)){
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
	
	public ArrayList returnPlayer(UUID player){
		ArrayList tempList = new ArrayList(store);
		ArrayList tempList2 = new ArrayList();
		ArrayList tempList3 = new ArrayList();
		for(int a = 0; a < tempList.size(); a++){
			tempList2 = new ArrayList((ArrayList) tempList.get(a));
			if(player == (UUID) tempList2.get(0) || player.equals((UUID) tempList2.get(0))){
				for(int b = 0; b < tempList2.size(); b++){
					tempList3.add(tempList2.get(b));
				}
			}
			
		}
		
		return tempList3;
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
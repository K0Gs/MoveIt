package com.k0gshole.MoveIt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Animations {
	
	ArrayList store;
	
	public Animations(){
		store  = new ArrayList();
	}
	
	public void addIndex(String animName, Player player, UUID uuid, Instant now){
		
		ArrayList tList = new ArrayList();
		tList.add(animName);
		tList.add(player);
		tList.add(uuid);
		tList.add(now);
		this.store.add(tList);
	}
	
	public void removeAnimation(UUID uuid){
			for (int a = 0;a < this.store.size();a++){
				ArrayList tempList = new ArrayList((ArrayList)this.store.get(a));
				UUID tempInst2 = (UUID)tempList.get(1);

				if(tempInst2 == uuid){
					this.store.remove(a);
				}
				
			}
			
		
	}
	
	public void removeAnimation(String animName){
		for (int a = 0;a < this.store.size();a++){
			ArrayList tempList = new ArrayList((ArrayList)this.store.get(a));
			String tempInst2 = (String)tempList.get(0);

			if(tempInst2 == animName){
				this.store.remove(a);
			}
			
		}
		
	
	}
	
	public void animationsList(Player player){
		ArrayList tempList = new ArrayList(store);
		player.sendMessage("Animations: ");
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			player.sendMessage((String) tempList2.get(0));
		}
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
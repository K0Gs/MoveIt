package com.k0gshole.MoveIt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Frames {
	
	ArrayList store;
	
	public Frames(){
		store  = new ArrayList();
	}
	
	public void addIndex(Player player, UUID animUuid, UUID frameUuid, int frame, Instant now){
		
		ArrayList tList = new ArrayList();
		tList.add(player);
		tList.add(animUuid);
		tList.add(frameUuid);
		tList.add(frame);
		tList.add(now);
		this.store.add(tList);
	}
	
	public void removeFrame(UUID uuid){
			for (int a = 0;a < this.store.size();a++){
				ArrayList tempList = new ArrayList((ArrayList)this.store.get(a));
				UUID tempInst2 = (UUID)tempList.get(2);

				if(tempInst2 == uuid){
					this.store.remove(a);
				}
				
			}
			
		
	}
	
	public void removeFrame(int frame){
		for (int a = 0;a < this.store.size();a++){
			ArrayList tempList = new ArrayList((ArrayList)this.store.get(a));
			int tempInst2 = (Integer)tempList.get(2);

			if(tempInst2 == frame){
				this.store.remove(a);
			}
			
		}
		
	
	}
	
	public void framesList(Player player, UUID uuid){
		ArrayList tempList = new ArrayList(store);
		player.sendMessage("Frames: ");
		
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			if(uuid == (UUID)tempList2.get(2)){
				player.sendMessage(Integer.toString((Integer) tempList2.get(3)));
			}
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
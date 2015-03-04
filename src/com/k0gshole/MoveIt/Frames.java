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
	
	public void addIndex(UUID player, UUID animUuid, UUID frameUuid, int frame){
		
		ArrayList tList = new ArrayList();
		tList.add(player);
		tList.add(animUuid);
		tList.add(frameUuid);
		tList.add(frame);
		this.store.add(tList);
	}
	
	public String removeFrame(UUID uuid, String dummy){
		int count = 0;
		ArrayList tempList = new ArrayList((ArrayList) this.store);
			for (int a = tempList.size() - 1; a > -1; a--){
				ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
				//UUID tempInst2 = (UUID)tempList.get(2);

				if(uuid.equals((UUID)tempList2.get(1))){
					this.store.remove(a);
					count++;
				}
				
			}
			
		return Integer.toString(count);
	}
	
	public String removeFrame(UUID uuid, int dummy){
		int count = 0;
			for (int a = 0;a < this.store.size();a++){
				ArrayList tempList = new ArrayList((ArrayList) this.store.get(a));
				//UUID tempInst2 = (UUID)tempList.get(2);

				if(uuid.equals((UUID)tempList.get(2))){
					this.store.remove(a);
					count++;
				}
				
			}
			
		return Integer.toString(count);
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
			if(uuid.toString().equals(((UUID)tempList2.get(1)).toString()) || uuid.toString() == ((UUID) tempList2.get(1)).toString() || uuid == (UUID) tempList2.get(1)){
				player.sendMessage(Integer.toString((Integer) tempList2.get(3)));
			}
		}
	}
	
	public ArrayList framesList(UUID uuid){
		ArrayList tempList = new ArrayList();
		ArrayList tempList2 = new ArrayList();
		for(int a = 0; a < this.store.size(); a++){
			tempList = new ArrayList((ArrayList) this.store.get(a));
			if(uuid.equals((UUID) tempList.get(1))){
			tempList2.add((UUID) tempList.get(2));
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

	public void setArray(ArrayList newArray){
		this.store = new ArrayList((ArrayList) newArray);
	}
}

/*
 * Version 1.0 
 * By K0Gs
 */
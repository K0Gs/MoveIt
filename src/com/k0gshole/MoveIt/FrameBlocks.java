package com.k0gshole.MoveIt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FrameBlocks {

	ArrayList store;

	public FrameBlocks(){
		store  = new ArrayList();
	}

	public void addIndex(Block block, Player player, UUID frameUuid, Instant now){

		ArrayList tList = new ArrayList();
		tList.add(block);
		tList.add(player);
		tList.add(frameUuid);
		tList.add(now);
		this.store.add(tList);
	}

	public String removeBlock(ArrayList uuidList){
		int count = 0;
		for (int a = 0;a < this.store.size();a++){
			ArrayList tempList = new ArrayList((ArrayList) store.get(a));
			UUID frameUuid = (UUID) tempList.get(2);

			for(int b = 0; b < uuidList.size(); b++){
				UUID uuid = (UUID) uuidList.get(b);
			if(uuid.equals(frameUuid)){
				this.store.remove(a);
				count++;
			}
			}

		}
		return Integer.toString(count);

	}
	
	public String removeBlock(Block block){
		int count = 0;
		ArrayList tempList = new ArrayList(store);
		for (int a = 0;a < tempList.size();a++){
			ArrayList tempList2 = new ArrayList((ArrayList)tempList.get(a));
			Block frameBlock = (Block) tempList.get(0);

			if(frameBlock.equals(block)){
				this.store.remove(a);
				count++;
			}

		}
		return Integer.toString(count);

	}
	
	public void blocksList(Player player, UUID uuid){
		ArrayList tempList = new ArrayList(store);
		player.sendMessage("Blocks: ");
		
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			if(uuid == (UUID)tempList2.get(2)){
				Block tempBlock = (Block) tempList2.get(0);
				String blockX = Integer.toString(tempBlock.getLocation().getBlockX());
				String blockY = Integer.toString(tempBlock.getLocation().getBlockY());
				String blockZ = Integer.toString(tempBlock.getLocation().getBlockZ());
				player.sendMessage(blockX+" "+blockY+" "+blockZ);
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
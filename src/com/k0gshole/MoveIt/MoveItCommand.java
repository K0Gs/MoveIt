package com.k0gshole.MoveIt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoveItCommand implements CommandExecutor{
	
	private MoveItMain plugin;
	Animations animations;
	Frames frames;
	FrameBlocks frameBlocks;
	PlayerSelections playerSelections;
	public UUID pAnimUuid;
	public UUID pFrameUuid;
	public int pFrameInt;
	public Block pFrameBlock;
	public ArrayList pSelections;
	
	public MoveItCommand(){
		animations = new Animations();
		frames = new Frames();
		frameBlocks = new FrameBlocks();
		playerSelections = new PlayerSelections();
		
		pAnimUuid = UUID.randomUUID();
		pFrameUuid = UUID.randomUUID();
		pFrameBlock = null;
		pFrameInt = 0;
		pSelections = new ArrayList();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String arg1,
			String arg2[]){
		
		if(!(sender instanceof Player)){
			return false;	
		}
		
		Player player = (Player)sender;
		pSelections = new ArrayList((ArrayList) playerSelections.returnPlayer(player));
		if(pSelections.size() == 0){
			pAnimUuid = null;
			pFrameUuid = null;
			pFrameBlock = null;
		}else{
		pAnimUuid = (UUID) pSelections.get(1);
		pFrameInt = (Integer) pSelections.get(2);
		pFrameUuid = (UUID) pSelections.get(3);
		pFrameBlock = (Block) pSelections.get(4);
		}
		if (arg2.length == 0){
			//if (arg2 == ""){
			//player.openInventory(GUEye.getInstance().displayGUI(player));
			//player .sendMessage("Player "+ sender + "Command " + command.toString());
			return true;
			//}
		}
		
		if(arg2.length != 0){
			String newStr = "";
			for(int a = 0;a < arg2.length;a++){
				newStr = newStr + " " + arg2[a];
			}
			player .sendMessage("Player "+ sender + "Command " + command.toString() + " First String " + arg1.toString() +"Second String "+ newStr);
		
			
			if (arg2[0].equalsIgnoreCase("create")){
				if(arg2.length < 2){
					player.sendMessage("You need specify the creation type...");
					player.sendMessage("Create Types: animation, frame, block...");
					player.sendMessage("./moveit create <animation,frame,block>");
					return false;
				}
				ArrayList playerSelection = new ArrayList((ArrayList) playerSelections.returnL());
				UUID uuid = UUID.randomUUID();
				UUID frameUuid = UUID.randomUUID();
				
				
				if(arg2[1].equalsIgnoreCase("animation")){
					if(arg2.length < 3){
						

							player.sendMessage("You need to name the animation...");
							return false;
						
					}
				ArrayList tempList = new ArrayList(animations.returnL());
				for(int a = 0; a < tempList.size();a++){
					ArrayList tempList2 = (ArrayList)tempList.get(a);
					if( arg2[2].equalsIgnoreCase((String)tempList2.get(0))){
						player.sendMessage("This animation "+arg2[2]+" has already been created...");
						return false;
					}
				}
				
				animations.addIndex(arg2[2], player, uuid, Instant.now());
				frames.addIndex(player, uuid, frameUuid, 1, Instant.now());
				playerSelections.addIndex(player, uuid, 1, frameUuid, null);
				player.sendMessage("The animation "+arg2[2]+" has been created...");
				return true;
				}
				
				if(arg2[1].equalsIgnoreCase("frame")){
					ArrayList plSelec = new ArrayList(playerSelections.returnPlayer(player));
					if(plSelec.size() == 0){
						player.sendMessage("You must have an animation selected...");
						return false;
					}

					if(arg2.length > 2){
					ArrayList tempList = new ArrayList((ArrayList) frames.returnL());
					for(int a = 0; a < tempList.size(); a++){
						ArrayList tempList2 = (ArrayList) tempList.get(a);
						if(arg2[2].equals(Integer.toString((Integer) tempList2.get(3)))){
							player.sendMessage("This frame "+arg2[2]+" has already been created...");
							return false;
						}
					}


					playerSelections.addIndex(player, pAnimUuid, Integer.parseInt(arg2[2]), frameUuid, null);
					frames.addIndex(player, pAnimUuid, frameUuid, Integer.parseInt(arg2[2]), Instant.now());
					player.sendMessage("The frame "+arg2[2]+" has been created...");
					return true;
					}
					if(arg2.length < 3){
						ArrayList tempList = new ArrayList((ArrayList) frames.returnL());
						ArrayList tempList2 = new ArrayList();
						int lastFrame = 0;
						for(int a = 0; a < tempList.size(); a++){
							ArrayList tempList3 = (ArrayList) tempList.get(a);
								if(pAnimUuid == (UUID) tempList3.get(1)){
									tempList2.add(new ArrayList(tempList3));
									
								}
								lastFrame = tempList2.size();
								playerSelections.addIndex(player, pAnimUuid, lastFrame+1, frameUuid, null);
								frames.addIndex(player, pAnimUuid, frameUuid, lastFrame + 1, Instant.now());
								player.sendMessage("The frame "+Integer.toString(lastFrame+1)+" has been created...");
								return true;
						}
						
						
						//playerSelections.addIndex(player, pAnimUuid, lastFrame, frameUuid, null);
						//frames.addIndex(player, pAnimUuid, frameUuid, Integer.parseInt(arg2[2]), Instant.now());
						//player.sendMessage("The frame "+arg2[2]+" has been created...");	
						//return true;
					}
				}
				
				if(arg2[1].equalsIgnoreCase("block")){
					if(player.getTargetBlock(null, 15).isEmpty()){
						player.sendMessage("You need to be looking at a block within a radius of 15...");
						return false;
					}
					ArrayList plSelec = new ArrayList(playerSelections.returnPlayer(player));
					if(plSelec.size() == 0){
						player.sendMessage("You must have an animation selected...");
						return false;
					}
					Block tempBlock = player.getTargetBlock(null, 15);
					frameBlocks.addIndex(tempBlock, player, pFrameUuid, Instant.now());
					playerSelections.addIndex(player, pAnimUuid, pFrameInt, pFrameUuid, tempBlock);
					player.sendMessage("The block has been added...");
					return true;
					
				}
			}
			if (arg2[0].equalsIgnoreCase("delete")){
				if(arg2.length < 2){
					player.sendMessage("You need specify the deletion type...");
					player.sendMessage("Delete Types: animation, frame, block...");
					player.sendMessage("./moveit delete <animation,frame,block>");
					return false;
				}
				
				
				if(arg2[1].equalsIgnoreCase("animation")){
				if(arg2.length < 3){
					//String deleted = deleteAnimation.deleteAnimations(pAnimUuid);
					ArrayList framesList = new ArrayList();
					ArrayList tempList = new ArrayList((ArrayList) frames.returnL());
					for(int a = 0; a < tempList.size(); a++){
						ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
						if(pAnimUuid.equals((UUID) tempList.get(1))){
						framesList.add((UUID) tempList2.get(2));
						}
					}
					
					String blockCount = frameBlocks.removeBlock(framesList);
					String frameCount = frames.removeFrame(pAnimUuid, "");
					String animRemoved = animations.removeAnimation(pAnimUuid);
					
					player.sendMessage(animRemoved + " animation(s) have been deleted...");
					player.sendMessage(frameCount + " frame(s) have been deleted...");
					player.sendMessage(blockCount + " block(s) have been deleted...");
					return true;
					
				}
				
				if(arg2.length > 2){

				
				player.sendMessage("The animation cannot be deleted by name...");
				return false;
				}
				}
				
				if(arg2[1].equalsIgnoreCase("frame")){
				
					if(arg2.length < 3){
						
						ArrayList framesList = new ArrayList();
						ArrayList tempList = new ArrayList((ArrayList) frames.returnL());
						for(int a = 0; a < tempList.size(); a++){
							ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
							if(pFrameUuid.equals((UUID) tempList2.get(2))){
							framesList.add((UUID) tempList2.get(2));
							}
						}
						String blockCount = frameBlocks.removeBlock(framesList);
						String frameCount = frames.removeFrame(pFrameUuid, 0);
						
						player.sendMessage(frameCount + " frame(s) have been deleted...");
						player.sendMessage(blockCount + " block(s) have been deleted...");
						return true;
						
						
					}
					
					if(arg2.length > 2){
						
						player.sendMessage("Frames cannot be deleted by name...");
						return false;
					}
					
				}
			
				if(arg2[1].equalsIgnoreCase("block")){
					
					if(arg2.length < 3){
						
						String blockCount = frameBlocks.removeBlock(pFrameBlock);
						
						player.sendMessage(blockCount + " block(s) have been deleted...");
						return true;
					}
					
					if(arg2.length > 2){
						if(arg2[2].equalsIgnoreCase("look")){
							
							String blockCount = frameBlocks.removeBlock(player.getTargetBlock(null, 15));
							
							player.sendMessage(blockCount + " block(s) have been deleted...");
							return true;
						}
						
					}
					
				}
				
			}
			
			if(arg2[0].equalsIgnoreCase("select")){
				if(arg2.length < 2){
					player.sendMessage("You need specify the select type...");
					player.sendMessage("Select Types: animation, frame...");
					player.sendMessage("./moveit select <animation,frame>");
					return false;
				}
				
				if(arg2[1].equalsIgnoreCase("animation")){
					if(arg2.length < 3){
						

							player.sendMessage("You need the name of the animation...");
							return false;
						
					}
					
					if(arg2.length > 2){
						ArrayList tempList = new ArrayList(animations.returnL());
						for(int a = 0; a < tempList.size();a++){
							ArrayList tempList2 = (ArrayList)tempList.get(a);
							if( arg2[1].equalsIgnoreCase((String)tempList2.get(0))){
								ArrayList tempList3 = new ArrayList((ArrayList) frames.returnL());
								ArrayList tempList4 = new ArrayList();
								ArrayList tempList5 = new ArrayList();
								for(int b = 0; b < tempList3.size(); b++){
									tempList4 = new ArrayList((ArrayList) tempList3.get(b));
									if((UUID) tempList2.get(1) == (UUID) tempList4.get(1)){
										tempList5.add((ArrayList) tempList3.get(b));
									}
								}
								
								ArrayList tempListFin = (ArrayList) tempList5.get(0);
								playerSelections.addIndex(player, (UUID) tempList2.get(1), 1, (UUID) tempListFin.get(2), null);
								return true;
							}
						}
						player.sendMessage("The animation "+arg2[1]+" cannot be found...");
						return false;
					}
					
				}
				
				if(arg2[1].equalsIgnoreCase("frame")){
					if(arg2.length < 3){
						

							player.sendMessage("You need the number of the frame...");
							return false;
						
					}
					
					if(arg2.length > 2){
						ArrayList tempList3 = new ArrayList((ArrayList) frames.returnL());
						ArrayList tempList4 = new ArrayList();
						ArrayList tempList5 = new ArrayList();
						ArrayList tempList6 = new ArrayList();
						for(int b = 0; b < tempList3.size(); b++){
							tempList4 = new ArrayList((ArrayList) tempList3.get(b));
							if(pAnimUuid == (UUID) tempList4.get(1)){
								tempList5.add((ArrayList) tempList3.get(b));
							}
						}
						for(int c = 0; c < tempList5.size(); c++){
							tempList6 = new ArrayList((ArrayList) tempList5.get(c));
							if(arg2[2].equals((String) tempList6.get(3))){
								playerSelections.addIndex(player, pAnimUuid, Integer.parseInt(arg2[2]), (UUID) tempList6.get(2), null);
								return true;
							}
						}
						player.sendMessage("The frame "+arg2[2]+" cannot be found...");
						return false;
						
					}
				}
				
				
			}
			
			if(arg2[0].equalsIgnoreCase("list")){
				if(arg2.length < 2){
					player.sendMessage("You need specify the list type...");
					player.sendMessage("List Types: animation, frame, block...");
					player.sendMessage("./moveit select <animation,frame,block>");
					return false;
				}
				
				if(arg2[1].equalsIgnoreCase("animation")){
					animations.animationsList(player);
					return true;
					
				}
				
				if(arg2[1].equalsIgnoreCase("frame")){
					frames.framesList(player, pAnimUuid);
					return true;
					
				}
				
				if(arg2[1].equalsIgnoreCase("block")){
					frameBlocks.blocksList(player, pFrameUuid);
					return true;
				}
				
			}
			
		}
		
		
		return false;
	}
	

}

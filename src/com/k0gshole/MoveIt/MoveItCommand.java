package com.k0gshole.MoveIt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.WorldServer;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftFallingSand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.k0gshole.MoveIt.NewFloatBlock.*;

public class MoveItCommand implements CommandExecutor, Listener{

	private MoveItMain plugin;
	//Animations animations;
	//Frames frames;
	//FrameBlocks frameBlocks;
	//MoveItMain moveitmain;
	public UUID pAnimUuid;
	public UUID pFrameUuid;
	public int pFrameInt;
	public Block pFrameBlock;
	public int pwand;
	public ArrayList pSelections;
	public Material floatMat = null;
	public Byte floatByte = new Byte((byte) 0);
	public Location floatLocation = null;
	NewFloatBlock entity = null;
	//boolean up = true;
	//public static double d0 = 0;
	//public static double d1 = 0;
	//public static double d2 = 0;
	//public static int playInt = 0;
	//public static net.minecraft.server.v1_8_R2.Block playBlock = null;
	
	int count = 0;
	int count2 = 0;
	//int tempFrameInt = 0;
	
	public String thePath = "./plugins/MoveIt/";

	public MoveItCommand(){
		//animations = new Animations();
		//frames = new Frames();
		//frameBlocks = new FrameBlocks();
		//moveitmain = new MoveItMain();
		
		pAnimUuid = UUID.randomUUID();
		pFrameUuid = UUID.randomUUID();
		pFrameBlock = null;
		pFrameInt = 0;
		pwand = 0;
	}
	
	public MoveItCommand(MoveItMain plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String arg1,
			String arg2[]){

		if(!(sender instanceof Player)){
			return false;	
		}

		Player player = (Player)sender;
		pSelections = new ArrayList((ArrayList) MoveItMain.instance.returnPlayerPSelect(player.getUniqueId()));
		if(pSelections.size() == 0){
			pAnimUuid = null;
			pFrameUuid = null;
			pFrameBlock = null;
			pwand = 0;
		}else{
			pAnimUuid = (UUID) pSelections.get(1);
			pFrameInt = (Integer) pSelections.get(2);
			pFrameUuid = (UUID) pSelections.get(3);
			pFrameBlock = (Block) pSelections.get(4);
			pwand = (Integer) pSelections.get(5);
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
			//player .sendMessage("Player "+ sender + "Command " + command.toString() + " First String " + arg1.toString() +"Second String "+ newStr);


			if (arg2[0].equalsIgnoreCase("create")){
				if(arg2.length < 2){
					player.sendMessage("You need specify the creation type...");
					player.sendMessage("Create Types: animation, frame, block...");
					player.sendMessage("./moveit create <animation,frame,block>");
					return false;
				}
				ArrayList playerSelection = new ArrayList((ArrayList) MoveItMain.instance.returnLPSelect());
				UUID uuid = UUID.randomUUID();
				UUID frameUuid = UUID.randomUUID();


				if(arg2[1].equalsIgnoreCase("animation")){
					if(arg2.length < 3){


						player.sendMessage("You need to name the animation...");
						return false;

					}
					ArrayList tempList = new ArrayList(MoveItMain.instance.returnLAnimation());
					for(int a = 0; a < tempList.size();a++){
						ArrayList tempList2 = (ArrayList)tempList.get(a);
						if( arg2[2].equalsIgnoreCase((String)tempList2.get(0))){
							player.sendMessage("This animation "+arg2[2]+" has already been created...");
							return false;
						}
					}

					MoveItMain.instance.addIndexAnimation(arg2[2], player.getUniqueId(), uuid, Instant.now().toString());
					//System.out.println(arg2[2] +" "+ player +" "+ uuid +" "+ Instant.now());
					MoveItMain.instance.addIndexFrames(player.getUniqueId(), uuid, frameUuid, 1);
					//System.out.println(player +" "+ uuid +" "+ frameUuid +" "+ 1 +" "+Instant.now());
					MoveItMain.instance.addIndexPSelect(player.getUniqueId(), uuid, 1, frameUuid, null, pwand);
					//System.out.println(player +" "+ uuid +" "+ 1 +" "+ frameUuid +" "+ null);
					MoveItMain.instance.addIndexAnimationPosition(uuid, 1, "loop");
					player.sendMessage("The animation "+arg2[2]+" has been created...");
					return true;
				}

				if(arg2[1].equalsIgnoreCase("frame")){
					ArrayList plSelec = new ArrayList(MoveItMain.instance.returnPlayerPSelect(player.getUniqueId()));
					if(plSelec.size() == 0){
						player.sendMessage("You must have an animation selected...");
						return false;
					}

					if(arg2.length > 2){
						player.sendMessage("Frames cannot be created by number...");
						return false;

					}

					if(arg2.length < 3){

						MoveItMain.instance.addIndexPSelect(player.getUniqueId(), pAnimUuid, pFrameInt+1, frameUuid, null, pwand);
						//System.out.println(player +" "+ pAnimUuid +" "+ pFrameInt+1 +" "+ frameUuid +" "+ null);
						MoveItMain.instance.addIndexFrames(player.getUniqueId(), pAnimUuid, frameUuid, pFrameInt + 1);
						//System.out.println(player +" "+ pAnimUuid +" "+ frameUuid +" "+ pFrameInt + 1 +" "+ Instant.now());
						player.sendMessage("The frame "+Integer.toString(pFrameInt+1)+" has been created...");
						return true;



						//playerSelections.addIndex(player, pAnimUuid, lastFrame, frameUuid, null);
						//frames.addIndex(player, pAnimUuid, frameUuid, Integer.parseInt(arg2[2]), Instant.now());
						//player.sendMessage("The frame "+arg2[2]+" has been created...");	
						//return true;
					}
				}

				if(arg2[1].equalsIgnoreCase("block")){
					createNewFrameBlocks(player);

				}
				
				if(arg2[1].equalsIgnoreCase("float")){
					if(player.getTargetBlock((HashSet<Byte>) null, 15).isEmpty()){
						player.sendMessage("You need to be looking at a block within a radius of 15...");
						//return false;
					}
					ArrayList plSelec = new ArrayList(MoveItMain.instance.returnPlayerPSelect(player.getUniqueId()));
					if(plSelec.size() == 0){
						player.sendMessage("You must have an animation selected...");
						//return false;
					}
					Block tempBlock = player.getTargetBlock((HashSet<Byte>) null, 15);
					String tempWorld = tempBlock.getWorld().getName();
					World realWorld = tempBlock.getWorld(); 
					 floatMat = tempBlock.getType();
					 floatByte = tempBlock.getData();
					 floatLocation = tempBlock.getLocation();
					
					//tempBlock.setType(Material.AIR);
					//tempBlock.setData(new Byte((byte) 0));
					
					spawnFallingBlock(floatLocation, floatMat, floatByte, null);
					//entity.teleportTo(floatLocation, false);
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
						ArrayList framesList = new ArrayList(MoveItMain.instance.framesList(pAnimUuid));


						String blockCount = MoveItMain.instance.removeBlock(framesList);
						String frameCount = MoveItMain.instance.removeFrame(pAnimUuid, "0");
						String animRemoved = MoveItMain.instance.removeAnimation(pAnimUuid);
						String animPositionRemoved = MoveItMain.instance.removeAnimationPosition(pAnimUuid);

						player.sendMessage(animRemoved + " animation(s) have been deleted...");
						player.sendMessage(frameCount + " frame(s) have been deleted...");
						player.sendMessage(blockCount + " block(s) have been deleted...");
						MoveItMain.instance.addIndexPSelect(player.getUniqueId(), null, 0, null, null, pwand);
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
						ArrayList framesListFull = new ArrayList();
						ArrayList tempList = new ArrayList((ArrayList) MoveItMain.instance.returnLFrames());
						for(int a = 0; a < tempList.size(); a++){
							ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
							if(pFrameUuid.equals((UUID) tempList2.get(2))){
								framesListFull.add((ArrayList) tempList2);
								framesList.add((UUID) tempList2.get(2));
							}
						}

						String blockCount =MoveItMain.instance.removeBlock(framesList);
						String frameCount = MoveItMain.instance.removeFrame(pFrameUuid, 0);

						player.sendMessage(frameCount + " frame(s) have been deleted...");
						player.sendMessage(blockCount + " block(s) have been deleted...");

						tempList = new ArrayList((ArrayList) MoveItMain.instance.returnLFrames());
						for(int a = 0; a < tempList.size(); a++){
							ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
							if(pFrameUuid.equals((UUID) tempList2.get(2))){
								framesListFull.add((ArrayList) tempList2);
								framesList.add((UUID) tempList2.get(2));
							}
						}

						int listCount = framesListFull.size()-1;
						ArrayList  tempSelectList = new ArrayList((ArrayList)framesListFull.get(listCount));

						MoveItMain.instance.addIndexPSelect(player.getUniqueId(), pAnimUuid, (Integer) tempSelectList.get(3), (UUID) tempSelectList.get(2), null, pwand);
						return true;


					}

					if(arg2.length > 2){

						player.sendMessage("Frames cannot be deleted by name...");
						return false;
					}

				}

				if(arg2[1].equalsIgnoreCase("block")){

					if(arg2.length < 3){
						removeFrameBlocks(player);
					}

					if(arg2.length > 2){


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
						ArrayList tempList = new ArrayList(MoveItMain.instance.returnLAnimation());
						ArrayList tempList5 = new ArrayList();
						UUID tempUUID = null;
						for(int a = 0; a < tempList.size();a++){
							ArrayList tempList2 = (ArrayList)tempList.get(a);
							if( arg2[2].equalsIgnoreCase((String)tempList2.get(0)) ){
								tempUUID = (UUID) tempList2.get(2);
							}
						}
						if(tempUUID == null){
							player.sendMessage("The animation "+arg2[2]+" cannot be found...");
							return false;
						}

						ArrayList tempList3 = new ArrayList((ArrayList) MoveItMain.instance.returnLFrames());
						ArrayList tempList4 = new ArrayList();
						//tempList5 = new ArrayList();
						for(int b = 0; b < tempList3.size(); b++){
							tempList4 = new ArrayList((ArrayList) tempList3.get(b));
							if(tempUUID.equals((UUID) tempList4.get(1))){
								tempList5.add((ArrayList) tempList3.get(b));
							}
						}

						ArrayList tempListFin = (ArrayList) tempList5.get(0);
						//player.sendMessage("Player: "+player.getUniqueId().toString()+"Animation: "+tempUUID+"Frame: 1 FrameUUID: "+tempListFin.get(2));
						player.sendMessage("Animation " + arg2[2] + " has been selected...");
						MoveItMain.instance.addIndexPSelect(player.getUniqueId(), (UUID) tempUUID, 1, (UUID) tempListFin.get(2), null, pwand);
						return true;
					}



				}

				if(arg2[1].equalsIgnoreCase("frame")){
					if(arg2.length < 3){


						player.sendMessage("You need the number of the frame...");
						return false;

					}

					if(arg2.length > 2){
						ArrayList tempList3 = new ArrayList((ArrayList) MoveItMain.instance.returnLFrames());
						ArrayList tempList4 = new ArrayList();
						ArrayList tempList5 = new ArrayList();
						ArrayList tempList6 = new ArrayList();
						for(int b = 0; b < tempList3.size(); b++){
							tempList4 = new ArrayList((ArrayList) tempList3.get(b));
							if(pAnimUuid.equals((UUID) tempList4.get(1))){
								tempList5.add((ArrayList) tempList3.get(b));
							}
						}
						for(int c = 0; c < tempList5.size(); c++){
							tempList6 = new ArrayList((ArrayList) tempList5.get(c));
							if(Integer.parseInt(arg2[2]) == ((Integer) tempList6.get(3))){
								//player.sendMessage("Player: "+player.getUniqueId().toString()+"Animation: "+pAnimUuid.toString()+"Frame: "+arg2[2]+"FrameUUID: "+ ((UUID) tempList6.get(2)).toString());

								player.sendMessage("Frame " + arg2[2] + " has been selected...");
								MoveItMain.instance.addIndexPSelect(player.getUniqueId(), pAnimUuid, Integer.parseInt(arg2[2]), (UUID) tempList6.get(2), null, pwand);
								return true;
							}
						}
						player.sendMessage("The frame "+arg2[2]+" cannot be found...");
						return false;

					}
				}


			}
			
			if(arg2[0].equalsIgnoreCase("save")){
				if(arg2.length < 2){
					saveArrays();
					player.sendMessage("Saved...");
					return true;
				}
				if(arg2.length > 3){
					return true;
				}
			}
			
			if(arg2[0].equalsIgnoreCase("load")){
				if(arg2.length < 2){
					MoveItMain.instance.clearDataAnimation();
					MoveItMain.instance.clearDataFrames();
					MoveItMain.instance.clearDataFrameBlocks();
					MoveItMain.instance.clearDataPSelect();
					
					loadArrays();
					player.sendMessage("Loaded...");
					return true;
				}
				if(arg2.length > 3){
					return true;
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
					//player.sendMessage("Animation: "+pAnimUuid.toString()+" "+"Frame "+ Integer.toString(pFrameInt)+" "+"FrameUUID "+ pFrameUuid);
					MoveItMain.instance.animationsList(player);
					return true;

				}

				if(arg2[1].equalsIgnoreCase("frame")){
					//player.sendMessage("Animation: "+pAnimUuid.toString()+" "+"Frame "+ Integer.toString(pFrameInt)+" "+"FrameUUID "+ pFrameUuid);
					MoveItMain.instance.framesList(player, pAnimUuid);
					return true;

				}

				if(arg2[1].equalsIgnoreCase("block")){
					//player.sendMessage("Animation: "+pAnimUuid.toString()+" "+"Frame "+ Integer.toString(pFrameInt)+" "+"FrameUUID "+ pFrameUuid);
					MoveItMain.instance.blocksList(player, pFrameUuid, player.getWorld().getName());
					return true;
				}

			}
			
			if(arg2[0].equalsIgnoreCase("wand")){
				if(arg2.length < 2){
					if(pwand == 0){
						pwand = 1;
						MoveItMain.instance.addIndexPSelect(player.getUniqueId(), pAnimUuid, pFrameInt, pFrameUuid, pFrameBlock, pwand);
						player.sendMessage("Wand has been enabled...");
						return true;
					}
					else if(pwand == 1){
						pwand = 0;
						MoveItMain.instance.addIndexPSelect(player.getUniqueId(), pAnimUuid, pFrameInt, pFrameUuid, pFrameBlock, pwand);
						player.sendMessage("Wand has been disabled...");
						return true;
					}
					return true;
				}
				if(arg2.length > 3){
					return true;
				}
			}
			
			if(arg2[0].equalsIgnoreCase("play")){
				if(arg2.length < 2){
					player.sendMessage("You need specify the animation...");
					player.sendMessage("./moveit play <animation name>");
					return false;
				}	
				
				ArrayList tempList = new ArrayList((ArrayList) MoveItMain.instance.returnLAnimation());
				ArrayList tempList2 = new ArrayList();
				ArrayList tempList3 = new ArrayList();
				ArrayList tempList4 = new ArrayList();
				ArrayList countList = new ArrayList();
				UUID tempAnimUUID = UUID.randomUUID();
				//player.sendMessage(arg2[1] +" "+ tempAnimUUID);
				for(int a = 0; a < tempList.size(); a++){
					tempList2 = new ArrayList((ArrayList) tempList.get(a));
					if(arg2[1] == (String) tempList2.get(0) || arg2[1].equalsIgnoreCase((String) tempList2.get(0))){
						//player.sendMessage("Match...");
						tempAnimUUID = (UUID) tempList2.get(2);
					}
					else if(arg2[1] != (String) tempList2.get(0) || !arg2[1].equalsIgnoreCase((String) tempList2.get(0))){
						//player.sendMessage("No match...");
					}
				}
				//player.sendMessage(arg2[1] +" "+ tempAnimUUID);
				tempList3 = new ArrayList((ArrayList) MoveItMain.instance.returnLFrames());
				//player.sendMessage(Integer.toString(tempList3.size()));
				for(int b = 0; b < tempList3.size(); b++){
					tempList4 = new ArrayList((ArrayList) tempList3.get(b));
					UUID tempUUID = UUID.randomUUID();
					if((UUID) tempList4.get(1) != null){
						//player.sendMessage("Not null...");
						tempUUID = (UUID) tempList4.get(1);
					}
					if(tempAnimUUID.toString() == tempUUID.toString() || tempAnimUUID.equals(tempUUID) || tempAnimUUID == tempUUID){
						//player.sendMessage("count match..."+tempUUID.toString());
						ArrayList countAddList = new ArrayList();
						countAddList.add(tempUUID);
						countAddList.add(b+1);
						countList.add(countAddList);
					}
				}
				//player.sendMessage(Integer.toString(countList.size()));
				
				for(int c = 0; c < countList.size(); c++){
					ArrayList tempListExpand = new ArrayList((ArrayList) countList.get(c));
				MoveItMain.instance.addIndexPlayList((UUID) tempListExpand.get(0), (Integer) tempListExpand.get(1), countList.size());
				}
				
				ArrayList playListCount = new ArrayList((ArrayList) MoveItMain.instance.returnLPlayList());
				//player.sendMessage("Play List Size "+Integer.toString(playListCount.size()) +"...");
				return true;
			}

			
			if(arg2[0].equalsIgnoreCase("stop")){
				if(arg2.length < 2){
					player.sendMessage("You need specify the animation...");
					player.sendMessage("./moveit stop <animation name>");
					return false;
				}	
				
				stopPlay(arg2[1]);
				
				
				
				//player.sendMessage("Play List Size "+Integer.toString(playListCount.size()) +"...");
				return true;
			}

		}


		return false;
	}
	
	public void stopPlay(String arg2){
		ArrayList tempList = new ArrayList((ArrayList) MoveItMain.instance.returnLAnimation());
		ArrayList tempList2 = new ArrayList();
		ArrayList tempList3 = new ArrayList();
		ArrayList tempList4 = new ArrayList();
		ArrayList countList = new ArrayList();
		UUID tempAnimUUID = UUID.randomUUID();
		//player.sendMessage(arg2[1] +" "+ tempAnimUUID);
		for(int a = 0; a < tempList.size(); a++){
			tempList2 = new ArrayList((ArrayList) tempList.get(a));
			if(arg2 == (String) tempList2.get(0) || arg2.equalsIgnoreCase((String) tempList2.get(0))){
				//player.sendMessage("Match...");
				tempAnimUUID = (UUID) tempList2.get(2);
			}
			else if(arg2 != (String) tempList2.get(0) || !arg2.equalsIgnoreCase((String) tempList2.get(0))){
				//player.sendMessage("No match...");
			}
		}
		//player.sendMessage(arg2[1] +" "+ tempAnimUUID);
		
		ArrayList playListCount = new ArrayList((ArrayList) MoveItMain.instance.returnLPlayList());
		for(int b = playListCount.size()-1; b > -1; b--){
			ArrayList tempListstop = new ArrayList((ArrayList) playListCount.get(b));
			UUID tempUUID = (UUID) tempListstop.get(0);
			if(tempAnimUUID.toString() == tempUUID.toString() || tempAnimUUID.equals(tempUUID) || tempAnimUUID == tempUUID){
				MoveItMain.instance.playList.remove(b);
			}
				
		}
	}
	
	public void floatBlocks(){
		List worldList = MoveItMain.instance.getServer().getWorlds();
		for(int a = 0; a < worldList.size(); a++){
			
			List entityList = ((World) worldList.get(a)).getEntities();
		for(int f = 0; f < entityList.size(); f++){
			if( ((org.bukkit.entity.Entity) entityList.get(f)).getType().getTypeId() == 21){
				//((org.bukkit.entity.Entity) entityList.get(f)).remove();
				
				//NewFloatBlock fb = (NewFloatBlock) ((CraftFallingSand) entityList.get(f)).getHandle();
					
	        		
	        		//fb.setVelocity(fb.getBukkitEntity().getVelocity().multiply(-1));
	        		//MoveItMain.instance.getServer().broadcastMessage("Float that block...");
	        		//((CraftFallingSand)entityList.get(f)).getHandle().getBukkitEntity().setVelocity(((CraftFallingSand)entityList.get(f)).getHandle().getBukkitEntity().getVelocity().multiply(-1));;
				((org.bukkit.entity.Entity) entityList.get(f)).setVelocity(((org.bukkit.entity.Entity) entityList.get(f)).getVelocity().multiply(-1));
			}

		}
		}
	}

	public NewFloatBlock spawnFallingBlock(Location location, org.bukkit.Material material, byte data, UUID frameUUID) throws IllegalArgumentException {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(material.isBlock(), "Material must be a block");

        

        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY() + 1.5;
        double z = location.getBlockZ() + 0.5;
        WorldServer world = ((CraftWorld)location.getWorld()).getHandle();
        //NewFloatingBlock temptrans = new NewFloatingBlock(world);
        com.k0gshole.MoveIt.NewFloatBlock.d0 = x;
        com.k0gshole.MoveIt.NewFloatBlock.d1 = y - 1.5;
        com.k0gshole.MoveIt.NewFloatBlock.d2 = z;
        com.k0gshole.MoveIt.NewFloatBlock.playBlock = net.minecraft.server.v1_8_R2.Block.getById(material.getId()).getBlockData();
        com.k0gshole.MoveIt.NewFloatBlock.playInt = data;
        //com.k0gshole.MoveIt.NewFloatingBlock.dataTest(world);
        //MoveItMain.instance.getServer().broadcastMessage(Double.toString(d0) + " " + Double.toString(d1) + " " +Double.toString(d2) + " " + playBlock.getName() + " " + Integer.toString(playInt));
        //MoveItMain.instance.getServer().broadcastMessage(testData);
        //NewFloatingBlock entity = new NewFloatingBlock(world, "null");

        entity = new NewFloatBlock(world, 0);
        entity.ticksLived = 1;

        world.addEntity(entity, SpawnReason.CUSTOM);
		entity.setVelocity(new Vector(0,0.09,0));
		//MoveItMain.instance.getServer().broadcastMessage(entity.getUniqueID().toString());
        

		
		return entity;
    }
	
	public void loadArrays(){
		try {
			MoveItMain.instance.setArrayAnimation(OpenFile("animations.dat"));


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
		try {
			MoveItMain.instance.setArrayFrames(OpenFile("frames.dat"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		try {
			MoveItMain.instance.setArrayFrameBlocks(OpenFile("frameBlocks.dat"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		try {
			MoveItMain.instance.setArrayAnimationPosition(OpenFile("animationPosition.dat"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
		try {
			MoveItMain.instance.setArrayPlayList(OpenFile("playList.dat"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		//animations.store = new ArrayList(dataFromFile("animations.dat"));
		//frames.store = new ArrayList(dataFromFile("frames.dat"));
		//frameBlocks.store = new ArrayList(dataFromFile("frameBlocks.dat"));
	}
	
	public void saveArrays(){
		ArrayList tempList = new ArrayList((ArrayList) MoveItMain.instance.returnLAnimation());
		ArrayList tempList2 = new ArrayList((ArrayList) MoveItMain.instance.returnLFrames());
		ArrayList tempList3 = new ArrayList((ArrayList) MoveItMain.instance.returnLFrameBlocks());
		ArrayList tempList4 = new ArrayList((ArrayList) MoveItMain.instance.returnLAnimationPosition());
		ArrayList tempList5 = new ArrayList((ArrayList) MoveItMain.instance.returnLPlayList());
		
		
		try {
			writeToFile(tempList, "animations.dat");
			writeToFile(tempList2, "frames.dat");
			writeToFile(tempList3, "frameBlocks.dat");
			writeToFile(tempList4, "animationPosition.dat");
			writeToFile(tempList5, "playList.dat");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dataToFile(ArrayList arrayOut, String theFile) {
		MoveItMain.instance.getServer().broadcastMessage(arrayOut.toString());
		String newPath = thePath+theFile;
		try {
			writeToFile(arrayOut, newPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public ArrayList dataFromFile(String file) {
		String filePath = thePath + file;
		//AlphaReadFile read = new AlphaReadFile(filePath);
		ArrayList tempList = new ArrayList();
		try {
			tempList = new ArrayList(OpenFile(filePath));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MoveItMain.instance.getServer().broadcastMessage(tempList.toString());
		return tempList;
	}
	
	public void writeToFile(ArrayList arrayOut, String fileName) throws IOException{
		String filePath = thePath + fileName;
		ArrayList tempList = new ArrayList();
		File tempFile = new File(filePath);
		tempFile.delete();
		tempFile.createNewFile();
		//for(int a = 0; a < arrayOut.size(); a++){
			//tempList = new ArrayList((ArrayList) arrayOut.get(a));
			tempList = new ArrayList((ArrayList) arrayOut);
		//for(int b = 0; b < tempList.size(); b++){
		FileOutputStream write = new FileOutputStream(filePath, true);
		ObjectOutputStream print_line = new ObjectOutputStream(write);
		print_line.writeObject( tempList);
		//print_line.writeObject( tempList.get(b));
		print_line.close();
		write.close();
		
		//System.out.println(tempList);
		//System.out.println(tempList.get(b));
		//}
		//}
	}
	
	public ArrayList OpenFile(String fileName) throws IOException, ClassNotFoundException{
		String filePath = thePath + fileName;
		ArrayList tempList = new ArrayList();
		FileInputStream fr = new FileInputStream(filePath);
		ObjectInputStream textData = new ObjectInputStream(fr);

			tempList = (ArrayList) textData.readObject();
			
		
		
		//textReader.close();
			//System.out.println(tempList);
			return tempList;
		
	}
	
	public void removeFrameBlocks(Player player){
		if(player.getTargetBlock((HashSet<Byte>) null, 15).isEmpty()){
			player.sendMessage("You need to be looking at a block within a radius of 15...");
			//return false;
		}
		String blockCount = MoveItMain.instance.removeBlock(player.getTargetBlock((HashSet<Byte>) null, 15), pFrameUuid);

		player.sendMessage(blockCount + " block(s) have been deleted...");
		//return true;
	
	}
	public void createNewFrameBlocks(Player player){
		if(player.getTargetBlock((HashSet<Byte>) null, 15).isEmpty()){
			player.sendMessage("You need to be looking at a block within a radius of 15...");
			//return false;
		}
		ArrayList plSelec = new ArrayList(MoveItMain.instance.returnPlayerPSelect(player.getUniqueId()));
		if(plSelec.size() == 0){
			player.sendMessage("You must have an animation selected...");
			//return false;
		}
		Block tempBlock = player.getTargetBlock((HashSet<Byte>) null, 15);
		String tempWorld = tempBlock.getWorld().getName();
		String tempCoord = Integer.toString(tempBlock.getLocation().getBlockX()) +":"+ Integer.toString(tempBlock.getLocation().getBlockY()) + ":" + Integer.toString(tempBlock.getLocation().getBlockZ());
		//player.sendMessage(tempCoord);
		MoveItMain.instance.addIndexFrameBlocks(tempBlock.getType(), tempBlock.getData(),  tempCoord, tempWorld, player.getUniqueId(), pFrameUuid, Instant.now().toString());
		MoveItMain.instance.addIndexPSelect(player.getUniqueId(), pAnimUuid, pFrameInt, pFrameUuid, tempBlock, pwand);
		player.sendMessage("The block has been added...");
		//return true;
	}
	
	
	
	public void updatePlayerSelection(Player player){
		pSelections = new ArrayList((ArrayList) MoveItMain.instance.returnPlayerPSelect(player.getUniqueId()));
		if(pSelections.size() == 0){
			pAnimUuid = null;
			pFrameUuid = null;
			pFrameBlock = null;
			pwand = 0;
		}else{
			pAnimUuid = (UUID) pSelections.get(1);
			pFrameInt = (Integer) pSelections.get(2);
			pFrameUuid = (UUID) pSelections.get(3);
			pFrameBlock = (Block) pSelections.get(4);
			pwand = (Integer) pSelections.get(5);
		}
	}
	
	public void playFrames(){
		//tempFrameInt++;
		
		//ArrayList animationPosition =new ArrayList((ArrayList) MoveItMain.instance.returnLAnimationPosition());
		ArrayList tempList = new ArrayList(MoveItMain.instance.returnLPlayList());
		ArrayList toPlayList = new ArrayList();

		ArrayList toRemoveList = new ArrayList();
		int tempFrameInt = 1;
		UUID finUUID = UUID.randomUUID();
		String playMode = "";
		for(int a = 0; a < tempList.size(); a++){
			
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			UUID tempUUID = UUID.randomUUID();
			tempUUID = (UUID) tempList2.get(0);
			//int tempFrameInt = (Integer) tempList2.get(1);
			int tempMaxFrame = (Integer) tempList2.get(2);
			ArrayList animPos = new ArrayList((ArrayList) MoveItMain.instance.animationPositionList(tempUUID));
			tempFrameInt = (Integer) animPos.get(1);
			//tempFrameInt = tempFrameInt + 1;
			int tempFrameInt2 = tempFrameInt - 1;
			playMode = (String) animPos.get(2);
			//tempFrameInt2--;
			if(tempFrameInt2 <= 0){
				tempFrameInt2 = tempMaxFrame;
			}	

			//tempFrameInt--;

			ArrayList tempList3 = new ArrayList(MoveItMain.instance.returnLFrames());
			for(int b = 0; b < tempList3.size(); b++){
				ArrayList tempList4 = new ArrayList((ArrayList) tempList3.get(b));
				if(tempUUID == (UUID) tempList4.get(1) && tempFrameInt2 == (Integer) tempList4.get(3)){
					//MoveItMain.instance.getServer().broadcastMessage("Frame match..." + Integer.toString(tempFrameInt));
					UUID tempUUID2 = (UUID) tempList4.get(2);
					ArrayList tempList5 = new ArrayList(MoveItMain.instance.returnLFrameBlocks());
					//MoveItMain.instance.getServer().broadcastMessage("frameUUID "+tempUUID2.toString() + Integer.toString(tempList5.size()));
					for(int c = 0; c < tempList5.size(); c++){
						ArrayList tempList6 = new ArrayList((ArrayList) tempList5.get(c));
						UUID tempUUID3 = (UUID) tempList6.get(5);
						if(tempUUID.toString() == tempUUID3.toString() || tempUUID2.toString().equals(tempUUID3.toString()) || tempUUID2 == tempUUID3){
							//MoveItMain.instance.getServer().broadcastMessage("Add to toremoveList...");
							toRemoveList.add(tempList5.get(c));
						}
					}
				}
			}
			
			//tempFrameInt++;
			//MoveItMain.instance.getServer().broadcastMessage("Frame..." + Integer.toString(tempFrameInt2));
			
			if(tempFrameInt > tempMaxFrame){
				tempFrameInt = 1;
			}	

			
			tempList3 = new ArrayList(MoveItMain.instance.returnLFrames());
			for(int b = 0; b < tempList3.size(); b++){
				ArrayList tempList4 = new ArrayList((ArrayList) tempList3.get(b));
				if(tempUUID == (UUID) tempList4.get(1) && tempFrameInt == (Integer) tempList4.get(3)){
					//MoveItMain.instance.getServer().broadcastMessage("Frame match..." + Integer.toString(tempFrameInt));
					UUID tempUUID2 = (UUID) tempList4.get(2);
					ArrayList tempList5 = new ArrayList(MoveItMain.instance.returnLFrameBlocks());
					//MoveItMain.instance.getServer().broadcastMessage("frameUUID "+tempUUID2.toString() + Integer.toString(tempList5.size()));
					for(int c = 0; c < tempList5.size(); c++){
						ArrayList tempList6 = new ArrayList((ArrayList) tempList5.get(c));
						UUID tempUUID3 = (UUID) tempList6.get(5);
						if(tempUUID.toString() == tempUUID3.toString() || tempUUID2.toString().equals(tempUUID3.toString()) || tempUUID2 == tempUUID3){
							//MoveItMain.instance.getServer().broadcastMessage("Add to toPlayList...");
							toPlayList.add(tempList5.get(c));

						}
					}
				}
			}
			
			
		}
		
		//MoveItMain.instance.getServer().broadcastMessage("toRemoveList size "+Integer.toString(toRemoveList.size())+"...");
		/*for(int d = 0; d < toRemoveList.size(); d++){
			ArrayList tempList7 = new ArrayList((ArrayList) toRemoveList.get(d));
			Material tempMat = (Material) tempList7.get(0);
			//MoveItMain.instance.getServer().broadcastMessage("Material..." + tempMat.name());
			Byte tempData = (Byte) tempList7.get(1);
			String tempLoc = (String) tempList7.get(2);
			//MoveItMain.instance.getServer().broadcastMessage("tempLoc..." + tempLoc);
			String worldString = (String) tempList7.get(3);
			//MoveItMain.instance.getServer().broadcastMessage("World..." + worldString);
			String[] tempCoord = tempLoc.split(":");
			//MoveItMain.instance.getServer().broadcastMessage("tempCoord..." + Integer.parseInt(tempCoord[0]) + " " + Integer.parseInt(tempCoord[1]) + " " + Integer.parseInt(tempCoord[2]));
			World world = MoveItMain.instance.getServer().getWorld(worldString);
			Block tempBlock = world.getBlockAt(Integer.parseInt(tempCoord[0]), Integer.parseInt(tempCoord[1]), Integer.parseInt(tempCoord[2])); 
			*/
			//schedulePlaceBlocks(toRemoveList);
			//tempBlock.setType(Material.AIR);
			//tempBlock.setData(new Byte((byte) 0));
			//MoveItMain.instance.getServer().broadcastMessage("Place block...");
		//}
		
		
		/*for(int a = 0; a < tempList.size(); a++){
			
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			UUID tempUUID = UUID.randomUUID();
			tempUUID = (UUID) tempList2.get(0);
			finUUID = tempUUID;
			//int tempFrameInt = (Integer) tempList2.get(1);
			int tempMaxFrame = (Integer) tempList2.get(2);
			ArrayList animPos = new ArrayList((ArrayList) MoveItMain.instance.animationPositionList(tempUUID));
			tempFrameInt =(Integer) animPos.get(1);
			playMode = (String) animPos.get(2);
			//tempFrameInt++;
			if(tempFrameInt > tempMaxFrame){
				tempFrameInt = 1;
			}	

			
			ArrayList tempList3 = new ArrayList(MoveItMain.instance.returnLFrames());
			for(int b = 0; b < tempList3.size(); b++){
				ArrayList tempList4 = new ArrayList((ArrayList) tempList3.get(b));
				if(tempUUID == (UUID) tempList4.get(1) && tempFrameInt == (Integer) tempList4.get(3)){
					//MoveItMain.instance.getServer().broadcastMessage("Frame match..." + Integer.toString(tempFrameInt));
					UUID tempUUID2 = (UUID) tempList4.get(2);
					ArrayList tempList5 = new ArrayList(MoveItMain.instance.returnLFrameBlocks());
					//MoveItMain.instance.getServer().broadcastMessage("frameUUID "+tempUUID2.toString() + Integer.toString(tempList5.size()));
					for(int c = 0; c < tempList5.size(); c++){
						ArrayList tempList6 = new ArrayList((ArrayList) tempList5.get(c));
						UUID tempUUID3 = (UUID) tempList6.get(5);
						if(tempUUID.toString() == tempUUID3.toString() || tempUUID2.toString().equals(tempUUID3.toString()) || tempUUID2 == tempUUID3){
							//MoveItMain.instance.getServer().broadcastMessage("Add to toPlayList...");
							toPlayList.add(tempList5.get(c));

						}
					}
				}
			}
			
		}*/
		
		
		//MoveItMain.instance.getServer().broadcastMessage("toPlayList size "+Integer.toString(toPlayList.size())+"...");
		/*for(int d = 0; d < toPlayList.size(); d++){
			ArrayList tempList7 = new ArrayList((ArrayList) toPlayList.get(d));
			Material tempMat = (Material) tempList7.get(0);
			//MoveItMain.instance.getServer().broadcastMessage("Material..." + tempMat.name());
			Byte tempData = (Byte) tempList7.get(1);
			String tempLoc = (String) tempList7.get(2);
			//MoveItMain.instance.getServer().broadcastMessage("tempLoc..." + tempLoc);
			String worldString = (String) tempList7.get(3);
			//MoveItMain.instance.getServer().broadcastMessage("World..." + worldString);
			String[] tempCoord = tempLoc.split(":");
			//MoveItMain.instance.getServer().broadcastMessage("tempCoord..." + Integer.parseInt(tempCoord[0]) + " " + Integer.parseInt(tempCoord[1]) + " " + Integer.parseInt(tempCoord[2]));
			World world = MoveItMain.instance.getServer().getWorld(worldString);
			Block tempBlock = world.getBlockAt(Integer.parseInt(tempCoord[0]), Integer.parseInt(tempCoord[1]), Integer.parseInt(tempCoord[2])); 
			*/
			ArrayList finList = new ArrayList((ArrayList) MoveItMain.instance.returnLAnimationPosition());
			ArrayList finPlayList = new ArrayList((ArrayList) MoveItMain.instance.returnLPlayList());
			ArrayList tempAnimList = new ArrayList(0);
			for(int a = 0; a < finPlayList.size(); a++){
				ArrayList tempfinList = new ArrayList((ArrayList) finPlayList.get(a));
				UUID playUUID = (UUID) tempfinList.get(0);
				int playMaxInt = (Integer) tempfinList.get(2);
				for(int b = 0; b < finList.size(); b++){
					ArrayList tempfinList3 = new ArrayList((ArrayList) finList.get(b));
					UUID animUUID = (UUID) tempfinList3.get(0);
					int curFrame = (Integer) tempfinList3.get(1);
					String finPlayMode = (String) tempfinList3.get(2);
					if(playUUID.toString().equals(animUUID.toString()) || playUUID.toString() == animUUID.toString() || playUUID.equals(animUUID) || playUUID == animUUID){

						if(curFrame == playMaxInt){
							MoveItMain.instance.addIndexAnimationPosition(animUUID, 1, finPlayMode);
							//MoveItMain.instance.getServer().broadcastMessage("Frame: " + Integer.toString(1));	
						}else
						{
							
							MoveItMain.instance.addIndexAnimationPosition(animUUID, curFrame + 1, finPlayMode);
							//MoveItMain.instance.getServer().broadcastMessage("Frame: " + curFrame + 1);
						}
						
					}
				}
				
			}



	
			placeBlock(toPlayList, toRemoveList);
			
			//tempBlock.setType(tempMat);
			//tempBlock.setData(tempData);
			//MoveItMain.instance.getServer().broadcastMessage("Place block..." + Integer.toString(tempFrameInt));
		 //}

		
	}
		
	public void placeBlock(ArrayList toPlayList, ArrayList toRemoveList){
		
		for(int d = 0; d < toRemoveList.size(); d++){
			ArrayList tempList7 = new ArrayList((ArrayList) toRemoveList.get(d));
			Material tempMat = (Material) tempList7.get(0);
			//MoveItMain.instance.getServer().broadcastMessage("Material..." + tempMat.name());
			Byte tempData = (Byte) tempList7.get(1);
			String tempLoc = (String) tempList7.get(2);
			//MoveItMain.instance.getServer().broadcastMessage("tempLoc..." + tempLoc);
			String worldString = (String) tempList7.get(3);
			//MoveItMain.instance.getServer().broadcastMessage("World..." + worldString);
			String[] tempCoord = tempLoc.split(":");
			//MoveItMain.instance.getServer().broadcastMessage("tempCoord..." + Integer.parseInt(tempCoord[0]) + " " + Integer.parseInt(tempCoord[1]) + " " + Integer.parseInt(tempCoord[2]));
			World world = MoveItMain.instance.getServer().getWorld(worldString);
			Block tempBlock = world.getBlockAt(Integer.parseInt(tempCoord[0]), Integer.parseInt(tempCoord[1]), Integer.parseInt(tempCoord[2])); 

			
			List entityList = world.getEntities();

			//schedulePlaceBlocks(toRemoveList);
			tempBlock.setType(Material.AIR);
			tempBlock.setData(new Byte((byte) 0));
			for(int f = 0; f < entityList.size(); f++){
				if( ((org.bukkit.entity.Entity) entityList.get(f)).getType().getTypeId() == 21){
					((org.bukkit.entity.Entity) entityList.get(f)).remove();
				}
				//MoveItMain.instance.getServer().broadcastMessage(((org.bukkit.entity.Entity) entityList.get(f)).getUniqueId().toString());
				//MoveItMain.instance.getServer().broadcastMessage(entityUUID.toString());

			}
			//MoveItMain.instance.getServer().broadcastMessage("Place block...");
		}
		
		for(int d = 0; d < toPlayList.size(); d++){
			ArrayList tempList7 = new ArrayList((ArrayList) toPlayList.get(d));
			Material tempMat = (Material) tempList7.get(0);
			//MoveItMain.instance.getServer().broadcastMessage("Material..." + tempMat.name());
			Byte tempData = (Byte) tempList7.get(1);
			String tempLoc = (String) tempList7.get(2);
			//MoveItMain.instance.getServer().broadcastMessage("tempLoc..." + tempLoc);
			String worldString = (String) tempList7.get(3);
			//MoveItMain.instance.getServer().broadcastMessage("World..." + worldString);
			String[] tempCoord = tempLoc.split(":");
			UUID frameUUID = UUID.randomUUID();
			frameUUID = (UUID) tempList7.get(5); 
			//MoveItMain.instance.getServer().broadcastMessage("tempCoord..." + Integer.parseInt(tempCoord[0]) + " " + Integer.parseInt(tempCoord[1]) + " " + Integer.parseInt(tempCoord[2]));
			World world = MoveItMain.instance.getServer().getWorld(worldString);
			Block tempBlock = world.getBlockAt(Integer.parseInt(tempCoord[0]), Integer.parseInt(tempCoord[1]), Integer.parseInt(tempCoord[2])); 
			
			
			//schedulePlaceBlocks(tempBlock, tempMat, tempData);
			//tempBlock.setType(tempMat);
			//tempBlock.setData(tempData);
			//MoveItMain.instance.playBlock = tempBlock;
			///MoveItMain.instance.playMat = tempMat;
			//MoveItMain.instance.playByte = tempData;
			
			spawnFallingBlock(tempBlock.getLocation(), tempMat, tempData, frameUUID);

		}
		
		//MoveItMain.instance.getServer().broadcastMessage("toRemoveList size "+Integer.toString(toRemoveList.size())+"...");


		//block.setType(mat);
		//block.setData(data);
	}
	


	
	@EventHandler
	public void ongameTick(GameTickEvent event){
		count++;
		count2++;
		int count3 = 0;
		
		if (count3 == 1){
			floatBlocks();
		}
		
		if(count2 == 5){
			playFrames();
			count2 = 0;
		}
		
		
		if(count == 1200){
			//MoveItMain.instance.getServer().broadcastMessage("Save Array...");
			saveArrays();
			count = 0;
		}
		
		
	}
	

	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
		updatePlayerSelection(player);
		//player.sendMessage(Integer.toString(pwand));
		//MoveItMain.instance.getServer().broadcastMessage(Integer.toString(player.getItemInHand().getTypeId()));
		//MoveItMain.instance.getServer().broadcastMessage(player.getItemInHand().getType().name());
			if(player.getItemInHand().getType() == Material.SHEARS){
				//MoveItMain.instance.getServer().broadcastMessage("Wand_CLick");
				if(event.getAction() == Action.LEFT_CLICK_BLOCK){
					//MoveItMain.instance.getServer().broadcastMessage("left_Wand_CLick");
					if(pwand == 1){

						//MoveItMain.instance.getServer().broadcastMessage("wand_enabled");
						createNewFrameBlocks(player);
						event.setCancelled(true);
				}
			}
			
				if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
					//MoveItMain.instance.getServer().broadcastMessage("Right_Wand_CLick");
					if(pwand == 1){

						//MoveItMain.instance.getServer().broadcastMessage("wand_enabled");
						removeFrameBlocks(player);
						event.setCancelled(true);
				}
			}

				
		}
		
	}
	
}

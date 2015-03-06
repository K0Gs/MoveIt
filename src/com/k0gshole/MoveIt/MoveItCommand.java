package com.k0gshole.MoveIt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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

	
	int count = 0;
	int count2 = 0;
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


					player.sendMessage("You need the name of the animation...");
					return false;

				}	
				
				ArrayList tempList = new ArrayList((ArrayList) MoveItMain.instance.animation);
				ArrayList tempList2 = new ArrayList();
				ArrayList countList = new ArrayList();
				UUID tempAnimUUID = UUID.randomUUID();
				for(int a = 0; a < tempList.size(); a++){
					tempList2 = new ArrayList((ArrayList) tempList.get(a));
					if(arg2[1] == (String) tempList2.get(0)){
						tempAnimUUID = (UUID) tempList2.get(2);
					}
					
				}
				tempList = new ArrayList((ArrayList) MoveItMain.instance.frames);
				for(int b = 0; b < tempList.size(); b++){
					tempList2 = new ArrayList((ArrayList) tempList.get(b));
					if(tempAnimUUID == (UUID) tempList2.get(1)){
						countList.add((ArrayList) tempList.get(b));
					}
				}
				
				MoveItMain.instance.addIndexPlayList(tempAnimUUID, 1, countList.size());
				
			}

			if(arg2[0].equalsIgnoreCase("stop")){
				if(arg2.length < 2){


					player.sendMessage("You need the name of the animation...");
					return false;

				}
				
				
			}
		}


		return false;
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
		//animations.store = new ArrayList(dataFromFile("animations.dat"));
		//frames.store = new ArrayList(dataFromFile("frames.dat"));
		//frameBlocks.store = new ArrayList(dataFromFile("frameBlocks.dat"));
	}
	
	public void saveArrays(){
		ArrayList tempList = new ArrayList((ArrayList) MoveItMain.instance.returnLAnimation());
		ArrayList tempList2 = new ArrayList((ArrayList) MoveItMain.instance.returnLFrames());
		ArrayList tempList3 = new ArrayList((ArrayList) MoveItMain.instance.returnLFrameBlocks());

		
		try {
			writeToFile(tempList, "animations.dat");
			writeToFile(tempList2, "frames.dat");
			writeToFile(tempList3, "frameBlocks.dat");

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
		if(player.getTargetBlock(null, 15).isEmpty()){
			player.sendMessage("You need to be looking at a block within a radius of 15...");
			//return false;
		}
		String blockCount = MoveItMain.instance.removeBlock(player.getTargetBlock(null, 15), pFrameUuid);

		player.sendMessage(blockCount + " block(s) have been deleted...");
		//return true;
	
	}
	public void createNewFrameBlocks(Player player){
		if(player.getTargetBlock(null, 15).isEmpty()){
			player.sendMessage("You need to be looking at a block within a radius of 15...");
			//return false;
		}
		ArrayList plSelec = new ArrayList(MoveItMain.instance.returnPlayerPSelect(player.getUniqueId()));
		if(plSelec.size() == 0){
			player.sendMessage("You must have an animation selected...");
			//return false;
		}
		Block tempBlock = player.getTargetBlock(null, 15);
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
		
		ArrayList tempList = new ArrayList(MoveItMain.instance.playList);
		ArrayList toPlayList = new ArrayList();
		for(int a = 0; a < tempList.size(); a++){
			toPlayList = new ArrayList();
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			UUID tempUUID = UUID.randomUUID();
			tempUUID = (UUID) tempList2.get(0);
			int tempFrameInt = (Integer) tempList2.get(1);
			int tempMaxFrame = (Integer) tempList2.get(2);
			
			tempFrameInt++;
			if(tempFrameInt > tempMaxFrame){
				tempFrameInt = 1;
			}
			
			ArrayList tempList3 = new ArrayList(MoveItMain.instance.frames);
			for(int b = 0; b < tempList3.size(); b++){
				if(tempUUID == (UUID) tempList3.get(1) && tempFrameInt == (Integer) tempList3.get(3)){
					UUID tempUUID2 = (UUID) tempList3.get(2);
					ArrayList tempList4 = new ArrayList(MoveItMain.instance.frameblocks);
					for(int c = 0; c < tempList4.size(); c++){
						ArrayList tempList5 = new ArrayList((ArrayList) tempList4.get(c));
						if(tempUUID == (UUID) tempList5.get(5)){
							toPlayList.add(tempList4.get(c));
						}
					}
				}
			}
			
			for(int d = 0; d < toPlayList.size(); d++){
				ArrayList tempList6 = new ArrayList((ArrayList) toPlayList.get(d));
				Material tempMat = (Material) tempList6.get(0);
				Byte tempData = (Byte) tempList6.get(1);
				String tempLoc = (String) tempList6.get(2);
				String worldString = (String) tempList6.get(3);
				String[] tempCoord = tempLoc.split(":");
				
				World world = MoveItMain.instance.getServer().getWorld(worldString);
				Block tempBlock = world.getBlockAt(Integer.parseInt(tempCoord[0]), Integer.parseInt(tempCoord[1]), Integer.parseInt(tempCoord[3])); 
				
				tempBlock.setType(tempMat);
				tempBlock.setData(tempData);
				
			}
		}
		
	}
		
	
	@EventHandler
	public void ongameTick(GameTickEvent event){
		count++;
		count2++;
		
		if(count2 == 10){
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
		player.sendMessage(Integer.toString(pwand));
		MoveItMain.instance.getServer().broadcastMessage(Integer.toString(player.getItemInHand().getTypeId()));
		MoveItMain.instance.getServer().broadcastMessage(player.getItemInHand().getType().name());
			if(player.getItemInHand().getType() == Material.SHEARS){
				MoveItMain.instance.getServer().broadcastMessage("Wand_CLick");
				if(event.getAction() == Action.LEFT_CLICK_BLOCK){
					MoveItMain.instance.getServer().broadcastMessage("left_Wand_CLick");
					if(pwand == 1){

						MoveItMain.instance.getServer().broadcastMessage("wand_enabled");
						createNewFrameBlocks(player);
						event.setCancelled(true);
				}
			}
			
				if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
					MoveItMain.instance.getServer().broadcastMessage("Right_Wand_CLick");
					if(pwand == 1){

						MoveItMain.instance.getServer().broadcastMessage("wand_enabled");
						removeFrameBlocks(player);
						event.setCancelled(true);
				}
			}

				
		}
		
	}
	
}

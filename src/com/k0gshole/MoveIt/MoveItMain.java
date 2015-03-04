package com.k0gshole.MoveIt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;


public class MoveItMain extends JavaPlugin{
	
	public static MoveItMain instance;
	private MoveItMain plugin;
	private GameTickEvent gametickevent = new GameTickEvent();
	public static Permission perms = null;
	public String thePath = "./plugins/MoveIt/";
	public File configyml = new File("plugins/MoveIt/config.yml");
	public File animationsFile = new File("plugins/MoveIt/animations.dat");
	public File framesFile = new File("plugins/MoveIt/frames.dat");
	public File frameBlocksFile = new File("plugins/MoveIt/frameBlocks.dat");
	public ArrayList animation = new ArrayList();
	public ArrayList frameblocks = new ArrayList();
	public ArrayList frames = new ArrayList();
	//Animations animations = new Animations();
	//Frames frames = new Frames();
	//FrameBlocks frameBlocks = new FrameBlocks();

	public MoveItMain(){
		
	}
	
	public static MoveItMain getInstance(){
		return instance;
	}
	
	
	@EventHandler
	public void onEnable(){
		instance = this;
		instance.getServer().broadcastMessage("[MoveIt] Now Loading...]");
		
		getCommand("moveit").setExecutor(new MoveItCommand());
		Bukkit.getPluginManager().registerEvents(new MoveItCommand(this), this);
		
		registerGameTickEvent();
		//MoveItCommand loadDat = new MoveItCommand();


		if(!configyml.exists()){
			getInstance().saveDefaultConfig();
		}
		if(!animationsFile.exists()){
			getInstance().saveResource("animations.dat", false);
		}
		
		if(!framesFile.exists()){

			getInstance().saveResource("frames.dat", false);
		}
		
		if(!frameBlocksFile.exists()){

			getInstance().saveResource("frameBlocks.dat", false);
		}
		MoveItCommand loadFile = new MoveItCommand();
		loadFile.loadArrays();
		
	}
	
	@EventHandler
	public void onDisable(){
		MoveItCommand saveDat = new MoveItCommand();
		saveDat.saveArrays();
		instance.getServer().broadcastMessage("[MoveIt] Good bye...");
	}

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        System.out.println("[MoveIt] Permissions Enabled...");
        return perms != null;
    }

	private void registerGameTickEvent(){
		Bukkit.getScheduler().runTaskTimer(this, new Runnable(){
		public void run(){
			Bukkit.getPluginManager().callEvent(gametickevent);
		}
		}, 1, 1);
	}
	
	public void dataToFile(ArrayList arrayOut, String theFile) {
		instance.getServer().broadcastMessage(arrayOut.toString());
		AlphaWriteFile write = new AlphaWriteFile(thePath+theFile, false);
		try {
			write.writeToFile(arrayOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public ArrayList dataFromFile(String file) {
		String filePath = thePath + file;
		AlphaReadFile read = new AlphaReadFile(filePath);
		ArrayList tempList = new ArrayList();
		try {
			tempList = new ArrayList(read.OpenFile());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//instance.getServer().broadcastMessage(tempList.toString());
		return tempList;
	}
	


	public void addIndexAnimation(String animName, UUID player, UUID uuid, String now){
		
		ArrayList tList = new ArrayList();
		tList.add(animName);
		tList.add(player);
		tList.add(uuid);
		tList.add(now);
		this.animation.add(tList);
	}
	
	public String removeAnimation(UUID uuid){
		int count = 0;
		ArrayList tempList = new ArrayList((ArrayList) animation);
			for (int a = 0;a < tempList.size();a++){
				ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));

				if(uuid.equals((UUID) tempList2.get(2))){
					this.animation.remove(a);
					count++;
				}
				
			}
			
		return Integer.toString(count);
	}
	
	public void removeAnimation(String animName){
		for (int a = 0;a < this.animation.size();a++){
			ArrayList tempList = new ArrayList((ArrayList)this.animation.get(a));
			String tempInst2 = (String)tempList.get(0);

			if(tempInst2 == animName){
				this.animation.remove(a);
			}
			
		}
		
	
	}
	
	public void animationsList(Player player){
		ArrayList tempList = new ArrayList(animation);
		//Player player = Player.this.getPlayer().
		player.sendMessage("Animations: ");
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			player.sendMessage((String) tempList2.get(0));
		}
	}
	
	public void clearDataAnimation(){
		this.animation.clear();
		//MoveItMain.instance.getServer().broadcastMessage("Clear Data");
	}
	
	public ArrayList returnLAnimation(){
		return this.animation;
	}

	public void setArrayAnimation(ArrayList newArray){
		this.animation = new ArrayList((ArrayList) newArray);
	}
	
	public void addIndexFrameBlocks(Material block, Byte data, String loc, String world, UUID player, UUID frameUuid, String now){

		ArrayList tList = new ArrayList();
		tList.add(block);
		tList.add(data);
		tList.add(loc);
		tList.add(world);
		tList.add(player);
		tList.add(frameUuid);
		tList.add(now);
		this.frameblocks.add(tList);
	}

	public String removeBlock(ArrayList uuidList){
		int count = 0;
		for (int a = this.frameblocks.size()-1; a > -1; a--){
			ArrayList tempList = new ArrayList((ArrayList) frameblocks.get(a));
			UUID frameUuid = (UUID) tempList.get(5);

			for(int b = 0; b < uuidList.size(); b++){
				UUID uuid = (UUID) uuidList.get(b);
			if(uuid.equals(frameUuid)){
				this.frameblocks.remove(a);
				count++;
			}
			}

		}
		return Integer.toString(count);

	}
	
	/*public String removeBlock(Block block){
		int count = 0;
		ArrayList tempList = new ArrayList(frameblocks);
		for (int a = 0;a < tempList.size();a++){
			ArrayList tempList2 = new ArrayList((ArrayList)tempList.get(a));
			Block frameBlock = (Block) tempList2.get(0);

			if(frameBlock.equals(block)){
				this.frameblocks.remove(a);
				count++;
			}

		}
		return Integer.toString(count);

	}*/
	
	public void blocksList(Player player, UUID uuid, String worldName){
		ArrayList tempList = new ArrayList(frameblocks);
		player.sendMessage("Blocks: ");
		
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			if(uuid.toString().equals(((UUID) tempList2.get(5)).toString()) || uuid.toString() == ((UUID) tempList2.get(5)).toString() || uuid == (UUID)tempList2.get(5)){
				String tempLoc = (String) tempList2.get(2);
				String[] tempCoord = tempLoc.split(":");
				//player.sendMessage(Integer.toString(tempCoord.length));
				World world = MoveItMain.instance.getServer().getWorld(worldName);
				Block tempBlock = world.getBlockAt(Integer.parseInt(tempCoord[0]), Integer.parseInt(tempCoord[1]), Integer.parseInt(tempCoord[2]));
				
				String blockX = Integer.toString(tempBlock.getLocation().getBlockX());
				String blockY = Integer.toString(tempBlock.getLocation().getBlockY());
				String blockZ = Integer.toString(tempBlock.getLocation().getBlockZ());
				player.sendMessage(blockX+" "+blockY+" "+blockZ);
			}
		}
	}

	public void clearDataFrameBlocks(){
		this.frameblocks.clear();
		//MoveItMain.instance.getServer().broadcastMessage("Clear Data");
	}

	public ArrayList returnLFrameBlocks(){
		return this.frameblocks;
	}

	public void setArrayFrameBlocks(ArrayList newArray){
		this.frameblocks = new ArrayList((ArrayList) newArray);
	}
	
public void addIndexFrames(UUID player, UUID animUuid, UUID frameUuid, int frame){
		
		ArrayList tList = new ArrayList();
		tList.add(player);
		tList.add(animUuid);
		tList.add(frameUuid);
		tList.add(frame);
		this.frames.add(tList);
	}
	
	public String removeFrame(UUID uuid, String dummy){
		int count = 0;
		ArrayList tempList = new ArrayList((ArrayList) this.frames);
			for (int a = tempList.size() - 1; a > -1; a--){
				ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
				//UUID tempInst2 = (UUID)tempList.get(2);

				if(uuid.equals((UUID)tempList2.get(1))){
					this.frames.remove(a);
					count++;
				}
				
			}
			
		return Integer.toString(count);
	}
	
	public String removeFrame(UUID uuid, int dummy){
		int count = 0;
			for (int a = 0;a < this.frames.size();a++){
				ArrayList tempList = new ArrayList((ArrayList) this.frames.get(a));
				//UUID tempInst2 = (UUID)tempList.get(2);

				if(uuid.equals((UUID)tempList.get(2))){
					this.frames.remove(a);
					count++;
				}
				
			}
			
		return Integer.toString(count);
	}
	
	public void removeFrame(int frame){
		for (int a = 0;a < this.frames.size();a++){
			ArrayList tempList = new ArrayList((ArrayList)this.frames.get(a));
			int tempInst2 = (Integer)tempList.get(2);

			if(tempInst2 == frame){
				this.frames.remove(a);
			}
			
		}
		
	
	}
	
	public void framesList(Player player, UUID uuid){
		ArrayList tempList = new ArrayList(frames);
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
		for(int a = 0; a < this.frames.size(); a++){
			tempList = new ArrayList((ArrayList) this.frames.get(a));
			if(uuid.equals((UUID) tempList.get(1))){
			tempList2.add((UUID) tempList.get(2));
			}
		}
		
		return tempList2;
	}
	
	public void clearDataFrames(){
		this.frames.clear();
		//MoveItMain.instance.getServer().broadcastMessage("Clear Data");
	}
	
	public ArrayList returnLFrames(){
		return this.frames;
	}

	public void setArrayFrames(ArrayList newArray){
		this.frames = new ArrayList((ArrayList) newArray);
	}


}

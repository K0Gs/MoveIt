package com.k0gshole.MoveIt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import net.milkbowl.vault.permission.Permission;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.EntityFallingBlock;
import net.minecraft.server.v1_8_R2.EntityTypes;
import net.minecraft.server.v1_8_R2.WorldServer;

import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;


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
	public File animationPositionFile = new File("plugins/MoveIt/animationPosition.dat");
	public File playListFile = new File("plugins/MoveIt/playList.dat");
	public ArrayList animation = new ArrayList();
	public ArrayList frameblocks = new ArrayList();
	public ArrayList frames = new ArrayList();
	public ArrayList selections = new ArrayList();
	public ArrayList playList = new ArrayList();
	public ArrayList animationPosition = new ArrayList();
	public int wand_tool = 0;
	public YamlConfiguration config = null;
	public net.minecraft.server.v1_8_R2.Block playBlock = null;
	public Material playMat = Material.AIR;
	public Byte playByte = new Byte((byte) 0);
	public double d0 = 0;
	public double d1 = 0;
	public double d2 = 0;
	public int playInt = 0;
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
		
		if(!animationPositionFile.exists()){

			getInstance().saveResource("animationPosition.dat", false);
		}
		
		if(!playListFile.exists()){

			getInstance().saveResource("playList.dat", false);
		}
		
		config = YamlConfiguration.loadConfiguration(configyml);
		wand_tool = config.getInt("wand_tool");
		
		
		MoveItCommand loadFile = new MoveItCommand();
		loadFile.loadArrays();

		registerEntityType(NewFloatBlock.class, "FallingBlock", 21);
	}
	
	@EventHandler
	public void onDisable(){
		ArrayList stopList = new ArrayList((ArrayList) animationsList());
		MoveItCommand saveDat = new MoveItCommand();
		for(int a = 0; a < stopList.size(); a++){
			saveDat.stopPlay((String) stopList.get(a)); 
		}
		
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
	

	/**
	 * Registers custom entity class at the native minecraft entity enum
	 * 
	 * @param inClass	class of the entity
	 * @param name		minecraft entity name
	 * @param inID		minecraft entity id
	 */
	public static void registerEntityType(Class<?> inClass, String name, int inID)
	{
		try
		{
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;
 
            a(inClass, name, inID);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
	}
	
	/*
	 * Since 1.7.2 added a check in their entity registration, simply bypass it and write to the maps ourself.
	 */
	@SuppressWarnings("unchecked")
	private static void a(Class paramClass, String paramString, int paramInt)
	{
		try
		{
			((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
			((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
			((Map) getPrivateStatic(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass);
			((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
			((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
		}
		catch (Exception exc)
		{
			// Unable to register the new class.
		}
	}
	
	public static Object getPrivateStatic(Class clazz, String f) throws Exception
	{
		Field field = clazz.getDeclaredField(f);
		field.setAccessible(true);
		return field.get(null);
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
	
	public ArrayList animationsList(){
		ArrayList tempList = new ArrayList(animation);
		//Player player = Player.this.getPlayer().
		//player.sendMessage("Animations: ");
		ArrayList returnList = new ArrayList();
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			//player.sendMessage((String) tempList2.get(0));
			returnList.add((String) tempList2.get(0));
			
		}
		return returnList;
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
	
	public String removeBlock(Block block, UUID frameUUID){
		int count = 0;
		ArrayList tempList = new ArrayList(frameblocks);
		for (int a = 0;a < tempList.size();a++){
			ArrayList tempList2 = new ArrayList((ArrayList)tempList.get(a));
			String frameLoc = (String) tempList2.get(2);
			String[] FrameLocArray = frameLoc.split(":");
			Block frameBlock = block.getWorld().getBlockAt(Integer.parseInt(FrameLocArray[0]), Integer.parseInt(FrameLocArray[1]), Integer.parseInt(FrameLocArray[2]) );
			if(frameBlock.equals(block)){
			if(frameUUID.equals((UUID) tempList2.get(5))){
				this.frameblocks.remove(a);
				count++;
			}
			}

		}
		return Integer.toString(count);

	}
	
	public void blocksList(Player player, UUID uuid, String worldName){
		ArrayList tempList = new ArrayList(frameblocks);
		player.sendMessage("Blocks: ");
		if(uuid == null){
			uuid = UUID.randomUUID();
		}
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			//player.sendMessage(Integer.toString(tempList2.size()));
			UUID tempUUID = UUID.randomUUID();
			if((UUID) tempList2.get(5) != null){
				tempUUID = (UUID) tempList2.get(5);
			}
			if(uuid.toString().equals(tempUUID.toString()) || uuid.toString() == tempUUID.toString() || uuid == tempUUID){
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
		if(uuid == null){
			uuid = UUID.randomUUID();
		}
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			UUID tempUUID = UUID.randomUUID();
			if((UUID)tempList2.get(1) != null){
				tempUUID = (UUID)tempList2.get(1);
			}
			if(uuid.toString().equals(tempUUID.toString()) || uuid.toString() == tempUUID.toString() || uuid == tempUUID){
				player.sendMessage(Integer.toString((Integer) tempList2.get(3)));
			}
		}
	}
	
	public ArrayList framesList(UUID uuid){
		ArrayList tempList = new ArrayList();
		ArrayList tempList2 = new ArrayList();
		for(int a = 0; a < this.frames.size(); a++){
			tempList = new ArrayList((ArrayList) this.frames.get(a));
			UUID tempUUID = UUID.randomUUID();
			if((UUID) tempList.get(1) != null){
				tempUUID = (UUID) tempList.get(1);
			}
			if(uuid.equals(tempUUID)){
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


	public void addIndexPSelect(UUID player, UUID animation, Integer frameInt, UUID frame, Block block, int wand, int fineWand, String Cor1, String Cor2){
		
		for(int a = 0; a < selections.size(); a++){
			ArrayList tempList = new ArrayList((ArrayList) selections.get(a));
			if(player == (UUID) tempList.get(0)){
				selections.remove(a);
			}
			
		}
		
		ArrayList tList = new ArrayList();
		tList.add(player);
		tList.add(animation);
		tList.add(frameInt);
		tList.add(frame);
		tList.add(block);
		tList.add(wand);
		tList.add(fineWand);
		tList.add(Cor1);
		tList.add(Cor2);
		this.selections.add(tList);
		
	}
	
	public void removePlayerPSelect(Player player){
		for (int a = 0;a < this.selections.size();a++){
			ArrayList tempList = new ArrayList((ArrayList)this.selections.get(a));
			Player tempPlayer = (Player) tempList.get(0);

			if(tempPlayer == player){
				this.selections.remove(a);
			}
			
		}
		
	
	}
	
	public ArrayList returnPlayerPSelect(UUID player){
		ArrayList tempList = new ArrayList(selections);
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
	
	public void clearDataPSelect(){
		this.selections.clear();
		MoveItMain.instance.getServer().broadcastMessage("Clear Data");
	}
	
	public ArrayList returnLPSelect(){
		return this.selections;
	}
	
	public void addIndexPlayList(UUID animUUID, int frame, int frameMax){
		
		ArrayList tList = new ArrayList();
		tList.add(animUUID);
		tList.add(frame);
		tList.add(frameMax);
		this.playList.add(tList);
	}

	public String removePlayList(UUID uuid, String dummy){
	int count = 0;
	ArrayList tempList = new ArrayList((ArrayList) this.frames);
		for (int a = tempList.size() - 1; a > -1; a--){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			//UUID tempInst2 = (UUID)tempList.get(2);

			if(uuid.equals((UUID)tempList2.get(0))){
				this.playList.remove(a);
				count++;
			}
			
		}
		
	return Integer.toString(count);
	}

	public ArrayList returnLPlayList(){
	return this.playList;
	}
	
	public void setArrayPlayList(ArrayList newArray){
		this.playList = new ArrayList((ArrayList) newArray);
	}
	
	public void addIndexAnimationPosition(UUID animUUID, int lastFrame, String playMode){
		
		//removePlayMode(animUUID);
		for(int a = 0; a < animationPosition.size(); a++){
			ArrayList tempList = new ArrayList((ArrayList) animationPosition.get(a));
			if(animUUID == (UUID) tempList.get(0)){
				animationPosition.remove(a);
			}
		}
		
		ArrayList tList = new ArrayList();
		tList.add(animUUID);
		tList.add(lastFrame);
		tList.add(playMode);
		this.animationPosition.add(tList);
	}
	
	public ArrayList animationPositionList(UUID animUUID){
		ArrayList tempList = new ArrayList((ArrayList) animationPosition);
		ArrayList returnList = new ArrayList();
		for(int a = 0; a < tempList.size(); a++){
			ArrayList tempList2 = new ArrayList((ArrayList) tempList.get(a));
			UUID tempUUID = (UUID) tempList2.get(0);
			if(animUUID.toString() == tempUUID.toString() || animUUID.toString().equals(tempUUID.toString()) || animUUID == tempUUID){
				returnList = new ArrayList((ArrayList) tempList.get(a));
			}
		}
		return returnList;
	}
	

	
	public ArrayList returnLAnimationPosition(){
	return this.animationPosition;
	}
	
	public void setArrayAnimationPosition(ArrayList newArray){
		this.animationPosition = new ArrayList((ArrayList) newArray);
	}
	
	public String removeAnimationPosition(UUID uuid){
		int count = 0;
			for (int a = 0;a < this.animationPosition.size();a++){
				ArrayList tempList = new ArrayList((ArrayList) this.animationPosition.get(a));
				//UUID tempInst2 = (UUID)tempList.get(2);
				UUID tempUUID = UUID.randomUUID();
				if((UUID) tempList.get(0) != null){
					tempUUID = (UUID) tempList.get(0);
				}
				if(uuid.equals(tempUUID)){
					this.animationPosition.remove(a);
					count++;
				}
				
			}
			
		return Integer.toString(count);
	}
	
}



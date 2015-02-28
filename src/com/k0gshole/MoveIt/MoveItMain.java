package com.k0gshole.MoveIt;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;


public class MoveItMain extends JavaPlugin{
	
	public static MoveItMain instance;
	private MoveItMain plugin;
	private GameTickEvent gametickevent = new GameTickEvent();
	public static Permission perms = null;
	
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
		
		registerGameTickEvent();
		
	}
	
	@EventHandler
	public void onDisable(){
		instance.getServer().broadcastMessage("[MoveIt] Good bye...]");
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
	

}

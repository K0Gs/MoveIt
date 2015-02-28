package com.k0gshole.MoveIt;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoveItCommand implements CommandExecutor{
	
	private MoveItMain plugin;
	
	
	public MoveItCommand(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String arg1,
			String arg2[]){
		
		if(!(sender instanceof Player)){
			return false;	
		}
		
		Player player = (Player)sender;
		
		if (arg2.length == 0){
			//if (arg2 == ""){
			//player.openInventory(GUEye.getInstance().displayGUI(player));
			player .sendMessage("Player "+ sender + "Command " + command.toString());
			return true;
			//}
		}
		
		if(arg2.length != 0){
			String newStr = "";
			for(int a = 0;a < arg2.length;a++){
				newStr = newStr + " " + arg2[a];
			}
			player .sendMessage("Player "+ sender + "Command " + command.toString() + " First String " + arg1.toString() +"Second String "+ newStr);
		
			
			if (arg2[0].equalsIgnoreCase("invsee")){
				if(arg2.length < 2){
					return false;
				}
				return true;
			}
		}
		
		
		return false;
	}
	

}

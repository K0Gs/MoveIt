package com.k0gshole.MoveIt;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameTickEvent extends Event{

	private MoveItMain plugin;
	//Animations animations;
	//Frames frames;
	//FrameBlocks frameBlocks;
	
	public GameTickEvent(){
		//animations = new Animations();
		//frames = new Frames();
		//frameBlocks = new FrameBlocks();
	}
	
	public GameTickEvent(MoveItMain plugin){
		this.plugin = plugin;
	}
	
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return GameTickEvent.handlers;
	}

	public static HandlerList getHandlerList(){
		return GameTickEvent.handlers;
	}
}

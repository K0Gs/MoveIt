package com.k0gshole.MoveIt;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameTickEvent extends Event{


	//Animations animations;
	//Frames frames;
	//FrameBlocks frameBlocks;
	
	public GameTickEvent(){
		//animations = new Animations();
		//frames = new Frames();
		//frameBlocks = new FrameBlocks();
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

package com.k0gshole.MoveIt;

import java.util.ArrayList;
import java.util.UUID;

public class DeleteAnimation {

	Animations animations;
	Frames frames;
	FrameBlocks frameBlocks;
	
	public String animNameString;
	public UUID uuid;
	public ArrayList frameUuid;
	public ArrayList animList;
	public ArrayList frameList;
	public ArrayList blockList;
	
	public DeleteAnimation(){
			animations = new Animations();
			frames = new Frames();
			frameBlocks = new FrameBlocks();
			
			animNameString = "";
			uuid = UUID.randomUUID();
			frameUuid = new ArrayList();
			animList = new ArrayList();
			frameList = new ArrayList();
			blockList = new ArrayList();
	}
	
	
	public String deleteAnimations(String animName){
		animList = new ArrayList((ArrayList) animations.returnL());
		frameList = new ArrayList((ArrayList) frames.returnL());
		blockList = new ArrayList((ArrayList) frameBlocks.returnL());
		ArrayList tempList = new ArrayList();
		int animRemoved = 0;
		int frameCount = 0;
		int blockCount = 0;
		
		for(int a = 0; a < animList.size(); a++){
			tempList = new ArrayList((ArrayList) animList.get(a));
			if(animName.equalsIgnoreCase((String) tempList.get(0))){
				uuid = (UUID) tempList.get(2);
				animations.removeAnimation(uuid);
				animRemoved++;
			}
			
		}
		
		for(int b = 0; b < frameList.size(); b++){
			tempList = new ArrayList((ArrayList) frameList.get(b));
			if(uuid == (UUID) tempList.get(1)){
				frameUuid.add((UUID) tempList.get(2));
				frames.removeFrame((UUID) tempList.get(2));
				frameCount++;
			}
			
		}
		
		for(int c = 0; c < blockList.size(); c++){
			tempList = new  ArrayList((ArrayList) blockList.get(c));
		for(int d = 0; d < frameUuid.size(); d++){
			if((UUID) frameUuid.get(d) == (UUID) tempList.get(2)){
				frameBlocks.removeBlock((UUID) frameUuid.get(d));
				blockCount++;
			}
			
		}
		}
		
		String count = Integer.toString(animRemoved)+ ":" + Integer.toString(frameCount) +
				":" + Integer.toString(blockCount);
		return count;
	}
	
	public String deleteAnimations(UUID animName){
		animList = new ArrayList((ArrayList) animations.returnL());
		frameList = new ArrayList((ArrayList) frames.returnL());
		blockList = new ArrayList((ArrayList) frameBlocks.returnL());
		ArrayList tempList = new ArrayList();
		int animRemoved = 0;
		int frameCount = 0;
		int blockCount = 0;
		
		uuid = animName;
		animations.removeAnimation(uuid);
		animRemoved++;
		
		for(int b = 0; b < frameList.size(); b++){
			tempList = new ArrayList((ArrayList) frameList.get(b));
			if(uuid == (UUID) tempList.get(1)){
				frameUuid.add((UUID) tempList.get(2));
				frames.removeFrame((UUID) tempList.get(2));
				frameCount++;
			}
			
		}
		
		for(int c = 0; c < blockList.size(); c++){
			tempList = new  ArrayList((ArrayList) blockList.get(c));
		for(int d = 0; d < frameUuid.size(); d++){
			if((UUID) frameUuid.get(d) == (UUID) tempList.get(2)){
				frameBlocks.removeBlock((UUID) frameUuid.get(d));
				blockCount++;
			}
			
		}
		}
		
		String count = Integer.toString(animRemoved)+ ":" + Integer.toString(frameCount) +
				":" + Integer.toString(blockCount);
		return count;
	}
}

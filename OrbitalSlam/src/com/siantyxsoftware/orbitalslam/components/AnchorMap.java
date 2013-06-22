package com.siantyxsoftware.orbitalslam.components;

import java.util.ArrayList;

import com.siantyxsoftware.framework.Game;
import com.siantyxsoftware.orbitalslam.Assets;

public class AnchorMap {
	private ArrayList<Entity> entities;
	
	public static int w, h;
	
	private int numPlayers;
	
	private int numAnc;
	
	private int startPosX;
	private double startPercentX;
	private int numAncPerRow;
	
	private int startPosY;
	private double startPercentY;
	private int numAncPerColumn;
	
	//private final String anchorPath = "res/sprites/anchorstar.png";
	private final float stdScale = 0.00002f;
	
	// default map
	public AnchorMap(int w, int h) {
		numAnc = 12;
		
		startPercentX = 0.2;
		startPosX = (int)Math.round(w * startPercentX);
		numAncPerRow = 4;
		
		startPercentY = 0.23;
		startPosY = (int)Math.round(h * startPercentY);
		numAncPerColumn = numAnc/numAncPerRow;
		
		numPlayers = 4;
		
		entities = new ArrayList<Entity>();
		
		AnchorMap.w = w;
		AnchorMap.h = h;
		
		createMap();
	}
	
	// debug map
	public AnchorMap(int numAnc, int startPercentX, int numAncPerRow, int startPercentY, int numPlayers, int w, int h) {
		this.numAnc = numAnc;
		
		this.startPercentX = startPercentX;
		this.startPosX = (int)Math.round(w * startPercentX);
		this.numAncPerRow = numAncPerRow;
		
		this.startPercentY = startPercentY;
		this.startPosY = (int)Math.round(h * startPercentY);
		this.numAncPerColumn = numAnc/numAncPerRow;
		
		this.numPlayers = numPlayers;
		
		entities = new ArrayList<Entity>();
		
		AnchorMap.w = w;
		AnchorMap.h = h;
		
		createMap();
	}
	
	private void createMap() {
		for(int i = 0; i < numAnc; i++) {
			Entity e = new Entity("Anchor " + Integer.toString(i));
			ImageRenderComponent c = new ImageRenderComponent("Anchor " + Integer.toString(i), Assets.anchor);
			e.AddComponent(c);
			// homemade
			e.setScale(0.05f);//stdScale*w); // trololol
			Vector2f pos = new Vector2f(startPosX + (i%numAncPerRow) * (((w-(2*startPosX))/(numAncPerRow-1))), startPosY + (i%numAncPerColumn) * (((h-(2*startPosY))/(numAncPerColumn-1))));
			e.setPosition(pos);
			entities.add(e);
		}
	}
	
	
	public void render(Game game, float delta) {
		for(Entity e : entities) {
			e.render(game, delta);
		}
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public int getNumAncPerRow() {
		return numAncPerRow;
	}
	
	public int getStartPosX() {
		return startPosX;
	}
	
	public int getStartPosY() {
		return startPosY;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
}

package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.ImageRenderComponent;

public class AnchorMap extends GameMap {
	private ArrayList<Entity> entities;
	
	private int startPosX;
	private double startPercentX;
	private int numAncPerRow;
	
	private int startPosY;
	private double startPercentY;
	private int numAncPerColumn;
	
	// default map
	public AnchorMap() throws SlickException {
		
		numAnc = 12;
		
		startPercentX = 0.2;
		startPosX = (int)Math.round(Game.WIDTH * startPercentX);
		numAncPerRow = 4;
		
		startPercentY = 0.23;
		startPosY = (int)Math.round(Game.HEIGHT * startPercentY);
		numAncPerColumn = numAnc/numAncPerRow;
		
		numPlayers = 4;
		
		entities = new ArrayList<Entity>();
		
		createMap();
	}
	
	// debug map
	public AnchorMap(int numAnc, int startPercentX, int numAncPerRow, int startPercentY, int numPlayers) throws SlickException {
		this.numAnc = numAnc;
		
		this.startPercentX = startPercentX;
		this.startPosX = (int)Math.round(Game.WIDTH * startPercentX);
		this.numAncPerRow = numAncPerRow;
		
		this.startPercentY = startPercentY;
		this.startPosY = (int)Math.round(Game.HEIGHT * startPercentY);
		this.numAncPerColumn = numAnc/numAncPerRow;
		
		this.numPlayers = numPlayers;
		
		entities = new ArrayList<Entity>();
		
		createMap();
	}
	
	private void createMap() throws SlickException {
		for(int i = 0; i < numAnc; i++) {
			Entity e = new Entity("Anchor " + Integer.toString(i));
			Image img = new Image(anchorPath);
			ImageRenderComponent c = new ImageRenderComponent("Anchor " + Integer.toString(i), img);
			e.AddComponent(c);
			// homemade
			e.setScale(stdScale*Game.WIDTH); // trololol
			Vector2f pos = new Vector2f(startPosX + (i%numAncPerRow) * (((Game.WIDTH-(2*startPosX))/(numAncPerRow-1))), startPosY + (i%numAncPerColumn) * (((Game.HEIGHT-(2*startPosY))/(numAncPerColumn-1))));
			e.setPosition(pos);
			entities.add(e);
		}
	}
	
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		for(Entity e : entities) {
			e.render(gc, sb, g);
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

	@Override
	public Vector2f getStartPos(int i) {
		return new Vector2f(startPosX*(i+1), startPosY - (startPosY/3));
	}

}

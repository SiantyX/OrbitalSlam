package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import components.ImageRenderComponent;

public class AnchorMap {
	private ArrayList<Entity> entities;
	
	private int numAnc;
	
	private int startPosX;
	private int numAncPerRow;
	
	private int startPosY;
	private int numAncPerColumn;
	
	private final String anchorPath = "res/sprites/anchorstar.png";
	
	// default map
	public AnchorMap() throws SlickException {
		numAnc = 12;
		
		startPosX = 120;
		numAncPerRow = 4;
		
		startPosY = 150;
		numAncPerColumn = numAnc/numAncPerRow;
		
		entities = new ArrayList<Entity>();
		
		createMap();
	}
	
	// debug map
	public AnchorMap(int numAnc, int startPosX, int numAncPerRow, int startPosY) throws SlickException {
		this.numAnc = numAnc;
		
		this.startPosX = startPosX;
		this.numAncPerRow = numAncPerRow;
		
		this.startPosY = startPosY;
		this.numAncPerColumn = numAnc/numAncPerRow;
		
		entities = new ArrayList<Entity>();
		
		createMap();
	}
	
	private void createMap() throws SlickException {
		for(int i = 0; i < numAnc; i++) {
			Entity e = new Entity("Anchor " + Integer.toString(i));
			Image img = new Image(anchorPath);
			ImageRenderComponent c = new ImageRenderComponent("Anchor " + Integer.toString(i), img);
			e.AddComponent(c);
			e.setScale(0.03f);
			entities.add(e);
		}
	}
	
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		for(Entity e : entities) {
			e.render(gc, sb, g);
		}
	}
}

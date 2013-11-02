package game.maps;

import game.Entity;
import game.Game;
import game.Player;
import game.ViewPort;
import game.maps.interactables.Brick;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import components.ImageRenderComponent;

public class ExperimentalMap extends GameMap {
	private int startPosX;
	private double startPercentX;
	private int numAncPerRow;
	
	private int startPosY;
	private double startPercentY;
	private int numAncPerColumn;

	public ExperimentalMap() throws SlickException {
		super();
		numAnc = 12;
		
		
		startPercentX = 0.2;
		startPosX = (int)Math.round(Game.WIDTH * startPercentX);
		numAncPerRow = 4;
		
		startPercentY = 0.23;
		startPosY = (int)Math.round(Game.HEIGHT * startPercentY);
		numAncPerColumn = numAnc/numAncPerRow;
		
		numPlayers = 4;
		
		
	}
	
	public String toString(){
		return "Experimental Map";
	}
	
	public void createMap(ViewPort vp) throws SlickException{
		anchors.clear();
		
		Brick brick = new Brick("Brick");
		brick.setPosition(new Vector2f(0,500));
		brick.setScaleWidth(5);
		interactables.add(brick);
		
		
		for(int i = 0; i < numAnc; i++) {
			Entity e = new Entity("Anchor " + Integer.toString(i));
			Image img = new Image(anchorPath);
			ImageRenderComponent c = new ImageRenderComponent("Anchor " + Integer.toString(i), img);
			e.AddComponent(c);
			// homemade
			e.setScale(stdScale*Game.WIDTH); 
			Vector2f pos = new Vector2f(startPosX + (i%numAncPerRow) * (((Game.WIDTH-(2*startPosX))/(numAncPerRow-1))), startPosY + (i%numAncPerColumn) * (((Game.HEIGHT-(2*startPosY))/(numAncPerColumn-1))));
			pos = vp.toAbsolute(pos);
			e.setPosition(pos);
			anchors.add(e);
		}
			
		Vector2f tmp = new Vector2f(startPosX, startPosY);
		tmp = vp.toAbsolute(tmp);
		this.startPosY = Math.round(tmp.x);
		this.startPosX = Math.round(tmp.y);
		
	}

	@Override
	public Vector2f getStartPos(int i, Entity e, ViewPort vp) {
		return new Vector2f(startPosX*(i+1), startPosY - (startPosY/3));
	}
	

}

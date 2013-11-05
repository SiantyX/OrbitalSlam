package game.maps;

import game.Entity;
import game.Game;
import game.ViewPort;
import game.maps.interactables.Brick;
import game.maps.interactables.powerups.EpicMass;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;


public class ExperimentalMap extends GameMap {
	private int numAncPerRow;
	
	private int numAncPerColumn;

	public ExperimentalMap() throws SlickException {
		super();
		numAnc = 12;
		
		numAncPerRow = 4;
		
		numAncPerColumn = numAnc/numAncPerRow;
		
		
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
		
		EpicMass epicmass = new EpicMass("mass");
		epicmass.setPosition(new Vector2f(300,200));
		interactables.add(epicmass);
		
		
		for(int i = 0; i < numAnc; i++) {
			Vector2f pos = new Vector2f(startPosX + (i%numAncPerRow) * (((Game.WIDTH-(2*startPosX))/(numAncPerRow-1))), startPosY + (i%numAncPerColumn) * (((Game.HEIGHT-(2*startPosY))/(numAncPerColumn-1))));
			addAnchor(i, pos, vp);
		}
			
		Vector2f tmp = new Vector2f(startPosX, startPosY);
		tmp = vp.toAbsolute(tmp);
		this.startPosY = Math.round(tmp.x);
		this.startPosX = Math.round(tmp.y);
		
	}

	@Override
	public Vector2f getStartPos(int i, Entity e, ViewPort vp) {
		return standardStartPosition(i, vp);
	}

	@Override
	public void mapSpecificChange() {
		// TODO Auto-generated method stub
		
	}
	

}

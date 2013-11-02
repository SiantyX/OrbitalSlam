package game.maps;

import java.util.Random;

import game.Entity;
import game.Game;
import game.ViewPort;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import components.ImageRenderComponent;

public class EverChangingMap extends GameMap {
	private Random rand;
	private ViewPort vp;
	
	public EverChangingMap(){
		super();
		numAnc = 150;
		rand = new Random();
	}

	@Override
	public Vector2f getStartPos(int i, Entity e, ViewPort vp) {
		this.vp = vp;
		return standardStartPosition(i, vp);
		
	}

	@Override
	public void createMap(ViewPort vp) throws SlickException {
		Vector2f pos;
		for (int i = 0; i < numAnc; i++) {
			pos = new Vector2f(rand.nextFloat() * Game.WIDTH,
					rand.nextFloat() * Game.HEIGHT);
			addAnchor(i,pos,vp);
			
		}
		
	}

	@Override
	public void mapSpecificChange() throws SlickException {
		anchors.remove(rand.nextInt(numAnc));
		addAnchor(1, new Vector2f(rand.nextFloat() * Game.WIDTH,
					rand.nextFloat() * Game.HEIGHT), vp);
		
	}

	@Override
	public String toString() {
		return "Everchanging Map";
	}

}

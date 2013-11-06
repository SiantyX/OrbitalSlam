package game.maps;

import java.util.Random;

import game.Entity;
import game.Game;
import game.Player;
import game.ViewPort;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;


public class EverChangingMap extends GameMap {
	private Random rand;
	private ViewPort vp;
	
	public EverChangingMap(){
		super();
		numAnc = 150;
		rand = new Random();
	}

	@Override
	public Vector2f getStartPos(Player p, ViewPort vp) {
		this.vp = vp;
		return standardStartPosition(p.getNum(), vp);
		
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
		int removed = rand.nextInt(numAnc);
		anchors.get(removed).clear();
		anchors.remove(removed);
		addAnchor(removed, new Vector2f(rand.nextFloat() * Game.WIDTH,
					rand.nextFloat() * Game.HEIGHT), vp);
		
	}

	@Override
	public String toString() {
		return "Everchanging Map";
	}

}

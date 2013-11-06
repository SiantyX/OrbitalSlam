package game.maps;

import game.Entity;
import game.Game;
import game.Player;
import game.ViewPort;
import game.maps.interactables.Interactable;
import game.maps.interactables.Mine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;


public class RandomFunkyMap extends GameMap {
	// extra deluxe funk edition
	private Random rand;
	private int numMines;



	public RandomFunkyMap() throws SlickException {
		
		super();
		
		rand = new Random();
		numAnc = rand.nextInt(32) + 25;
		numMines = rand.nextInt(10);
	}

	public void createMap(ViewPort vp) throws SlickException {
		anchors.clear();
		interactables.clear();
		
		for (int i = 0; i < numAnc; i++) {
			Vector2f pos = new Vector2f(rand.nextFloat() * Game.WIDTH,
					rand.nextFloat() * Game.HEIGHT);
			
			addAnchor(i, pos,vp);
		}
		
		Mine a;
		Vector2f vector;
		for (int i = 0; i < numMines;i++){
			a = new Mine("Mine " + i);
			vector = new Vector2f(rand.nextFloat() * Game.WIDTH,
					rand.nextFloat() * Game.HEIGHT);
			vector = vp.toAbsolute(vector);
			a.setCenterPosition(vector);
			interactables.add(a);
		}
	}

	private boolean collisionCheck(Interactable e2) {
		for(Interactable i : interactables) {
			if(e2.collisionCircle(i)) {
				return true;
			}
		}
		return false;
	}

	// handlar om att de inte skall spawna i varandra
	public Vector2f getStartPos(Player p, ViewPort vp) {
		int x, y;
		Vector2f vector;
		interactables.remove(p);
		do {
			x = (int) Math.round(rand.nextFloat() * (Game.WIDTH / 2)
					+ Game.WIDTH / 4);
			y = (int) Math.round(rand.nextFloat() * (Game.HEIGHT / 2));
			vector = new Vector2f(x, y);
			vector = vp.toAbsolute(vector);
			p.setCenterPosition(vector);
		} while(collisionCheck(p));
		
		interactables.add(p);

		return vector;

	}

	@Override
	public String toString() {
		return "Random Map";
	}

	@Override
	public void mapSpecificChange() {
		// TODO Auto-generated method stub
		
	}
}

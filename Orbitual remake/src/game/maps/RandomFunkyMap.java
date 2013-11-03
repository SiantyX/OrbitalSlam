package game.maps;

import game.Entity;
import game.Game;
import game.Player;
import game.ViewPort;
import game.maps.interactables.Mine;
import game.maps.interactables.powerups.EpicMass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import components.ImageRenderComponent;

public class RandomFunkyMap extends GameMap {
	// extra deluxe funk edition
	private Random rand;
	private Map<Integer, Vector2f> startPositions;
	private Map<Integer, Entity> mapPlayers;
	protected int mapnr = 0;
	private int numMines;



	public RandomFunkyMap() throws SlickException {
		
		super();
		startPositions = new HashMap<Integer, Vector2f>();
		mapPlayers = new HashMap<Integer, Entity>();
		
		rand = new Random();
		numAnc = rand.nextInt(32) + 25;
		numMines = rand.nextInt(10);
	}

	public void createMap(ViewPort vp) throws SlickException {
		anchors.clear();
		startPositions.clear();
		mapPlayers.clear();
		interactables.clear();
		
		for (int i = 0; i < numAnc; i++) {
			Vector2f pos = new Vector2f(rand.nextFloat() * Game.WIDTH,
					rand.nextFloat() * Game.HEIGHT);
			
			addAnchor(i, pos,vp);
		}
		
		Mine a;
		Vector2f vector;
		for (int i = 0; i < numMines;i++){
			a = new Mine();
			vector = new Vector2f(rand.nextFloat() * Game.WIDTH,
					rand.nextFloat() * Game.HEIGHT);
			vector = vp.toAbsolute(vector);
			a.setCenterPosition(vector);
			mapPlayers.put(100+i, a);
			interactables.add(a);
		}
	}

	private boolean collisionCheck(Entity e2) {
		for(Entry<Integer, Entity> e : mapPlayers.entrySet()) {
			if(e2.collisionCircle(e.getValue())) {
				return true;
			}
		}
		return false;
	}

	// handlar om att de inte skall spawna i varandra
	public Vector2f getStartPos(int i, Entity e, ViewPort vp) {
		int x, y;
		Vector2f vector;
		do {
			x = (int) Math.round(rand.nextFloat() * (Game.WIDTH / 2)
					+ Game.WIDTH / 4);
			y = (int) Math.round(rand.nextFloat() * (Game.HEIGHT / 2));
			vector = new Vector2f(x, y);
			vector = vp.toAbsolute(vector);
			e.setCenterPosition(vector);
		} while(collisionCheck(e));
		
		startPositions.put(i, vector);
		mapPlayers.put(i, e);

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

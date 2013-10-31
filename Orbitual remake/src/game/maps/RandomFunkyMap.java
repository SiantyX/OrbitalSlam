package game.maps;

import game.Entity;
import game.Game;
import game.Player;

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


	public RandomFunkyMap() throws SlickException {
		
		super();
		startPositions = new HashMap<Integer, Vector2f>();
		mapPlayers = new HashMap<Integer, Entity>();
		
		rand = new Random();
		numAnc = rand.nextInt(24) + 20;

		createMap();
		Mine a = new Mine();
		Vector2f vector = new Vector2f(1000,500);
		a.setCenterPosition(vector);
		mapPlayers.put(100,a);
		interactibles.add(a);
		
	}

	private void createMap() throws SlickException {
		for (int i = 0; i < numAnc; i++) {
			Entity e = new Entity("Anchor " + Integer.toString(i));
			Image img = new Image(anchorPath);
			ImageRenderComponent c = new ImageRenderComponent("Anchor "
					+ Integer.toString(i), img);
			e.AddComponent(c);
			// homemade
			e.setScale(stdScale * Game.WIDTH);
			Vector2f pos = new Vector2f(rand.nextFloat() * Game.WIDTH,
					rand.nextFloat() * Game.HEIGHT);
			e.setPosition(pos);
			anchors.add(e);
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
	public Vector2f getStartPos(int i, Entity e) {
		int x, y;
		Vector2f vector = null;
		do {
			x = (int) Math.round(rand.nextFloat() * (Game.WIDTH / 2)
					+ Game.WIDTH / 4);
			y = (int) Math.round(rand.nextFloat() * (Game.HEIGHT / 2));
			vector = new Vector2f(x, y);
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
	public Vector2f getStartPos(int i, Player p) {
		// TODO Auto-generated method stub
		return getStartPos(i,p.getEntity());
	}

}

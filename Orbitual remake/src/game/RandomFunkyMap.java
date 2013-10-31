package game;

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
	private Map<Integer, Player> mapPlayers;
	protected int mapnr = 0;


	public RandomFunkyMap() throws SlickException {
		
		super();
		startPositions = new HashMap<Integer, Vector2f>();
		mapPlayers = new HashMap<Integer, Player>();
		
		rand = new Random();
		numAnc = rand.nextInt(24) + 12;

		createMap();
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
			entities.add(e);
		}
	}

	private boolean collisionCheck(Player p) {
		for(Entry<Integer, Player> e : mapPlayers.entrySet()) {
			if(p.getEntity().collisionCircle(e.getValue().getEntity())) {
				return true;
			}
		}
		return false;
	}

	// handlar om att de inte skall spawna i varandra
	public Vector2f getStartPos(int i, Player p) {
		int x, y;
		Vector2f vector = null;
		do {
			x = (int) Math.round(rand.nextFloat() * (Game.WIDTH / 3)
					+ Game.WIDTH / 3);
			y = (int) Math.round(rand.nextFloat() * (Game.HEIGHT / 3)
					+ Game.HEIGHT / 3);
			vector = new Vector2f(x, y);
			p.getEntity().setCenterPosition(vector);
		} while(collisionCheck(p));
		
		startPositions.put(i, vector);
		mapPlayers.put(i, p);
		/*mapnr ++;
		mapnr = mapnr % Game.MAX_PLAYERS + 1;
		if (mapnr == 0) {
			startPositions.clear();
			mapPlayers.clear();
		}*/

		return vector;

	}

	@Override
	public String toString() {
		return "Random Map";
	}

}

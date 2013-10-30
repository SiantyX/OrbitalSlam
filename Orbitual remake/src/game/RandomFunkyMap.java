package game;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import components.ImageRenderComponent;

public class RandomFunkyMap extends GameMap {
	// extra deluxe funk edition
	private Random rand;
	private ArrayList<Vector2f> startpositions;
	ArrayList<Vector2f> list = new ArrayList<Vector2f>();
	int mapnr = 0;


	public RandomFunkyMap() throws SlickException {
		
		super();
		startpositions = new ArrayList<Vector2f>();

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

	private boolean collisioncheck(Vector2f vector) {
		for (Vector2f v : startpositions) {
			if (v.distance(vector) < 100) {
				return false;

			}

		}
		return true;
	}

	// handlar om att de inte skall spawna i varandra
	public Vector2f getStartPos(int i) {
		
		boolean b = false;
		int x = 0;
		int y = 0;
		Vector2f vector = null;
		while (b != true) {
			x = (int) Math.round(rand.nextFloat() * (Game.WIDTH / 3)
					+ Game.WIDTH / 3);
			y = (int) Math.round(rand.nextFloat() * (Game.HEIGHT / 3)
					+ Game.HEIGHT / 3);
			vector = new Vector2f(x, y);
			b = collisioncheck(vector);
		}
		startpositions.add(vector);
		mapnr ++;
		mapnr = mapnr % 10;
		if (mapnr == 0)
			startpositions.clear();

		return vector;

	}

}

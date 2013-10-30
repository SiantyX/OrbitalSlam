package game;
 
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import components.ImageRenderComponent;

public class RandomFunkyMap extends GameMap {
	// extra deluxe funk edition
	Random rand;
	ArrayList<Vector2f> list = new ArrayList<Vector2f>();

	public RandomFunkyMap() throws SlickException {
		numPlayers = 4;
		
		rand = new Random();	
		numAnc = rand.nextInt(24) + 12;
		entities = new ArrayList<Entity>();
			
		
		
		
		createMap();
	}
	private void createMap() throws SlickException {
		for(int i = 0; i < numAnc; i++) {
			Entity e = new Entity("Anchor " + Integer.toString(i));
			Image img = new Image(anchorPath);
			ImageRenderComponent c = new ImageRenderComponent("Anchor " + Integer.toString(i), img);
			e.AddComponent(c);
			// homemade
			e.setScale(stdScale*Game.WIDTH);
			Vector2f pos = new Vector2f(rand.nextFloat()*Game.WIDTH, (float)rand.nextFloat()*(Game.HEIGHT));
			e.setPosition(pos);
			entities.add(e);
		}
	}
	


	public Vector2f getStartPos(int i){
		int x = rand.nextInt(Game.WIDTH - (Game.WIDTH/3)) + (Game.WIDTH/3);
		int y = rand.nextInt(Game.HEIGHT - (Game.HEIGHT/ 3)) + (Game.HEIGHT/ 3);
		return new Vector2f(x, y);
		
	}
}

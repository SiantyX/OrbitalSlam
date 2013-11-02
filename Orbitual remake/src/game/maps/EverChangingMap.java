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
	
	public EverChangingMap(){
		super();
		numAnc = 50;
		rand = new Random();
	}

	@Override
	public Vector2f getStartPos(int i, Entity e, ViewPort vp) {
		return standardStartPosition(i, vp);
	}

	@Override
	public void createMap(ViewPort vp) throws SlickException {
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
			pos = vp.toAbsolute(pos);
			e.setCenterPosition(pos);
			anchors.add(e);
		}
		
	}

	@Override
	public void mapSpecificChange() {
		anchors.remove(rand.nextInt(numAnc));
		
	}

	@Override
	public String toString() {
		return "Everchanging Map";
	}

}

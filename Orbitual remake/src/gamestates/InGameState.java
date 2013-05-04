package gamestates;

import game.AnchorMap;
import game.Entity;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import components.*;

public class InGameState extends BasicGameState {

	public static final int ID = 1;
	private ArrayList<Entity> entities;
	private AnchorMap map;

	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		entities = new ArrayList<Entity>();
		map = new AnchorMap();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		map.render(gc, sb, g);
		
		if(entities.isEmpty()) return;
		for (Entity e : entities) {
			e.render(gc, sb, g);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
	}
	
	private boolean collisionCircle(Entity e1, Entity e2) {
		float radii = e1.getRadius() + e2.getRadius();
		float dx = e2.getPosition().x + e2.getRadius() - e1.getPosition().x - e1.getRadius();
		float dy = e2.getPosition().y + e2.getRadius() - e1.getPosition().y - e1.getRadius();
		if( dx * dx + dy * dy < radii * radii){
			return true;
		}
		return false;
	}

	@Override
	public int getID() {
		return ID;
	}
}

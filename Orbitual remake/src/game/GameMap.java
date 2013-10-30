package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class GameMap {
	protected ArrayList<Entity> entities;
	protected int numAnc;

	protected int numPlayers;

	protected final String anchorPath = "res/sprites/anchorstar.png";
	protected final float stdScale = 0.00002f;

	public GameMap() {
		entities = new ArrayList<Entity>();
		numPlayers = 4;
	}

	public abstract Vector2f getStartPos(int i);

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		for (Entity e : entities) {
			e.render(gc, sb, g);
		}
	}

	public ArrayList<Entity> getEntities() {
		return entities;

	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public int getScorePlacementY() {
		return (Game.HEIGHT / 18);
	}

	public int getScorePlacementX(int i) {
		return (Game.WIDTH / numPlayers) * (i) + Game.HEIGHT / (numPlayers * 2);
	}

}

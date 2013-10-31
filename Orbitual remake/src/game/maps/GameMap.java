package game.maps;

import game.Entity;
import game.Game;
import game.Player;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class GameMap {
	protected ArrayList<Entity> anchors;
	protected ArrayList<Interactible> interactibles;
	protected int numAnc;

	protected int numPlayers;

	protected final String anchorPath = "res/sprites/anchorstar.png";
	protected final float stdScale = 0.00002f;
	protected final static String minePath = "res/sprites/mine.png";

	public GameMap() {
		anchors = new ArrayList<Entity>();
		interactibles = new ArrayList<Interactible>();
		numPlayers = 4;
	}

	public abstract Vector2f getStartPos(int i, Player p);

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		for (Entity e : anchors) {
			e.render(gc, sb, g);
		}
		for (Interactible i : interactibles)
			i.render(gc, sb, g);
	}

	public ArrayList<Entity> getAnchors() {
		return anchors;

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
	public void update(GameContainer gc, StateBasedGame sb, int delta){
		for (Interactible i : interactibles){
			i.collisionCheck(sb);
		}
	}
	public abstract String toString();

}

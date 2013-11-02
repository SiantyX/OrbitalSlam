package game.maps;

import game.Entity;
import game.Game;
import game.Player;
import game.ViewPort;
import game.maps.interactables.*;
import gamestates.BeforeGameState;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class GameMap {
	protected ArrayList<Entity> anchors;
	protected ArrayList<Interactable> interactables;
	protected int numAnc;

	protected int numPlayers;

	protected final String anchorPath = "res/sprites/interactables/anchorstar.png";
	protected final float stdScale = 0.00002f;
	protected float scale;

	public GameMap() {
		anchors = new ArrayList<Entity>();
		interactables = new ArrayList<Interactable>();
		numPlayers = 4;
	}
	

	public abstract Vector2f getStartPos(int i, Entity e, ViewPort vp);

	public void render(GameContainer gc, StateBasedGame sb, Graphics g, ViewPort vp) {
		for (Entity e : anchors) {
			e.render(gc, sb, g, vp);
		}
		for (Interactable i : interactables)
			i.render(gc, sb, g, vp);
	}

	public ArrayList<Entity> getAnchors() {
		return anchors;

	}
	
	public abstract void createMap(ViewPort vp) throws SlickException;
	
	public void reset(){
		for (Interactable i : interactables)
			i.reset();
		
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public int getScorePlacementY() {
		return (Game.HEIGHT / 18);
	}
	protected Vector2f standardStartPosition(int i){
		
		double startPercentX = 0.2;
		double startPercentY = 0.23;
		
		int startPosX = (int)Math.round(Game.WIDTH * startPercentX );
		int startPosY = (int)Math.round(Game.HEIGHT * startPercentY);
		
		return new Vector2f(startPosX*(i+1), startPosY - (startPosY/3));
	}

	public int getScorePlacementX(int i) {
		return (Game.WIDTH / numPlayers) * (i) + Game.HEIGHT / (numPlayers * 2);
	}
	public void update(GameContainer gc, StateBasedGame sb, int delta){
		for (Interactable i : interactables){
			i.collisionCheck(sb);
			i.update(gc, sb, delta);
		}
		mapSpecificChange();
	}
	public abstract void mapSpecificChange();


	public abstract String toString();

}

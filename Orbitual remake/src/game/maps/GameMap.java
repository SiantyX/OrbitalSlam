package game.maps;

import game.Entity;
import game.Game;
import game.ViewPort;
import game.maps.interactables.*;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.ImageRenderComponent;

public abstract class GameMap {
	protected ArrayList<Entity> anchors;
	protected ArrayList<Interactable> interactables;
	protected int numAnc;

	protected int numPlayers;
	protected Rectangle bounds;

	protected final String anchorPath = "res/sprites/interactables/anchorstar.png";
	protected final float stdScale = 0.00002f;
	protected float scale;
	
	protected double startPercentX = 0.2;
	protected double startPercentY = 0.23;
	
	protected int startPosX;
	protected int startPosY;
	
	

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
	
	public ArrayList<Interactable> getInteractables() {
		return interactables;
	}
	
	public void setBoundsAndCreateMap(ViewPort vp) throws SlickException {
		bounds = new Rectangle(vp.getPosX(), vp.getPosY(), vp.getResX(), vp.getResY());
		createMap(vp);
	}
	
	public abstract void createMap(ViewPort vp) throws SlickException;
	
	public void reset(){
		for (Interactable i : interactables)
			i.reset();
		
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setBounds(Rectangle rect) {
		bounds = rect;
	}
	

	protected void addAnchor(int i, Vector2f pos, ViewPort vp) throws SlickException{
		Entity e = new Entity("Anchor " + Integer.toString(i));
		Image img = new Image(anchorPath);
		ImageRenderComponent c = new ImageRenderComponent("Anchor " + Integer.toString(i), img);
		e.addComponent(c);
		// homemade
		e.setScale(stdScale*Game.WIDTH); 
		pos = vp.toAbsolute(pos);
		e.setPosition(pos);
		anchors.add(e);
		
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public int getScorePlacementY() {
		return (Game.HEIGHT / 18);
	}
	protected Vector2f standardStartPosition(int i, ViewPort vp){
		
		startPosX = (int)Math.round(Game.WIDTH * startPercentX );
		startPosY = (int)Math.round(Game.HEIGHT * startPercentY);
		Vector2f vector = new Vector2f(startPosX*(i+1), startPosY - (startPosY/3));
		vp.toAbsolute(vector);
		
		return vector;
	}

	public int getScorePlacementX(int i) {
		return (Game.WIDTH / numPlayers) * (i) + Game.HEIGHT / (numPlayers * 2);
	}
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException{
		for (Interactable i : interactables){
			//i.collisionCheck(sb);
			i.update(gc, sb, delta);
		}
		mapSpecificChange();
	}
	public abstract void mapSpecificChange() throws SlickException;


	public abstract String toString();

}

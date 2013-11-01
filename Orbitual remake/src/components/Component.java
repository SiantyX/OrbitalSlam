package components;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import game.Entity;
import game.ViewPort;

public abstract class Component {

	protected String id;
	protected Entity owner;
	protected Image currentImage;
	protected float scale;
	protected float rotation;
	
	public Component(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public void setOwnerEntity(Entity owner) {
		this.owner = owner;
	}

	public abstract void update(GameContainer gc, StateBasedGame sb, int delta);
	public abstract void render(GameContainer gc, StateBasedGame sb, Graphics gr);
	public abstract void render(GameContainer gc, StateBasedGame sb, Graphics gr, ViewPort vp);
	
	public float getRadius(){
		return (currentImage.getWidth()/2)*scale;
	}
	
	public Image getImage() {
		return currentImage;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public void setRotation(float rotation) {
		currentImage.rotate(getRotation() - rotation);
		this.rotation = rotation;
	}

	public float getRotation() {
		return rotation;
	}
	
	public float getWidth() {
		return currentImage.getWidth()*scale;
	}
	
	public float getHeight() {
		return currentImage.getHeight()*scale;
	}
}

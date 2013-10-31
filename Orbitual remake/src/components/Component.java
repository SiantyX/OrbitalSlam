package components;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import game.Entity;

public abstract class Component {

	protected String id;
	protected Entity owner;
	protected Image currentImage;
	
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
	
	public abstract Image getImage();
	
	public abstract void setScale(float scale);
	public abstract float getScale();
	public abstract void setRotation(float rotation);
	public abstract float getRotation();
	public abstract float getRadius();
}

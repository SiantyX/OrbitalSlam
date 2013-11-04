package components;

import java.awt.Toolkit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import game.Entity;
import game.ViewPort;

public abstract class Component {

	protected String id;
	protected Entity owner;
	protected Image currentImage;
	protected float scale;
	protected float rotation;
	protected Toolkit toolkit;
	protected float scaleWidth, scaleHeight;
	
	public Component(String id) {
		this.id = id;
		scaleWidth = scaleHeight = 1;
	}

	public String getID() {
		return id;
	}

	public void setOwnerEntity(Entity owner) {
		this.owner = owner;
	}

	public abstract void update(GameContainer gc, StateBasedGame sb, int delta);
	public abstract void update(GameContainer gc, StateBasedGame sb, int delta, ViewPort vp);
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
	
	public void setScaleWidth(float scale) {
		this.scaleWidth = scale;
	}
	
	public void setScaleHeight(float scale) {
		this.scaleHeight = scale;
	}
	
	public float getScaleWidth() {
		return scaleWidth;
	}
	
	public float getScaleHeight() {
		return scaleHeight;
	}

	public void setRotation(float rotation) {
		//currentImage.rotate(getRotation() - rotation);
		this.rotation = rotation;
	}

	public float getRotation() {
		return rotation;
	}
	
	public float getWidth() {
		return currentImage.getWidth()*scale*scaleWidth;
	}
	
	public float getHeight() {
		return currentImage.getHeight()*scale*scaleHeight;
	}
	
	public Vector2f getMidPoint(float x, float y, float width, float height, float rotation) {
	    double angle_rad = rotation * Math.PI / 180;
	    double cosa = Math.cos(angle_rad);
	    double sina = Math.sin(angle_rad);
	    double wp = width/2;
	    double hp = height/2;
	    return new Vector2f((float) (x + wp * cosa - hp * sina), (float) (y + wp * sina + hp * cosa));
	}
}

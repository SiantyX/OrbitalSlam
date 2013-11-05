package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.Component;

public class Entity extends Node {
	protected float speed;
	protected float dx;
	protected float dy;

	protected static final float TIME_CONST = 16.6f;

	private String id;
	private float rotation;
	private ArrayList<Component> components = null;

	public Entity(String id) {
		super(0, 0);
		this.id = id;
		rotation = 0;
		dx = dy = speed = 0;

		components = new ArrayList<Component>();
	}

	public void addComponent(Component component) {
		component.setOwnerEntity(this);
		components.add(component);
	}

	public Component getComponent(String id) {
		for (Component comp : components) {
			if (comp.getID().equalsIgnoreCase(id))
				return comp;
		}

		return null;
	}

	public boolean removeComponent(String id) {
		return components.remove(getComponent(id));
	}

	public Vector2f getPosition() {
		return pos;
	}

	public Vector2f getCenterPosition() {
		return new Vector2f(getPosition().x + getRadius(), getPosition().y
				+ getRadius());
	}

	public Vector2f getCenterPositionRectangle(){
		return new Vector2f(getPosition().x + getWidth()/2,getPosition().y + getHeight()/2 );
	}

	public String getId() {
		return id;
	}

	public void setPosition(Vector2f position) {
		moveTo(position.x, position.y);
	}

	public void setCenterPosition(Vector2f vector2f) {
		moveTo(vector2f.x - getRadius(), vector2f.y - getRadius());
	}

	public float getAllRotation() {
		return rotation;
	}

	public float getRotation() {
		if (components.isEmpty())
			return 1;

		return components.get(0).getRotation();
	}

	public float getRotation(String id) {
		if (components.isEmpty())
			return 1;

		return getComponent(id).getRotation();
	}

	public void setAllRotation(float rotate) {
		this.rotation = rotate;
	}

	public void setRotation(float rotate) {
		if (components.isEmpty())
			return;

		components.get(0).setRotation(rotate);
	}

	public void setRotation(float rotate, String id) {
		if (components.isEmpty())
			return;

		getComponent(id).setRotation(rotate);
	}

	public float getScale() {
		if (components.isEmpty())
			return 1;

		return components.get(0).getScale();
	}

	public float getScale(String id) {
		if (components.isEmpty())
			return 1;

		return getComponent(id).getScale();
	}

	public void setScale(float scale) {
		if (components.isEmpty())
			return;

		components.get(0).setScale(scale);
	}

	public void setScale(float scale, String id) {
		if (components.isEmpty())
			return;

		getComponent(id).setScale(scale);
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		if(dx != 0 || dy != 0) {
			speed = (float) (Math.hypot(dx, dy) * delta / TIME_CONST);
			translate(dx * delta / TIME_CONST, dy * delta / TIME_CONST);
		}
		else {
			speed = 0;
		}

		for (Component component : components) {
			component.update(gc, sb, delta);
		}
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta, ViewPort vp) {
		if(dx != 0 || dy != 0) {
			speed = (float) (Math.hypot(dx, dy) * delta / TIME_CONST);
			translate(dx * delta / TIME_CONST, dy * delta / TIME_CONST);
		}
		else {
			speed = 0;
		}

		for (Component component : components) {
			component.update(gc, sb, delta, vp);
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		for (Component component : components) {
			component.render(gc, sb, g);
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g, ViewPort vp) {
		for (Component component : components) {
			component.render(gc, sb, g, vp);
		}
	}

	public float getRadius() {
		if (components.isEmpty())
			return 0;

		return components.get(0).getRadius();
	}

	public float getRadius(String id) {
		if (components.isEmpty())
			return 0;

		return getComponent(id).getRadius();
	}

	public void setRadius(float radius) {
		if (components.isEmpty())
			return;

		setScale(radius / getRadius());
	}

	public void setRadius(float radius, String id) {
		if (components.isEmpty())
			return;

		setScale(radius / getRadius(), id);
	}


	public float getWidth() {
		if(components.isEmpty())
			return -1;

		return components.get(0).getWidth();
	}

	public float getWidth(String id) {
		if(components.isEmpty())
			return -1;

		return getComponent(id).getWidth();
	}

	public float getHeight() {
		if(components.isEmpty())
			return -1;

		return components.get(0).getHeight();
	}

	public float getHeight(String id) {
		if(components.isEmpty())
			return -1;

		return getComponent(id).getHeight();
	}

	public boolean collisionCircle(Entity e1) {
		float radii = e1.getRadius() + this.getRadius();
		float dx = this.getCenterPosition().x - e1.getCenterPosition().x;
		float dy = e1.getCenterPosition().y - this.getCenterPosition().y;
		if (dx * dx + dy * dy <= radii * radii) {
			return true;
		}
		return false;
	}

	// 
	public boolean collisionSquare(Vector2f e1) {
		return e1.x > pos.x && e1.x < pos.x + getWidth() && e1.y > pos.y
				&& e1.y < pos.y + getHeight();
	}

	// square in square inte klar
	public boolean collisionSquare(Entity e1) {
		return (Math.abs((e1.getPosition().getX() - pos.x)) < getRadius()
				&& Math.abs(e1.getPosition().getY() - pos.y) < getRadius());
	}

	public boolean collisionRectangle(Entity e1){
		float distanceX = Math.abs(e1.getCenterPositionRectangle().x - getCenterPositionRectangle().x);
		float distanceY = Math.abs(e1.getCenterPositionRectangle().y - getCenterPositionRectangle().y);
		float sizeOfHitboxX = e1.getRadius() + getWidth()/2;
		float sizeOfHitboxY = e1.getRadius() + getHeight()/2;
		return (distanceX < sizeOfHitboxX && distanceY < sizeOfHitboxY);
	}

	public void clear() {
		components.clear();
	}

	public void changeImage(Component component) {
		clear();
		addComponent(component);
	}

	public void changeImageOnNotEqual(String id, Component component) {
		if (getComponent(id) == null) {
			changeImage(component);
		}
	}

	// Dx,Dy
	public float getDx() {
		return dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public void increaseDy(float increment) {
		dy += increment;
	}

	public void increaseDx(float increment) {
		dx += increment;
	}
}

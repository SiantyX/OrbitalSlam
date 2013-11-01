package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.Component;
import components.AnimationRenderComponent;
import components.ImageRenderComponent;

public class Entity extends Node {

	private String id;

	private ArrayList<Component> components = null;

	public Entity(String id) {
		super(0, 0);
		this.id = id;

		components = new ArrayList<Component>();
	}

	public void AddComponent(Component component) {
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

	public String getId() {
		return id;
	}

	public void setPosition(Vector2f position) {
		moveTo(position.x, position.y);
	}

	public void setCenterPosition(Vector2f vector2f) {
		moveTo(vector2f.x - getRadius(), vector2f.y - getRadius());
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
		for (Component component : components) {
			component.update(gc, sb, delta);
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		for (Component component : components) {
			component.render(gc, sb, g);
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

	public void setWidth(float width) {
		if (components.isEmpty())
			return;

		setScale(width / getWidth());
	}

	public void setHeight(float width) {
		if (components.isEmpty())
			return;

		setScale(width / getWidth());

	}

	public float getWidth() {
		return components.get(0).getWidth();
	}

	public float getWidth(String id) {
		return getComponent(id).getWidth();
	}

	public float getHeight() {
		return components.get(0).getHeight();
	}

	public float getHeight(String id) {
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

	public boolean collisionSquare(Vector2f e1) {
		return e1.x > pos.x && e1.x < pos.x + getWidth() && e1.y > pos.y
				&& e1.y < pos.y + getHeight();
	}

	public boolean collisionSquare(Entity e1) {
		return (Math.abs((e1.getPosition().getX() - pos.x)) < getRadius()
				&& Math.abs(e1.getPosition().getY() - pos.y) < getRadius());
	}

	public void clear() {
		components.clear();
	}

	public void changeImage(Component component) {
		clear();
		AddComponent(component);
	}

	public void changeImageOnNotEqual(String id, Component component) {
		if (getComponent(id) == null) {
			changeImage(component);
		}
	}
}

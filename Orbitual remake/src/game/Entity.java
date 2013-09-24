package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.Component;
import components.RenderComponent;
import components.ImageRenderComponent;

public class Entity extends Node {

	private String id;

	private float scale;
	private float rotation;
	private float radius;
	//
	private ArrayList<RenderComponent> renderComponent = null;
	private ArrayList<Component> components = null;

	public Entity(String id) {
		super(0, 0);
		this.id = id;

		components = new ArrayList<Component>();
		renderComponent = new ArrayList<RenderComponent>();

		scale = 1;
		rotation = 0;
		
		radius = 0;
		
	}

	public void AddComponent(Component component) {
		if (RenderComponent.class.isInstance(component)){
			renderComponent.add((RenderComponent) component);
		}

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

	public Vector2f getPosition() {
		return pos;
	}
	
	public Vector2f getCenterPosition() {
		return new Vector2f(getPosition().x + getRadius(), getPosition().y + getRadius());
	}

	public float getScale() {
		return scale;
	}

	public float getRotation() {
		return rotation;
	}

	public String getId() {
		return id;
	}

	public void setPosition(Vector2f position) {
		moveTo(position.x, position.y);
	}
	
	public void setCenterPosition(Vector2f position) {
		moveTo(position.x - getRadius(), position.y - getRadius());
	}

	public void setRotation(float rotate) {
		rotation = rotate;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		for (Component component : components) {
			component.update(gc, sb, delta);
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		for(RenderComponent re : renderComponent){
			re.render(gc, sb, g);
		}
	}
	
	public float getRadius(){
		if(radius == 0) {
			ImageRenderComponent irc = (ImageRenderComponent) getComponent(id);
			radius = irc.getRadius();
		}
		
		return radius*scale;
	}
	
	public void setRadius(float radius){
		this.radius = radius;
	}
	
	public ArrayList<Component> getComponents() {
		return components;
	}
}

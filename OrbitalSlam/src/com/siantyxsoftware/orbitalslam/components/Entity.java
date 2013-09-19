package com.siantyxsoftware.orbitalslam.components;

import java.util.ArrayList;

import com.siantyxsoftware.framework.Game;

public class Entity {

	private String id;

	private Vector2f position;
	private float scale;
	private float rotation;
	private float radius;
	//
	private ArrayList<RenderComponent> renderComponent = null;
	private ArrayList<Component> components = null;

	public Entity(String id) {
		this.id = id;

		components = new ArrayList<Component>();
		renderComponent = new ArrayList<RenderComponent>();

		position = new Vector2f(0, 0);
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
		return position;
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
		this.position = position;
	}
	
	public void setCenterPosition(Vector2f position) {
		Vector2f v = new Vector2f(position.x - getRadius(), position.y - getRadius());
		this.position = v;
	}
	
	public void translate(float dx, float dy) {
		this.position = new Vector2f(position.x + dx, position.y + dy);
	}

	public void setRotation(float rotate) {
		rotation = rotate;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void update(Game game, float delta) {
		for (Component component : components) {
			component.update(game, delta);
		}
	}

	public void render(Game game, float delta) {
		for(RenderComponent re : renderComponent){
			re.render(game, delta);
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

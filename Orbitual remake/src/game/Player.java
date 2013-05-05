package game;

import java.util.ArrayList;

import gamestates.MenuState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import components.ImageRenderComponent;

public class Player {
	private boolean dead;
	private Entity entity;
	private final String[] playerImg = new String[]{"res/sprites/smiley1.png", "res/sprites/smiley2.png", "res/sprites/smiley3.png", "res/sprites/smiley4.png", "res/sprites/smiley5.png", "res/sprites/smiley6.png", "res/sprites/smiley7.png", "res/sprites/smiley8.png"};
	private final float stdScale = 0.0005208f;
	private int num;
	
	private double dx;
	private double dy;
	
	private double speed;
	
	// hook variables
	private boolean hooked;
	private Entity hookedTo;
	
	private double wSpeed;
	private double degrees;
	private double hookLength;
	
	private boolean clockWise;
	// -----------------------
	
	public static ArrayList<Entity> anchorList;
	
	private final float gravity = 0.01f;
	
	public Player(int num, Vector2f startPos) throws SlickException {
		dead = false;
		entity = new Entity("Player");
		entity.AddComponent(new ImageRenderComponent("Player", new Image(playerImg[num])));
		entity.setPosition(startPos);
		entity.setScale(stdScale*Game.WIDTH);
		
		dx = 0;
		dy = 0;
		speed = 0;
		
		// hook variables
		hookedTo = null;
		hooked = false;
		
		wSpeed = 0;
		degrees = 0;
		hookLength = 0;
		
		clockWise = false;
		// -----------------
		
		this.num = num;
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_S)) {
			hooked = !hooked;
			if(hooked) {
				hook();
			}
		}
		
		if(!hooked)
			dy += gravity*delta;
		
		else {
			// SNURRA
			double cosOldV = Math.cos(degrees*Math.PI/180);
			double sinOldV = Math.sin(degrees*Math.PI/180);
			
			if(clockWise) {
				double cosNewV = Math.cos((degrees-wSpeed)*Math.PI/180);
				double sinNewV = Math.sin((degrees-wSpeed)*Math.PI/180);
				
				dx = hookLength * cosNewV - hookLength * cosOldV;
				dy = hookLength * sinOldV - hookLength * sinNewV;
				degrees -= wSpeed;
			}
			else {
				double cosNewV = Math.cos((degrees+wSpeed)*Math.PI/180);
				double sinNewV = Math.sin((degrees+wSpeed)*Math.PI/180);
				
				dx = hookLength * cosNewV - hookLength * cosOldV;
				dy = hookLength * sinOldV - hookLength * sinNewV;
				degrees += wSpeed;
			}
			
		}
		
		entity.setPosition(new Vector2f( entity.getPosition().x + (float)dx, entity.getPosition().y + (float)dy));
	}
	
	private void hook() {
		// get closest anchor
		hookLength = 10000;
		for(Entity e : anchorList) {
			double eHypot = Math.hypot(e.getPosition().x - entity.getPosition().x, e.getPosition().y - entity.getPosition().y);
			if(eHypot < hookLength) {
				hookLength = eHypot;
				hookedTo = e;
			}
		}
		
		speed = Math.hypot(dx, dy);
		wSpeed = speed / hookLength * 180/Math.PI;
		degrees = Math.atan2(hookedTo.getPosition().y - entity.getPosition().y + entity.getRadius(), 
				hookedTo.getPosition().x - entity.getPosition().x + entity.getRadius()) * 180/Math.PI;
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		if(hooked) {
			g.drawLine(entity.getCenterPosition().x, entity.getCenterPosition().y, hookedTo.getCenterPosition().x, hookedTo.getCenterPosition().y);
		}
		entity.render(gc, sb, g);
	}
}

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
	
	private double centriAcc;
	private final double MAXSPINSPEED = 16;
	private final double ACC_CONST = 300;
	// -----------------------
	
	public static ArrayList<Entity> anchorList;
	
	private final float gravity = 0.015f;
	
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
		
		centriAcc = 0;
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
			if(clockWise) {
				degrees -= wSpeed;
			}
			else {
				degrees += wSpeed;
			}
			dx = hookedTo.getCenterPosition().x + Math.cos(degrees*Math.PI/180) * hookLength - entity.getCenterPosition().x;
			dy = hookedTo.getCenterPosition().y - Math.sin(degrees*Math.PI/180) * hookLength - entity.getCenterPosition().y;
			
			if(wSpeed >= MAXSPINSPEED) {
				wSpeed = MAXSPINSPEED;
			}
			else {
				wSpeed += centriAcc;
			}
		}
		
		speed = Math.hypot(dx, dy);
		entity.setCenterPosition(new Vector2f( entity.getCenterPosition().x + (float)dx, entity.getCenterPosition().y + (float)dy));
	}
	
	private void hook() {
		// get closest anchor
		hookLength = 10000;
		for(Entity e : anchorList) {
			double eHypot = Math.hypot(e.getCenterPosition().x - entity.getCenterPosition().x, e.getCenterPosition().y - entity.getCenterPosition().y);
			if(eHypot < hookLength) {
				hookLength = eHypot;
				hookedTo = e;
			}
		}
		
		degrees = Math.atan2(hookedTo.getCenterPosition().y - entity.getCenterPosition().y, 
				-(hookedTo.getCenterPosition().x - entity.getCenterPosition().x)) * 180/Math.PI;
		
		// --------------
		// clockwise
		double e1x, e1y, e2x, e2y;
		double p1x, p1y, p2x, p2y, p3x, p3y;
		p1x = entity.getCenterPosition().x;
		p1y = entity.getCenterPosition().y;
		p2x = entity.getCenterPosition().x + 99999*dx;
		p2y = entity.getCenterPosition().y + 99999*dy;
		p3x = hookedTo.getCenterPosition().x;
		p3y = hookedTo.getCenterPosition().y;
		
		e1x = p1x-p2x;
		e1y = p1y-p2y;
		e2x = p3x-p2x;
		e2y = p3y-p2y;
		
		if((e1x * e2y - e1y * e2x) >= 0) {
			clockWise = false;
		}
		else {
			clockWise = true;
		}
		// ----------
		// speed reduction at weird angles
		double tmpSpeed = Math.hypot(Math.cos(degrees*Math.PI/180) * dy, Math.sin(degrees*Math.PI/180) * dx);
		wSpeed = tmpSpeed / hookLength * 180/Math.PI;
		
		p1x = hookedTo.getCenterPosition().x;
		p1y = hookedTo.getCenterPosition().y;
		
		p2x = entity.getCenterPosition().x;
		p2y = entity.getCenterPosition().y;
		
		p3x = entity.getCenterPosition().x + 100*dx;
		p3y = entity.getCenterPosition().y + 100*dy;
		
		e1x = p2x - p1x;
		e1y = p2y - p1y;
		
		e2x = p2x - p3x;
		e2y = p2y - p3y;
		
		double test = ((e1x*e2x) + (e1y*e2y)) / (Math.hypot(e1x, e1y) * Math.hypot(e2x, e2y));
		test = Math.acos(test) * 180/Math.PI;
		if(test > 90) {
			test = Math.abs(test - 180);
		}
		wSpeed = test/90 * wSpeed;
		// ----------------------------------
		
		centriAcc = ACC_CONST/(hookLength*hookLength);
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		if(dead) return;
		
		if(hooked) {
			g.drawLine(entity.getCenterPosition().x, entity.getCenterPosition().y, hookedTo.getCenterPosition().x, hookedTo.getCenterPosition().y);
		}
		entity.render(gc, sb, g);
	}
}

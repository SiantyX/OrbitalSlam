package game.maps.interactables;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import game.Entity;
import game.Player;

public abstract class Interactable extends Entity {
	org.newdawn.slick.geom.Vector2f vector;
	

	public Interactable(String id) {
		super(id);
		
	}
	
	public void setPosition(Vector2f vector){
		this.setCenterPosition(vector);
		
	}
	
	public abstract void collisionCheck(StateBasedGame sb);
	
	public abstract void collision(Player player);
	
	public abstract void reset();

}

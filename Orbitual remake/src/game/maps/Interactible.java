package game.maps;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import game.Entity;
import game.Player;

public abstract class Interactible extends Entity {
	org.newdawn.slick.geom.Vector2f vector;

	public Interactible(String id) {
		super(id);
		
	}
	
	public void setPosition(Vector2f vector){
		this.setCenterPosition(vector);
		
	}
	
	public abstract void collisionCheck(StateBasedGame sb);
	
	public abstract void collision(Player player);
	
	public abstract void reset();

}

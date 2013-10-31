package game.maps;

import org.newdawn.slick.state.StateBasedGame;

import game.Entity;

public abstract class Interactible extends Entity {

	public Interactible(String id) {
		super(id);
		
	}
	
	public abstract void collisionCheck(StateBasedGame sb);

}

package game.maps.interactables.powerups;

import org.newdawn.slick.state.StateBasedGame;

import game.Entity;
import game.Player;
import game.maps.interactables.Interactable;

public abstract class PowerUp extends Interactable {

	public PowerUp(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public abstract void collisionCheck(StateBasedGame sb);

	public abstract void collision(Player player);

	public abstract void reset();
	
	public abstract void powerDown(Player player);

}

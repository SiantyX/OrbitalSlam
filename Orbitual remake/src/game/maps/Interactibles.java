package game.maps;

import game.Entity;

public abstract class Interactibles extends Entity {

	public Interactibles(String id) {
		super(id);
		
	}
	
	public abstract void collisioncheck();

}

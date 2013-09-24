package gamestates;

import networking.Lobby;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ClientMultiplayerState extends MultiplayerState {
	public static final int ID = 10;
	public static Lobby lobby = null;
	
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.init(gc, sb);
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		super.render(gc, sb, g);
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		super.update(gc, sb, delta);
	}
	
	public int getID() {
		return ID;
	}

}

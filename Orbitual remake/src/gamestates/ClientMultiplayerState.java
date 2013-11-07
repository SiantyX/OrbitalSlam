package gamestates;

import game.Game;
import game.Player;
import game.ViewPort;
import networking.Lobby;
import networking.NetHandler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ClientMultiplayerState extends MultiplayerState implements KeyListener {
	public static NetHandler hndlr;
	private int keyBind;
	
	public ClientMultiplayerState(int id) {
		super(id);
	}
	
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.init(gc, sb);
		
		if(hndlr != null) {
			keyBind = ((ControlsSettingsState)sb.getState(Game.State.CONTROLSSETTINGSSTATE.ordinal())).getKeyBinds()[8];
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		super.render(gc, sb, g);
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		super.update(gc, sb, delta);
		
		Game.UPDATE_BACKGROUND = Game.State.CLIENTMULTIPLAYERSTATE.ordinal();
	}
	
	public void keyPressed(int key, char c) {
		if(key == keyBind && !onCountDown) {
			hndlr.sendHookUpdate();
		}
	}
	
	public void setControls(int keyBinds[]) {
		keyBind = keyBinds[8];
	}
	
	public void updatePlayer(GameContainer gc, StateBasedGame sb, int delta, ViewPort vp, Player player) {
		//player.update(gc, sb, delta, vp);
	}
	
	public void close() {
		hndlr.close();
		hndlr = null;
	}
}

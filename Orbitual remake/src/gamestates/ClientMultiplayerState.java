package gamestates;

import java.util.concurrent.CopyOnWriteArrayList;

import game.Game;
import game.MultiPlayer;
import game.Player;
import game.ViewPort;
import game.maps.GameMap;
import networking.NetHandler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ClientMultiplayerState extends AbstractInGameState implements KeyListener {
	public static NetHandler hndlr;
	private int keyBind;
	public static CopyOnWriteArrayList<String> names = new CopyOnWriteArrayList<String>();
	
	public ClientMultiplayerState(int id) {
		super(id);
		scoreLimit = 20;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		if(names.isEmpty()) return;
		
		super.init(gc, sb);
		
		if(hndlr != null) {
			keyBind = ((ControlsSettingsState)sb.getState(Game.State.CONTROLSSETTINGSSTATE.ordinal())).getKeyBinds()[8];
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		super.render(gc, sb, g);
	}

	@Override
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
	
	public void close() {
		hndlr.close();
		hndlr = null;
	}

	@Override
	protected void specificInit(GameContainer gc, StateBasedGame sb) {
	}

	@Override
	protected int numberOfPlayers() {
		return names.size();
	}

	@Override
	protected Player createPlayer(int i, GameMap map) throws SlickException {
		return new MultiPlayer(i, map);
	}

	@Override
	protected void collisionCheck() {
	}

	@Override
	protected void updatePlayer(GameContainer gc, StateBasedGame sb, int delta,
			ViewPort vp, Player player) {
		((MultiPlayer)player).update(gc, sb, delta, vp);
	}
}

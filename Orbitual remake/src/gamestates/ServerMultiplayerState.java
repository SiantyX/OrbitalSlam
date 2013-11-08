package gamestates;

import game.Game;
import game.Player;
import game.QuadTree;
import game.ViewPort;
import game.maps.GameMap;
import game.maps.interactables.Interactable;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import networking.InGameHosting;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import components.SXTimer;

public class ServerMultiplayerState extends AbstractInGameState {
	private InGameHosting hosted;
	private SXTimer updatePosTimer;
	private static final float tickRate = 128;
	public static CopyOnWriteArrayList<String> names = new CopyOnWriteArrayList<String>();

	public ServerMultiplayerState(int id) {
		super(id);
		scoreLimit = 20;
		updatePosTimer = new SXTimer(Math.round(1000/tickRate));
	}

	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		if(names.isEmpty()) return;
		if(hosted != null) {
			super.init(gc, sb);

			for(int i = 0; i < players.size(); i++) {
				hosted.ipplayermap.put(names.get(i).split("\\@")[1], players.get(i));
			}
			hosted.start();

			players.get(0).KEYBIND = ((ControlsSettingsState)sb.getState(Game.State.CONTROLSSETTINGSSTATE.ordinal())).getKeyBinds()[8];
		}
	}

	public void newRound(StateBasedGame sb) throws SlickException {
		super.newRound(sb);

		hosted.ipplayermap.clear();
		for(int i = 0; i < players.size(); i++) {
			hosted.ipplayermap.put(names.get(i).split("\\@")[1], players.get(i));
		}

		players.get(0).KEYBIND = ((ControlsSettingsState)sb.getState(Game.State.CONTROLSSETTINGSSTATE.ordinal())).getKeyBinds()[8];
		
		hosted.setAllKeys("newround");
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		super.render(gc, sb, g);
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		super.update(gc, sb, delta);

		Game.UPDATE_BACKGROUND = Game.State.SERVERMULTIPLAYERSTATE.ordinal();

		if(updatePosTimer.isTriggered() >= 0) {
			for(Player p : players) {
				hosted.sendPlayerUpdate("pos", p);
			}
		}
	}

	public void setHoster(InGameHosting hosted) {
		this.hosted = hosted;
	}

	public void setControls(int keyBinds[]) {
		players.get(0).KEYBIND = keyBinds[8];
	}

	public void close() {
		hosted.close();
		try {
			hosted.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hosted = null;
	}

	@Override
	protected void specificInit(GameContainer gc, StateBasedGame sb) {
		qt = new QuadTree(0, map.getBounds());
	}

	@Override
	protected int numberOfPlayers() {
		return names.size();
	}

	@Override
	protected Player createPlayer(int i, GameMap map) throws SlickException {
		return new Player(i, map);
	}

	@Override
	protected void collisionCheck() {
		qt.clear();
		ArrayList<Interactable> allInters = getAllInteractables();
		for(Interactable i : allInters) {
			qt.insert(i);
		}
		ArrayList<Interactable> rObj = new ArrayList<Interactable>();
		for(Interactable i : allInters) {
			rObj.clear();
			qt.retrieve(rObj, i);

			for(Interactable j : rObj) {
				if(i.collisionCheck(j)) {
					i.collision(j);
				}

			}
		}
		// -------> to here
	}

	@Override
	protected void updatePlayer(GameContainer gc, StateBasedGame sb, int delta,
			ViewPort vp, Player player) {
		player.update(gc, sb, delta, vp);
	}
}

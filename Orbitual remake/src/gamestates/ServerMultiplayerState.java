package gamestates;

import game.Entity;
import game.Game;
import game.Player;
import game.maps.AnchorMap;

import java.awt.Font;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import networking.Hosting;
import networking.InGameHosting;
import networking.LobbyHosting;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

import components.SXTimer;

public class ServerMultiplayerState extends MultiplayerState {
	private InGameHosting hosted;
	private SXTimer updatePosTimer;
	private static final float tickRate = 64;

	public ServerMultiplayerState(int id) {
		super(id);
		updatePosTimer = new SXTimer(Math.round(1000/tickRate));
	}

	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.init(gc, sb);
		if(hosted != null) {
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
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		super.render(gc, sb, g);
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		super.update(gc, sb, delta);
		
		Game.UPDATE_BACKGROUND = Game.State.SERVERMULTIPLAYERSTATE.ordinal();

		hooked = players.get(0).isHooked();
		if(hooked != oldHooked) {
			hosted.setAllKeys("hook" + "\n" + "0" + "\n" + new Boolean(hooked).toString() + "\n" + players.get(0).getPosition().x + "\n" + players.get(0).getPosition().y + "\n" + players.get(0).getDx() + "\n" + players.get(0).getDy());
			oldHooked = hooked;
		}

		if(updatePosTimer.isTriggered() >= 0) {
			for(Player p : players) {
				hosted.setAllKeys("pos" + "\n" + players.indexOf(p) + "\n" + p.getPosition().x + "\n" + p.getPosition().y + "\n" + p.getDx() + "\n" + p.getDy());
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
}

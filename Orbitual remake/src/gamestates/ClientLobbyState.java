package gamestates;

import java.io.IOException;

import game.Game;
import game.MenuButton;
import networking.LobbyHosting;
import networking.NetHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class ClientLobbyState extends LobbyState {
	private Thread updateThread;

	public ClientLobbyState(int id) {
		super(id);
	}

	public static NetHandler hndlr;

	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.init(gc, sb);

		if(Game.LASTID == Game.State.BROWSERSTATE.ordinal()) {
			Runnable updateLobby = new Runnable() {
				public void run() {
					hndlr.updateClientLobby(players, mbox);
				}
			};
			Thread updateThread = new Thread(updateLobby);
			updateThread.start();
		}
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		super.update(gc, sb, delta);

		if(hndlr.started) {
			ClientMultiplayerState.lobby = hndlr.currentLobby;
			hndlr.close();
			ClientMultiplayerState.names = players;
			sb.getState(Game.State.CLIENTMULTIPLAYERSTATE.ordinal()).init(gc, sb);
			sb.enterState(Game.State.CLIENTMULTIPLAYERSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}

		if(cancelButton.isMousePressed() || hndlr == null) {
			if(hndlr != null) {
				hndlr.close();
				hndlr = null;
			}
			sb.enterState(Game.State.BROWSERSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}

		users.clear();
		for(int i = 0; i < players.size(); i++) {
			users.add(new MenuButton(players.get(i), new Rectangle(Game.centerWidth - 50, Game.centerHeight - 200 + (i*50), 100, 30), Color.black,
					players.get(i).length() < 1 ? "Unknown" : players.get(i), ttf, Color.yellow));
		}


	}

	public void sendText(String str) {
		hndlr.sendChatUpdate(Game.username + ": " + str);
	}
}

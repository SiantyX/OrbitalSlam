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
	public static final int ID = 11;
	public static NetHandler hndlr;

	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.init(gc, sb);

		if(Game.LASTID == BrowserState.ID) {
			Runnable updateLobby = new Runnable() {
				public void run() {
					hndlr.updateClientLobby(players, mbox.getMessages());
				}
			};
			Thread thread = new Thread(updateLobby);
			thread.start();
		}
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		super.update(gc, sb, delta);

		if(cancelButton.isMousePressed()) {
			hndlr.close();
			hndlr = null;
			sb.enterState(BrowserState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		users.clear();
		for(int i = 0; i < players.size(); i++) {
			users.add(new MenuButton(players.get(i), new Rectangle(Game.centerWidth - 50, Game.centerHeight - 200 + (i*50), 100, 30), Color.black,
					players.get(i).length() < 1 ? "Unknown" : players.get(i), ttf, Color.yellow));
		}
		
		if(hndlr.started) {
			ClientMultiplayerState.lobby = hndlr.currentLobby;
			hndlr.close();
			ClientMultiplayerState.names = players;
			sb.getState(ClientMultiplayerState.ID).init(gc, sb);
			sb.enterState(ClientMultiplayerState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
	}
	
	public void sendText(String str) {
		mbox.addMessage(str);
	}

	public int getID() {
		return ID;
	}
}

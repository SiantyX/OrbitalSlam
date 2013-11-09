package gamestates;

import game.Game;
import game.MenuButton;

import java.io.IOException;

import networking.InGameHosting;
import networking.LobbyHosting;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class HostLobbyState extends LobbyState implements KeyListener {
	public HostLobbyState(int id) {
		super(id);
	}
	
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.init(gc, sb);
		
		startButton = new MenuButton("start", new Rectangle(Game.centerWidth + 100, Game.centerHeight + 400, 200, 50), Color.white, "Start", ttf);
		buttons.add(startButton);
		
		if(Game.LASTID == Game.State.BEFOREGAMESTATE.ordinal()) {
			try {
				hosted = new LobbyHosting(hostname, ((ServerMultiplayerState)sb.getState(Game.State.SERVERMULTIPLAYERSTATE.ordinal())).getNumPlayers(), mbox);
				hosted.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		super.render(gc, sb, g);
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		super.update(gc, sb, delta);
		
		users.clear();
		players = hosted.getPlayers();
		
		if(startButton.isMousePressed()) {
			InGameHosting tmpHost = startGame();
			AbstractInGameState.finished = true;
			ServerMultiplayerState.names = players;
			((ServerMultiplayerState) sb.getState(Game.State.SERVERMULTIPLAYERSTATE.ordinal())).setHoster(tmpHost);
			sb.getState(Game.State.SERVERMULTIPLAYERSTATE.ordinal()).init(gc, sb);
			AbstractInGameState.finished = false;
			sb.enterState(Game.State.SERVERMULTIPLAYERSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if(cancelButton.isMousePressed()) {
			hosted.close();
			sb.enterState(Game.State.BROWSERSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		mbox = hosted.getBox();
		users.add(new MenuButton(players.get(0), new Rectangle(Game.centerWidth - 50, Game.centerHeight - 200, 100, 30), Color.black,
				"(Host) " + Game.username, ttf, Color.yellow));
		
		for(int i = 1; i < players.size(); i++) {
			users.add(new MenuButton(players.get(i), new Rectangle(Game.centerWidth - 50, Game.centerHeight - 200 + (i*50), 100, 30), Color.black,
					players.get(i).split("\\@")[0].length() < 1 ? "Unknown" : players.get(i).split("\\@")[0], ttf, Color.yellow));
		}
	}
	
	public void close() {
		hosted.close();
	}
	
	public void sendText(String str) {
		hosted.addToBox(Game.username + ": " + str);
		hosted.setAllKeys("chat\n" + Game.username + ": " + str);
	}
	
	public InGameHosting startGame() {
		hosted.setAllKeys("start");
		hosted.setInLobby(false);
		hosted.changeHost();
		hosted.wakeup();
		try {
			hosted.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new InGameHosting(hosted);
	}
}
package game;

import gamestates.AfterGameState;
import gamestates.AudioSettingsState;
import gamestates.BrowserState;
import gamestates.ClientLobbyState;
import gamestates.ClientMultiplayerState;
import gamestates.ControlsSettingsState;
import gamestates.DisplayModeState;
import gamestates.HostLobbyState;
import gamestates.InGameState;
import gamestates.MenuState;
import gamestates.MultiplayerState;
import gamestates.PauseMenuState;
import gamestates.ServerMultiplayerState;
import gamestates.SettingsState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public static AppGameContainer app;
	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;
	public static boolean fullscreen = false;
	public static boolean showHitbox = false;
	
	public static int centerHeight;
	public static int centerWidth;
	
	public static int LASTID;
	
	public static final int MAX_PLAYERS = 4;//8;
	public static int SCORE_LIMIT = 20;
	
	public static String username = "Player";
	
	public static Music MENU_MUSIC;
	public static Music INGAME_MUSIC;
	
	public Game() {
		super("Orbital Slam");
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		centerHeight = app.getHeight()/2;
		centerWidth = app.getWidth()/2;
		addState(new MenuState());
		addState(new DisplayModeState());
		addState(new InGameState());
		addState(new PauseMenuState());
		addState(new SettingsState());
		addState(new AudioSettingsState());
		addState(new ControlsSettingsState());
		addState(new AfterGameState());
		addState(new BrowserState());
		addState(new HostLobbyState());
		addState(new ClientMultiplayerState());
		addState(new ClientLobbyState());
		addState(new ServerMultiplayerState());
	}
	
	
	public static void main(String[] args) {
		
		try {
			app = new AppGameContainer(new Game());
			app.setDisplayMode(WIDTH, HEIGHT, fullscreen);
			app.setTargetFrameRate(120);
			app.setShowFPS(false);
			app.setSmoothDeltas(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
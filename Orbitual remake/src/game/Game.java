package game;

import gamestates.AfterGameState;
import gamestates.AudioSettingsState;
import gamestates.BeforeGameState;
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
	public static boolean fullscreen = true;
	public static boolean showHitbox = false;
	
	public static int centerHeight;
	public static int centerWidth;
	
	public static int LASTID;
	
	public static final int MAX_PLAYERS = 4;//8;
	
	public static String username = "Player";
	
	public static Music MENU_MUSIC;
	public static Music INGAME_MUSIC;
	
	public static enum State {
		MENUSTATE, DISPLAYMODESTATE, PAUSEMENUSTATE, SETTINGSSTATE,	AUDIOSETTINGSSTATE, CONTROLSSETTINGSSTATE,
		BEFOREGAMESTATE, INGAMESTATE, AFTERGAMESTATE,
		BROWSERSTATE, HOSTLOBBYSTATE, CLIENTMULTIPLAYERSTATE, CLIENTLOBBYSTATE, SERVERMULTIPLAYERSTATE
	}
	
	public Game() {
		super("Orbital Slam");
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		centerHeight = app.getHeight()/2;
		centerWidth = app.getWidth()/2;
		addState(new MenuState(State.MENUSTATE.ordinal()));
		addState(new DisplayModeState(State.DISPLAYMODESTATE.ordinal()));
		addState(new BeforeGameState(State.BEFOREGAMESTATE.ordinal()));
		addState(new InGameState(State.INGAMESTATE.ordinal()));
		addState(new PauseMenuState(State.PAUSEMENUSTATE.ordinal()));
		addState(new SettingsState(State.SETTINGSSTATE.ordinal()));
		addState(new AudioSettingsState(State.AUDIOSETTINGSSTATE.ordinal()));
		addState(new ControlsSettingsState(State.CONTROLSSETTINGSSTATE.ordinal()));
		addState(new AfterGameState(State.AFTERGAMESTATE.ordinal()));
		addState(new BrowserState(State.BROWSERSTATE.ordinal()));
		addState(new HostLobbyState(State.HOSTLOBBYSTATE.ordinal()));
		addState(new ClientMultiplayerState(State.CLIENTMULTIPLAYERSTATE.ordinal()));
		addState(new ClientLobbyState(State.CLIENTLOBBYSTATE.ordinal()));
		addState(new ServerMultiplayerState(State.SERVERMULTIPLAYERSTATE.ordinal()));
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
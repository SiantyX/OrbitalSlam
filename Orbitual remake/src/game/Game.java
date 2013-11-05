package game;

import java.io.File;

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
import gamestates.LoadState;
import gamestates.MenuState;
import gamestates.MultiplayerState;
import gamestates.PauseMenuState;
import gamestates.ServerMultiplayerState;
import gamestates.SettingsState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
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
	public static int UPDATE_BACKGROUND;

	public static final int MAX_PLAYERS = 4;//8;

	public static String username = "Player";

	public static Music MENU_MUSIC;
	public static Music INGAME_MUSIC;

	public static enum State {
		LOADSTATE, MENUSTATE, DISPLAYMODESTATE, PAUSEMENUSTATE, SETTINGSSTATE,	AUDIOSETTINGSSTATE, CONTROLSSETTINGSSTATE,
		BEFOREGAMESTATE, INGAMESTATE, AFTERGAMESTATE,
		BROWSERSTATE, HOSTLOBBYSTATE, CLIENTMULTIPLAYERSTATE, CLIENTLOBBYSTATE, SERVERMULTIPLAYERSTATE
	}

	public Game() {
		super("Orbital Slam");
	}


	public static void main(String[] args) {
		System.setProperty("java.library.path", "libs");

		//Extracted from Distributing Your LWJGL Application
		System.setProperty("org.lwjgl.librarypath", new File("libs/natives").getAbsolutePath());

		try {
			Game game = new Game();
			app = new AppGameContainer(game);
			app.setDisplayMode(WIDTH, HEIGHT, fullscreen);
			centerHeight = app.getHeight()/2;
			centerWidth = app.getWidth()/2;
			app.setTargetFrameRate(120);
			app.setShowFPS(false);
			app.setSmoothDeltas(false);
			app.setUpdateOnlyWhenVisible(false);
			app.setAlwaysRender(true);
			app.start();

		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new LoadState(State.LOADSTATE.ordinal(), this));
	}
}
package gamestates;

import game.Game;
import game.Game.State;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

import components.SXTimer;

public class LoadState extends BasicGameState{
	private final int ID;
	private Game game;
	//private Thread initThread;
	private State[] allStates;
	private int nextLoad;
	private SXTimer timer;
	private String dots;
	private Thread initThread;

	public LoadState(int id, Game game) {
		ID = id;
		this.game = game;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		addAll();
		allStates = Game.State.values();
		nextLoad = 1;
		timer = new SXTimer(1000);
		dots = "...";

		Runnable r = new Runnable() {
			public void run() {
				loadAssets();
			}
		};

		initThread = new Thread(r);
		initThread.start();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		FontUtils.drawCenter(g.getFont(), "Loading" + dots, Game.centerWidth, Game.centerHeight, 100);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		loadNext(gc, sb);

		if(timer.isTriggered() >= 0) {
			dots += ".";
			if(dots.length() > 3) {
				dots = ".";
			}
		}

		if(nextLoad == allStates.length) {
			try {
				initThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sb.enterState(Game.State.MENUSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black));
		}
	}

	public void loadNext(final GameContainer gc, final StateBasedGame sb) {
		try {
			//long old = System.currentTimeMillis();
			sb.getState(nextLoad).init(gc, sb);
			//System.out.println((System.currentTimeMillis() - old)/(float)1000 + Integer.toString(nextLoad));
			nextLoad++;
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void loadAssets() {
		try {
			Game.INGAME_MUSIC = new Music("res/audio/music/ingame.ogg");
			Game.MENU_MUSIC = new Music("res/audio/music/menu.ogg");
			Game.MENU_MUSIC.loop();
			Game.MENU_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void addAll() {
		game.addState(new MenuState(State.MENUSTATE.ordinal()));
		game.addState(new DisplayModeState(State.DISPLAYMODESTATE.ordinal()));
		game.addState(new BeforeGameState(State.BEFOREGAMESTATE.ordinal()));
		game.addState(new InGameState(State.INGAMESTATE.ordinal()));
		game.addState(new PauseMenuState(State.PAUSEMENUSTATE.ordinal()));
		game.addState(new SettingsState(State.SETTINGSSTATE.ordinal()));
		game.addState(new AudioSettingsState(State.AUDIOSETTINGSSTATE.ordinal()));
		game.addState(new ControlsSettingsState(State.CONTROLSSETTINGSSTATE.ordinal()));
		game.addState(new AfterGameState(State.AFTERGAMESTATE.ordinal()));
		game.addState(new BrowserState(State.BROWSERSTATE.ordinal()));
		game.addState(new HostLobbyState(State.HOSTLOBBYSTATE.ordinal()));
		game.addState(new ClientMultiplayerState(State.CLIENTMULTIPLAYERSTATE.ordinal()));
		game.addState(new ClientLobbyState(State.CLIENTLOBBYSTATE.ordinal()));
		game.addState(new ServerMultiplayerState(State.SERVERMULTIPLAYERSTATE.ordinal()));
	}

	@Override
	public int getID() {
		return ID;
	}
}

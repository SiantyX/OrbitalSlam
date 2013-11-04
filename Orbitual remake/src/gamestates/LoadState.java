package gamestates;

import game.Game;
import game.Game.State;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

public class LoadState extends BasicGameState{
	private final int ID;
	private Game game;
	private Thread initThread;
	private State[] allStates;
	private int nextLoad;

	public LoadState(int id, Game game) {
		ID = id;
		this.game = game;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		addAll();
		allStates = Game.State.values();
		nextLoad = 1;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		FontUtils.drawCenter(g.getFont(), "Loading...", Game.centerWidth, Game.centerHeight, 100);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		loadNext(gc, sb);
		
		if(nextLoad == allStates.length) {
			sb.enterState(Game.State.MENUSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black));
		}
	}
	
	public void loadNext(GameContainer gc, StateBasedGame sb) {
		try {
			sb.getState(allStates[nextLoad].ordinal()).init(gc, sb);
			nextLoad++;
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

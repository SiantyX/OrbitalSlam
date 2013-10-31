package gamestates;

import java.awt.Font;
import java.util.ArrayList;

import game.Game;
import game.MenuButton;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

public class MenuState extends BasicGameState{
	private final int ID;
	
	private ArrayList<MenuButton> buttons;
	private MenuButton playButton, multiButton, settingsButton, quitButton;
	private TrueTypeFont ttf;

	public MenuState(int id) {
		ID = id;
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		
		/*
		playButton = new MenuButton("Local Multiplayer", new Vector2f(Game.centerWidth -100 , Game.centerHeight -125), new Image("res/buttons/play.png"));
		buttons.add(playButton);
		
		multiButton = new MenuButton("Local Multiplayer", new Vector2f(Game.centerWidth -100 , Game.centerHeight -125), new Image("res/buttons/play.png"));
		buttons.add(playButton);
	
		settingsButton = new MenuButton("settings", new Vector2f(Game.centerWidth -100 , Game.centerHeight), new Image("res/buttons/settings.png"));
		buttons.add(settingsButton);
		
		quitButton = new MenuButton("quit", new Vector2f(Game.centerWidth -100, Game.centerHeight + 125), new Image("res/buttons/quit.png"));
		buttons.add(quitButton);
		*/
		Font f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);
		// --------
		playButton = new MenuButton("Local Multiplayer", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 150, 200, 50), Color.white, "Local Multiplayer", ttf);
		multiButton = new MenuButton("Online Multiplayer", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 50, 200, 50), Color.white, "Online Multiplayer", ttf);
		settingsButton = new MenuButton("Settings", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 50, 200, 50), Color.white, "Settings", ttf);
		quitButton = new MenuButton("Quit", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 150, 200, 50), Color.white, "Quit", ttf);
		
		buttons.add(playButton);
		buttons.add(multiButton);
		buttons.add(settingsButton);
		buttons.add(quitButton);
		//-------------------
		
		f = new Font("Comic Sans", Font.ITALIC, 50);
		ttf = new TrueTypeFont(f, true);
		
		Game.MENU_MUSIC = new Music("res/audio/music/menu.ogg");
		Game.MENU_MUSIC.loop();
		Game.MENU_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
		
		Game.INGAME_MUSIC = new Music("res/audio/music/ingame.ogg");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		
		FontUtils.drawCenter(ttf, "Orbital Slam", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		InGameState.finished = true;
		
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}
		
		Input input = gc.getInput();
		
		if (playButton.isMousePressed()) {
			/*Game.LASTID = getID();
			InGameState.finished = true;
			sb.getState(InGameState.ID).init(gc, sb);
			Game.INGAME_MUSIC.loop();
			Game.INGAME_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
			sb.enterState(InGameState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));*/
			Game.LASTID = getID();
			sb.enterState(Game.State.BEFOREGAMESTATE.ordinal());
			InGameState.finished = true;
		}
		
		if(multiButton.isMousePressed()) {
			Game.LASTID = getID();
			InGameState.finished = true;
			sb.enterState(Game.State.BROWSERSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black));
		}
		
		if (settingsButton.isMousePressed()) {
			Game.LASTID = getID();
			sb.enterState(Game.State.SETTINGSSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (quitButton.isMousePressed() || input.isKeyPressed(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}

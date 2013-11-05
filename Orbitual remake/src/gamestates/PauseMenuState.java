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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

public class PauseMenuState extends ResumableState {
	private final int ID;
	private ArrayList<MenuButton> buttons;
	private MenuButton continueButton, settingsButton, exitButton;
	private TrueTypeFont ttf;
	
	public PauseMenuState(int id) {
		ID = id;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		
		continueButton = new MenuButton("continue", new Vector2f(Game.centerWidth -100 , Game.centerHeight -125), new Image("res/buttons/continue.png"));
		buttons.add(continueButton);
	
		settingsButton = new MenuButton("settings", new Vector2f(Game.centerWidth -100 , Game.centerHeight), new Image("res/buttons/settings.png"));
		buttons.add(settingsButton);
		
		exitButton = new MenuButton("exit", new Vector2f(Game.centerWidth -100, Game.centerHeight + 125), new Image("res/buttons/exit.png"));
		buttons.add(exitButton);
		
		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		ttf = new TrueTypeFont(f, true);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		super.render(gc, sb, g);
		
		if(Game.UPDATE_BACKGROUND == 0) {
			sb.getState(Game.State.INGAMESTATE.ordinal()).render(gc, sb, g);
			g.setColor(new Color(0, 0, 0, 125));
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		}
		
		FontUtils.drawCenter(ttf, "Paused", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		super.update(gc, sb, delta);
		
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}
		
		Input input = gc.getInput();

		if (continueButton.isMousePressed() || input.isKeyPressed(Input.KEY_ESCAPE)) {
			Game.LASTID = getID();
			Game.MENU_MUSIC.stop();
			Game.INGAME_MUSIC.resume();
			Game.INGAME_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
			if(Game.UPDATE_BACKGROUND > 0) {
				sb.enterState(Game.UPDATE_BACKGROUND, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
			}
			else {
				sb.enterState(Game.State.INGAMESTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
						100));
				InGameState.startCountDown();
			}
		}
		
		if (settingsButton.isMousePressed()) {
			Game.LASTID = getID();
			sb.enterState(Game.State.SETTINGSSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (exitButton.isMousePressed()) {
			Game.LASTID = getID();
			sb.enterState(Game.State.MENUSTATE.ordinal());
			
			if(Game.UPDATE_BACKGROUND > 0) {
				((MultiplayerState) sb.getState(Game.UPDATE_BACKGROUND)).close();
			}
			
			Game.UPDATE_BACKGROUND = 0;
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}

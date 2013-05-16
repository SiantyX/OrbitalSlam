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

public class PauseMenuState extends BasicGameState{
	public static final int ID = 0;
	private ArrayList<MenuButton> buttons;
	private MenuButton continueButton, settingsButton, exitButton;
	private Image playImage;
	private TrueTypeFont ttf;

	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		
		playImage = new Image("res/buttons/play.png");
	
		
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
		sb.getState(InGameState.ID).render(gc, sb, g);
		
		g.setColor(new Color(0, 0, 0, 125));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		FontUtils.drawCenter(ttf, "Paused", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}
		
		Input input = gc.getInput();

		if (continueButton.isMousePressed() || input.isKeyPressed(Input.KEY_ESCAPE)) {
			Game.LASTID = getID();
			sb.enterState(InGameState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
			InGameState.startCountDown();
		}
		
		if (settingsButton.isMousePressed()) {
			Game.LASTID = getID();
			sb.enterState(SettingsState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (exitButton.isMousePressed()) {
			Game.LASTID = getID();
			sb.enterState(MenuState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}

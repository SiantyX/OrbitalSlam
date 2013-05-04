package gamestates;

import java.util.ArrayList;

import game.MenuButton;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuState extends BasicGameState{
	public static final int ID = 2;
	private ArrayList<MenuButton> buttons;
	private MenuButton playButton, settingsButton, exitButton;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		
		playButton = new MenuButton("play", new Vector2f(100, 100), new Image("res/buttons/play.png"));
		buttons.add(playButton);
	
		settingsButton = new MenuButton("settings", new Vector2f(100, 200), new Image("res/buttons/settings.png"));
		buttons.add(settingsButton);
		
		exitButton = new MenuButton("exit", new Vector2f(100, 300), new Image("res/buttons/exit.png"));
		buttons.add(exitButton);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
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
		
		if (playButton.isMousePressed()) {
			sb.enterState(InGameState.ID);
		}
		
		if (exitButton.isMousePressed()) {
			System.exit(0);
		}
		
	}

	@Override
	public int getID() {
		return ID;
	}

}

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

public class MenuState extends BasicGameState{
	public static final int ID = 2;
	private ArrayList<MenuButton> buttons;
	private MenuButton playButton, settingsButton, quitButton;
	private Image playImage;
	private TrueTypeFont ttf;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		
		playImage = new Image("res/buttons/play.png");
		
		
		playButton = new MenuButton("play", new Vector2f(Game.centerWidth -100 , Game.centerHeight -125), new Image("res/buttons/play.png"));
		buttons.add(playButton);
	
		settingsButton = new MenuButton("settings", new Vector2f(Game.centerWidth -100 , Game.centerHeight), new Image("res/buttons/settings.png"));
		buttons.add(settingsButton);
		
		quitButton = new MenuButton("quit", new Vector2f(Game.centerWidth -100, Game.centerHeight + 125), new Image("res/buttons/quit.png"));
		buttons.add(quitButton);
		
		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		ttf = new TrueTypeFont(f, true);
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
		/*if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			System.exit(0);
		}*/
		
		if (playButton.isMousePressed()) {
			Game.LASTID = getID();
			InGameState.finished = true;
			sb.getState(InGameState.ID).init(gc, sb);
			sb.enterState(InGameState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (settingsButton.isMousePressed()) {
			Game.LASTID = getID();
			sb.enterState(SettingsState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));;
		}
		
		if (quitButton.isMousePressed()) {
			Game.LASTID = getID();
			System.exit(0);
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}

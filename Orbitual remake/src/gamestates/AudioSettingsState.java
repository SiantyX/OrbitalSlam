package gamestates;

import game.Game;
import game.MenuButton;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

public class AudioSettingsState extends BasicGameState {
	public static final int ID = 6;
	private ArrayList<MenuButton> buttons;
	private MenuButton okButton, cancelButton;
	
	private TrueTypeFont bigText;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();

		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);

		f = new Font("Arial", Font.PLAIN, 18);
		TrueTypeFont ttf = new TrueTypeFont(f, true);
		
		okButton = new MenuButton("ok", new Rectangle(Game.centerWidth - 250, Game.centerHeight + 200, 200, 50), Color.white, "Ok", ttf);
		cancelButton = new MenuButton("cancel", new Rectangle(Game.centerWidth + 50, Game.centerHeight + 200, 200, 50), Color.white, "Cancel", ttf);

		buttons.add(okButton);
		buttons.add(cancelButton);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		FontUtils.drawCenter(bigText, "Audio", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
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
		
		if (input.isKeyPressed(Input.KEY_ENTER) || okButton.isMousePressed()) {
			sb.enterState(SettingsState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}

		if (input.isKeyPressed(Input.KEY_ESCAPE) || cancelButton.isMousePressed()) {
			sb.enterState(SettingsState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
	}

	@Override
	public int getID() {
		return ID;
	}
}

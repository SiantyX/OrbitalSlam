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

public class BeforeGameState extends BasicGameState{
	public static final int ID = 13;
	private ArrayList<MenuButton> buttons;
	private MenuButton okButton, backButton;
	private TrueTypeFont ttf;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		

		Font f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);
		okButton = new MenuButton("knas", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 150, 200, 50), Color.white, "Ok", ttf);
		backButton = new MenuButton("back", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 200, 200, 50), Color.white, "Back", ttf);
		
		buttons.add(okButton);
		buttons.add(backButton);
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
		
		if (okButton.isMousePressed()) {
			Game.LASTID = getID();
			InGameState.finished = true;
			sb.getState(InGameState.ID).init(gc, sb);
			Game.INGAME_MUSIC.loop();
			Game.INGAME_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
			sb.enterState(InGameState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		if (input.isKeyPressed(Input.KEY_ESCAPE) || backButton.isMousePressed()) {
			sb.enterState(MenuState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}

	}

	@Override
	public int getID() {
		return ID;
	}

}

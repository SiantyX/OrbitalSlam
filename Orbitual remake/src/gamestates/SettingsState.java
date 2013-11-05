package gamestates;

import java.awt.Font;
import java.util.ArrayList;

import game.Game;
import game.MenuButton;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.FontUtils;

public class SettingsState extends ResumableState implements KeyListener{
	private final int ID;
	private ArrayList<MenuButton> buttons;
	private MenuButton backButton, videoButton, controlsButton, audioButton;
	private TrueTypeFont ttf;
	private TrueTypeFont bigText;
	
	public SettingsState(int id) {
		ID = id;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();

		
		Font f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);
		
		f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);
		
		controlsButton = new MenuButton("controls", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 150, 200, 50), Color.white, "Controls", ttf);
		audioButton = new MenuButton("audio", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 50, 200, 50), Color.white, "Audio", ttf);
		videoButton = new MenuButton("video", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 50, 200, 50), Color.white, "Video", ttf);
		backButton = new MenuButton("back", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 150, 200, 50), Color.white, "Back", ttf);
		
		buttons.add(controlsButton);
		buttons.add(backButton);
		buttons.add(audioButton);
		buttons.add(videoButton);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		super.render(gc, sb, g);
		
		FontUtils.drawCenter(bigText, "Settings", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
		

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		super.update(gc, sb, delta);
		
		Input input = gc.getInput();
		
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}

		if (controlsButton.isMousePressed()) {
			
			sb.enterState(Game.State.CONTROLSSETTINGSSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (audioButton.isMousePressed()) {
			sb.enterState(Game.State.AUDIOSETTINGSSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (videoButton.isMousePressed()) {
			sb.enterState(Game.State.DISPLAYMODESTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (backButton.isMousePressed() || input.isKeyPressed(Input.KEY_ESCAPE)) {
			sb.enterState(Game.LASTID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
			Game.LASTID = getID();
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}

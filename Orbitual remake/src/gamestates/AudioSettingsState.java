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
import org.newdawn.slick.geom.Circle;
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
	private MenuButton musicButton, masterButton, soundButton;
	private Circle musicCircle, masterCircle, soundCircle;
	private String overMusic, overMaster, overSound;
	
	private TrueTypeFont bigText;
	private TrueTypeFont ttf;
	
	public static float MASTER_LEVEL = 0.5f;
	public static float MUSIC_LEVEL = 1;
	public static float SOUND_LEVEL = 1;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();

		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);

		f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);
		
		okButton = new MenuButton("ok", new Rectangle(Game.centerWidth - 250, Game.centerHeight + 200, 200, 50), Color.white, "Ok", ttf);
		cancelButton = new MenuButton("cancel", new Rectangle(Game.centerWidth + 50, Game.centerHeight + 200, 200, 50), Color.white, "Cancel", ttf);
		
		masterButton = new MenuButton("master", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 200, 200, 50), new Color(255, 255, 255, 30), "", ttf);
		musicButton = new MenuButton("music", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 100, 200, 50), new Color(255, 255, 255, 30), "", ttf);
		soundButton = new MenuButton("sound", new Rectangle(Game.centerWidth - 100, Game.centerHeight, 200, 50), new Color(255, 255, 255, 30), "", ttf);
		
		masterCircle = new Circle(Game.centerWidth + (MASTER_LEVEL*200-100), Game.centerHeight - 175, 5);
		musicCircle = new Circle(Game.centerWidth + (MUSIC_LEVEL*200-100), Game.centerHeight - 75, 5);
		soundCircle = new Circle(Game.centerWidth + (SOUND_LEVEL*200-100), Game.centerHeight + 25, 5);
		
		overMaster = "Master level: " + (int)(MASTER_LEVEL*100);
		overMusic = "Music level: " + (int)(MUSIC_LEVEL*100);
		overSound = "Sound level: " + (int)(SOUND_LEVEL*100);

		buttons.add(okButton);
		buttons.add(cancelButton);
		
		buttons.add(masterButton);
		buttons.add(musicButton);
		buttons.add(soundButton);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		FontUtils.drawCenter(bigText, "Audio", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
		
		FontUtils.drawCenter(ttf, overMaster, Game.centerWidth - 100, Game.centerHeight - 225, 200);
		FontUtils.drawCenter(ttf, overMusic, Game.centerWidth - 100, Game.centerHeight - 125, 200);
		FontUtils.drawCenter(ttf, overSound, Game.centerWidth - 100, Game.centerHeight - 25, 200);
		
		g.setColor(Color.white);
		g.fill(masterCircle);
		g.fill(musicCircle);
		g.fill(soundCircle);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}
		
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ENTER) || okButton.isMousePressed()) {
			MASTER_LEVEL = ((masterCircle.getCenterX() - masterButton.getPosition().x) / 200);
			MUSIC_LEVEL = ((musicCircle.getCenterX() - musicButton.getPosition().x) / 200);
			SOUND_LEVEL = ((soundCircle.getCenterX() - soundButton.getPosition().x) / 200);
			
			Game.MENU_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*MASTER_LEVEL);
			Game.INGAME_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*MASTER_LEVEL);
			sb.enterState(SettingsState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}

		if (input.isKeyPressed(Input.KEY_ESCAPE) || cancelButton.isMousePressed()) {
			sb.enterState(SettingsState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		// sound editor
		if (masterButton.isMousePressed()) {
			masterCircle.setLocation(input.getMouseX() - masterCircle.getRadius(), masterCircle.getY());
			overMaster = "Master level: " + (int)((masterCircle.getCenterX() - masterButton.getPosition().x) / 2);
		}
		
		if (musicButton.isMousePressed()) {
			musicCircle.setLocation(input.getMouseX() - musicCircle.getRadius(), musicCircle.getY());
			overMusic = "Music level: " + (int)((musicCircle.getCenterX() - musicButton.getPosition().x) / 2);
		}
		if (soundButton.isMousePressed()) {
			soundCircle.setLocation(input.getMouseX() - soundCircle.getRadius(), soundCircle.getY());
			overSound = "Sound level: " + (int)((soundCircle.getCenterX() - soundButton.getPosition().x) / 2);
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}

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
	private final int ID;
	
	private ArrayList<MenuButton> buttons;
	private MenuButton okButton, cancelButton;
	private MenuButton[] sliders;//musicButton, masterButton, soundButton;
	private Circle[] circles;//musicCircle, masterCircle, soundCircle;
	private String[] overhead;//overMusic, overMaster, overSound;
	private String[] overheadHelper;
	private boolean[] pressed; //masterPressed, musicPressed, soundPressed;

	
	private TrueTypeFont bigText;
	private TrueTypeFont ttf;
	
	public static float MASTER_LEVEL = 0.5f;
	public static float MUSIC_LEVEL = 1;
	public static float SOUND_LEVEL = 1;
	
	public AudioSettingsState(int id) {
		ID = id;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		overhead = new String[3];
		circles = new Circle[3];
		pressed = new boolean[3];
		sliders = new MenuButton[3];
		overheadHelper = new String[]{"Master level: ","Music level: ","Sound level: "};
		
		pressed[0] = pressed[1] = pressed[2] = false;

		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);

		f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);
		
		okButton = new MenuButton("ok", new Rectangle(Game.centerWidth - 250, Game.centerHeight + 200, 200, 50), Color.white, "Ok", ttf);
		cancelButton = new MenuButton("cancel", new Rectangle(Game.centerWidth + 50, Game.centerHeight + 200, 200, 50), Color.white, "Cancel", ttf);
		
		sliders[0] = new MenuButton("master", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 200, 200, 50), new Color(255, 255, 255, 30), "", ttf);
		sliders[1] = new MenuButton("music", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 100, 200, 50), new Color(255, 255, 255, 30), "", ttf);
		sliders[2] = new MenuButton("sound", new Rectangle(Game.centerWidth - 100, Game.centerHeight, 200, 50), new Color(255, 255, 255, 30), "", ttf);
		
		circles[0] = new Circle(Game.centerWidth + (MASTER_LEVEL*200-100), Game.centerHeight - 175, 5);
		circles[1] = new Circle(Game.centerWidth + (MUSIC_LEVEL*200-100), Game.centerHeight - 75, 5);
		circles[2] = new Circle(Game.centerWidth + (SOUND_LEVEL*200-100), Game.centerHeight + 25, 5);
		
		overhead[0] = "Master level: " + (int)(MASTER_LEVEL*100);
		overhead[1] = "Music level: " + (int)(MUSIC_LEVEL*100);
		overhead[2] = "Sound level: " + (int)(SOUND_LEVEL*100);

		buttons.add(okButton);
		buttons.add(cancelButton);
		
		buttons.add(sliders[0]);
		buttons.add(sliders[1]);
		buttons.add(sliders[2]);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		FontUtils.drawCenter(bigText, "Audio", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
		
		FontUtils.drawCenter(ttf, overhead[0], Game.centerWidth - 100, Game.centerHeight - 225, 200);
		FontUtils.drawCenter(ttf, overhead[1], Game.centerWidth - 100, Game.centerHeight - 125, 200);
		FontUtils.drawCenter(ttf, overhead[2], Game.centerWidth - 100, Game.centerHeight - 25, 200);
		
		g.setColor(Color.white);
		g.fill(circles[0]);
		g.fill(circles[1]);
		g.fill(circles[2]);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}
		
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ENTER) || okButton.isMousePressed()) {
			MASTER_LEVEL = ((circles[0].getCenterX() - sliders[0].getPosition().x) / 200);
			MUSIC_LEVEL = ((circles[1].getCenterX() - sliders[1].getPosition().x) / 200);
			SOUND_LEVEL = ((circles[2].getCenterX() - sliders[2].getPosition().x) / 200);
			
			Game.MENU_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*MASTER_LEVEL);
			Game.INGAME_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*MASTER_LEVEL);
			sb.enterState(Game.State.SETTINGSSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}

		if (input.isKeyPressed(Input.KEY_ESCAPE) || cancelButton.isMousePressed()) {
			sb.enterState(Game.State.SETTINGSSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		
		if(!input.isMouseButtonDown(0)) {
			pressed[0] = pressed[1] = pressed[2] = false;
		}
		for(int i = 0; i < 3; i++) {
			if(sliders[i].isMousePressed()) {
				circles[i].setLocation(input.getMouseX() - circles[i].getRadius(), circles[i].getY());
				overhead[i] = overheadHelper[i] + (int)((circles[i].getCenterX() - sliders[i].getPosition().x) / 2);
				pressed[i] = true;
			}
			if(pressed[i]) {
				circles[i].setLocation(input.getMouseX() - circles[i].getRadius(), circles[i].getY());
				overhead[i] = overheadHelper[i] + (int)((circles[i].getCenterX() - sliders[i].getPosition().x) / 2);
			}
			if(circles[i].getCenterX() < sliders[i].getPosition().x) {
				circles[i].setCenterX(sliders[i].getPosition().x);
				overhead[i] = overheadHelper[i] + (int)((circles[i].getCenterX() - sliders[i].getPosition().x) / 2);
			}
			if(circles[i].getCenterX() > (sliders[i].getPosition().x + sliders[i].getWidth())) {
				circles[i].setCenterX(sliders[i].getPosition().x + sliders[i].getWidth());
				overhead[i] = overheadHelper[i] + (int)((circles[i].getCenterX() - sliders[i].getPosition().x) / 2);
			}
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}

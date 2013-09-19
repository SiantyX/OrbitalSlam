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

public class SettingsState extends BasicGameState implements KeyListener{
	public static final int ID = 4;
	private ArrayList<MenuButton> buttons;
	private MenuButton backButton, videoButton, controlsButton, audioButton, scoreLimitButton;
	private TrueTypeFont ttf;
	private TrueTypeFont bigText;
	
	private boolean changeScoreLimit;
	private static boolean justChanged = false;
	private Color oldColor;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		changeScoreLimit = false;
		
		Font f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);
		
		f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);
		
		controlsButton = new MenuButton("controls", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 150, 200, 50), Color.white, "Controls", ttf);
		audioButton = new MenuButton("audio", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 50, 200, 50), Color.white, "Audio", ttf);
		videoButton = new MenuButton("video", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 50, 200, 50), Color.white, "Video", ttf);
		backButton = new MenuButton("back", new Rectangle(Game.centerWidth - 100, Game.centerHeight + 150, 200, 50), Color.white, "Back", ttf);
		scoreLimitButton = new MenuButton("score", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 250, 200, 50), new Color(0, 0, 0, 0), "Score limit: " + Game.SCORE_LIMIT, ttf);
		oldColor = scoreLimitButton.getBackColor();
		
		buttons.add(controlsButton);
		buttons.add(backButton);
		buttons.add(audioButton);
		buttons.add(videoButton);
		buttons.add(scoreLimitButton);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		FontUtils.drawCenter(bigText, "Settings", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
		

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		Input input = gc.getInput();
		
		if(justChanged) {
			input.clearKeyPressedRecord();
			justChanged = false;
		}
		
		if(changeScoreLimit) {
			if(!scoreLimitButton.getBackColor().equals(new Color(40, 40, 40))) {
				oldColor = scoreLimitButton.getBackColor();
				scoreLimitButton.setBackColor(new Color(40, 40, 40));
			}
		}
		
		else {
			if(scoreLimitButton.getBackColor().equals(new Color(40, 40, 40))) {
				scoreLimitButton.setBackColor(oldColor);
			}
		}
		
		for (MenuButton button : buttons) {
			button.update(gc, sb, delta);
		}

		if (controlsButton.isMousePressed()) {
			changeScoreLimit = false;
			scoreLimitButton.setBackColor(oldColor);
			scoreLimitButton.setText("Score limit: " + Game.SCORE_LIMIT);
			sb.enterState(ControlsSettingsState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (audioButton.isMousePressed()) {
			changeScoreLimit = false;
			scoreLimitButton.setBackColor(oldColor);
			scoreLimitButton.setText("Score limit: " + Game.SCORE_LIMIT);
			sb.enterState(AudioSettingsState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (videoButton.isMousePressed()) {
			changeScoreLimit = false;
			scoreLimitButton.setBackColor(oldColor);
			scoreLimitButton.setText("Score limit: " + Game.SCORE_LIMIT);
			sb.enterState(DisplayModeState.ID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (backButton.isMousePressed() || input.isKeyPressed(Input.KEY_ESCAPE)) {
			changeScoreLimit = false;
			scoreLimitButton.setBackColor(oldColor);
			scoreLimitButton.setText("Score limit: " + Game.SCORE_LIMIT);
			sb.enterState(Game.LASTID, new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
			Game.LASTID = getID();
		}
		
		if (scoreLimitButton.isMousePressed()) {
			changeScoreLimit = true;
			scoreLimitButton.setText("");
		}
	}
	
	public void keyPressed(int key, char c) {
		if(changeScoreLimit) {
			if(key == Input.KEY_ENTER) {
				changeScoreLimit = false;
				int oldscore = Game.SCORE_LIMIT;
				try {
					Game.SCORE_LIMIT = Integer.parseInt(scoreLimitButton.getText()) < 1 ? 1 : Integer.parseInt(scoreLimitButton.getText());
				}
				catch(NumberFormatException e) {
					Game.SCORE_LIMIT = oldscore;
				}
				scoreLimitButton.setText("Score limit: " + Game.SCORE_LIMIT);
			}
			else if(key == Input.KEY_ESCAPE) {
				changeScoreLimit = false;
				justChanged = true;
				scoreLimitButton.setText("Score limit: " + Game.SCORE_LIMIT);
			}
			else if(key >= Input.KEY_1 && key <= Input.KEY_0) {
				scoreLimitButton.setText(scoreLimitButton.getText() + Input.getKeyName(key));
			}
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}

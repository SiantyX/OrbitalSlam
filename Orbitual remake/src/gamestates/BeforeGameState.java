package gamestates;

import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;

import game.Game;
import game.MenuButton;
import game.maps.AnchorMap;
import game.maps.GameMap;
import game.maps.RandomFunkyMap;

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

public class BeforeGameState extends BasicGameState implements KeyListener {
	private final int ID;
	
	private ArrayList<MenuButton> buttons;
	private MenuButton okButton, backButton, mapButton, modeButton, scoreLimitButton;
	private TrueTypeFont ttf;
	private GameMap selectedMap;
	private LinkedList<GameMap> maplist;
	private Color oldColor;
	private boolean changeScoreLimit;
	private boolean justChanged;
	private int scoreLimit;
	
	public BeforeGameState(int id) {
		ID = id;
		scoreLimit = 20;
	}
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		buttons = new ArrayList<MenuButton>();
		
		maplist = new LinkedList<GameMap>();
		maplist.add(new AnchorMap());
		maplist.add(new RandomFunkyMap());
		
		selectedMap = (GameMap) maplist.poll();
		maplist.addLast(selectedMap);

		Font f = new Font("Arial", Font.PLAIN, 18);
		ttf = new TrueTypeFont(f, true);
		okButton = new MenuButton("Ok", new Rectangle(Game.centerWidth - 250, Game.centerHeight + 200, 200, 50), Color.white, "Ok", ttf);
		backButton = new MenuButton("Back", new Rectangle(Game.centerWidth + 50, Game.centerHeight + 200, 200, 50), Color.white, "Back", ttf);
		mapButton = new MenuButton(selectedMap.toString(), new Rectangle(Game.centerWidth - 100, Game.centerHeight - 150, 200, 50), Color.white, selectedMap.toString(), ttf);
		
		changeScoreLimit = false;
		justChanged = false;
		
		scoreLimitButton = new MenuButton("score", new Rectangle(Game.centerWidth - 100, Game.centerHeight - 250, 200, 50), new Color(0, 0, 0, 0), "Score limit: " + scoreLimit, ttf);
		oldColor = scoreLimitButton.getBackColor();
		
		
		
		buttons.add(okButton);
		buttons.add(backButton);
		buttons.add(mapButton);
		buttons.add(scoreLimitButton);
		//-------------------
		
		f = new Font("Comic Sans", Font.ITALIC, 50);
		ttf = new TrueTypeFont(f, true);
	
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		
		FontUtils.drawCenter(ttf, "Game Settings", Game.centerWidth - 300, Game.centerHeight/3, 600);
		
		for (MenuButton button : buttons) {
			button.render(gc, sb, g);
		}
		
	}
	public GameMap getMap(){
		return selectedMap;
		
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
		
		if (okButton.isMousePressed()) {
			Game.LASTID = getID();
			changeScoreLimit = false;
			scoreLimitButton.setBackColor(oldColor);
			scoreLimitButton.setText("Score limit: " + scoreLimit);
			InGameState.finished = true;
			sb.getState(Game.State.INGAMESTATE.ordinal()).init(gc, sb);
			((InGameState) sb.getState(Game.State.INGAMESTATE.ordinal())).setScoreLimit(scoreLimit);
			Game.INGAME_MUSIC.loop();
			Game.INGAME_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
			sb.enterState(Game.State.INGAMESTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		if (input.isKeyPressed(Input.KEY_ESCAPE) || backButton.isMousePressed()) {
			changeScoreLimit = false;
			scoreLimitButton.setBackColor(oldColor);
			scoreLimitButton.setText("Score limit: " + scoreLimit);
			sb.enterState(Game.State.MENUSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		if (mapButton.isMousePressed()){
			changeScoreLimit = false;
			scoreLimitButton.setBackColor(oldColor);
			scoreLimitButton.setText("Score limit: " + scoreLimit);
			selectedMap = (GameMap) maplist.poll();
			maplist.addLast(selectedMap);
			mapButton.setText(selectedMap.toString());
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
				int oldscore = scoreLimit;
				try {
					scoreLimit = Integer.parseInt(scoreLimitButton.getText()) < 1 ? 1 : Integer.parseInt(scoreLimitButton.getText());
				}
				catch(NumberFormatException e) {
					scoreLimit = oldscore;
				}
				scoreLimitButton.setText("Score limit: " + scoreLimit);
			}
			else if(key == Input.KEY_ESCAPE) {
				changeScoreLimit = false;
				justChanged = true;
				scoreLimitButton.setText("Score limit: " + scoreLimit);
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

package gamestates;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import game.AnchorMap;
import game.Game;
import game.GameMap;
import game.MenuButton;
import game.RandomFunkyMap;

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
	private final int ID;
	
	private ArrayList<MenuButton> buttons;
	private MenuButton okButton, backButton, mapButton;
	private TrueTypeFont ttf;
	private GameMap selectedMap;
	private LinkedList<GameMap> maplist;
	
	public BeforeGameState(int id) {
		ID = id;
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
		
		
		buttons.add(okButton);
		buttons.add(backButton);
		buttons.add(mapButton);
		//-------------------
		
		f = new Font("Comic Sans", Font.ITALIC, 50);
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
	public GameMap getMap(){
		return selectedMap;
		
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
			sb.getState(Game.State.INGAMESTATE.ordinal()).init(gc, sb);
			Game.INGAME_MUSIC.loop();
			Game.INGAME_MUSIC.setVolume(AudioSettingsState.MUSIC_LEVEL*AudioSettingsState.MASTER_LEVEL);
			sb.enterState(Game.State.INGAMESTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		if (input.isKeyPressed(Input.KEY_ESCAPE) || backButton.isMousePressed()) {
			sb.enterState(Game.State.MENUSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		if (mapButton.isMousePressed()){
			selectedMap = (GameMap) maplist.poll();
			maplist.addLast(selectedMap);
			mapButton.setText(selectedMap.toString());
		}

	}

	@Override
	public int getID() {
		return ID;
	}

}

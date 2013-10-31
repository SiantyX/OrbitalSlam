package gamestates;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import game.Game;
import game.MenuButton;
import game.Player;

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

public class AfterGameState extends BasicGameState implements Comparator<Player> {
	private final int ID;
	private TrueTypeFont bigText;
	private TrueTypeFont scoreFont;
	private ArrayList<Player> scoreList;
	
	private ArrayList<MenuButton> buttons;
	private MenuButton playButton, backButton;
	
	public AfterGameState(int id) {
		ID = id;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {
		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		bigText = new TrueTypeFont(f, true);
		
		f = new Font("Arial", Font.BOLD, 20);
		scoreFont = new TrueTypeFont(f, true);
		
		scoreList = new ArrayList<Player>();
		for(Player player : InGameState.players) {
			scoreList.add(player);
		}
		Collections.sort(scoreList, this);
		
		f = new Font("Arial", Font.PLAIN, 18);
		TrueTypeFont ttf = new TrueTypeFont(f, true);
		
		playButton = new MenuButton("play", new Rectangle(Game.centerWidth - 250, Game.centerHeight + 200, 200, 50), Color.white, "Play Again", ttf);
		backButton = new MenuButton("back", new Rectangle(Game.centerWidth + 50, Game.centerHeight + 200, 200, 50), Color.white, "Back to Menu", ttf);
		
		buttons = new ArrayList<MenuButton>();
		buttons.add(playButton);
		buttons.add(backButton);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		
		sb.getState(Game.State.INGAMESTATE.ordinal()).render(gc, sb, g);
		
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		for(MenuButton b : buttons) {
			b.render(gc, sb, g);
		}
		
		FontUtils.drawCenter(bigText, scoreList.get(0).toString() + " Wins!", Game.centerWidth - 200, 150, 400, scoreList.get(0).myColor);
		
		for(int i = 0; i < scoreList.size(); i++) {
			FontUtils.drawCenter(scoreFont, (i+1) + ".  " + scoreList.get(i).toString() + "                     " + scoreList.get(i).getScore(), Game.centerWidth - 200, 300 + (i * ((Game.HEIGHT - 600) / (Game.MAX_PLAYERS-1))), 400, scoreList.get(i).myColor);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		for(MenuButton b : buttons) {
			b.update(gc, sb, delta);
		}
		
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE) || backButton.isMousePressed()) {
			Game.LASTID = getID();
			sb.enterState(Game.State.MENUSTATE.ordinal(), new FadeOutTransition(Color.black, 100), new FadeInTransition(Color.black,
					100));
		}
		
		if (playButton.isMousePressed()) {
			Game.LASTID = getID();
			InGameState.finished = true;
			sb.getState(Game.State.INGAMESTATE.ordinal()).init(gc, sb);
			Game.INGAME_MUSIC.play();
			sb.enterState(Game.State.INGAMESTATE.ordinal());
		}
	}

	@Override
	public int compare(Player p1, Player p2) {		
		if(p1.getScore() < p2.getScore()) {
			return 1;
		}
		else if(p1.getScore() > p2.getScore()) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
	
	@Override
	public int getID() {
		return ID;
	}
}
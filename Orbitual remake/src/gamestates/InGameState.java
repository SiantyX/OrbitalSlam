package gamestates;

import game.AnchorMap;
import game.Entity;
import game.Game;
import game.Player;

import java.awt.Font;
import java.util.ArrayList;

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

import components.*;

public class InGameState extends BasicGameState {

	public static final int ID = 1;
	private AnchorMap map;
	private ArrayList<Player> players;
	private int numLocalPlayers;
	
	public static boolean finished = true;
	
	private TrueTypeFont ttf;
	
	private ArrayList<Player> playersAlive;

	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		if(!finished) {
			reInit(gc, sb);
			return;
		}
		
		playersAlive = new ArrayList<Player>();
		map = new AnchorMap();
		players = new ArrayList<Player>();
		
		numLocalPlayers = 1;
		if(numLocalPlayers > map.getNumPlayers()) numLocalPlayers = map.getNumPlayers();
		
		Player.anchorList = map.getEntities();
		// players
		for(int i = 0; i < numLocalPlayers; i++) {
			Player p = new Player(i, map);
			p.KEYBIND = ControlsSettingsState.KEYBINDS[i];
			players.add(p);
			playersAlive.add(players.get(i));
		}
		
		// font for winner
		Font f = new Font("Comic Sans", Font.ITALIC, 50);
		ttf = new TrueTypeFont(f, true);
		
		finished = false;
		
		DisplayModeState.OLD_WIDTH = Game.WIDTH;
		DisplayModeState.OLD_HEIGHT = Game.HEIGHT;
	}
	
	/**
	 * Runs if the game is initiated when not finished.
	 * Happens if you change resolution.
	 * 
	 * @param gc
	 * @param sb
	 * @throws SlickException
	 */
	private void reInit(GameContainer gc, StateBasedGame sb) throws SlickException {
		for(Player player : players) {
			Entity e = player.getEntity();
			Vector2f v = new Vector2f(e.getPosition().x/DisplayModeState.OLD_WIDTH * Game.WIDTH, e.getPosition().y/DisplayModeState.OLD_HEIGHT * Game.HEIGHT);
			e.setPosition(v);
			e.setScale(Player.stdScale*Game.WIDTH);
		}
		
		ArrayList<Entity> anchors = map.getEntities();
		for(Entity e : anchors) {
			Vector2f v = new Vector2f(e.getPosition().x/DisplayModeState.OLD_WIDTH * Game.WIDTH, e.getPosition().y/DisplayModeState.OLD_HEIGHT * Game.HEIGHT);
			e.setPosition(v);
		}
		
		for(int i = 0; i < numLocalPlayers; i++) {
			players.get(i).KEYBIND = ControlsSettingsState.KEYBINDS[i];
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		map.render(gc, sb, g);
		
		if(players.isEmpty()) return;
		for(Player player : players) {
			player.render(gc, sb, g);
		}
		
		if(finished) {
			g.setColor(new Color(0, 0, 0, 125));
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.setColor(Color.white);
			if(playersAlive.size() < 1) {
				ttf.drawString(Game.centerWidth - 130, Game.centerHeight - 42, "It's a Draw!");
			}
			else {
				ttf.drawString(Game.centerWidth - 130, Game.centerHeight - 42, playersAlive.get(0).toString() + " Wins!");
			}
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		if(finished) return;
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			Game.LASTID = getID();
			finished = false;
			sb.enterState(PauseMenuState.ID);
		}
		
		if(players.isEmpty()) return;
		if(!playersAlive.isEmpty()) {
			ArrayList<Player> tmpPlayers = new ArrayList<Player>();
			for(Player player : playersAlive) {
				tmpPlayers.add(player);
			}
			for(Player player : tmpPlayers) {
				if(player.isDead()) {
					playersAlive.remove(player);
				}
				else {
					player.update(gc, sb, delta);
				}
			}
		}
		
		if(playersAlive.size() == 1 && numLocalPlayers > 1) {
			// player wins
			// playersAlive.get(0)
			Game.LASTID = getID();
			finished = true;
			sb.enterState(MenuState.ID, new FadeOutTransition(Color.black, 2000), new FadeInTransition(Color.black,
					2000));
		}
		else if(playersAlive.size() < 1) {
			// draw should not happen
			// happens when playing alone
			Game.LASTID = getID();
			finished = true;
			sb.enterState(MenuState.ID, new FadeOutTransition(Color.black, 2000), new FadeInTransition(Color.black,
					2000));
		}
	}
	
	private boolean collisionCircle(Entity e1, Entity e2) {
		float radii = e1.getRadius() + e2.getRadius();
		float dx = e2.getPosition().x + e2.getRadius() - e1.getPosition().x - e1.getRadius();
		float dy = e2.getPosition().y + e2.getRadius() - e1.getPosition().y - e1.getRadius();
		if( dx * dx + dy * dy < radii * radii){
			return true;
		}
		return false;
	}

	@Override
	public int getID() {
		return ID;
	}
}

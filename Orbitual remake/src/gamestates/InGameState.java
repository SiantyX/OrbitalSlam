package gamestates;

import game.AnchorMap;
import game.Entity;
import game.Game;
import game.Player;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
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

	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		map = new AnchorMap();
		players = new ArrayList<Player>();
		
		numLocalPlayers = 2;
		if(numLocalPlayers > map.getNumPlayers()) numLocalPlayers = map.getNumPlayers();
		
		// players
		for(int i = 0; i < numLocalPlayers; i++) {
			//startPosX + (i%numAncPerRow) * (((Game.WIDTH-(2*startPosX))/(numAncPerRow-1))
			Vector2f v = new Vector2f(map.getStartPosX() + (i) * (((Game.WIDTH-(2*map.getStartPosX()))/(map.getNumAncPerRow()-1))) - Game.WIDTH/14, map.getStartPosY() - Game.HEIGHT/10);
			players.add(new Player(i, v));
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
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			sb.enterState(MenuState.ID);
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

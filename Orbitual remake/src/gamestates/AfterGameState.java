package gamestates;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class AfterGameState extends BasicGameState {
	public static final int ID = 6;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb)
			throws SlickException {

		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		
		sb.getState(InGameState.ID).render(gc, sb, g);
		
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		
		
	}

	@Override
	public int getID() {
		return ID;
	}
	
	
}
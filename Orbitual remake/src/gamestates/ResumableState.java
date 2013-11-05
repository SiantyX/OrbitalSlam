package gamestates;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class ResumableState extends BasicGameState {
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		if(Game.UPDATE_BACKGROUND > 0) {
			sb.getState(Game.UPDATE_BACKGROUND).render(gc, sb, g);
			g.setColor(new Color(0, 0, 0, 125));
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		if(Game.UPDATE_BACKGROUND > 0) {
			sb.getState(Game.UPDATE_BACKGROUND).update(gc, sb, delta);
		}
	}
}

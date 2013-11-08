package gamestates;

import game.Game;
import game.Player;
import game.QuadTree;
import game.ViewPort;
import game.maps.GameMap;
import game.maps.interactables.Interactable;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class InGameState extends AbstractInGameState {

	public InGameState(int id) {
		super(id);
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.init(gc, sb);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		Game.UPDATE_BACKGROUND = 0;
		
		super.update(gc, sb, delta);
	}

	@Override
	protected void specificInit(GameContainer gc, StateBasedGame sb) {
		for(int i = 0; i < numberOfPlayers(); i++) {
			players.get(i).KEYBIND = ((ControlsSettingsState)sb.getState(Game.State.CONTROLSSETTINGSSTATE.ordinal())).getKeyBinds()[i];
		}
		
		qt = new QuadTree(0, map.getBounds());
	}

	@Override
	protected int numberOfPlayers() {
		return numLocalPlayers;
	}

	@Override
	protected Player createPlayer(int i, GameMap map) throws SlickException {
		return new Player(i, map);
	}

	@Override
	protected void collisionCheck() {
		qt.clear();
		ArrayList<Interactable> allInters = getAllInteractables();
		for(Interactable i : allInters) {
			qt.insert(i);
		}
		ArrayList<Interactable> rObj = new ArrayList<Interactable>();
		for(Interactable i : allInters) {
			rObj.clear();
			qt.retrieve(rObj, i);
			
			for(Interactable j : rObj) {
				if(i.collisionCheck(j)) {
					i.collision(j);
				}
				
			}
		}
	}

	@Override
	protected void close() {
	}

	@Override
	protected void updatePlayer(GameContainer gc, StateBasedGame sb, int delta,
			ViewPort vp, Player player) {
		player.update(gc, sb, delta, vp);
	}
}

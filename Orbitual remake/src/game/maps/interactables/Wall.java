package game.maps.interactables;

import java.util.ArrayList;

import game.Game;
import game.Player;
import gamestates.InGameState;

import org.newdawn.slick.state.StateBasedGame;

public class Wall extends Interactable{

	public Wall(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void collisionCheck(StateBasedGame sb) {

		ArrayList<Player> playerlist = new ArrayList<Player>();
		playerlist = ((InGameState)sb.getState(Game.State.INGAMESTATE.ordinal())).getPlayers();

		for (Player e : playerlist){
			if (this.collisionSquare(e.getEntity())){
				collision(e);
			}
		}
		
	}

	@Override
	public void collision(Player player) {
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}

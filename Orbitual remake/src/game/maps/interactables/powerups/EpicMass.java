package game.maps.interactables.powerups;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import components.ImageRenderComponent;

import game.Game;
import game.Player;
import gamestates.InGameState;

public class EpicMass extends PowerUp {
	private ImageRenderComponent epicMassRender;
	protected static final String epicMassPath = "res/sprites/interactables/epicmass.png";
	protected boolean exists = true;

	public EpicMass(String id) throws SlickException {
		super(id);
		epicMassRender = new ImageRenderComponent("Epic Mass", new Image(epicMassPath));
		this.addComponent(epicMassRender);
	}

	@Override
	public void collisionCheck(StateBasedGame sb) {
		ArrayList<Player> playerlist = new ArrayList<Player>();
		playerlist = ((InGameState) sb.getState(Game.State.INGAMESTATE
				.ordinal())).getPlayers();
		
		for (Player e : playerlist) {
			if (this.collisionCircle(e)) {
				collision(e);
			}
		}

	}

	@Override
	public void collision(Player player) {
		if (exists == true)
			player.setMass(player.getMass()*2);
		exists = false;
		this.clear();
		

	}

	@Override
	public void reset() {
		exists = true;
		this.changeImage(epicMassRender);
		

	}

	@Override
	public void powerDown(Player player) {
		player.setMass(player.getMass()/2);
		
	}
	
	
}

package game.maps;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.ImageRenderComponent;
import game.Entity;
import game.Game;
import game.Player;
import gamestates.AudioSettingsState;
import gamestates.BeforeGameState;
import gamestates.InGameState;


public class Mine extends Interactible {
	private Image img;
	private int mass;
	public Mine() throws SlickException{
		super("Mine");
		this.setRadius(50);
		this.img = new Image(GameMap.minePath);
		ImageRenderComponent c = new ImageRenderComponent("Mine ", img);
		this.AddComponent(c);
	}

	public void collisionCheck(StateBasedGame sb) {
		ArrayList<Player> playerlist = new ArrayList<Player>();
		playerlist = ((InGameState)sb.getState(Game.State.INGAMESTATE.ordinal())).getPlayers();
	
		for (Player e : playerlist){
			this.collisionCircle(e.getEntity());
		}
			
		}

	@Override
	public void collision(Player player) {
		player.die();
	
	}


}

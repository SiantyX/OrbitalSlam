package game.maps.interactables;

import java.util.ArrayList;

import game.Entity;
import game.Game;
import game.Player;
import gamestates.InGameState;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.ImageRenderComponent;

public class Brick extends Interactable{
	protected static final float bounciness = .02f;
	protected static final String brickPath = "res/sprites/interactables/brick.png";
	private Image img;
	protected final static float Height = 200;
	

	public Brick(String id) throws SlickException {
		super(id);
		this.img = new Image(brickPath);
		ImageRenderComponent brick = new ImageRenderComponent("Brick",img);
		this.AddComponent(brick);
		setHeight(Height);
		
	}

	@Override
	public void collisionCheck(StateBasedGame sb) {

		ArrayList<Player> playerlist = new ArrayList<Player>();
		playerlist = ((InGameState)sb.getState(Game.State.INGAMESTATE.ordinal())).getPlayers();

		for (Player e : playerlist){
			if (this.collisionRectangle(e.getEntity())){
				collision(e);
			}
		}
		
	}

	@Override
	public void collision(Player player) {

		Entity entity = this;
		
		float degreeOfImpactX = (entity.getCenterPosition().x - player.getEntity().getCenterPosition().x);
		float degreeOfImpactY = (entity.getCenterPosition().y - player.getEntity().getCenterPosition().y);
		
		float directionX = - degreeOfImpactX*bounciness;
		float directionY = - degreeOfImpactY*bounciness;


		Vector2f v = new Vector2f(directionX,directionY);
		player.setHooked(false);

		player.setVelocity(v);
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}

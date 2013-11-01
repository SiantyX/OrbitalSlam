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

public class Brick extends Interactable {
	protected static final float bounciness = .01f;
	protected static final String brickPath = "res/sprites/interactables/brick.png";
	private Image img;
	protected final static float Height = 200;

	public Brick(String id) throws SlickException {
		super(id);
		this.img = new Image(brickPath);
		ImageRenderComponent brick = new ImageRenderComponent("Brick", img);
		this.AddComponent(brick);
		setHeight(Height);
		setRelativeHeight(2);
		setRelativeWidth(2);

	}

	@Override
	public void collisionCheck(StateBasedGame sb) {

		ArrayList<Player> playerlist = new ArrayList<Player>();
		playerlist = ((InGameState) sb.getState(Game.State.INGAMESTATE
				.ordinal())).getPlayers();

		for (Player e : playerlist) {
			if (this.collisionRectangle(e.getEntity())) {
				collision(e);
			}
		}

	}
	

	@Override
	public void collision(Player player) {

		boolean hitTopOrBottom = (Math.abs(this.getCenterPositionRectangle().x - player.getEntity().getCenterPosition().x)) < (this.getWidth()/2 -5f + player.getEntity().getRadius());
		
		float degreeOfImpactX = (this.getCenterPositionRectangle().x - player
				.getEntity().getCenterPosition().x);
		float degreeOfImpactY = (this.getCenterPosition().y - player
				.getEntity().getCenterPosition().y);

		float directionX;
		float directionY;
		if (hitTopOrBottom) {
			directionY = -degreeOfImpactY * bounciness;
			player.setDy(directionY);

		} else {
			directionX = -degreeOfImpactX * bounciness;
			player.setDx(directionX);
		}

		// Vector2f v = new Vector2f(directionX, directionY);
		player.setHooked(false);

		// player.setVelocity(v);

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}

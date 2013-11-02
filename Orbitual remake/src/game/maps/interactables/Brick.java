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
	protected static final String brickPath = "res/sprites/interactables/brick.png";
	private Image img;
	private final float bounciness = 0.9f;

	public Brick(String id) throws SlickException {
		super(id);
		this.img = new Image(brickPath);
		ImageRenderComponent brick = new ImageRenderComponent("Brick", img);
		this.AddComponent(brick);

	}
	
	public void setScaleWidth(float a){
		this.getComponent("Brick").setScaleWidth(a);
	}
	public void setScaleHeight(float a ){
		this.getComponent("Brick").setScaleHeight(a);
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

		boolean hitTopOrBottom = (Math.abs(this.getCenterPositionRectangle().x
				- player.getEntity().getCenterPosition().x)) < (this.getWidth() / 2 - 10f + player
				.getEntity().getRadius());

		float directionX;
		float directionY = 0;
		if (hitTopOrBottom) {

			if (player.getVelocity().y < 0.5f && player.getVelocity().y > -0.5f)
				directionY = -0.5f;
			else {
				directionY = -player.getVelocity().y * bounciness;

			}

			player.setDy(directionY);

		} else {
			directionX = -player.getVelocity().x * bounciness;
			player.setDx(directionX);
		}

		player.setHooked(false);

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}

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
	private final float bounciness = 0.9f;
	private final float slipperyness = 0.95f;
	private Image img;

	public Brick(String id) throws SlickException {
		super(id);
		this.img = new Image(brickPath);
		ImageRenderComponent brick = new ImageRenderComponent("Brick", img);
		this.addComponent(brick);

	}

	public void setScaleWidth(float a) {
		this.getComponent("Brick").setScaleWidth(a);
	}

	public void setScaleHeight(float a) {
		this.getComponent("Brick").setScaleHeight(a);
	}

	@Override
	public boolean collisionCheck(Interactable inter) {
		return collisionRectangle(inter);
		
		/*
		ArrayList<Player> playerlist = new ArrayList<Player>();
		playerlist = ((InGameState) sb.getState(Game.State.INGAMESTATE
				.ordinal())).getPlayers();

		for (Player e : playerlist) {
			if (this.collisionRectangle(e)) {
				collision(e);
			}
		}*/
	}

	@Override
	public void collision(Interactable inter) {
		if(inter instanceof Player) {
			Player player = (Player) inter;

			boolean hitTop = (Math.abs(this.getCenterPositionRectangle().x
					- player.getCenterPosition().x) < (this.getWidth() / 2 - 15f + player.getRadius()) && (player.getCenterPosition().y < this.getCenterPositionRectangle().y));

			boolean hitBottom = (Math.abs(this.getCenterPositionRectangle().x
					- player.getCenterPosition().x) < (this.getWidth() / 2 - 15f + player.getRadius()) && (player.getCenterPosition().y > this.getCenterPositionRectangle().y));

			boolean hitLeft = (!hitTop && !hitBottom && this.getCenterPositionRectangle().x < player.getCenterPosition().x);

			float directionX;
			float directionY;

			if (hitTop) {

				if (player.getVelocity().y < 0.5f && player.getVelocity().y > -1f) {
					directionY = -.3f;
				} else {
					directionY = -Math.abs(player.getVelocity().y * bounciness);
				}
				directionX = player.getVelocity().x * slipperyness;

				player.setDy(directionY);
				player.setDx(directionX);
				player.rest();



			} else if (hitBottom) {
				directionY = Math.abs(player.getVelocity().y * bounciness);
				directionX = player.getVelocity().x * bounciness;

				player.setDy(directionY);
				player.setDx(directionX);

			} else if (hitLeft) {
				directionX = Math.abs(player.getVelocity().x * bounciness);
				player.setDx(directionX);

			}

			else {
				directionX = -Math.abs(player.getVelocity().x * bounciness);
				player.setDx(directionX);

			}

			player.setHooked(false);

		}
	}


	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}

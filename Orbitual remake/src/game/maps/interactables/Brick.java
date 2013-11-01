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
	private float brickPowerAbsorption = 0;
	protected static final String brickPath = "res/sprites/interactables/brick.png";
	private Image img;

	public Brick(String id) throws SlickException {
		super(id);
		this.img = new Image(brickPath);
		ImageRenderComponent brick = new ImageRenderComponent("Brick", img);
		this.AddComponent(brick);

	}
	
	public void MultiplyHeightBy(int i){
		setRelativeHeight(i);
	}
	public void MultiplyWidthBy(int i){
		setRelativeWidth(i);
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
		float directionY;
		if (hitTopOrBottom) {
			directionY = -player.getVelocity().y + brickPowerAbsorption;
			player.setDy(directionY);
		} else {
			directionX = -player.getVelocity().x;
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

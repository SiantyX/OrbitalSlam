package game.maps.interactables;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.ImageRenderComponent;
import game.Entity;
import game.Game;
import game.Player;
import game.maps.GameMap;
import gamestates.AudioSettingsState;
import gamestates.BeforeGameState;
import gamestates.InGameState;

public class Mine extends Interactable {
	private boolean detonated;
	private Image img;
	private Image noImg;
	private int mass;
	private float SPEED_LOST = 0.6f;
	protected final static String minePath = "res/sprites/mine.png";
	protected final static String emptyPath ="res/sprites/empty.png";
	private float power;
	ImageRenderComponent realMine;
	ImageRenderComponent invMine;

	public Mine() throws SlickException{
		
		super("Mine");
		detonated = false;
		this.setRadius(100);
		
		this.img = new Image(minePath);
		this.noImg = new Image(emptyPath);
		realMine = new ImageRenderComponent("Mine", img);
		invMine = new ImageRenderComponent("Empty", noImg);
		this.AddComponent(realMine);
		this.AddComponent(invMine);
		
		setScale((float) 0.2);
		this.mass = 100;
		this.power =(float) .3;
		
	}

	public void reset(){
		detonated = false;
		this.changeImage(realMine);

	}

	public void collisionCheck(StateBasedGame sb) {
		ArrayList<Player> playerlist = new ArrayList<Player>();
		playerlist = ((InGameState)sb.getState(Game.State.INGAMESTATE.ordinal())).getPlayers();

		for (Player e : playerlist){
			if (this.collisionCircle(e.getEntity())){
				collision(e);
			}
		}
	}

	@Override
	public void collision(Player player) {
		
		if (detonated)
			return;
		
		Entity entity = this;
		
		float degreeOfImpactX = (entity.getCenterPosition().x - player.getEntity().getCenterPosition().x);
		float degreeOfImpactY = (entity.getCenterPosition().y - player.getEntity().getCenterPosition().y);
		
		float directionX = - degreeOfImpactX*power;
		float directionY = - degreeOfImpactY*power;


		Vector2f v = new Vector2f(directionX,directionY);
		detonated = true;
		player.setHooked(false);
		this.changeImage(invMine);
		player.setStunTime(200);

		player.setVelocity(v);
		
	}





}

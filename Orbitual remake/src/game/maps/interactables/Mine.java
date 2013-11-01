package game.maps.interactables;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.ImageRenderComponent;
import game.Entity;
import game.Game;
import game.Player;
import gamestates.AudioSettingsState;
import gamestates.InGameState;

public class Mine extends Interactable {
	private boolean detonated;
	private Image img;
	protected final static String minePath = "res/sprites/interactables/mine.png";
	protected final static String mineSound = "res/audio/sound/explosion.ogg";
	protected final static float power = .3f;
	protected final static float radius = 100;
	private Sound sound;
	
	ImageRenderComponent realMine;

	public Mine() throws SlickException{
		
		super("Mine");
		detonated = false;
		this.setRadius(radius);
		
		this.img = new Image(minePath);
		realMine = new ImageRenderComponent("Mine", img);
		this.AddComponent(realMine);
		
		setScale(scale);
		
		sound = new Sound(mineSound);
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
		
		this.clear();
		player.setStunTime(200);

		player.setVelocity(v);
		
		sound.play(1, AudioSettingsState.SOUND_LEVEL*AudioSettingsState.MASTER_LEVEL);
	}





}

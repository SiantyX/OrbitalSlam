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
		// ------------------------------------------------------------------------------------------------------
				// TAKEN FROM http://stackoverflow.com/questions/345838/ball-to-ball-collision-detection-and-handling user Simucal
				// ------------------------------------------------------------------------------------------------------
				
				Entity entity = this;
				Vector2f delta = new Vector2f(entity.getCenterPosition().x - player.getEntity().getCenterPosition().x, entity.getCenterPosition().y - player.getEntity().getCenterPosition().y);
				float r = entity.getRadius() + player.getEntity().getRadius();
				float dist2 = delta.dot(delta);

				if(dist2 > r*r) return;

				float d = delta.length();

				Vector2f mtd;
				if(d == 0.0f) {
					d = player.getEntity().getRadius() + entity.getRadius() - 1;
					delta = new Vector2f(player.getEntity().getRadius() + entity.getRadius(), 0);
				}

				mtd = new Vector2f(delta.x * (((entity.getRadius() + player.entity.getRadius()) - d)/d), delta.y * (((entity.getRadius() + player.entity.getRadius()) - d)/d));

				float im1 = (float) (1 / this.getMass());
				float im2 = (float) (1 / player.getMass());

				Vector2f mtdScaled1 = new Vector2f(mtd.x * (im1 / (im1 + im2)), mtd.y * (im1 / (im1 + im2)));
				Vector2f mtdScaled2 = new Vector2f(mtd.x * (im2 / (im1 + im2)), mtd.y * (im2 / (im1 + im2)));
				entity.setCenterPosition(new Vector2f(entity.getCenterPosition().x + mtdScaled1.x, entity.getCenterPosition().y + mtdScaled1.y));
				player.getEntity().setCenterPosition(new Vector2f(player.getEntity().getCenterPosition().x + mtdScaled2.x, player.getEntity().getCenterPosition().y + mtdScaled2.y));

				Vector2f v = new Vector2f((float)dx - player.getVelocity().x, (float)dy - player.getVelocity().y);

				float vn = v.dot(mtd.normalise());

				if(vn > 0.0f) return;

				// impulse
				float i = (-(1.0f + SPEED_LOST) * vn) / (im1 + im2);
				Vector2f impulse = new Vector2f(mtd.x * i, mtd.y * i);

				// momentum
				Vector2f dim1 = new Vector2f(impulse.x * im1, impulse.y * im1);
				Vector2f dim2 = new Vector2f(impulse.x * im2, impulse.y * im2);
				Vector2f newV = new Vector2f((float)dx + dim1.x, (float)dy + dim1.y);
				Vector2f otherNewV = new Vector2f(player.getVelocity().x - dim2.x, player.getVelocity().y - dim2.y);
				// ------------------------------------------------------------------------------------------------------
				// ------------------------------------------------------------------------------------------------------

				hooked = false;
				player.setHooked(false);

				double dSpeed = 0;
				double deg1 = Math.atan2(player.entity.getCenterPosition().y - entity.getCenterPosition().y, 
						-(player.entity.getCenterPosition().x - entity.getCenterPosition().x)) * 180/Math.PI;
				
				double deg2 = Math.atan2(entity.getCenterPosition().y - player.entity.getCenterPosition().y, 
						-(entity.getCenterPosition().x - player.entity.getCenterPosition().x)) * 180/Math.PI;
				if(getDegSpeed(deg1) < player.getDegSpeed(deg2)) {
					// this stunnad
					// stunduration i skillnad
					dSpeed = Math.abs(getDegSpeed(deg1) - player.getDegSpeed(deg2));
					stunTime = dSpeed*STUN_LENGTH;
				}
				else if(getDegSpeed(deg1) > player.getDegSpeed(deg2)){
					// player stunnad
					// stunduration i skillnad
					dSpeed = Math.abs(getDegSpeed(deg1) - player.getDegSpeed(deg2));
					player.setStunTime(dSpeed*STUN_LENGTH);
				}
				player.setVelocity(otherNewV);
				
				//sound.play(1, AudioSettingsState.SOUND_LEVEL*AudioSettingsState.MASTER_LEVEL);
		
	}


}

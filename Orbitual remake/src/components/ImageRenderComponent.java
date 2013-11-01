package components;

import game.Game;
import game.ViewPort;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class ImageRenderComponent extends Component {
	public ImageRenderComponent(String id, Image image) {
		super(id);
		this.currentImage = image;
		scale = 1;
		rotation = 0;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		Vector2f pos = owner.getPosition();

		currentImage.draw(pos.x, pos.y, scale);
		if(Game.showHitbox) {
			gr.setColor(Color.red);
			gr.drawOval(pos.x, pos.y, currentImage.getWidth(), currentImage.getHeight());
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr, ViewPort vp) {
		Vector2f pos = owner.getPosition();

		pos = vp.toRelative(pos);
		
		currentImage.draw(pos.x, pos.y, scale);
		if(Game.showHitbox) {
			gr.setColor(Color.red);
			gr.drawOval(pos.x, pos.y, currentImage.getWidth(), currentImage.getHeight());
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		currentImage.rotate(owner.getRotation() - currentImage.getRotation());
	}
}

package components;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class ImageRenderComponent extends RenderComponent {

	private Image image;

	public ImageRenderComponent(String id, Image image) {
		super(id);
		this.image = image;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		Vector2f pos = owner.getPosition();
		float scale = owner.getScale();

		image.draw(pos.x, pos.y, scale);
		if(game.hitBox){
			gr.setColor(Color.red);
			gr.drawOval(pos.x, pos.y, owner.getRadius()*2, owner.getRadius()*2);
		}

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {

		image.rotate(owner.getRotation() - image.getRotation());
	}
	
	public float getRadius(){
		return image.getWidth()/2;
	}
	
	public Image getImage() {
		return image;
	}
}

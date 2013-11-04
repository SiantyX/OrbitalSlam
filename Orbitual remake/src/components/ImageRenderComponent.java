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
	protected Vector2f pos;
	
	public ImageRenderComponent(String id, Image image) {
		super(id);
		this.currentImage = image;
		scale = 1;
		rotation = 0;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		Vector2f pos = owner.getPosition();

		currentImage.draw(pos.x, pos.y, getWidth(), getHeight());
		if(Game.showHitbox) {
			gr.setColor(Color.red);
			gr.drawOval(pos.x, pos.y, getWidth(), getHeight());
			gr.drawRect(pos.x, pos.y, getWidth(), getHeight());
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr, ViewPort vp) {
		Vector2f pos = owner.getPosition();
		
		pos = vp.toRelative(pos);
		
		currentImage.setCenterOfRotation(getWidth()*vp.getZoom()/2, getHeight()*vp.getZoom()/2);
		currentImage.rotate(owner.getAllRotation() - currentImage.getRotation());
		
		currentImage.draw(pos.x, pos.y, getWidth()*vp.getZoom(), getHeight()*vp.getZoom());
		if(Game.showHitbox) {
			gr.setColor(Color.red);
			gr.drawOval(pos.x, pos.y, getWidth()*vp.getZoom(), getHeight()*vp.getZoom());
			gr.drawRect(pos.x, pos.y, getWidth()*vp.getZoom(), getHeight()*vp.getZoom());
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		pos = owner.getPosition();

		currentImage.rotate(owner.getAllRotation() - currentImage.getRotation());
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, ViewPort vp) {
		Vector2f tmp = owner.getPosition();
		
		pos = vp.toRelative(tmp);
		
		currentImage.setCenterOfRotation(getWidth()*vp.getZoom()/2, getHeight()*vp.getZoom()/2);
		currentImage.rotate(owner.getAllRotation() - currentImage.getRotation());
	}
}

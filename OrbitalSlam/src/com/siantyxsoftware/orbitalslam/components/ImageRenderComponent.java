package com.siantyxsoftware.orbitalslam.components;

import com.siantyxsoftware.framework.Game;
import com.siantyxsoftware.framework.Graphics;
import com.siantyxsoftware.framework.Image;

public class ImageRenderComponent extends RenderComponent {
	//private Matrix matrix;
	private Image image;

	public ImageRenderComponent(String id, Image image) {
		super(id);
		this.image = image;
	}

	@Override
	public void render(Game game, float delta) {
		Vector2f pos = owner.getPosition();
		if(image.getScale() != owner.getScale()) {
			image.setScale(owner.getScale());
		}
		
		Graphics g = game.getGraphics();
		g.drawImage(image, Math.round(pos.x), Math.round(pos.y)); // and scale
		/*if(oldgame.showHitbox){
			gr.setColor(Color.red);
			gr.drawOval(pos.x, pos.y, owner.getRadius()*2, owner.getRadius()*2);
		}*/
	}

	@Override
	public void update(Game game, float delta) {

		//image.rotate(owner.getRotation() - image.getRotation());
	}
	
	public float getRadius(){
		return image.getWidth()/2;
	}
	
	public Image getImage() {
		return image;
	}
	
	
}

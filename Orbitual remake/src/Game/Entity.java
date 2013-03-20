package Game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;

public class Entity {
	public static ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	private Image im;
	private Shape sh;
	private ShapeFill sf;
	private Color c;
	
	private int x,y;
	
	/**
	 * Adds entity to entityList.
	 */
	public Entity() {
		entityList.add(this);
		x = y = 0;
	}
	
	/**
	 * Creates an entity that renders a sprite.
	 * @param im Image to render
	 */
	public Entity(Image im, int x, int y) {
		this();
		this.im = im;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates an entity that renders a shape.
	 * @param sh Shape to render filled with standard color or color specified later.
	 */
	public Entity(Shape sh) {
		this();
		this.sh = sh;
	}
	
	/**
	 * Creates an entity that renders a shape filled with the specified color.
	 * @param sh Shape to render
	 * @param c Color to fill the shape with
	 */
	public Entity(Shape sh, Color c) {
		this();
		this.sh = sh;
		this.c = c;
	}
	
	/**
	 * Creates an entity that renders a shape with the specified ShapeFill.
	 * @param sh Shape to render
	 * @param sf ShapeFill to fill the Shape with
	 */
	public Entity(Shape sh, ShapeFill sf) {
		this();
		this.sh = sh;
		this.sf = sf;
	}
	
	/**
	 * Creates an entity that render a shape with the specified image as texture.
	 * @param sh Shape to render
	 * @param im Image to fill the Shape with
	 */
	public Entity(Shape sh, Image im) {
		this();
		this.sh = sh;
		this.im = im;
	}
	

	/**
	 * Renders the entity in different ways depending on the content of the entity.
	 * @param g Graphics to draw to
	 */
	public void render(Graphics g) {
		// sets color before doing anything
		if(c != null)
			g.setColor(c);
		else
			g.setColor(Color.orange);
		// -------------------------------
		
		if(sh != null) {
			if(sf != null)
				g.fill(sh, sf);
			else if(im != null)
				g.texture(sh, im);
			
			g.fill(sh);
		}
		
		
		else if(im != null) {
			im.draw(x, y);
		}
	}

}

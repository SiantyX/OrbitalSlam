package Game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.*;

public class Game extends BasicGame{
	 
	private float w;
	private float h;
	
    public Game() {
        super("Orbitual Remake");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
    	w = Math.round(gc.getWidth());
    	h = Math.round(gc.getHeight());
    	
    	int numAnc = 12;
    	
    	int startPosX = 120;
    	int numAncPerRow = 4;
    	
    	int startPosY = 150;
    	int numAncPerColumn = numAnc/numAncPerRow;
    	
    	Entity[] anchors = new Entity[numAnc];
    	for(int i = 0; i < numAnc; i++) {
    		anchors[i] = new Entity(new Circle(startPosX + (i%numAncPerRow) * (((w-(2*startPosX))/(numAncPerRow-1))),
    				startPosY + (i%numAncPerColumn) * (((h-(2*startPosY))/(numAncPerColumn-1))), 5));
    	}
    	
    	//new Entity(new Rectangle(400, 300, 100 ,100));
    }
 
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
    	
    }
 
    public void render(GameContainer gc, Graphics g) throws SlickException {
    	for(Entity e : Entity.entityList) {
    		e.render(g);
    	}
    }
 
    public static void main(String[] args) throws SlickException {
         AppGameContainer app = new AppGameContainer(new Game());
 
         app.setDisplayMode(800, 600, false);
         app.start();
    }
}
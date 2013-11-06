package game.maps;

import java.util.ArrayList;
import java.util.Random;

import game.Game;
import game.Player;
import game.ViewPort;
import game.maps.interactables.Interactable;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tests.xml.Entity;

public class DissapearingAnchorsMap extends GameMap {
	
	private Random rand;
	private float dist;
	private int counter;
	private ArrayList<Entity> backupAnchors;

	public Vector2f getStartPos(Player p, ViewPort vp) {
		return standardStartPosition(p.getNum(), vp);
	}
	
	public DissapearingAnchorsMap(){
		super();
		this.dist = 0;
	}

	
	public void createMap(ViewPort vp) throws SlickException {
		anchors.clear();
		interactables.clear();
		counter = 0;
		
		backupAnchors = new ArrayList<Entity>();
		
		float distX;
		float distY;
		numAnc = 200;
		rand = new Random();
		float dirY;
		float dirX;
		
		


		for (int i = 0; i < numAnc; i++) {
			
			dirX = 2*(rand.nextFloat() - 0.5f);
			
			dirY = (float) (Math.sqrt(1 - Math.pow(dirX,2)));
			
			int knas = rand.nextInt(2);
			if (knas == 1){
				dirY = -dirY;
			}

			distX = (dirX * dist) + Game.WIDTH/2;
			distY = (dirY * dist) + Game.HEIGHT/2;
			
			System.out.println(distX);
			System.out.println(distY);
			
			dist += 1000/((float)numAnc) ;
			

			Vector2f pos = new Vector2f(distX, distY);
			
			pos = vp.toAbsolute(pos);

			addAnchor(i, pos, vp);
		}
		
		backupAnchors = (ArrayList<Entity>) anchors.clone();
		
	}

	private boolean collisionCheck(Interactable e2) {
		for (Interactable i : interactables) {
			if (e2.collisionCircle(i)) {
				return true;
			}
		}
		return false;
	}

	public void mapSpecificChange() throws SlickException {
		counter = counter%10;
		
		if (counter == 0){
			if (anchors.size() > 5)
				anchors.remove(anchors.size()-1);
			
		}
		
		counter++;
		

	}
	
	public void reset(){
		anchors = (ArrayList<game.Entity>) backupAnchors.clone();
	}

	
	public String toString() {
		return "Dissapearing Anchors Map";
	}

}

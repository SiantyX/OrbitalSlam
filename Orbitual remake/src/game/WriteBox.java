package game;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Vector2f;

public class WriteBox extends Entity implements KeyListener, MouseListener{
	private boolean focused;

	public WriteBox(String id) {
		super(id);
		focused = false;
	}

	@Override
	public void keyPressed(int arg0, char arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAcceptingInput() {
		return false;
	}

	@Override
	public void setInput(Input input) {
	}

	@Override
	public void keyReleased(int key, char c) {
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if(button == Input.MOUSE_LEFT_BUTTON) {
			if(collisionSquare(new Vector2f(x, y))) {
				focused = true;
			}
			else {
				focused = false;
			}
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	}

	@Override
	public void mousePressed(int button, int x, int y) {
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
	}

	@Override
	public void mouseWheelMoved(int change) {
	}
}

package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import components.SXTimer;

public class WriteBox extends MenuButton implements KeyListener {
	private boolean focused;
	private String acceptable;
	private SXTimer timer;

	public WriteBox(String id, Shape shape, Color color, String text, TrueTypeFont ttf) throws SlickException {
		super(id, shape, color, text, ttf);
		Init();
	}
	
	public WriteBox(String id, Shape shape, Color color, String text, TrueTypeFont ttf, Color color2) throws SlickException {
		super(id, shape, color, text, ttf, color2);
		Init();
	}
	
	private void Init() {
		focused = false;
		acceptable = "";
		timer = new SXTimer(500);
	}

	public void setAcceptable(String a) {
		acceptable = a;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		super.update(gc, sb, delta);
		
		if(this.isMousePressed(0)) {
			if(!focused) {
				focused = true;
				text = "";
			}
		}
		else {
			focused = false;
		}
		
		if(focused) {
			if(timer.isTriggered() >= 0) {
				if(text.endsWith("|")) {
					text = text.substring(0, text.length()-2);
				}
				else {
					text += "|";
				}
			}
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) {
		super.render(gc, sb, g);
	}

	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE) {
			focused = false;
			text = "";
		}
		else if(key == Input.KEY_ENTER) {
			focused = false;
		}
		
		if(acceptable.equals("")) {
			text += c;
		}
		else {
			if(acceptable.contains(Character.toString(c))) {
				text += c;
			}
		}
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public void inputStarted() {
	}

	@Override
	public boolean isAcceptingInput() {
		return focused;
	}

	@Override
	public void setInput(Input input) {
	}

	@Override
	public void keyReleased(int key, char c) {
	}
}

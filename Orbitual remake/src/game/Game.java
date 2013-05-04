package game;

import gamestates.InGameState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public static AppGameContainer app;
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;
	private final static boolean fullscreen = false;
	public static boolean showHitbox;
	
	public static int centerHeight;
	public static int centerWidth;
	
	
	public Game() {
		super("Orbitual remake");
		showHitbox = false;
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		centerHeight = app.getHeight() / 2;
		centerWidth = app.getWidth() / 2;
		addState(new InGameState());
		//addState(new MenuState());
	}
	
	
	public static void main(String[] args) {
		
		try {
			app = new AppGameContainer(new Game());
			app.setDisplayMode(WIDTH, HEIGHT, fullscreen);
			app.setTargetFrameRate(60);
			app.setShowFPS(false);
			app.setSmoothDeltas(true);
			app.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
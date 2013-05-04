package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public static AppGameContainer app;
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;
	private final static boolean fullscreen = false;
	
	
	public Game() {
		super("Orbitual remake");
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		//addState(new InGameState());
		//addState(new MenuState());
	}
	
	
	public static void main(String[] args) throws SlickException {
		
		app = new AppGameContainer(new Game());
		app.setDisplayMode(WIDTH, HEIGHT, fullscreen);
		app.setTargetFrameRate(60);
		app.setShowFPS(false);
		app.setSmoothDeltas(true);
		app.start();
	}
}
package com.siantyxsoftware.orbitalslam;

import android.graphics.Color;
import android.graphics.Paint;

import com.siantyxsoftware.framework.Game;
import com.siantyxsoftware.framework.Graphics;
import com.siantyxsoftware.framework.Screen;
import com.siantyxsoftware.framework.Graphics.ImageFormat;


public class Loading extends Screen {
	public static int ID = 404;
	
    public Loading(Game game) {
        super(game);
    }


    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.mainPaint = new Paint();
        Assets.mainPaint.setColor(Color.WHITE);
        Assets.anchor = g.newImage("anchorstar.png", ImageFormat.ARGB4444);
        Assets.player = g.newImage("smiley1.png", ImageFormat.ARGB4444);
        Assets.click = game.getAudio().createSound("klick.ogg");
        Assets.hit = game.getAudio().createSound("hit.ogg");
        Assets.theme = game.getAudio().createMusic("menu.ogg");
        Assets.theme.play();
        Assets.theme.setLooping(true);
       
        game.setScreen(new MainMenu(game));


    }


    @Override
    public void paint(float deltaTime) {
    	Graphics g = game.getGraphics();
        Assets.mainPaint = new Paint();
    	Assets.mainPaint.setColor(Color.WHITE);
    	Assets.mainPaint.setTextSize(30);
    	
    	g.drawString("Loading", 300, 300, Assets.mainPaint);

    }


    @Override
    public void pause() {


    }


    @Override
    public void resume() {


    }


    @Override
    public void dispose() {


    }


    @Override
    public void backButton() {


    }
}
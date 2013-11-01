package components;

import game.ViewPort;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class AnimationRenderComponent extends Component {
	
	private ArrayList<ImageRenderComponent> renderImages;
	private float fps;
	private SXTimer timer;
	private ImageRenderComponent currentIRC;
	private boolean loop;
	private boolean started;
	private boolean done;

	
	public AnimationRenderComponent(String id, float fps, Image ... images) {
		super(id);
		this.fps = fps;
		
		renderImages = new ArrayList<ImageRenderComponent>();
		
		for(Image img : images) {
			ImageRenderComponent temp = new ImageRenderComponent(id + renderImages.size(), img);
			temp.setOwnerEntity(owner);
			renderImages.add(temp);
		}
		
		
		init();
	}
	
	public AnimationRenderComponent(String id, float fps, ImageRenderComponent ... images) {
		super(id);
		this.fps = fps;
		renderImages = new ArrayList<ImageRenderComponent>();
		
		for(ImageRenderComponent img : images) {
			img.setOwnerEntity(owner);
			renderImages.add(img);
		}
		
		
		init();
	}
	
	public AnimationRenderComponent(String id, float fps, String folderPath) throws SlickException {
		super(id);
		this.fps = fps;
		renderImages = new ArrayList<ImageRenderComponent>();
		
		File folder = new File(folderPath);
		if(!folder.exists()) return;
		
		File[] fileList = folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				return fileName.endsWith(".jpg") || fileName.endsWith(".png");
			}
		});
		
		for(File file : fileList) {
			System.out.println("path: "+file.getAbsolutePath());
			ImageRenderComponent temp = new ImageRenderComponent(id + renderImages.size(), new Image(file.getAbsolutePath()));
			temp.setOwnerEntity(owner);
			renderImages.add(temp);
		}
		
		init();
	}
	
	private void init() {
		timer = new SXTimer(Math.round(1000/fps));
		currentIRC = renderImages.get(0);
		currentImage = currentIRC.getImage();
		loop = false;
		started = false;
		done = false;
	}
	
	public void setLoop(boolean l) {
		loop = l;
	}
	
	public void start() {
		started = true;
		done = false;
		currentIRC = renderImages.get(0);
		currentImage = currentIRC.getImage();
	}
	
	public void stop() {
		started = false;
		done = false;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		if(!started || done) return;
		
		
		if(timer.isTriggered() >= 0) {
			int i = renderImages.indexOf(currentIRC);
			i++;
			if(i >= renderImages.size()) {
				if(loop)
					i = 0;
				else {
					done = true;
					return;
				}
			}
			
			currentIRC = renderImages.get(i);
			currentImage = currentIRC.getImage();
		}
	}

	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		currentIRC.setOwnerEntity(owner);
		currentIRC.setScale(scale);
		currentIRC.render(gc, sb, gr);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr, ViewPort vp) {
		currentIRC.setOwnerEntity(owner);
		currentIRC.setScale(scale);
		currentIRC.render(gc, sb, gr, vp);
	}
}
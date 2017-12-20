import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;

public class Bullet implements Runnable{
	
	public int xPos;
	public int yPos;
	private boolean alive;
	private int heightchange;

	private int WIDTH;
	private int HEIGHT;
	
	Invader_Army army;
	Space_Invaders spaceIn;
	Barriers barriers;
	
	public SimpleSoundPlayer sound;
	public InputStream stream;
	
	public Bullet(int x, int y, Invader_Army a, Space_Invaders space, Barriers b){
		spaceIn = space;
		
		xPos = x + 10;// add half of defender width
		yPos = y;
		army = a;
		WIDTH = 2;
		HEIGHT = 10;
		
		sound = new SimpleSoundPlayer("gunshot.wav");
		stream = new ByteArrayInputStream(sound.getSamples());
		
		barriers = b;
		alive = true;
		heightchange = 15;
		paintBullet();
		
		Thread thread = new Thread(this);
		thread.start();
// 		sound.play(stream);
		
	}
	
	public void run(){
		
		while(alive){
			try{
				Thread.sleep(spaceIn.DefenderFireRate);
			} catch(InterruptedException e) {
			}
			moveBullet();
		}
	}
	
	private void moveBullet(){
		if(barriers.checkHitBullet(xPos, yPos - HEIGHT/2)){
			alive = false;
			clearBullet();
		}
		else if(army.checkHit(xPos, yPos - HEIGHT/2)){ //check if hit invader
			spaceIn.score += 1;
			spaceIn.winNumber++;
			System.out.println("Score: " + spaceIn.score);
			alive = false;
			clearBullet();
		}
		else if(yPos < 0){
			alive = false;
			clearBullet();
		}
		else{
			clearBullet();
			yPos -= heightchange;
			paintBullet();
		}	
		
	}
	
	public void clearBullet(){
		spaceIn.g = (Graphics2D) spaceIn.strategy.getDrawGraphics();
		spaceIn.g.clearRect(xPos, yPos, WIDTH, HEIGHT);
	}

	
	public void paintBullet() {
		spaceIn.g = (Graphics2D) spaceIn.strategy.getDrawGraphics();
		spaceIn.g.setColor(Color.magenta);
		spaceIn.g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
	}

}
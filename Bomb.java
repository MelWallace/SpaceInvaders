import java.awt.*;

public class Bomb implements Runnable{

	public int xPos;
	public int yPos;
	private boolean alive;
	private int heightchange;
	private int i;
	private int j;

	private int WIDTH;
	private int HEIGHT;
	
	public int rand;
	private boolean hasDropped;
	private boolean fullStop;
	
	Invader_Army army;
	Space_Invaders spaceIn;
	Barriers barriers;
	Defender  defender;
	
	public Bomb(int x, int y, Invader_Army a, Space_Invaders space, Barriers b, Defender d, int r){
		spaceIn = space;
		
		i = x;
		j = y; 
		
		rand = r;
		army = a;
		WIDTH = 6;
		HEIGHT = 6;
		
		barriers = b;
		defender = d;
		alive = true;
		hasDropped = false;
		heightchange = 20;
		Thread thread = new Thread(this);
		thread.start();
	
	}
	public void run(){
		while(alive){
			try{
				Thread.sleep(spaceIn.DefenderFireRate);
			} catch(InterruptedException e) {
			}
			moveBomb();
		}
	}
	
	private void moveBomb(){
		
		if(spaceIn.lost == true){
			alive = false;
			clearBomb();
		}
		else if (army.checkCount(rand) && !hasDropped){
			if(army.checkStatus(i, j)){
				hasDropped = true;
				xPos = army.getXPos(i, j) + 10;
				yPos = army.getYPos(i, j) + 10;
				paintBomb();
			}
			else{
				alive = false;
			}
		}
		else if (barriers.checkHitBombs(xPos, yPos)){
			alive = false;
			clearBomb();
		}
		else if (defender.checkHit(xPos, yPos)){
			spaceIn.lives--;
			alive = false;
			spaceIn.lost = true;
			System.out.println("num lives left: " + spaceIn.lives);
			if(spaceIn.lives <= 0){
				System.out.println("Game Over");
				alive = false;
				spaceIn.finished = true;
				spaceIn.g = (Graphics2D) spaceIn.strategy.getDrawGraphics();
				spaceIn.reset(spaceIn.g);
			}
		}
		else if(yPos > spaceIn.HEIGHT){
			alive = false;
			clearBomb();
		}
		else if(hasDropped){
			clearBomb();
			yPos += heightchange;
			paintBomb();
		}		
		
	}
	
	public void clearBomb(){
		spaceIn.g = (Graphics2D) spaceIn.strategy.getDrawGraphics();
		spaceIn.g.clearRect(xPos, yPos, WIDTH, HEIGHT);

	}

	
	public void paintBomb() {
		spaceIn.g = (Graphics2D) spaceIn.strategy.getDrawGraphics();
		spaceIn.g.setColor(Color.black);
		spaceIn.g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
	}




}
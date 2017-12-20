import java.awt.*;
import java.util.*;

public class Invader {
	
	public int xPos;
	public int yPos;
	private int WIDTH;
	private int HEIGHT;
	public int xMove;
	public int yMove;
	public boolean alive;
	
	public Invader(int x, int y){
	
		xPos = x;
		yPos = y;
		WIDTH = 20;
		HEIGHT = 20;
		xMove = 0;
		yMove = 0;
		alive = true;
	
	
	
	}
	
	public boolean hit(int x, int y){
		int bx = x;
		int by = y;
		int leftEnd = xPos - (WIDTH/2);	
		int topEnd = yPos - (HEIGHT/2);	
		if(alive == false){
			return false;
		}
		else if (bx >= leftEnd && bx <= leftEnd + WIDTH){
			if(by <= topEnd && by >= topEnd - HEIGHT){
				alive = false;
				return true;
			}
		}
		return false;
			

	}
	
	public void paintInvader(Graphics g) {
		if(alive){
			g.clearRect(xPos, yPos, WIDTH, HEIGHT);
			xPos = xPos + xMove;
			yPos = yPos + yMove;
			xMove = 0;
			yMove = 0;
			g.setColor(Color.blue);
			g.fillRect(xPos,yPos, WIDTH, HEIGHT);
		}
		
	}

}
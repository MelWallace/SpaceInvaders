import java.awt.*;
import java.util.*;

public class block {
	
	public int xPos;
	public int yPos;

	private int WIDTH;
	private int HEIGHT;
	public boolean alive;
	
	public block(int x, int y){
	
		xPos = x;
		yPos = y;
		alive = true;
		WIDTH = 10;
		HEIGHT = 10;
	
	}
	public boolean hit(int x, int y, int t){
		int bx = x;
		int by = y;
		int type = t;
		int leftEnd = xPos - (WIDTH/2);
		int topEnd = yPos - (HEIGHT/2);
		if(alive == false){
			return false;
		}
		else if (bx >=leftEnd && bx <= leftEnd + WIDTH){
			if(by <= topEnd && by >= topEnd - HEIGHT){
				if(type == 0){
					alive = false;
				}
				return true;
			}
		}
		return false;
	}

	
	public void paintBlock(Graphics g) {
		if (alive == true){
			g.setColor(Color.green);
			g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		}
		else{
			g.clearRect(xPos, yPos, WIDTH, HEIGHT);
		}
		
	}

}
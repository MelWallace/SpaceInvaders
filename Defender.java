import java.awt.event.*;
import java.awt.*;


public class Defender{

	Space_Invaders spaceIn = null;
	public int xPos;
	public int yPos;
	private int xSize;
	private int ySize;
	private int xMove;
	
	
	public Defender(Space_Invaders space){
		spaceIn = space;
		xSize = 20;
		ySize = 20;
		xMove = 0;
		
		xPos = spaceIn.WIDTH/2;
		yPos = spaceIn.HEIGHT - ySize;
		
	}
	
	public void reset(){
		xPos = spaceIn.WIDTH/2;
		yPos = spaceIn.HEIGHT - ySize;
	
	}
	
	public void move(String dir){
		if (dir == "left"){
			xMove = -spaceIn.DefenderSpeed;
		}
		else if (dir == "right"){
			xMove = spaceIn.DefenderSpeed;
		}
	
	}
	
	public void paintDefender(Graphics g){
		if (xPos < 0){
			xPos = 0;
		}
		else if(xPos > (spaceIn.WIDTH - xSize)){
			xPos = spaceIn.WIDTH - xSize;
		}
		else{
			g.clearRect(xPos, yPos, xSize, ySize);
			xPos = xPos + xMove;
			xMove = 0;
		}
		g.setColor(Color.red);
		g.fillRect(xPos,yPos, xSize, ySize);
	}
	
	public boolean checkHit(int x, int y){
		int bx = x;
		int by = y;
		int leftEnd = xPos - xSize/2;
		if (bx > (leftEnd - 5) && bx < (leftEnd + xSize + 5)){
			if(by >= yPos){
				return true;
			}
		}
		return false;
	}
	
}
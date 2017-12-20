import java.awt.event.*;
import java.awt.*;


public class Barriers{

	private int xPos;
	private int yPos;
	
	private int numRows;
	private int numCol;
	
	private int blockWidth;
	private int blockHeight;
	private int invadH;
	
	public block [][] blockOne;
	public block [][] blockTwo;
	public block [][] blockThree;
	public int [] start;
	public Space_Invaders spaceIn;
	
	public Barriers(Space_Invaders s){
		spaceIn = s;
		numRows = 4;
		numCol = 4;
		blockWidth = 10;
		blockHeight = 10;
		invadH = 20;
		
		createBarriers();


	}
	
	public void reset(){
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCol; j++){
				blockOne[i][j].alive = true;
				blockTwo[i][j].alive = true;
				blockThree[i][j].alive = true;
			}
		}
	}
	
	public void createBarriers(){
	
		if(((blockWidth * numCol * 3) > spaceIn.WIDTH) || (blockHeight * numRows > (spaceIn.HEIGHT/3))){
		//throw error
		}
		
		blockOne = new block[numRows][numCol];
		blockTwo = new block[numRows][numCol];
		blockThree = new block[numRows][numCol];
		
		start = new int [3];
		start[0] = (spaceIn.WIDTH / 4) - (blockWidth * numCol / 2);
		start[1] = (spaceIn.WIDTH / 2) - (blockWidth * numCol / 2);
		start[2] = (3 * spaceIn.WIDTH / 4) - (blockWidth * numCol / 2);
		int x , y;
		for (int i = 0; i < numRows; i++){
			for(int j = 0; j < numCol; j++){
				x = (blockWidth/2)+(j*blockWidth);
				y = spaceIn.HEIGHT - ((3 * blockHeight / 2) + ((numRows - i) * blockHeight) + invadH); //invader height - change
				blockOne[i][j] = new block(x + start[0], y);
				blockTwo[i][j] = new block(x + start[1], y);
				blockThree[i][j] = new block(x + start[2], y);

			}
		}
	}
	
	public void paintBarriers(Graphics g){
		for (int i = 0; i < numRows; i++){
			for(int j = 0; j < numCol; j++){
				blockOne[i][j].paintBlock(g);
				blockTwo[i][j].paintBlock(g);
				blockThree[i][j].paintBlock(g);
			}
		}
	}
	
//need to iterate through all blocks

	public boolean checkHitBombs(int x, int y){
		int bx = x;
		int by = y;
		spaceIn.g = (Graphics2D) spaceIn.strategy.getDrawGraphics();
		//if its slow make this more efficient
		for(int j = 0; j < numRows; j++){
			for(int i = 0; i < numCol; i++){
				if (blockOne[j][i].hit(bx, by, 0)){
					blockOne[j][i].paintBlock(spaceIn.g);
					return true;
				}
				else if (blockTwo[j][i].hit(bx, by, 0)){
					blockTwo[j][i].paintBlock(spaceIn.g);
					return true;
				}
				else if (blockThree[j][i].hit(bx, by, 0)){
					blockThree[j][i].paintBlock(spaceIn.g);
					return true;
				}	
				
			}
		}
		return false;
	}
	
	public boolean checkHitBullet(int x, int y){
		int bx = x;
		int by = y;
		if(by > ((blockHeight/2) + invadH)){
			for(int i = 0; i < numCol; i++){
				if (blockOne[numRows - 1][i].hit(bx, by, 1) || blockTwo[numRows - 1][i].hit(bx, by, 1) || blockThree[numRows - 1][i].hit(bx, by, 1)){
					return true;
				}
			}
		}
		return false;
	}
	
	
}
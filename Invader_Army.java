import java.awt.*;
import java.util.*;

public class Invader_Army {

	private int numRows;
	private int numCol;
	
	private int xPos;
	private int yPos;
	private int totalInWidth;
	private int xrate;
	private int rate;
	private int count;
	
	public static int inWIDTH;
	public static int inHEIGHT;
	
	private Defender defender;
	private Space_Invaders spaceIn;
	
	public Barriers barriers;
	public Bomb bomb; 
	
	public Invader [][] army;
	
	public Invader_Army(Defender dd, Space_Invaders space, Barriers b){
		defender = dd;
		spaceIn = space;
		barriers = b;
		numRows = space.InvaderRows; 
		numCol = space.InvaderCol;	
		rate = 1;	
		count = 0;
		inWIDTH = 20;
		inHEIGHT = 20;
		xrate = spaceIn.WIDTH/2;
		rate = spaceIn.InvaderSpeed;
		
		createArmy();		
	}
	
	public void reset(){
		xrate = spaceIn.WIDTH/2;
		createArmy();
	}
	
	public void createArmy(){
		totalInWidth = (2 * inWIDTH * numCol) - inWIDTH;
		int sideWidth = (spaceIn.WIDTH - totalInWidth) / 2; //check
		if(totalInWidth > spaceIn.WIDTH || (inHEIGHT * 2) > spaceIn.HEIGHT) {//throw error
		}
		army = new Invader[numRows][numCol];
		for (int i = 0; i < numRows; i++){
			for(int j = 0; j < numCol; j++){
				army[i][j] = new Invader(sideWidth + (inWIDTH / 2) + (2 * j * inWIDTH), (3 * inHEIGHT / 2) + (2 * i * inHEIGHT));
			}
		}

		
	}
	
	public void moveArmy(){
		int drop = 0;
		if(xrate >= (spaceIn.WIDTH - (totalInWidth /  2) - 10)){
			rate *= -1;
			count = 0;
			xrate = spaceIn.WIDTH - (totalInWidth /  2) - 10;
			drop = spaceIn.DropDistance;
			armyBombs();
		}
		else if(xrate <= (totalInWidth / 2) - 10){
			rate *= -1;
			count = 0;
			xrate = (totalInWidth / 2) - 10;
			drop = spaceIn.DropDistance;
			armyBombs();
		}
		xrate += rate;
		count++;
		for (int i = 0; i < numRows; i++){
			for(int j = 0; j < numCol; j++){
				if(drop == 0){
					army[i][j].xMove = rate;
				}
				else{
					army[i][j].yMove = drop;
				}
			}
		}	
	}
	
	
	public void paintArmy(Graphics g) {
		
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCol; j++){
				if (army[i][j].alive == true){
					army[i][j].paintInvader(g);
				}
			}
		}
	}
	
	public boolean checkHit(int x, int y){
		int bx = x;
		int by = y;
		for (int i = 0; i < numRows; i++){
			for(int j = 0; j < numCol; j++){
				if (army[i][j].hit(bx, by)){
					spaceIn.g = (Graphics2D) spaceIn.strategy.getDrawGraphics();
					spaceIn.g.clearRect(army[i][j].xPos, army[i][j].yPos, inWIDTH, inHEIGHT);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkCount(int r){
		if(r == count){
			return true;
		}
		return false;
	}
	
	public int getXPos(int i, int j){
		return army[i][j].xPos;
	}
	public int getYPos(int i, int j){
		return army[i][j].yPos;
	}
	public boolean checkStatus(int i, int j){
		if(army[i][j].alive){
			return true;
		}
		return false;
	}
	
	public void armyBombs(){
		int [] rand = new int [numCol];
		int numBombs = (spaceIn.WIDTH - totalInWidth) / spaceIn.InvaderSpeed;
		//call when rate changes
		Random generator = new Random();
		for (int k = 0; k < spaceIn.InvaderFireRate; k++){
			for (int r = 0; r < numCol; r++){
				rand[r] = generator.nextInt(numBombs);
				int i = numRows - 1;
				while(i >= 0){
					if(army[i][r].alive == true){
						bomb = new Bomb(i, r, this, spaceIn, barriers, defender, rand[r]);
						i = -1;
					}
					i--;
				}
			}
 		}
	
	}
	
	
	
}
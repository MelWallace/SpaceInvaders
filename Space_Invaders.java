import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.image.BufferStrategy;  
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.io.*;
import javax.sound.sampled.*;

public class Space_Invaders extends Canvas implements Runnable{

	public BufferStrategy strategy;
	public JFrame space;
	public JPanel controlPanel;
	
	Invader_Army army = null;
	Defender defender = null;
	Barriers barriers = null;
	Bullet bullet = null;
	
	private Thread t;
	private boolean leftKeyPressed = false;
	private boolean rightKeyPressed = false;
	private boolean fireKeyPressed = false;
	
	public SimpleSoundPlayer sound;
	public InputStream stream;
	
	private int GameSpeed = 100;
	public int InvaderSpeed;
	public int DefenderSpeed = 30;
	public int InvaderFireRate;
	public int DropDistance;
	public int DefenderFireRate = 100;
	public int InvaderRows;
	public int InvaderCol;
	
	Graphics2D g;
	
	private static final AudioFormat PLAYBACK_FORMAT = new AudioFormat(44100, 16, 1, true, false);
	
	public static int WIDTH = 800;
	public static int HEIGHT = 500;
	public static int score = 0;
	public static int lives;
	public boolean finished;
	public boolean lost;
	public int count; 
	public int winNumber;
	
	public Space_Invaders() {
		
		getInput();
		
		space = new JFrame("Space Invaders by Melanie Wallace");
		JPanel spaceStuff = (JPanel) space.getContentPane();
		spaceStuff.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		spaceStuff.setLayout(null);
	
		setBounds(0,0,WIDTH, HEIGHT);
		spaceStuff.add(this);
		
		space.pack();
		space.setResizable(false);
		space.setBackground(Color.black);
		space.setLocationRelativeTo(null);
		space.setVisible(true);

		JLabel scoreBoard = new JLabel("Score: " + score);
		scoreBoard.setForeground(Color.blue);
		scoreBoard.setHorizontalAlignment(JLabel.CENTER);
		space.add(scoreBoard, BorderLayout.SOUTH);

    	space.addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {
    			System.exit(0);
    		}
    	});
	
		sound = new SimpleSoundPlayer("once.wav");
		stream = new ByteArrayInputStream(sound.getSamples());



		createBufferStrategy(2);
		strategy = getBufferStrategy();
		addKeyListener(new KeyInput());
    	startGame();
    }
    private void getInput(){
    	Scanner scan = new Scanner(System.in);
    	System.out.println("How many rows of invaders? (between 1 and 15)");
    	InvaderRows = scan.nextInt();
    	System.out.println("How many columns of invaders? (between 1 and 20)");
    	InvaderCol = scan.nextInt();
    	System.out.println("How fast should the invaders go back and forth? (between 1 and 50)");
    	InvaderSpeed = scan.nextInt();
    	System.out.println("How far should the invaders drop each pass? (between 10 and 40)");
    	DropDistance = scan.nextInt();
    	System.out.println("How fast should the invaders fire? (how many time should each invader fire per pass)");
    	InvaderFireRate = scan.nextInt();
    	System.out.println("How many lives? (more than 0)");
    	lives = scan.nextInt();
    	winNumber = 0;
    }
    
    private void paintScore(Graphics g){
    	g.clearRect(0,0,100, 50);
		g.drawString("Score: " + score, 10, 20);
		g.drawString("Lives: " + lives, 10, 35); 
    }
    
    public void win(Graphics g){
    		g.clearRect(0,0,WIDTH, HEIGHT);
    		Font font = g.getFont().deriveFont(50.0f);
    		g.setFont(font);
    		g.drawString("WINNER", WIDTH/2 - 150, HEIGHT/2);
    		finished = true;
			strategy.show();
    }
    
    public void reset(Graphics g){
    	count++;
    	if(lives <= 0){
    		g.clearRect(0,0,WIDTH, HEIGHT);
    		Font font = g.getFont().deriveFont(50.0f);
    		g.setFont(font);
    		g.drawString("GAME OVER", WIDTH/2 - 150, HEIGHT/2);
    		finished = true;
			strategy.show();
		}
    	else if(count == 40){
    		defender.reset();
			barriers.reset();
			army.reset();
			winNumber = 0;
			g.clearRect(0,0,WIDTH, HEIGHT);
			strategy.show();
    		lost = false;
    		count = 0;
    	}
	}
    
	public void startGame() {
		leftKeyPressed = false;
		rightKeyPressed = false;
		fireKeyPressed = false;
		finished = false;
		lost = false;
		count = 0;
		
		defender = new Defender(this);
		barriers = new Barriers(this);
		army = new Invader_Army(defender, this, barriers);

		render();
		
		if (t == null) {
			t = new Thread(this);
			t.start();
			sound.play(stream);
		}

		
		
	}
	
	public void run() {
		
		while(!finished){
			try {
				Thread.sleep(GameSpeed);
			} catch (InterruptedException e){
				System.out.println("Thread Interrupted");
			}
			render();
		}
	}
	private void render() {
		if (lost == true){
			g.clearRect(0,0,WIDTH, HEIGHT);
    		Font font = g.getFont().deriveFont(50.0f);
    		g.setFont(font);
    		g.drawString("LIVES: " + lives, WIDTH/2 - 100, HEIGHT/2);
    		strategy.show();
			reset(g);
		}
		else if (winNumber == (InvaderRows * InvaderCol)){
			win(g);
		}
		else{
			g = (Graphics2D) strategy.getDrawGraphics();
			army.paintArmy(g); 
			army.moveArmy();
			if(army.checkHit(defender.xPos, defender.yPos)){
				lives--;
				reset(g);
			}
			paintScore(g);
			defender.paintDefender(g);
			barriers.paintBarriers(g);
			g.dispose();
			strategy.show();
		}
	}
	
	private void move(){
	
		if (leftKeyPressed){
			defender.move("left");
		}
		else if(rightKeyPressed){
			defender.move("right");
		}
		else if(fireKeyPressed){
			bullet = new Bullet(defender.xPos, defender.yPos, army, this, barriers);
		}
	
	
	}

	private class KeyInput extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftKeyPressed = true;
				move();
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightKeyPressed = true;
				move();
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				fireKeyPressed = true;
				move();
			}
			
		}
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftKeyPressed = false;
				move();
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightKeyPressed = false;
				move();
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				fireKeyPressed = false;
				move();
			}
		}
	}
	
	
	public static void main(String []args) {	        
		Space_Invaders player = new Space_Invaders();
	}
}
		
		
		
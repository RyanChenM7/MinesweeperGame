package minesweeper;

import java.util.Random;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.Border;

 
public class Minecraft {
	
	//Random number generator for generating grid
	static Random r = new Random();
	
	//Fields for the minesweeper game
	Button[][] buttons;
	int[][] minefield;
	static int[][] dir = {{1,1},{-1,-1},{1,-1},{-1,1},{0,1},{0,-1},{1,0},{-1,0}};
	//Size of text for the number on the mines
	static int size = 12;
	//Tile size
	static int tileSize = 40;
	
	//Starts timer
	boolean isGoing = false;
	//Prevents clicking if you aren't alive
	boolean isAlive = true;
	
	//Make a timer to keep track of passed seconds
	Timer tick = new Timer();
	
	//Default image icons with a size of tileSize*tileSize pixels
	static final ImageIcon flag = new ImageIcon(new ImageIcon("resources/Flag.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
	static final ImageIcon rushB = new ImageIcon(new ImageIcon("resources/Mine.jpg").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
	static final ImageIcon blanke = new ImageIcon(new ImageIcon("resources/Tile.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
	static final ImageIcon poggers = new ImageIcon(new ImageIcon("resources/Poggers.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
	//Custom cursor
	static final Image diamondSword = new ImageIcon("resources/DiamondSword.png").getImage();
	
	//Smiley Face to reset, always tileSize*tileSize pixels
	static final ImageIcon smiley = new ImageIcon(new ImageIcon("resources/Smiley.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));	
	
	//ActionListeners for the buttons in the game
	final ActionListener resetFunction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			restartGame(rows, cols, mines);
		}
	};
	
	final ActionListener easyFunction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			restartGame(8, 8, 10);
		}
	};
	
	final ActionListener mediumFunction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			restartGame(16, 16, 40);
		}
	};
	
	final ActionListener hardFunction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			restartGame(16, 30, 99);
		}
	};
	
	
	//A method which returns a scaled image icon with dimensions w by h pixels
	public ImageIcon scaleIcon(ImageIcon icon, int w, int h) {
		return new ImageIcon(icon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
	}
	
	//Values for the number of rows, columns, and mines
	private int rows, cols, mines;
	
	//Number of flags placed
	private int flags;
	
	//Count will keep track of the number of non-mine tiles left
	private int count;
	
	//Is able to keep time like a Rolex watch
	private int seconds = 0;
	
	//Creating the timer and mineCount variables for the label
	JLabel timer;
	JLabel mineCount;
	
	
	//Frame and panels
	JFrame frame;
	JPanel framePanel;
	JPanel resetRow;
	JPanel difficulty;
	JPanel mineGrid;
	
	//Getters, setters, incrementers, changing private variables/fields
	public void addSeconds() {
		seconds++;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setAlive(boolean val) {
		isAlive = val;
	}
	
	public boolean getAlive() {
		return isAlive;
	}
	
	public void setGoing(boolean val) {
		isGoing = val;
	}
	
	public boolean getGoing() {
		return isGoing;
	}
	
	public void minusCount(int val) {
		if (count - val == 0) {
			winGame();
		}
			
		count -= val;
	}
	
	public void minusFlags(int val) {
		flags -= val;
		mineCount.setText(padZeros(mines - flags));
	}
	
	public void setCount(int val) {
		count = val;
	}
	
	public void setFlags(int val) {
		flags = val;
		mineCount.setText(padZeros(mines - flags));
	}
	
	  
	
	//Generates random number between min and max inclusive
	public static int randint(int min, int max) {
		int x = r.nextInt(max+1);
		return min + x;
	}

	
	//Utility function that prints an array with lines between entries (like tic tac toe)
	public void printGrid(int arr[][]) {
		for (int i = 0; i < arr.length; ++i) {
			System.out.print('|');
			for (int j = 0; j < arr[0].length; ++j) {
				if (arr[i][j] >= 0) {
					System.out.print(String.format(" %d|",arr[i][j]));
					continue;
				}
				System.out.print(Integer.toString(arr[i][j]) + '|');
			}
			System.out.print('\n');
		}
	}
	
	//generates a grid using random number generation
	public int[][] genGrid(int rows, int cols, int nMines) {
		
		int x, y;
		int[][] arr = new int[rows][cols];
		
		//Sets everything to zero
		for (int i = 0; i < rows; ++i) 
			for (int j = 0; j < cols; ++j)
				arr[i][j] = 0;
		
		//randomly fills the array, keeping track of how many mines are left to place 
		while (nMines > 0) {
			x = randint(0,rows-1);
			y = randint(0,cols-1);
			
			//If it's a duplicate tile, try again. Do not decrement the number of mines left to place
			if (arr[x][y] == -1)
				continue;
			
			nMines--;
			arr[x][y] = -1;
		}
		
		//Dir is a 2d array of the 8 unit vectors, each representing a major direction on a compass
		//Goes tile by tile, setting the value by how many mines are around it by searching everything adjacent directly or diagonally
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				if (arr[i][j] != -1)
					continue;
				for (int[] xdir : dir) {
					if ((i + xdir[0] < rows && i + xdir[0] >= 0)&&(j + xdir[1] < cols && j + xdir[1] >= 0))
						if (arr[i + xdir[0]][j + xdir[1]] != -1)
							arr[i + xdir[0]][j + xdir[1]] += 1;
				}
			}
		}		
		
		return arr;
	}
	
	//Getter functions for rows and columns
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	//Formats a number by padding it with leading 0's to 3 digits. If it's negative, do not pad with leading 0's
	public String padZeros(int num) {
		if (num > 999)
			return "999";
		if (num < 0)
			return "" + num;
		return String.format("%03d", num);
	}
	
	//Timertask class to update seconds every second
	public class updateTime extends TimerTask {
		
		private Timer tick;
		private JLabel timer;
		
		//Constructor, passes in the label and timer to use
		public updateTime(Timer tick, JLabel timer) {
			this.tick = tick;
			this.timer = timer;
		}
		
		//Increments seconds and updates labels
		public void run() {
			if (isGoing && isAlive) {
				seconds++;
				timer.setText(padZeros(seconds));
			}
		}
	}
	
	//Restarts game and can change difficulty too. Does everything the same as the constructor, just does set size again.
	public void restartGame(int rows, int cols, int mines) {
		this.rows = rows;
		this.cols = cols;
		this.mines = mines;
		
		count = rows*cols - mines;
		
		//Randomly generates minefield
		minefield = genGrid(rows,cols,mines);
		buttons = new Button[rows][cols];
		
		//Disposes and resets the minefield
		mineGrid.removeAll(); 
		mineGrid.updateUI();
		
		//Toss the buttons back in
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				buttons[i][j] = new Button(minefield[i][j]);
				buttons[i][j].setLoc(i,j);
				mineGrid.add(buttons[i][j]);
				buttons[i][j].setFont(new Font("Arial", Font.BOLD, size));
				buttons[i][j].addMouseListener(new JerryWhisperer(this));	
			    buttons[i][j].setPreferredSize(new Dimension(tileSize, tileSize));
				buttons[i][j].setBackground(Color.LIGHT_GRAY);
			}
		
		//Resizes minefield panel
		mineGrid.setLayout(new GridLayout(rows, cols));
		
		//Sets everything to how it was at the very start
		setFlags(0);
		seconds = 0;
		timer.setText(padZeros(seconds));
		isGoing = false;
		isAlive = true;
		frame.pack();
	}
	
	//Constructor
	public Minecraft(int rows, int cols, int mines) {
		this.rows = rows;
		this.cols = cols;
		this.mines = mines;
		
		count = rows*cols - mines;
		minefield = genGrid(rows,cols,mines);
		buttons = new Button[rows][cols];
		
		
		frame = new JFrame();
		framePanel = new JPanel();
		resetRow = new JPanel();
		difficulty = new JPanel();
		mineGrid = new JPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Make a black border
		Border border = BorderFactory.createLineBorder(Color.black, 2);
		
		//Making mines remaining count
		mineCount = new JLabel(padZeros(mines));
		mineCount.setHorizontalAlignment(SwingConstants.CENTER);
		mineCount.setFont(new Font("Arial", Font.BOLD, 30));
		mineCount.setBackground(Color.blue);
		mineCount.setPreferredSize(new Dimension(128, 32));
		mineCount.setOpaque(true);
		mineCount.setBorder(border);
		
		//Making timer label
		timer = new JLabel(padZeros(seconds));
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("Arial", Font.BOLD, 30));
		timer.setPreferredSize(new Dimension(128, 32));
		timer.setBackground(Color.red);
		timer.setOpaque(true);
		timer.setBorder(border);

		framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.PAGE_AXIS));
		
		//Adding difficulty change buttons

		JButton easyButton = new JButton("EASY"); //easy
		easyButton.addActionListener(easyFunction);
		difficulty.add(easyButton);
		
		JButton mediumButton = new JButton("MEDIUM"); //medium
		mediumButton.addActionListener(mediumFunction);
		difficulty.add(mediumButton);
		
		JButton hardButton = new JButton("HARD"); //hard
		hardButton.addActionListener(hardFunction);
		difficulty.add(hardButton);
		
	
		//Adding reset smiley button
		JButton resetButton = new JButton();
		resetButton.setIcon(smiley);
		resetButton.setPreferredSize(new Dimension(64, 64));
		resetButton.addActionListener(resetFunction);
		
		resetRow.add(mineCount);
		resetRow.add(resetButton);
		resetRow.add(timer);
		
		
		//Setting the timer 
		tick.scheduleAtFixedRate(new updateTime(tick, timer), 1000, 1000);
		
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor c = toolkit.createCustomCursor(diamondSword, new Point(frame.getX(), frame.getY()), "img");
		frame.setCursor(c);
		
		
		//int sizeTile = (cols <= 24 && rows <= 12) ? 64 : 248;
		int sizeTile = 48;
		frame.setSize(sizeTile*cols,sizeTile*rows);
		
		
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				buttons[i][j] = new Button(minefield[i][j]);
				buttons[i][j].setLoc(i,j);
				mineGrid.add(buttons[i][j]);
				buttons[i][j].setFont(new Font("Arial", Font.BOLD, size));
				buttons[i][j].addMouseListener(new JerryWhisperer(this));	
			    buttons[i][j].setPreferredSize(new Dimension(tileSize, tileSize));
				buttons[i][j].setBackground(Color.LIGHT_GRAY);
			}
		
		//Setting the layout to a grid of tiles
		mineGrid.setLayout(new GridLayout(rows, cols));
		
		
		//Finish setting up the frame
		frame.setLayout(new BorderLayout());
		framePanel.add(resetRow);
		framePanel.add(difficulty);
		framePanel.add(mineGrid);
		frame.add(framePanel);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	//Winning game
	public void winGame() {
		isGoing = false;
		isAlive = false;
		for (int i = 0; i < rows; ++i)
			for (int j = 0; j < cols; ++j) {
				if (minefield[i][j] == -1)
					buttons[i][j].setIcon(poggers);
			}
	}
	
	//Main method
	public static void main(String[] args) {
		
		//Launches the Minesweeper game
		Minecraft Minesweeper = new Minecraft(10,10,5);
	}
	
}

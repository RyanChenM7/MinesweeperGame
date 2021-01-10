package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import minesweeper.Minecraft.updateTime;

import java.awt.event.MouseListener.*;
import java.util.Timer;
import java.util.TimerTask;

public class JerryWhisperer implements MouseListener {
	
	//Fields
	Minecraft game;
	Button button;
	static int[][] dir = {{1,1},{-1,-1},{1,-1},{-1,1},{0,1},{0,-1},{1,0},{-1,0}};
	
	//Passes game through
	public JerryWhisperer(Minecraft game) {
		this.game = game;
	}
	
	//Checks if (r, c) is within bounds for the minefield array
	public boolean withinBounds(int r, int c) {
		if (r >= 0 && r < game.getRows() && c >= 0 && c < game.getCols())
			return true;
			
		return false;
	}
	
	//Recursion clear for 0 tiles
	public void recursionClear(Button b) {
		game.minusCount(b.expose());
		int r = b.getLoc()[0];
		int c = b.getLoc()[1];
		//Checks all surroundings and exposes the ones around it
		for (int[] arr : dir) {
			int newR = r + arr[0];
			int newC = c + arr[1];
			if (withinBounds(newR, newC)) {
				Button b2 = game.buttons[newR][newC];
				
				if (b2.isFlagged() || b2.isExposed())
					continue;
				
				if (b2.getValue() == 0) {
					recursionClear(b2);	
				}
				else {
					game.minusCount(b2.expose());
				}
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {
		//Cannot click if dead
		if (game.getAlive()) {
	    	button = (Button) e.getSource();
	    	//The double click function is replaced by middle click
	    	if (SwingUtilities.isMiddleMouseButton(e)) {
	    		
	    		if (button.isExposed() == true) {
	    			int surroundFlags = 0;
	    			int r = button.getLoc()[0];
	    			int c = button.getLoc()[1];
	    			
	    			//Count the number of flags around
	    			for (int[] arr : dir) {
	    				int newR = r + arr[0];
	    				int newC = c + arr[1];
	    				if (withinBounds(newR, newC)) {
	    					Button b2 = game.buttons[newR][newC];
	    					if (b2.isFlagged() == true) 
	    						surroundFlags++;
	    				}
	    			}
	    			
	    			//Expose the surroundings
	    			if (surroundFlags == button.getValue())
		    			for (int[] arr : dir) {
		    				int newR = r + arr[0];
		    				int newC = c + arr[1];
		    				if (withinBounds(newR, newC)) {
		    					Button b2 = game.buttons[newR][newC];
		    					if (b2.getValue() == 0 && ! b2.isExposed() && ! b2.isFlagged()) {
		    						recursionClear(b2);
		    					}
		    					else if (! b2.isExposed() && ! b2.isFlagged()) { 
		    						b2.expose();
		    					}
		    				}
		    			}
	    		}
	    	}
	    	
	    	//Right click to flag
	    	else if (SwingUtilities.isRightMouseButton(e)) {
	    		
	    		//Updates the number of flags placed on the label
	    		if (! button.isExposed() && ! button.isExposed()) {
	    			game.minusFlags(button.flipFlag());
	    		}
	    		
	    		
	    	}
	    	
	    	//Left click
	    	else {
	    		if (! game.getGoing() && game.getAlive()) {
	    			game.setGoing(true);
	    		}
	    		//Exposes all mines because you hit a mine
	    		if (button.getValue() == -1) {
	        		game.setAlive(false);
	        		for (int i = 0; i < game.getRows(); ++i) 
	        			for (int j = 0; j < game.getCols(); ++j) {
	        				if (game.minefield[i][j] == -1)
	        					game.buttons[i][j].expose();
	        			}
	    		}
	    		//Recursion clearing the 0 tiles
	    		if (! button.isFlagged()) {
	    			if (button.getValue() == 0) {
	        			recursionClear(button);
	    			}
	    			else if (! button.isExposed()) {
	    				game.minusCount(button.expose());
	    			}
	    		}
	    	}
		}
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

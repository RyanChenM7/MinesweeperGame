package minesweeper;

import java.awt.Color;

import javax.swing.*;

public class Button extends JButton {
	//IDE suggestion removes red bars. Default ignore thing
	private static final long serialVersionUID = 1L;
	
	//Fields of the button
	private int value;
	private int[] location = new int[2];
	private boolean exposed = false;
	private boolean flag = false;
	
	//Constructor
	public Button(int num) {
		value = num;
	}
	
	//Changes the flagged state like a light switch
	public int flipFlag() {
		
		flag = ! flag;
		
		if (flag) {
			setIcon(Minecraft.flag);
			return -1;
		}
		
		else {
			setIcon(null);
			return 1;
		}
		
	}
	
	//Getter for if flagged
	public boolean isFlagged() {
		return flag;
	}
	
	//set value of the button
	public void setValue(int num) {
		value = num;
	}
	
	//getter for value
	public int getValue() {
		return value;
	}
	
	//Set location of button
	public void setLoc(int row, int col) {
		location[0] = row;
		location[1] = col;
	}
	
	//returns and prints coordinates (row, col) of button
	public int[] getLoc() {
		return location;
	}
	
	
	//Exposes the tiles as you play the game
	//Returns 1 if the exposed tile is not a mine
	public int expose() {
		exposed = true;
		
		//If it's a zero, change to the blank img
		if (value == 0) {
			setIcon(Minecraft.blanke);
			return 1;
		}
		
		//It's something that isn't a 0 or a bomb; a number tile
		else if (value != -1) {
			setIcon(null);
			setBackground(Color.white);
			setText("" + value);
			return 1;
		}
		
		//A bomb tile oof
		else {
			setIcon(Minecraft.rushB);
			setText("");
			return 0;
		}
		
	}
	
	//Getter for exposed
	public boolean isExposed() {
		return exposed;
	}


	
}

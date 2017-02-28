package graphics;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Board {
	private int[][] board;
	private int wide, height;
	
	
	public Board(int wide, int height){
		this.wide = wide;
		this.height = height;
		board = new int[wide][height];
		generate_board();
	}
	
	private void generate_board(){
		for(int i=0; i<wide; i++){
			for(int j=0; j<height; j++){
				board[i][j] = 0;
			}
		}
	}
	
	public int update(int value, int x, int y){
		if(x >= 0 && y >= 0 && x < wide && y < height && board[x][y] == 0){
			board[x][y] = value;
			return 1;
		}
		
		return 0;
	}
	
	public int clear_pos(int x, int y){
		if(x >= 0 && y >= 0 && x < wide && y < height){
			board[x][y] = 0;
			return 1;
		}
		return 0;
	}
		
	public int get_wide(){
		return wide;
	}
	public int get_height(){
		return height;
	}
	
	public int get_pos_value(int x, int y){
		if(x >= 0 && y >= 0 && x < wide && y < height) return board[x][y];
		return -1;
	}
	
}

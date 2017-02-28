package run;

import graphics.Board;
import graphics.Frame;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Stepper extends Thread{
	private Board b;
	private Frame f;
	private int nCycles, cCycles;
	
	public Stepper(Board board, Frame frame, int maxCycles){
		b = board;
		f = frame;
		nCycles = maxCycles;
		cCycles = 0;
	}
	
	public int get_time(){
		return this.cCycles;
	}
	
	public void run(){
		
		while(cCycles < nCycles){
			

			//Update the board
			
			//Update the frame
			f.setCycle(cCycles);
			f.update();
			cCycles++;
			
			try {
				Thread.sleep(f.getMSDelay());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

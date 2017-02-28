package run;

import graphics.Board;
import graphics.Frame;




public class Starter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		//Configuration variables
		int x = 100, y = 80;
		int pixelsPerCell = 10;
		int maxCycles = 100000;
		
		//System objects to model the board
		Board b = new Board(x, y);
		Frame f = new Frame();
		
		
		//Initialize frame and board
		f.start(b, pixelsPerCell);
		
		//Start thread to run the system through iterations
		Stepper stp = new Stepper(b, f, maxCycles);
		stp.run();
		
	}

}

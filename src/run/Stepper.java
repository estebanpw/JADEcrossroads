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
	private boolean []who_moved;
	private int n_traffics_lights;
	
	public Stepper(Board board, Frame frame, int maxCycles, int n_traffics_lights){
		b = board;
		f = frame;
		nCycles = maxCycles;
		cCycles = 0;
		this.n_traffics_lights = n_traffics_lights;
		this.who_moved = new boolean[n_traffics_lights];
		for(int i=0;i<this.n_traffics_lights;i++) this.who_moved[i] = false;
	}
	
	public int get_time(){
		return this.cCycles;
	}
	
	public synchronized void agent_moved(int ID){
		this.who_moved[ID] = true;
	}
	
	public synchronized boolean can_agent_move(int ID){
		return !this.who_moved[ID];
	}
	
	public synchronized void restart_moves(){
		for(int i=0;i<this.n_traffics_lights;i++){
			this.who_moved[i] = false;
		}
	}
	
	public synchronized boolean did_all_agents_move(){
		for(int i=0;i<this.n_traffics_lights;i++){
			if(this.who_moved[i] == false) return false;
		}
		return true;
	}
	
	public void run(){
		
		while(cCycles < nCycles){
			

			//Update the board
			while(!this.did_all_agents_move()); // Busy waiting
			
			this.restart_moves();
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

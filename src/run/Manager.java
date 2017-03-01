package run;

import java.awt.Point;

import graphics.Board;
import graphics.Frame;

public class Manager {
	private Frame f;
	private Board b;
	private Stepper p;
	
	public Manager(Frame f, Board b, Stepper p){
		this.f = f;
		this.b = b;
		this.p = p;
	}
	
	public int get_system_time(){
		return this.p.get_time();
	}
	
	public void deliver_message(String s){
		this.f.send_info(s, this.p.get_time());
	}
	
	public void change_traffic_light_state(Point p, int color){
		b.update(color, p.x, p.y);
	}
}

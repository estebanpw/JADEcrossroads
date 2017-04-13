package run;

import java.awt.Point;

import agents.Car;
import agents.TrafficLight;
import graphics.Board;
import graphics.Frame;

public class Manager {
	private Frame f;
	private Board b;
	private Stepper p;
	private String [] TFAgents;
	private Point [] TFpositions;
	private String [] cars;
	private int total_agents;
	
	
	public Manager(Frame f, Board b, Stepper p){
		this.f = f;
		this.b = b;
		this.p = p;
	}
	
	public int get_board_width(){
		return this.b.get_width();
	}
	
	public int get_board_height(){
		return this.b.get_height();
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
	
	public boolean is_car_outside(Point position){
		if(position.x >= 0 && position.y >= 0 && position.x < this.b.get_width() && position.y < this.b.get_height()){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean is_car_beyond_tf (Point position, Point direction){
		int x = direction.x;
		int y = direction.y;
		
		if(x == 0 && y == 1){
			if(position.x > (this.b.get_height()/2)-1) return true; else return false;
		}else if (x == 0 && y == -1){
		    if(position.y < (this.b.get_height()/2)+1) return true; else return false;
		}else if (x == 1 && y == 0) {
		    if(position.x < (this.b.get_width()/2)+1) return true; else return false;
		}else if (x == -1 && y == 0){
			if(position.x > (this.b.get_width()/2)-1) return true; else return false;
		}
		
		return false;
	}
	
	
	public synchronized void agent_moved(int ID){
		this.p.agent_moved(ID);
	}
	
	public synchronized boolean can_agent_move(int ID){
		return this.p.can_agent_move(ID);
	}
	
	public synchronized void restart_moves(){
		this.p.restart_moves();
	}
	
	public boolean can_car_move_to(Point to){
		if(b.get_pos_value(to.x, to.y).p != 1) return true; else return false;
	}
	
	public void move_car(Point from, Point to){
		
		b.update(4, from.x, from.y);
		b.update_p(0, from.x, from.y);
		
		b.update(1, to.x, to.y);
		b.update_p(1, to.x, to.y);
	}
	
	public void set_agents(String [] TFL, String [] CARS){
		this.TFAgents = TFL;
		this.cars = CARS; 
	}
	
	public void set_positions(Point [] TFlights){
		this.TFpositions = TFlights;
	}
	
	public String[] get_traffic_lights_AID(){
		return this.TFAgents;
	}
	
	public String[] get_cars_AID(){
		return this.cars;
	}
}

package agents;
import java.awt.Point;
import common.Common;

import run.Manager;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Car extends Agent{
	
	private Manager m;
	private Point pos;
	private Point direction;
	private int ID;

	
	protected void setup(){
		this.m = (Manager) this.getArguments()[0];
		this.m.deliver_message("Car Agent "+getLocalName()+" started");
		this.pos = (Point) this.getArguments()[1];
		this.direction = (Point) this.getArguments()[2];
		this.ID = (int) this.getArguments()[3];
		m.deliver_message("CAR HAS ID " + this.ID);
		
		addBehaviour(new CarBehaviour(this, this.m, this.pos, this.direction, this.ID));
	}
	
	protected void takeDown(){
		this.m.deliver_message("Car Agent "+getLocalName()+" saying goodbye");
	}
		
	
	class CarBehaviour extends CyclicBehaviour{
		
		Manager m;
		Point position, original_position;
		Point direction;
		int steps_counter;
		int last_time;
		boolean can_move, someone_before;
		int ID;
		
		public CarBehaviour(Agent a, Manager m, Point pos, Point direction, int ID){
			super(a);
			this.m = m;
			this.position = pos;
			this.original_position = new Point(pos.x, pos.y);
			this.direction = direction;
			can_move = true;
			this.ID = ID;
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			// Internal ticking
			
			Point new_pos = new Point(this.position.x + this.direction.x, this.position.y + this.direction.y);
			if(new_pos.x < 0) new_pos.x = this.m.get_board_width()-1;
			if(new_pos.y < 0) new_pos.y = this.m.get_board_height()-1;
			if(new_pos.x == this.m.get_board_width()) new_pos.x = 0;
			if(new_pos.y == this.m.get_board_height()) new_pos.y = 0;
			if(this.m.can_car_move_to(new_pos)) someone_before = false; else someone_before = true;
			
			// check if car can move
			if(!someone_before && last_time != this.m.get_system_time()){
				
				can_move = true;
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				// Send direction and ask for light status
				msg.setContent("" + this.direction.x + "," + this.direction.y);
				for(String tfl_id : this.m.get_traffic_lights_AID()){
					msg.addReceiver(new AID(tfl_id, AID.ISLOCALNAME));
				}
				send(msg);
				m.deliver_message("CAR "+this.ID+" sent message: "+msg.getContent());
				
				ACLMessage response = receive();
				if(response != null){
					// Only the semaphore that controlls this direction should answer
					m.deliver_message("CAR "+this.ID+" at "+this.position.x+","+this.position.y+" received message with: "+response.getContent());
					String []parsed_answer = response.getContent().split(",");
					if(parsed_answer[0].equals("GREEN")){
						this.can_move = true;
					}else{
						Point semaphore_point = new Point(Integer.parseInt(parsed_answer[1]), Integer.parseInt(parsed_answer[2]));
						if(1 == Common.manhattanDistance(this.position.x, this.position.y, semaphore_point.x, semaphore_point.y)) this.can_move = false; else this.can_move = true;
					}
				}else{
					block();
				}
			}else{
				can_move = false;
			}
			
			
			
			
			
			if(can_move){
				this.m.move_car(this.position, new_pos);
				this.position.x = this.position.x + this.direction.x;
				this.position.y = this.position.y + this.direction.y;
			}
							
			
			
			if(this.m.is_car_outside(this.position)){ 
				this.m.move_car(this.position, this.original_position);
				this.position.x = this.original_position.x;
				this.position.y = this.original_position.y;
			}
			last_time = this.m.get_system_time();
			
		}
	}
}

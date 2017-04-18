package agents;
import java.awt.Point;

import run.Manager;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class TrafficLight extends Agent{
	
	private Manager m;
	private Point pos;
	private Point dir;

	
	protected void setup(){
		this.m = (Manager) this.getArguments()[0];
		this.m.deliver_message("Agent "+getLocalName()+" started");
		this.m.deliver_message("Agent "+(int) this.getArguments()[3]+" ID");
		this.pos = (Point) this.getArguments()[1]; 
		
		addBehaviour(new TrafficLightBehaviour(this, this.m, this.pos, (int) this.getArguments()[2], (int) this.getArguments()[3], (Point) this.getArguments()[4]));
	}
	
	protected void takeDown(){
		this.m.deliver_message("Agent "+getLocalName()+" saying goodbye");
		
	}
		
	
	class TrafficLightBehaviour extends CyclicBehaviour{
		
		Manager m;
		int mod, last, current;
		boolean light_state;
		Point position;
		Point dir;
		int ID;
		
		public TrafficLightBehaviour(Agent a, Manager m, Point pos, int init_state, int ID, Point dir){
			super(a);
			this.m = m;
			this.mod = 10;
			if(init_state == 0) this.light_state = false; else this.light_state = true;
			this.position = pos;
			this.last = -1;
			this.ID = ID;
			this.dir = dir;
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			// Internal ticking
			
						
			if(m.get_system_time() % this.mod == 0 && last != m.get_system_time()){
				light_state = !light_state;
				last = m.get_system_time();
				if(light_state == true){
					//m.deliver_message("Traffic light "+getLocalName()+" switched to green");
					m.change_traffic_light_state(this.position, 2);
				}
				if(light_state == false){
					//m.deliver_message("Traffic light "+getLocalName()+" switched to red");
					m.change_traffic_light_state(this.position, 6);
				}
			}
			// Requesting and sending information
			ACLMessage msg = receive();
			if(msg != null){
				// If there is a message
				String[] splitted = msg.getContent().split(",");
				Point receiving_direction = new Point(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
				//m.deliver_message("TF "+this.ID+" received message: "+msg.getContent());
				if(this.dir.equals(receiving_direction)){
					ACLMessage response = new ACLMessage(ACLMessage.INFORM);
					response.addReceiver(msg.getSender());
					if(light_state) response.setContent("GREEN,"+this.position.x+","+this.position.y); else response.setContent("RED,"+this.position.x+","+this.position.y);
					m.deliver_message("TF "+this.ID+" sending response: "+response.getContent());
					send(response);
				}
			}
				
			this.m.agent_moved(this.ID);
		}
	}
}

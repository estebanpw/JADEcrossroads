package agents;
import java.awt.Point;

import run.Manager;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.Agent;

public class TrafficLight extends Agent{
	
	private Manager m;
	private Point pos;

	
	protected void setup(){
		this.m = (Manager) this.getArguments()[0];
		this.m.deliver_message("Agent "+getLocalName()+" started");
		this.pos = (Point) this.getArguments()[1];
		
		addBehaviour(new TrafficLightBehaviour(this, this.m, this.pos, (int) this.getArguments()[2]));
	}
	
	protected void takeDown(){
		this.m.deliver_message("Agent "+getLocalName()+" saying goodbye");
		
	}
		
	
	class TrafficLightBehaviour extends CyclicBehaviour{
		
		Manager m;
		int mod, last, current;
		boolean light_state;
		Point position;
		
		public TrafficLightBehaviour(Agent a, Manager m, Point pos, int init_state){
			super(a);
			this.m = m;
			this.mod = 10;
			if(init_state == 0) this.light_state = false; else this.light_state = true;
			this.position = pos;
			this.last = -1;
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			if(m.get_system_time() % this.mod == 0 && last != m.get_system_time()){
				light_state = !light_state;
				last = m.get_system_time();
				if(light_state == true){
					m.deliver_message("Traffic light "+getLocalName()+" switched to green");
					m.change_traffic_light_state(this.position, 2);
				}
				if(light_state == false){
					m.deliver_message("Traffic light "+getLocalName()+" switched to red");
					m.change_traffic_light_state(this.position, 6);
				}
			}
		}
	}
}

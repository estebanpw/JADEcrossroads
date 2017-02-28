package agents;
import run.Manager;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.Agent;

public class TrafficLight extends Agent{
	
	private Manager m;

	
	protected void setup(){
		this.m = (Manager) this.getArguments()[0];
		this.m.deliver_message("Agent "+getLocalName()+" started");
		addBehaviour(new TrafficLightBehaviour(this, this.m));
	}
	
	protected void takeDown(){
		this.m.deliver_message("Agent "+getLocalName()+" saying goodbye");
		
	}
		
	
	class TrafficLightBehaviour extends CyclicBehaviour{
		
		Manager m;
		int mod, last, current;
		boolean light_state;
		
		public TrafficLightBehaviour(Agent a, Manager m){
			super(a);
			this.m = m;
			this.mod = 10;
			this.light_state = false;
			this.last = -1;
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			if(m.get_system_time() % this.mod == 0 && last != m.get_system_time()){
				light_state = !light_state;
				last = m.get_system_time();
				m.deliver_message("Changed state");
				
			}
		}
	}
}

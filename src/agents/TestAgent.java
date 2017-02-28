package agents;
import run.Manager;
import jade.core.Agent;

public class TestAgent extends Agent{

	
	private Manager m;

	
	protected void setup(){
		this.m = (Manager) this.getArguments()[0];
		this.m.deliver_message("Agent "+getLocalName()+" started");
		
	}
	
	protected void takeDown(){
		this.m.deliver_message("Agent "+getLocalName()+" saying goodbye");
		
	}
}

package run;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import graphics.Board;
import graphics.Frame;




public class Starter {

	public static void main(String[] args) throws StaleProxyException {

				
		//Configuration variables
		int x = 25, y = 25;
		int pixelsPerCell = 20;
		int maxCycles = 100000;
		
		//System objects to model the board
		Board b = new Board(x, y);
		generate_crossroad(b);
		Frame f = new Frame();
		
		//Initialize frame and board
		f.start(b, pixelsPerCell);
		
		//Create thread to run the system through iterations
		Stepper stp = new Stepper(b, f, maxCycles);
		
		//Manager to handle board, frame and stepper at once
		Manager m = new Manager(f, b, stp);
		
		//Launch container

		//Get a hold on JADE runtime
		Runtime rt = Runtime.instance();

		//Exit the JVM when there are no more containers around
		rt.setCloseVM(true);
		m.deliver_message("JADE-Runtime created");

		//Create a default profile
		int port = 1200;
		Profile profile = new ProfileImpl(null, port, null);
		m.deliver_message("JADE-Created profile on port "+port);

		
		m.deliver_message("JADE-Launched platform");
		jade.wrapper.AgentContainer mainContainer = rt.createMainContainer(profile);

		//Now set the default Profile to start a container
		ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
		jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
		m.deliver_message("JADE-Container created");

		/*
		AgentController rma = mainContainer.createNewAgent("rma","jade.tools.rma.rma", new Object[0]);
		m.deliver_message("JADE-Launching Remote Management Agent");
		rma.start();
		*/
		
		//Parameters as objects for the constructor
		Object objs[] = new Object[1];
		objs[0] = (Object) m;
		
		/*
		//Create test agent
		AgentController ag1 = mainContainer.createNewAgent("John", "agents.TestAgent", objs);
		ag1.start();
		*/
		
		//Create traffic light
		AgentController ag2 = mainContainer.createNewAgent("TL1", "agents.TrafficLight", objs);
		ag2.start();
		
		//Start thread runner
		stp.run();
	
	}
	
	public static void generate_crossroad(Board b){
		for(int i=0;i<b.get_width();i++){
			for(int j=0;j<b.get_height();j++){
				b.update(2, i, j);
				b.update_p(0, i, j);
			}
		}
		for(int i=0;i<b.get_width();i++) b.update(4, i, b.get_height()/2);
		for(int i=0;i<b.get_width();i++) b.update(4, i, b.get_height()/2 + 1);
		
		for(int i=0;i<b.get_height();i++) b.update(4, b.get_width()/2, i);
		for(int i=0;i<b.get_height();i++) b.update(4, b.get_width()/2 + 1, i);
	}
}



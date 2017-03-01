package run;

import java.awt.Point;

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
		int n_traffic_lights = 4;
		
		//Traffic lights will have positions
		Point []pos_tfs = new Point[n_traffic_lights];
		
		
		//System objects to model the board
		Board b = new Board(x, y);
		generate_crossroad(b, pos_tfs, n_traffic_lights);
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
		
		
		/*
		//Create test agent
		AgentController ag1 = mainContainer.createNewAgent("John", "agents.TestAgent", objs);
		ag1.start();
		*/
		
		//Create traffic light
		AgentController ag2 = mainContainer.createNewAgent("TL1", "agents.TrafficLight", new Object[]{m, pos_tfs[0], (int) 1});
		ag2.start();
		
		AgentController ag3 = mainContainer.createNewAgent("TL2", "agents.TrafficLight", new Object[]{m, pos_tfs[1], (int) 1});
		ag3.start();
		
		AgentController ag4 = mainContainer.createNewAgent("TL3", "agents.TrafficLight", new Object[]{m, pos_tfs[2], (int) 0});
		ag4.start();
		
		AgentController ag5 = mainContainer.createNewAgent("TL4", "agents.TrafficLight", new Object[]{m, pos_tfs[3], (int) 0});
		ag5.start();
		
		//Start thread runner
		stp.run();
	
	}
	
	public static void generate_crossroad(Board b, Point []pos_tfs, int n_traffic_lights){
		for(int i=0;i<b.get_width();i++){
			for(int j=0;j<b.get_height();j++){
				b.update(5, i, j);
				b.update_p(0, i, j);
			}
		}
		//Paint crossroads
		for(int i=0;i<b.get_width();i++) b.update(4, i, b.get_height()/2);
		for(int i=0;i<b.get_width();i++) b.update(4, i, b.get_height()/2 + 1);
		
		for(int i=0;i<b.get_height();i++) b.update(4, b.get_width()/2, i);
		for(int i=0;i<b.get_height();i++) b.update(4, b.get_width()/2 + 1, i);
		
		//Add traffic lights
		pos_tfs[0] = new Point(b.get_width()/2 - 1, b.get_height()/2 + 1);
		b.update_p(2, pos_tfs[0].x, pos_tfs[0].y);
		
		pos_tfs[1] = new Point(b.get_width()/2 + 2, b.get_height()/2);
		b.update_p(2, pos_tfs[1].x, pos_tfs[1].y);
		
		pos_tfs[2] = new Point(b.get_width()/2, b.get_height()/2 - 1);
		b.update_p(2, pos_tfs[2].x, pos_tfs[2].y);
		
		pos_tfs[3] = new Point(b.get_width()/2 + 1, b.get_height()/2 + 2);
		b.update_p(2, pos_tfs[3].x, pos_tfs[3].y);
		
	}
}



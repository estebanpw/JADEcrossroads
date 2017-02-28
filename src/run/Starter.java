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
		int x = 100, y = 80;
		int pixelsPerCell = 10;
		int maxCycles = 100000;
		
		//System objects to model the board
		Board b = new Board(x, y);
		Frame f = new Frame();
		
		
		//Initialize frame and board
		f.start(b, pixelsPerCell);
		
		//Create thread to run the system through iterations
		Stepper stp = new Stepper(b, f, maxCycles);
		
		
		//Launch container

		//Get a hold on JADE runtime
		Runtime rt = Runtime.instance();

		//Exit the JVM when there are no more containers around
		rt.setCloseVM(true);
		f.send_info("JADE-Runtime created", stp.get_time());

		//Create a default profile
		int port = 1200;
		Profile profile = new ProfileImpl(null, port, null);
		f.send_info("JADE-Created profile on port "+port, stp.get_time());

		
		f.send_info("JADE-Launched platform", stp.get_time());
		jade.wrapper.AgentContainer mainContainer = rt.createMainContainer(profile);

		//Now set the default Profile to start a container
		ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
		jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
		f.send_info("JADE-Container created", stp.get_time());

		
		AgentController rma = mainContainer.createNewAgent("rma","jade.tools.rma.rma", new Object[0]);
		f.send_info("JADE-Launching Remote Management Agent", stp.get_time());
		rma.start();
		
		//Start thread runner
		stp.run();
	
	}
	
	

}

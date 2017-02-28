package graphics;

import java.awt.*;
import javax.swing.*;

import common.Common;
 
/* FrameDemo.java requires no other files. */
public class Frame {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
	
	class PaintPanel extends JPanel{
		Polygon p[][];
		Polygon bg;
		Color lc[][]; //line color
		Color bglc[][]; //line color
		int width, height;
		int cellSize;
		public PaintPanel(int width, int height, int cellSize){
			super();
			p = new Polygon[width][height];
			lc = new Color[width][height];
			bglc = new Color[width][height];
			this.width = width;
			this.height = height;
			this.cellSize = cellSize;
		}
		public void paintComponent(Graphics g){

			super.paintComponent(g);
			
			//Use double buffer strategy to paint
			Image offscreen = createImage(width*cellSize, height*cellSize);
			Graphics gg = offscreen.getGraphics();
			//Paint background
			
			for(int x=0; x<width; x++){
				for(int y=0; y<height; y++){
					gg.setColor(bglc[x][y]);
					bg = Common.idToPolygon(0, cellSize, cellSize);
					for(int len=0; len<bg.npoints; len++){
						bg.xpoints[len] += (int)x * cellSize;
						bg.ypoints[len] += (int)y * cellSize;
					}
					gg.fillPolygon(bg);
				}
			}
			
			//Paint figures
			for(int x=0; x<width; x++){
				for(int y=0; y<height; y++){
					if(p[x][y] != null){
						gg.setColor(lc[x][y]);
						for(int len=0; len<p[x][y].npoints; len++){
							p[x][y].xpoints[len] += (int)x * cellSize;
							p[x][y].ypoints[len] += (int)y * cellSize;
							
						}
						gg.fillPolygon(p[x][y]);
					}
				}
			}
			
			//Now paint the buffered image
			g.drawImage(offscreen, 0, 0, this);
		}
		public void colorBackground(int x, int y, Color c){
			bglc[x][y] = c;
		}
		
		public void updateColor(int x, int y, Color c){
			lc[x][y] = c;
		}
		
		public void updatePoly(int x, int y, int id){
			p[x][y] = Common.idToPolygon(id, cellSize, cellSize);
		}
	};
	//For display
	private Board board;
	private JFrame frame = new JFrame("Crossroads simulation using multiagents");
	private PaintPanel panelHolder;
	//Panels to hold subpanels and components
	private JPanel Center, East;
	//Panels to hold subcomponents
	private JPanel DELAY_THING;
	//Components
	private JLabel tempDisplay, cycleLabel, delayLabel;
	private JTextArea infoLabel;
	private JSlider msDelay;
	private JScrollPane sp;
	
	//For graphic size computation
	private int cellSize;
	
    public void create() {
        //Create and set up the window
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setLayout(new BorderLayout());
    	
    	//Real panels
    	Center = new JPanel();
    	East = new JPanel();
    	
    	//Subcomponents panels
    	DELAY_THING = new JPanel();
    	DELAY_THING.setLayout(new BorderLayout());
    	
    	//Labels and text
    	cycleLabel = new JLabel("Delta(T): ");
    	delayLabel = new JLabel("Adjust the delay between iterations in milliseconds");
    	infoLabel = new JTextArea(5, 30);
    	infoLabel.setText("@0: init");
    	infoLabel.setLineWrap(true);
    	infoLabel.setEditable(false);
    	infoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	
    	//Scrollpanes
    	sp = new JScrollPane(infoLabel);
    	sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	sp.setPreferredSize(new Dimension(400,200));
    	
    	//Delay slider
    	msDelay = new JSlider(JSlider.HORIZONTAL, 5, 1000, 500);
    	msDelay.setMajorTickSpacing(200);
    	msDelay.setMinorTickSpacing(50);
    	msDelay.setPaintTicks(true);
    	msDelay.setPaintLabels(true);
    	msDelay.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
    	
        
        
        //Subcomponents addition to subpanels
        DELAY_THING.add(delayLabel, BorderLayout.NORTH);
        DELAY_THING.add(msDelay, BorderLayout.SOUTH);
        
        //Add subpanels to panels
        East.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        East.setPreferredSize(new Dimension(400,frame.getHeight()));
        East.add(cycleLabel);
        East.add(DELAY_THING);
        East.add(sp);
        
        //Create panel that paints
        panelHolder = new PaintPanel(board.get_width(),board.get_height(), this.cellSize);
    	panelHolder.setPreferredSize(new Dimension(this.board.get_width()*cellSize, this.board.get_height()*cellSize));
    	
    	//Set everything to white for the first time
    	for(int i=0; i<board.get_width(); i++){
    		for(int j=0; j<board.get_height(); j++){
    			panelHolder.colorBackground(i, j, Color.white);
    		}
    	}
    	panelHolder.repaint();
    	
    	//Finally add panels to frame
    	Center.add(panelHolder);
    	frame.add(Center, BorderLayout.CENTER);
    	frame.add(East, BorderLayout.EAST);
    	
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    
    public void updateTemp(int t){
    	tempDisplay.setText(Integer.toString(t));
    }
    
    
    public void setCycle(int c){
    	this.cycleLabel.setText("Current iteration: "+c);
    }
    
    public void update(){
    	

		for(int i=0; i<board.get_width(); i++){
    		for(int j=0; j<board.get_height(); j++){
  
    			Cell c = board.get_pos_value(i, j); 
    			if(c != null && c.v > 0){
    				panelHolder.updateColor(i, j, Common.lineColorIdEquivalence(c.v));
    				panelHolder.updatePoly(i, j, c.p);
    			}else{
    				panelHolder.updatePoly(i, j, -1);
    			}
    			
    			//panelHolder[i][j].setBackground(Common.tempColorEquivalence(board.getTempOfCell(i, j), max_t, min_t));
    			//panelHolder[i][j].lc = Common.lineColorIdEquivalence(board.getCell(i, j));
    			//panelHolder[i][j].p = Common.idToPolygon(board.getCell(i, j), panelHolder[i][j].getWidth(), panelHolder[i][j].getHeight());
        		
    		}
    	}
		panelHolder.repaint();

    }
    
    
    public void start(Board b, int sizeOfCell) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
    	cellSize = sizeOfCell;
    	board = b;
    	if(board.get_height() != board.get_width() || board.get_height() % sizeOfCell != 0){
    		//throw new RuntimeException("Board dimensiones must be equal and multiples of the cell size");
    	}
        create();
    }
    
    public void send_info(String s, int year){
    	infoLabel.append("\n@"+year+": "+s);
    }
    
    public int getMSDelay(){
    	return msDelay.getValue();
    }
}
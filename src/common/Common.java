package common;

import java.awt.Color;
import java.awt.Polygon;

public class Common {
	
	
	public static int randomWithRange(int min, int max){
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}
	
	public static double uniRandomWithRange(double min, double max){
		   double range = (max - min) + 1;     
		   return (int)(Math.random() * range) + min;
	}
	
	public static int manhattanDistance(int x1, int y1, int x2, int y2){
		return (Math.abs(x1-x2) + Math.abs(y1-y2));
	}
		
	public static Color getBgColor(){
		return new Color(0.9f, 0.9f, 0.9f);
	}
	

	public static Polygon idToPolygon(int id, int width, int height){
		Polygon p = new Polygon();

		switch(id){
		case 0: {
			//A background square
			p.addPoint(1, 1);
			p.addPoint(width-1, 1);
			p.addPoint(width-1, height-1);
			p.addPoint(1, height-1);
			return p;
			}
		case 1: {
			//a triangle
			p.addPoint(1,height-1);
			p.addPoint(width/2,0);
			p.addPoint(width-1,height-1);
			return p;
			
			}
		case 2: {
			//a square
			p.addPoint(1, 1);
			p.addPoint(width-1, 1);
			p.addPoint(width-1, height-1);
			p.addPoint(1, height-1);
			
			return p;
			}
		default: return null;
		}
		
	}

	public static Color lineColorIdEquivalence(int id){
    	switch(id){
    	case 0: return Color.white;
    	case 1: return Color.magenta;
    	case 2: return Color.green;
    	case 3: return Color.cyan;
    	case 4: return Color.black;
    	case 5: return Color.gray;
    	case 6: return Color.red;
    	}
    	return Common.getBgColor();
    }
	
	public static Color backgroundColorIdEquivalence(int id){
    	switch(id){
    	case 0: return Color.white;
    	case 1: return Color.black;
    	case 2: return Color.green;
    	case 3: return Color.gray;
    	case 4: return Color.red;
    	case 5: return Color.cyan;
    	case 6: return Color.magenta;
    	}
    	return Color.magenta;
    }


}

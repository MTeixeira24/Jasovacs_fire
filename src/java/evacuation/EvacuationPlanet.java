package evacuation;

import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;

import java.util.logging.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;


public class EvacuationPlanet extends Environment {
	
    private EvacuationGUI gui;
    private EvacuationModel model = new EvacuationModel(20,20,2);
    
    boolean running = true;
    
    @Override
    public void init(String[] args) {
    	initWorld();
    }
    
    @Override
    public boolean executeAction(String ag, Structure action) {
    	Random r = new Random();
    	int x = r.nextInt(2) - 1, y = r.nextInt(2) - 1;
    	int agId = (Integer.parseInt(ag.substring(5))) - 1;
    	Location l = model.getAgPos(agId);
    	 /*try { Thread.sleep(100);}  catch (Exception e) {}*/
    	 if (action.getFunctor().equals("randomwalk")) {
    		 
    		 model.setAgPos(agId, l.x+x, l.y+y);
    	 }
    	return true;
    }
    
    @Override
    public void stop() {
        running = false;
        super.stop();
    }
    
    public void initWorld() {
    	//setModel
    	clearPercepts();
    	gui = new EvacuationGUI(model);
    	gui.setEnv(this);
    	
    }
}

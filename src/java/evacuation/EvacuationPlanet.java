package evacuation;

import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;

import java.util.logging.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

/*
 * Classe que funciona como um controller
 * entre o modelo e a view
 * Gere pedidos percepções dos agentes 
 */
public class EvacuationPlanet extends Environment {
	
	private Logger logger = Logger.getLogger("");
	
    private EvacuationGUI gui;
    private EvacuationModel model;
    
    boolean running = true;
    
    
    
    Term                    up       = Literal.parseLiteral("do(up)");
    Term                    down     = Literal.parseLiteral("do(down)");
    Term                    right    = Literal.parseLiteral("do(right)");
    Term                    left     = Literal.parseLiteral("do(left)");
    Term                    skip     = Literal.parseLiteral("do(skip)");
    Term                    randomwalk     = Literal.parseLiteral("randomwalk");
    
    public enum Move {
        UP, DOWN, RIGHT, LEFT
    };
    
    /*Começar aqui*/
    @Override
    public void init(String[] args) {
    	initWorld();
    }
    
    /*Executar as acçoes dos agentes*/
    @Override
    public boolean executeAction(String ag, Structure action) {
    	boolean result = false;
    	try {
    		Random r = new Random();
        	int x = r.nextInt(2) - 1, y = r.nextInt(2) - 1;
        	int agId = (Integer.parseInt(ag.substring(9))) - 1;
        	
        	Location l = model.getAgPos(agId);
        	 /*try { Thread.sleep(100);}  catch (Exception e) {}*/
        	 if (action.equals(randomwalk)) {
        		 model.setAgPos(agId, l.x+x, l.y+y);
        		 result = true;
        	 }else if (action.equals(up)) {
                 result = model.move(Move.UP, agId);
             } else if (action.equals(down)) {
                 result = model.move(Move.DOWN, agId);
             } else if (action.equals(right)) {
                 result = model.move(Move.RIGHT, agId);
             } else if (action.equals(left)) {
                 result = model.move(Move.LEFT, agId);
             } else if (action.equals(skip)) {
                 result = true;
             }else {
            	 logger.info("executing: " + action + ", but not implemented!");
             }
        	 if (result) {
                 updateAgPercept(agId);
                 return true;
             }
    	} catch (InterruptedException e) {
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error executing " + action + " for " + ag, e);
        }
        return false;
    	
    }
    
    @Override
    public void stop() {
        running = false;
        super.stop();
    }
    
    /*Chamada de modelos e views*/
    public void initWorld() {
    	//setModel
    	try {
			model = EvacuationModel.world1();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	clearPercepts();
    	gui = new EvacuationGUI(model);
    	gui.setEnv(this);
    	
    }
    
    /*Controlo das percepções dos agentes*/
    private void updateAgPercept(int ag) {
        updateAgPercept("evacuador" + (ag + 1), ag);
    }

    private void updateAgPercept(String agName, int ag) {
        clearPercepts(agName);
        // its location
        Location l = model.getAgPos(ag);
        addPercept(agName, Literal.parseLiteral("pos(" + l.x + "," + l.y + ")"));


        // what's around
        /*
        updateAgPercept(agName, l.x - 1, l.y - 1);
        updateAgPercept(agName, l.x - 1, l.y);
        updateAgPercept(agName, l.x - 1, l.y + 1);
        updateAgPercept(agName, l.x, l.y - 1);
        updateAgPercept(agName, l.x, l.y);
        updateAgPercept(agName, l.x, l.y + 1);
        updateAgPercept(agName, l.x + 1, l.y - 1);
        updateAgPercept(agName, l.x + 1, l.y);
        updateAgPercept(agName, l.x + 1, l.y + 1);*/
    }


  /*  private void updateAgPercept(String agName, int x, int y) {
        if (model == null || !model.inGrid(x,y)) return;
        if (model.hasObject(WorldModel.OBSTACLE, x, y)) {
            addPercept(agName, Literal.parseLiteral("cell(" + x + "," + y + ",obstacle)"));
        } else {
            if (model.hasObject(WorldModel.GOLD, x, y)) {
                addPercept(agName, Literal.parseLiteral("cell(" + x + "," + y + ",gold)"));
            }
            if (model.hasObject(WorldModel.ENEMY, x, y)) {
                addPercept(agName, Literal.parseLiteral("cell(" + x + "," + y + ",enemy)"));
            }
            if (model.hasObject(WorldModel.AGENT, x, y)) {
                addPercept(agName, Literal.parseLiteral("cell(" + x + "," + y + ",ally)"));
            }
        }
    }*/
}

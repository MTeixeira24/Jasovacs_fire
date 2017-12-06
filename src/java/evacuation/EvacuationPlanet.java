package evacuation;


import jason.asSemantics.Agent;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import jason.stdlib.foreach;

import java.util.logging.*;
import java.util.*;

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
    Term					getPosition = Literal.parseLiteral("agentGetPosition");
    Term					exit = Literal.parseLiteral("exit");
    Term                    randomwalk     = Literal.parseLiteral("randomwalk");
    Term					spread		=Literal.parseLiteral("spread");
    
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
    	boolean result = false, da = false;
    	int x=0;
    	int y=0;
    	try {
    		Random r = new Random();
    		int agId;
        	if(ag.contains("danger")) {
        		agId = -1;//(Integer.parseInt(ag.substring(5))) - 1;
        		da = true;
        	}else {
        		agId = (Integer.parseInt(ag.substring(9))) - 1;
        	}
        	Location l = model.getAgPos(agId);
    		boolean hashObj=true, isAgInPos=false, negative = true;
    		if(!da) {
    			Object cenas= new Object();
    			synchronized (cenas) {
		    		do {
		    			x = r.nextInt(3)-1;
		    			y = r.nextInt(3)-1;
		    			negative = (l.x+x < 0 || l.y - y < 0) ? true : false;
		    			hashObj=model.hasObject(EvacuationModel.OBSTACLE,l.x+x, l.y+y);
		    			
		    			if(model.getAgAtPos(x+l.x, y+l.y)==-1) {
		    				isAgInPos=true;
		    			}else {
		    				isAgInPos=false;
		    			}
		        	//Verificar se outro agente já ocupa a mesma posicao
		    		}while(hashObj || !isAgInPos || negative);
    			}
    		}
        	 /*try { Thread.sleep(100);}  catch (Exception e) {}*/
    		 if (action.getFunctor().equals("set_exit_location")) {
    			 System.out.println(action.getTerm(0).toString());
    			 String exitx = action.getTerm(0).toString();
    			 String exity = action.getTerm(1).toString();
    			 /*Update do local de saída*/
    			 removePerceptsByUnif(ag, Literal.parseLiteral("exit_location(_,_)"));
    			 addPercept(ag, Literal.parseLiteral("exit_location("+exitx+","+exity+")"));
    			 result = true;
    		 }else if (action.equals(randomwalk)) {
        		 if(!hashObj) {
        			 model.setAgPos(agId, l.x+x, l.y+y);
        		 }
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
             }else if(action.equals(getPosition)){
            	 updateAgPercept(agId);
            	 result = true;
             }else if(action.equals(spread)) {
            	 result = model.spreadFire();
            	 result = true;
             }else if(action.equals(exit)){
            	 model.setAgPos(agId, 0,0);
            	 String plist = consultPercepts(ag).toString();
            	 System.out.println(plist);
            	 plist = plist.substring(1, plist.length() - 1);
            	 System.out.println(plist);
            	 String[] perceps = plist.split(", ");
            	 System.out.println(perceps);
            	 String eloc = "", pos = "";
            	 boolean knowExit = false;
            	 for(int i = 0; i < perceps.length; i++) {
            		 if(perceps[i].contains("exit_location")) {
            			 eloc = perceps[i];
            		 }
            		 if(perceps[i].contains("pos")) {
            			 pos = perceps[i];
            		 }
            		 if(perceps[i].equals("knowExit(yes)")) {
            			 knowExit = true;
            		 }
            	 }
            	 Object [] stats = new Object[] {ag,pos,eloc,knowExit};
            	 model.exitdata.add(stats);
            	 gui.updateTable(stats);
            	 result = true;
             }else{
            	 logger.info("executing: " + action + ", but not implemented!");
             }
        	 if (result && !da) {
                 updateAgPercept(agId);
                 return true;
             }else if(result && da) {
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
        //clearPercepts(agName);
     	removePerceptsByUnif(agName, Literal.parseLiteral("pos(_,_)"));
     	removePerceptsByUnif(agName, Literal.parseLiteral("cell(_,_,_)"));
     	removePerceptsByUnif(agName, Literal.parseLiteral("cell(_,_,_,_)"));
        // its location
        Location l = model.getAgPos(ag);
        addPercept(agName, Literal.parseLiteral("pos(" + l.x + "," + l.y + ")"));
        //O que há a volta
        updateAgPercept(agName, l, 8);
    }
    
    //Obter informação na zona L até um raio radius
    private void updateAgPercept(String agName, Location agloc, int radius) {
    	if(model == null)
    		return;
    	generateVisibilityGrid(agName, agloc, radius); //Entrypoint para cálculo de visibilidade
    }
    
    
    private void generateVisibilityGrid(String name, Location l, int r) {
    	int steps = 16; //Incrementos para o ângulo;
    	double angleInc = (2*Math.PI)/steps;
    	double xn,yn;
    	int xr,yr;
    	ArrayList<Location> points;
    	for(int i = 0; i < steps; i++) {
    		//Cálculo dos pontos finais do segmento de recta
    		xn = l.x + r*Math.cos(angleInc*i);
            yn = l.y + r*Math.sin(angleInc*i);
            xr = (int)Math.floor(xn);
            yr = (int)Math.floor(yn);
            if((xn - xr) >= 0.5) {
            	xr++;
            }
            if((yn - yr) >= 0.5) {
            	yr++;
            }
            //Obter uma linha rasterizada compatível com a grid
            points = generateRasterLine(l.x,l.y,xr,yr);
            //Procurar por óbstaculos e mapear visibilidade
            for(int j = 0; j < points.size(); j++) {
            	Location pt = points.get(j);
            	if(!model.inGrid(pt)) {
            		break;
            	}else {
            		//vgrid[agentCenter.x+(pt.x-l.x)][agentCenter.y+(pt.y-l.y)] = true;
            		if(model.hasObject(EvacuationModel.EXIT_INFO_UP, pt)) {
            			addPercept(name, Literal.parseLiteral("cell(" + pt.x + "," + pt.y + ",exit_sign, up)"));
            			addPercept(name, Literal.parseLiteral("seen_exit_sign(yes)"));
            		}
            			
            		if(model.hasObject(EvacuationModel.EXIT_INFO_DOWN, pt)) {
            			addPercept(name, Literal.parseLiteral("cell(" + pt.x + "," + pt.y + ",exit_sign, down)"));
            			addPercept(name, Literal.parseLiteral("seen_exit_sign(yes)"));
            		}
            			
            		if(model.hasObject(EvacuationModel.EXIT_INFO_LEFT, pt)) {
            			addPercept(name, Literal.parseLiteral("cell(" + pt.x + "," + pt.y + ",exit_sign, left)"));
            			addPercept(name, Literal.parseLiteral("seen_exit_sign(yes)"));
            		}
            			
            		if(model.hasObject(EvacuationModel.EXIT_INFO_RIGHT, pt)) {
            			addPercept(name, Literal.parseLiteral("cell(" + pt.x + "," + pt.y + ",exit_sign, right)"));
            			addPercept(name, Literal.parseLiteral("seen_exit_sign(yes)"));
            		}
            			
            		if(model.hasObject(EvacuationModel.DANGER, pt))
            			addPercept(name, Literal.parseLiteral("cell(" + pt.x + "," + pt.y + ",danger)"));
            		if(model.hasObject(EvacuationModel.EXIT, pt)) {
            			addPercept(name, Literal.parseLiteral("cell("+pt.x+","+pt.y+",exit)"));
            			addPercept(name, Literal.parseLiteral("knowExit(yes)"));
            		}
            		if(model.hasObject(EvacuationModel.OBSTACLE, pt)) 
            			break;
            	}
            }
    	}
    	if(model.hasObject(EvacuationModel.DANGER, l))
			addPercept(name, Literal.parseLiteral("cell(" + l.x + "," + l.y + ",danger)"));
    }
    
    private ArrayList<Location> generateRasterLine(int x1,int y1,int x2,int y2) {
    	ArrayList<Location> points = new ArrayList<Location>();
    	// delta of exact value and rounded value of the dependent variable
        int d = 0;
 
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
 
        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point
 
        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;
 
        int x = x1;
        int y = y1;
        if(dx >= dy) {
        	while(true) {
        		if(x == x2)
        			break;
        		x += ix;
        		d += dy2;
        		if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
        		points.add(new Location(x,y));
        	}
        }else {
        	while (true) {
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
                points.add(new Location(x,y));
            }
        }
    	return points;
    }
    
}

package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;

import evacuation.EvacuationModel;


public class get_oposite_point extends DefaultInternalAction {

	private static final long serialVersionUID = -5971750739571943813L;
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception{
		try {
			
			EvacuationModel model = EvacuationModel.get();
			int iagx = (int)((NumberTerm)terms[0]).solve();//Atual
	        int iagy = (int)((NumberTerm)terms[1]).solve();//Atual
	        int itox = (int)((NumberTerm)terms[2]).solve();//Danger
	        int itoy = (int)((NumberTerm)terms[3]).solve();//Danger
	      
	        
	        int d = 0, dx = Math.abs(iagx - itox),dy = Math.abs(iagy - itoy);
	 
	        int dx2 = 2 * dx; // slope scaling factors to
	        int dy2 = 2 * dy; // avoid floating point
	 
	        int ix = itox < iagx ? 1 : -1; // increment direction
	        int iy = itoy < iagy ? 1 : -1;
	 
	        int nx = iagx, x = iagx; //Começar o planeamento do caminho na posição do agente
	        int ny = iagy, y = iagy;
	        if(dx >= dy) {
	        	while(true) {
	        		if(!model.inGrid(nx, ny)) {
	        			x = x == 0 ? x + 1 : x;
	        			x = x == 49 ? x - 1 : x; 
	        			y = y == 0 ? y + 1 : y;
	        			y = y == 49 ? y - 1 : y;  
	        			break;
	        		}
	        			
	        		x = nx;
	        		y = ny;
	        		nx += ix;
	        		d += dy2;
	        		if (d > dx) {
	                    ny += iy;
	                    d -= dx2;
	                }
	        	}
	        }else {
	        	while (true) {
	        		if(!model.inGrid(nx, ny)) {
	        			x = x == 0 ? x + 1 : x;
	        			x = x == 49 ? x - 1 : x; 
	        			y = y == 0 ? y + 1 : y;
	        			y = y == 49 ? y - 1 : y;
	        			break;
	        		}
	                x = nx;
	                y = ny;
	                ny += iy;
	                d += dx2;
	                if (d > dy) {
	                    nx += ix;
	                    d -= dy2;
	                }
	            }
	        }
	         
	        
	       
	        if (un.unifies(terms[4], new Atom(String.valueOf(x))) && un.unifies(terms[5], new Atom( String.valueOf(y))))
	        	return true;
	        return false;
	        
		}catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
}

package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;

import java.util.ArrayList;
import java.util.List;

import evacuation.EvacuationModel;


public class getNearestSign extends DefaultInternalAction {

	private static final long serialVersionUID = -5971750739571943813L;
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception{
		try {
			
			int minVal = 255;
			String dir = "up";
			int iagx = (int)((NumberTerm)terms[0]).solve();//Atual
	        int iagy = (int)((NumberTerm)terms[1]).solve();//Atual
	        List<Term> list = ((ListTerm)terms[2]).getAsList();
	        
	        for(int i = 0; i < list.size(); i++) {
	        	String c = list.get(i).toString();
	        	c = c.substring(5,c.length() - 1);
	        	String[] args=c.split(",");
	        	int cx = Integer.valueOf(args[0]);
	        	int cy = Integer.valueOf(args[1]);
	        	int val = Math.abs(cx - iagx)+Math.abs(cy-iagy); 
	        	if( val < minVal  ) {
	        		minVal = val;
	        		dir = args[2];
	        	}
	        }
	        
	        return un.unifies(terms[3], new Atom(dir));
	        
		}catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
}

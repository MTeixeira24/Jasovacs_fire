package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class get_sign_point extends DefaultInternalAction {

	private static final long serialVersionUID = -8607740739318575008L;
	
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception{
		try {
			boolean result = false;
			int iagx = (int)((NumberTerm)terms[0]).solve();//Atual
	        int iagy = (int)((NumberTerm)terms[1]).solve();//Atual
	        String dir = terms[2].toString();
	        if(dir.equals("up")) {
	        	result = (un.unifies(terms[3], new Atom(String.valueOf(iagx))) && un.unifies(terms[4], new Atom(String.valueOf(1))));  	
	        }else if(dir.equals("down")) {
	        	result = (un.unifies(terms[3], new Atom(String.valueOf(iagx))) && un.unifies(terms[4], new Atom(String.valueOf(48))));
	        }else if(dir.equals("left")) {
	        	result = (un.unifies(terms[3], new Atom(String.valueOf(1))) && un.unifies(terms[4], new Atom(String.valueOf(iagy))));
	        }else if(dir.equals("right")) {
	        	result = (un.unifies(terms[3], new Atom(String.valueOf(48))) && un.unifies(terms[4], new Atom(String.valueOf(iagy))));
	        }
	        return result;
	        
		}catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

}

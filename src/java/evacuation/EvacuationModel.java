package evacuation;

import java.util.Set;
import java.util.logging.Logger;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import evacuation.EvacuationPlanet.Move;

public class EvacuationModel extends GridWorldModel{
	
    private String            id = "EvacuationModel";
	
    protected static EvacuationModel model = null;
    
    synchronized public static EvacuationModel create(int w, int h, int nbAgs) {
    	if(model == null) {
    		model = new EvacuationModel(w,h,nbAgs);
    	}
    	return model;
    }
    
	public EvacuationModel(int w, int h, int nbAgs) {
		super(w,h,nbAgs);
	}
	
	static EvacuationModel world1() throws Exception {
	 EvacuationModel model = EvacuationModel.create(20, 20, 1);
	 model.setAgPos(0, 10, 10);
	 return model;
	}
	
	public static EvacuationModel get() {
        return model;
    }
	boolean move(Move dir, int ag) throws Exception {
        Location l = getAgPos(ag);
        switch (dir) {
        case UP:
            if (isFree(l.x, l.y - 1)) {
                setAgPos(ag, l.x, l.y - 1);
            }
            break;
        case DOWN:
            if (isFree(l.x, l.y + 1)) {
                setAgPos(ag, l.x, l.y + 1);
            }
            break;
        case RIGHT:
            if (isFree(l.x + 1, l.y)) {
                setAgPos(ag, l.x + 1, l.y);
            }
            break;
        case LEFT:
            if (isFree(l.x - 1, l.y)) {
                setAgPos(ag, l.x - 1, l.y);
            }
            break;
        }
        return true;
    }
}

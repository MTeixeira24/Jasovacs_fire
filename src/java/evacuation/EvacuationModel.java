package evacuation;

import java.util.Set;
import java.util.logging.Logger;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class EvacuationModel extends GridWorldModel{
	
    private String            id = "EvacuationModel";
	
	public EvacuationModel(int w, int h, int nbAgs) {
		super(w,h,nbAgs);
		setAgPos(0, 10, 10);
	}
}

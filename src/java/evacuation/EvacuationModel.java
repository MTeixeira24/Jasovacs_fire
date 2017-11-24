package evacuation;

import java.util.Set;
import java.util.logging.Logger;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import evacuation.EvacuationPlanet.Move;

/*
 * 
 * Classe responsável por gerir o estado da simulação
 * 
 */
public class EvacuationModel extends GridWorldModel{
	
	public static final int EXIT = 16;
	public static final int EXIT_INFO = 17; //Informacao de saida
	public static final int COLLAPSED_EXIT = 18; //Saida colapsada
	public static final int DANGER = 20;
	public static final int DANGER_INFO = 21; //Informacao de perigo
	public static final int SMOKE = 22; //Fumo
	
	
    private String            id = "EvacuationModel";
	
    //Declaração de variável static para implementar o padrão singleton
    protected static EvacuationModel model = null;
    
    //Instanciação de um modelo global
    synchronized public static EvacuationModel create(int w, int h, int nbAgs) {
    	if(model == null) {
    		model = new EvacuationModel(w,h,nbAgs); //width Height NºdeAgentes
    	}
    	return model;
    }
    
    //Construtor
	public EvacuationModel(int w, int h, int nbAgs) {
		super(w,h,nbAgs);
	}
	
	//Definição de um mundo, neste caso World1. Podemos acrescentar mais
	static EvacuationModel world1() throws Exception {
	 EvacuationModel model = EvacuationModel.create(20, 20, 2); 
	 model.setAgPos(0, 10, 10); //Definir id e posição do agente no mundo
	 model.setAgPos(1, 15, 3); //Definir id e posição do agente no mundo
	 model.add(EvacuationModel.OBSTACLE, 12, 10); //Obstacle é herdado de GridWorldModel
	 model.add(EvacuationModel.OBSTACLE, 12, 9);
	 model.add(EvacuationModel.OBSTACLE, 12, 8);
	 model.add(EvacuationModel.OBSTACLE, 11, 7);
	 model.add(EvacuationModel.OBSTACLE, 10, 7);
	 model.add(EvacuationModel.OBSTACLE, 9, 7);
	 model.add(EvacuationModel.OBSTACLE, 11, 11);
	 model.add(EvacuationModel.OBSTACLE, 10, 11);
	 model.add(EvacuationModel.OBSTACLE, 9, 11);
	 model.add(EvacuationModel.EXIT, 15, 0);
	 return model;
	}
	
	//getModelo
	public static EvacuationModel get() {
        return model;
    }
	
	//Aplicação das acções de movimento de um agente no mundo
	//UP DOWN RIGHT LEFT são enums de EvacuationPlanet
	boolean move(Move dir, int ag) throws Exception {
        Location l = getAgPos(ag);
        System.out.println("XXX->X"+l.x+"\n"+l.x);
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

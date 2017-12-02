package evacuation;

import java.util.Enumeration;
import java.util.Vector;
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
	public static final int EXIT_INFO_UP = 32; //Informacao de saida
	public static final int DANGER = 64;
	public static final int EXIT_INFO_DOWN = 128; //Informacao de saida
	public static final int EXIT_INFO_RIGHT = 512; //Informacao de saida
	public static final int COLLAPSED_EXIT = 1024; //Saida colapsada
	public static final int EXIT_INFO_LEFT = 2048; //Informacao de saida
	public static final int DANGER_INFO = 21; //Informacao de perigo
	public static final int SMOKE = 22; //Fumo
	public static Vector<Location> spreadStack = new Vector<Location>();
	
	
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
	 model.setAgPos(1, 12, 9); //Definir id e posição do agente no mundo
	 //Rodear a zona de paredes
	 //Obstacle é herdado de GridWorldModel
	 for(int i = 0; i < model.width; i++) {
		 if(i != 15) 
			 model.add(EvacuationModel.OBSTACLE, i, 0);
		 model.add(EvacuationModel.OBSTACLE, i, model.height - 1);
	 }
	 for(int i = 0; i < model.height  - 1; i++) {
		 model.add(EvacuationModel.OBSTACLE, 0, i);
		 model.add(EvacuationModel.OBSTACLE, model.width - 1, i);
	 }
	 
	 //Criar compartimentos dentro da nossa zona
	 //Sala inicial
	 boolean open = true;
	 for(int i = 8; i < 14; i++) {
		 if(!(open && i == 12))
			 model.add(EvacuationModel.OBSTACLE, i, 8);
		 model.add(EvacuationModel.OBSTACLE, i, 12);
	 }
	 for(int i = 8; i < 13; i++) {
		 if(i != 10) {
			 model.add(EvacuationModel.OBSTACLE, 8, i);
			 model.add(EvacuationModel.OBSTACLE, 13, i);
		 }
			 
	 }
	 //Corredores
	 for(int i = 1; i < 8; i++) {
		 if(i != 4)
			 model.add(EvacuationModel.OBSTACLE, i, 8);
		 model.add(EvacuationModel.OBSTACLE, i, 12);
	 }
	 for(int i = 14; i < model.width; i++) {
		 if(i != 17)
			 model.add(EvacuationModel.OBSTACLE, i, 8);
		 model.add(EvacuationModel.OBSTACLE, i, 12);
	 }
	 model.add(EvacuationModel.OBSTACLE, 14, 1);
	 model.add(EvacuationModel.OBSTACLE, 14, 3);
	 model.add(EvacuationModel.OBSTACLE, 14, 4);
	 model.add(EvacuationModel.OBSTACLE, 14, 5);
	 model.add(EvacuationModel.OBSTACLE, 13, 5);
	 model.add(EvacuationModel.OBSTACLE, 12, 5);
	 model.add(EvacuationModel.OBSTACLE, 11, 5);
	 model.add(EvacuationModel.OBSTACLE, 11, 6);
	 model.add(EvacuationModel.OBSTACLE, 11, 7);
	 model.add(EvacuationModel.OBSTACLE, 16, 1);
	 model.add(EvacuationModel.OBSTACLE, 16, 2);
	 model.add(EvacuationModel.OBSTACLE, 16, 3);
	 model.add(EvacuationModel.OBSTACLE, 16, 4);
	 model.add(EvacuationModel.OBSTACLE, 16, 5);
	 model.add(EvacuationModel.OBSTACLE, 16, 6);
	 model.add(EvacuationModel.OBSTACLE, 16, 7);
	 
	 model.add(EvacuationModel.EXIT_INFO_UP, 10, 8);
	 model.add(EvacuationModel.EXIT, 15, 0);
	 model.add(EvacuationModel.DANGER, 17,4);
	 spreadStack.add(new Location(17,4));
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
	
	boolean spreadFire() throws Exception{
		Vector<Location> burnTiles = new Vector<Location>();
		Location l, n;
		for(int i = 0; i < spreadStack.size(); i++) {
			l = spreadStack.get(i);
			if(!model.inGrid(l) || model.hasObject(EvacuationModel.OBSTACLE, l)) {
				continue;
			}
			n = new Location(l.x,l.y+1);
			if(model.inGrid(n) && !model.hasObject(EvacuationModel.OBSTACLE,n) && !model.hasObject(EvacuationModel.DANGER,n)) {
				burnTiles.add(n);
				model.add(EvacuationModel.DANGER, n.x, n.y);
			}
			n = new Location(l.x,l.y-1);
			if(model.inGrid(n) && !model.hasObject(EvacuationModel.OBSTACLE,n) && !model.hasObject(EvacuationModel.DANGER,n)) {
				burnTiles.add(n);
				model.add(EvacuationModel.DANGER, n.x, n.y);
			}
			n = new Location(l.x+1,l.y);
			if(model.inGrid(n) && !model.hasObject(EvacuationModel.OBSTACLE,n) && !model.hasObject(EvacuationModel.DANGER,n)){
				burnTiles.add(n);
				model.add(EvacuationModel.DANGER, n.x, n.y);
			}
			n = new Location(l.x-1,l.y);
			if(model.inGrid(n) && !model.hasObject(EvacuationModel.OBSTACLE,n) && !model.hasObject(EvacuationModel.DANGER,n)){
				burnTiles.add(n);
				model.add(EvacuationModel.DANGER, n.x, n.y);
			}
		}
		spreadStack = burnTiles;
		return true;
	}
}

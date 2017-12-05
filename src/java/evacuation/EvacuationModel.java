package evacuation;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.mas2j.*;
import jason.mas2j.parser.mas2j;
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
		
		FileInputStream fis= new FileInputStream(new File("evacuation.mas2j"));
		mas2j m2= new mas2j(fis);
		MAS2JProject init= m2.mas();
		int nEvacuadores=init.getAg("evacuador").getNbInstances();
	 EvacuationModel model = EvacuationModel.create(50, 50, nEvacuadores); 
	 
	 
	 
	 //Rodear a zona de paredes
	 //Obstacle é herdado de GridWorldModel
	 for(int i = 0; i < model.width; i++) {
		 if(i != 25) 
			 model.add(EvacuationModel.OBSTACLE, i, 0);
		 model.add(EvacuationModel.OBSTACLE, i, model.height - 1);
	 }
	 for(int i = 0; i < model.height  - 1; i++) {
		 model.add(EvacuationModel.OBSTACLE, 0, i);
		 model.add(EvacuationModel.OBSTACLE, model.width - 1, i);
	 }
	 
	 
	 for(int z = 5; z <= 45; z += 5) {
		 for(int i = 0, j = 49; i < 22; i++, j--) {
			 model.add(EvacuationModel.OBSTACLE, i, z);
			 model.add(EvacuationModel.OBSTACLE, j, z);
		 }
	 }
	 for(int z = 5; z <= 45; z += 5) {
		 for(int i = 2, j = 47; i < 15; i+=8, j-=8) {
			 model.add(EvacuationModel.EXIT_INFO_RIGHT, i, z);
			 model.add(EvacuationModel.EXIT_INFO_LEFT, j, z);
		 }
		 model.add(EvacuationModel.EXIT_INFO_UP, 21, z);
		 model.add(EvacuationModel.EXIT_INFO_UP, 28, z);
	 }
	 model.add(EvacuationModel.EXIT, 25, 0);
	 model.add(EvacuationModel.DANGER, 42,48);
	 spreadStack.add(new Location(42,48));
	 
	 //Colocar agentes no mundo
	 for (int i=0; i<nEvacuadores;i++) {
		 //Definir posição
		 int x,y;
		 Random r= new Random();
		 do {
			 
			 x= r.nextInt(model.width);
			 y= r.nextInt(model.height);
			 
		 }
		 while(model.hasObject(EvacuationModel.OBSTACLE, x, y)|| model.hasObject(EvacuationModel.DANGER, x, y) || model.hasObject(EvacuationModel.EXIT, x, y) );
		 model.setAgPos(i, x, y); //Definir id e posição do agente no mundo
	 }
	 
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

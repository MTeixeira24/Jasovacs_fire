package evacuation;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

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
	public ArrayList<Object[]> exitdata = new ArrayList<Object[]>();
	
	
    private String            id = "EvacuationModel";
	
    //Declaração de variável static para implementar o padrão singleton
    protected static EvacuationModel model = null;
    
    //Instanciação de um modelo global
    synchronized public static EvacuationModel create(int w, int h, int nbAgs) {
    	
    	model = new EvacuationModel(w,h,nbAgs); //width Height NºdeAgentes
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
	static EvacuationModel world2() throws Exception {
		
		FileInputStream fis= new FileInputStream(new File("evacuation.mas2j"));
		mas2j m2= new mas2j(fis);
		MAS2JProject init= m2.mas();
		int nEvacuadores=init.getAg("evacuador").getNbInstances();
		EvacuationModel model = EvacuationModel.create(50, 50, nEvacuadores); 
		
		for(int i = 0; i < model.width; i++) {
			 if(i != 25) 
				 model.add(EvacuationModel.OBSTACLE, i, 0);
			 model.add(EvacuationModel.OBSTACLE, i, model.height - 1);
		 }
		for(int i = 0; i < model.height  - 1; i++) {
			 model.add(EvacuationModel.OBSTACLE, 0, i);
			 model.add(EvacuationModel.OBSTACLE, model.width - 1, i);
		 }
		 
		model.add(EvacuationModel.EXIT, 25, 0);
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
	static EvacuationModel world3() throws Exception {
		
		FileInputStream fis= new FileInputStream(new File("evacuation.mas2j"));
		mas2j m2= new mas2j(fis);
		MAS2JProject init= m2.mas();
		int nEvacuadores=init.getAg("evacuador").getNbInstances();
		EvacuationModel model = EvacuationModel.create(50, 50, nEvacuadores); 
		
		for(int i = 0; i < model.width; i++) {
			 if(i != 25) {
				 model.add(EvacuationModel.OBSTACLE, i, 0);
				 model.add(EvacuationModel.OBSTACLE, i, model.height - 1);
			 }
			
		 }
		for(int i = 0; i < model.height  - 1; i++) {
			if(i != 25) {
				model.add(EvacuationModel.OBSTACLE, 0, i);
				 model.add(EvacuationModel.OBSTACLE, model.width - 1, i);
			}
		 }
		for(int i = 1; i < model.width - 1; i++) {
			if(i < 24 || i > 28) {
				model.add(EvacuationModel.OBSTACLE, i, 40);
				model.add(EvacuationModel.OBSTACLE, i, 20);
			}
		}
		for(int i = 21; i < 40; i++) {
			if(i != 31 && i != 32) {
				model.add(EvacuationModel.OBSTACLE, 23, i);
				 model.add(EvacuationModel.OBSTACLE, 29, i);
			}
			
		 }
		for(int i = 1; i < 21; i++) {
			if(i != 8 && i != 9) {
				model.add(EvacuationModel.OBSTACLE, 23, i);
				 model.add(EvacuationModel.OBSTACLE, 29, i);
			}
		 }
		for(int i = 41; i < model.height  - 1; i++) {
			if(i != 43 && i != 44) {
				model.add(EvacuationModel.OBSTACLE, 23, i);
				 model.add(EvacuationModel.OBSTACLE, 29, i);
			}
		 }
		for(int i = 1; i < 21; i++) {
			model.add(EvacuationModel.OBSTACLE, i, 7);
		}
		for(int i = 14; i < 20; i++) {
			model.add(EvacuationModel.OBSTACLE, 6, i);
		}
		for(int i = 14; i < 20; i++) {
			model.add(EvacuationModel.OBSTACLE, 12, i);
		}
		for(int i = 14; i < 20; i++) {
			model.add(EvacuationModel.OBSTACLE, 18, i);
		}
		for(int i = 1; i < 21; i++) {
			if(i != 4 && i !=5 && i != 11 && i != 10 && i != 16 && i!= 17)
			model.add(EvacuationModel.OBSTACLE, i, 14);
		}
		for(int i = 30; i < 49; i++) {
			if(i != 33 && i !=34 && i != 40 && i != 41 && i != 45 && i!= 46)
			model.add(EvacuationModel.OBSTACLE, i, 14);
		}
		for(int i = 14; i < 20; i++) {
			model.add(EvacuationModel.OBSTACLE, 36, i);
		}
		for(int i = 14; i < 20; i++) {
			model.add(EvacuationModel.OBSTACLE, 42, i);
		}
		for(int i = 14; i < 20; i++) {
			model.add(EvacuationModel.OBSTACLE, 48, i);
		}
		model.remove(EvacuationModel.OBSTACLE, 47,40);
		model.remove(EvacuationModel.OBSTACLE, 48,40);
		model.remove(EvacuationModel.OBSTACLE, 1,40);
		model.remove(EvacuationModel.OBSTACLE, 2,40);
		
		for(int j = 29; j < 47; j++) {
			for(int i = 33; i < 40; i++) {
				model.add(EvacuationModel.OBSTACLE, j, i);
			}
		}
		for(int j = 3; j < 23; j++) {
			for(int i = 33; i < 40; i++) {
				model.add(EvacuationModel.OBSTACLE, j, i);
			}
		}
		 
		model.add(EvacuationModel.EXIT, 25, 0);
		model.add(EvacuationModel.EXIT, 25, 49);
		model.add(EvacuationModel.EXIT, 0, 25);
		model.add(EvacuationModel.EXIT, 49, 25);
		
		model.add(EvacuationModel.EXIT_INFO_UP,3,33);
		model.add(EvacuationModel.EXIT_INFO_UP,46,33);
		model.add(EvacuationModel.EXIT_INFO_UP,3,40);
		model.add(EvacuationModel.EXIT_INFO_UP,46,40);
		model.add(EvacuationModel.EXIT_INFO_UP,26,11);
		model.add(EvacuationModel.EXIT_INFO_UP,26,19);
		
		model.add(EvacuationModel.EXIT_INFO_DOWN,23,37);
		model.add(EvacuationModel.EXIT_INFO_DOWN,29,37);
		model.add(EvacuationModel.EXIT_INFO_DOWN,23,29);
		model.add(EvacuationModel.EXIT_INFO_DOWN,29,29);
		model.add(EvacuationModel.EXIT_INFO_DOWN,26,44);
		
		model.add(EvacuationModel.EXIT_INFO_RIGHT,19,40);
		model.add(EvacuationModel.EXIT_INFO_RIGHT,41,40);
		model.add(EvacuationModel.EXIT_INFO_RIGHT,44,21);
		model.add(EvacuationModel.EXIT_INFO_RIGHT,35,21);
		model.add(EvacuationModel.EXIT_INFO_RIGHT,5,7);
		model.add(EvacuationModel.EXIT_INFO_RIGHT,16,7);
		
		
		model.add(EvacuationModel.EXIT_INFO_LEFT,16,21);
		model.add(EvacuationModel.EXIT_INFO_LEFT,8,21);
		model.add(EvacuationModel.EXIT_INFO_LEFT,35,40);
		model.add(EvacuationModel.EXIT_INFO_LEFT,33,7);
		model.add(EvacuationModel.EXIT_INFO_LEFT,46,7);
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
	public static void destroy() {
        model = null;
    }
}

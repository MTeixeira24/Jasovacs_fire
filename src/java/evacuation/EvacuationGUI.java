package evacuation;

import jason.environment.grid.GridWorldView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * 
 * Classe responsável pelas views da nossa simulação
 * 
 */

public class EvacuationGUI extends GridWorldView{
	
	private static final long serialVersionUID = -5707099999380284737L;
	//Guardar referencia ao controller
	EvacuationPlanet env = null;
	
	public EvacuationGUI(EvacuationModel model) {
		/*
		 * No controller temos de instanciar um modelo
		 * antes de instanciar uma classe view
		 */
		super(model, "Evacuation World", 600); //Modelo título e tamanho de janela
		setVisible(true);
		repaint();
		
	}
	public void setEnv(EvacuationPlanet env) {
        this.env = env;
        //scenarios.setSelectedIndex(env.getSimId()-1);
    }
	
	@Override
	public void initComponents(int width) {
		super.initComponents(width);
		JPanel args = new JPanel();
		args.setLayout(new BoxLayout(args, BoxLayout.Y_AXIS));
		/*
		 * Nota: as classes views estendem a classe GridWorldView
		 * que já possuem um objecto canvas instanciado
		 */
	}
	
	//Desenho do agente representado por um ponto numa grid canvas
	//Esta função é chamada a cada uso da função setAgPos, que é
	//usada pelo modelo
	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		if(!(x == 0 && y == 0)) {
			super.drawAgent(g, x, y, Color.yellow, -1);
		}
	}
	
	//Desenho de outras estruturas no mundo
	@Override
	public void draw(Graphics g, int x, int y, int object) {
		switch(object) {
		case EvacuationModel.EXIT:
			g.setColor(Color.green);
			int rx = x * cellSizeW + 2, ry = y * cellSizeH + 2, rw = cellSizeW - 4, rh = cellSizeH - 4;
			g.drawRect(rx, ry, rw, rh);
	        g.fillRect(rx, ry, rw, rh);
			break;
		case EvacuationModel.EXIT_INFO:
			draw_exitinfo(g,x,y);
			break;
		case EvacuationModel.DANGER:
			draw_danger(g,x,y);
			break;
		}
	}
	
	private void draw_danger(Graphics g, int x, int y) {
		g.setColor(Color.RED);
		g.fillOval(x*cellSizeW+cellSizeW/8, y*cellSizeH+cellSizeH/8, cellSizeW-cellSizeW/4, cellSizeH-cellSizeH/4);
		
	}
	public void draw_exitinfo(Graphics g, int x, int y) {
		g.setColor(Color.GREEN);
		int[] vx = new int[4];
		int[] vy = new int[4];
		vx[0] = x * cellSizeW;
		vy[0] = y * cellSizeH + (cellSizeH/4);
		vx[1] = x * cellSizeW;
		vy[1] = y * cellSizeH + (3*cellSizeH/4);
		vx[2] = x * cellSizeW + cellSizeW;
		vy[2] = y * cellSizeH + (3*cellSizeH/4);
		vx[3] = x * cellSizeW + cellSizeW;
		vy[3] = y * cellSizeH + (cellSizeH/4);
		g.fillPolygon(vx,vy,4);
	}
	
	public static void main(String[] args) throws Exception {
        EvacuationPlanet env = new EvacuationPlanet();
        env.init(new String[] {"5","50","yes"});
    }

}

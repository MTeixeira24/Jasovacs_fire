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


public class EvacuationGUI extends GridWorldView{
	
	private static final long serialVersionUID = -5707099999380284737L;
	EvacuationPlanet env = null;
	
	public EvacuationGUI(EvacuationModel model) {
		super(model, "Evacuation World", 600);
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
		
		
	}
	
	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		super.drawAgent(g, 50, 50, Color.BLACK, -1);
	}
	
	public static void main(String[] args) throws Exception {
        EvacuationPlanet env = new EvacuationPlanet();
        env.init(new String[] {"5","50","yes"});
    }

}

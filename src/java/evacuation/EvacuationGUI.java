package evacuation;

import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * 
 * Classe responsável pelas views da nossa simulação
 * 
 */

public class EvacuationGUI extends GridWorldView{
	
	private static final long serialVersionUID = -5707099999380284737L;
	//Guardar referencia ao controller
	EvacuationPlanet env = null;
	public enum orientation {UP, DOWN, LEFT, RIGHT};
	private DefaultTableModel db;
	private JTable table;
	private JLabel    jlMouseLoc;
	private JLabel 	jTimePassed;
	private double startTime = System.nanoTime();
	
	public EvacuationGUI(EvacuationModel model) {
		/*
		 * No controller temos de instanciar um modelo
		 * antes de instanciar uma classe view
		 */
		super(model, "São pedrogão 2017, colorized", 800); //Modelo título e tamanho de janela
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
		JPanel sp =  new JPanel(new FlowLayout(FlowLayout.CENTER));
		sp.setBorder(BorderFactory.createEtchedBorder());
		//sp.add(new JLabel("ExitInformation:"));
		db = new DefaultTableModel();
		db.addColumn("Agent");
		db.addColumn("Pos");
		db.addColumn("PosExit");
		db.addColumn("knowExit");
		db.addColumn("Status");
		table = new JTable(db);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(500,100));
		table.setFillsViewportHeight(true);
		sp.add(scrollPane);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBorder(BorderFactory.createEtchedBorder());
        final JComboBox scenarios = new JComboBox();
        for(int i = 1; i<=2;i++) {
        	scenarios.addItem(i);
        }
        JPanel lp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel cp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel rp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lp.add(new JLabel("Scenarios:"));
        lp.add(scenarios);
        cp.add(new JLabel("Selected coords"));
        jlMouseLoc = new JLabel("0,0)");
        cp.add(jlMouseLoc);
        rp.add(new JLabel("Elapsed time: "));
        jTimePassed = new JLabel("00:00");
        rp.add(jTimePassed);
        p.add(lp);
        p.add(cp);
        p.add(rp);
        args.add(p);
		args.add(sp);
		getContentPane().add(BorderLayout.SOUTH, args);
		ActionListener taskPerformer = new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		          int elapse = (int) Math.floor((System.nanoTime() - startTime)/1000000);
		          String timeFormat = "00:00";
		          Date date = new Date(elapse);
		          DateFormat formatter = new SimpleDateFormat("mm:ss");
		          timeFormat = formatter.format(date);
		          jTimePassed.setText(timeFormat);
		      }
		};
		new Timer(1000, taskPerformer).start();
		getCanvas().addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) { }
            public void mouseMoved(MouseEvent e) {
                int col = e.getX() / cellSizeW;
                int lin = e.getY() / cellSizeH;
                if (col >= 0 && lin >= 0 && col < getModel().getWidth() && lin < getModel().getHeight()) {
                    jlMouseLoc.setText("("+col+","+lin+")");
                }
            }
        });
		scenarios.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ievt) {
                int w = ((Integer)scenarios.getSelectedItem()).intValue();
                if (env != null && env.getSimId() != w) {
                    env.endSimulation();
                    env.initWorld(w);
                }
            }
        });
		getCanvas().addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / cellSizeW;
                int lin = e.getY() / cellSizeH;
                if (col >= 0 && lin >= 0 && col < getModel().getWidth() && lin < getModel().getHeight()) {
                    EvacuationModel em = (EvacuationModel)model;
                    em.add(EvacuationModel.DANGER, col, lin);
                    EvacuationModel.spreadStack.addElement(new Location(col,lin));
                }
            }
            public void mouseExited(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
        });
	}
	
	public void updateTable(Object[] l) {
		db.addRow(l);
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
		case EvacuationModel.EXIT_INFO_UP:
			draw_exitinfo(g,x,y,orientation.UP);
			break;
		case EvacuationModel.EXIT_INFO_DOWN:
			draw_exitinfo(g,x,y,orientation.DOWN);
			break;
		case EvacuationModel.EXIT_INFO_LEFT:
			draw_exitinfo(g,x,y,orientation.LEFT);
			break;
		case EvacuationModel.EXIT_INFO_RIGHT:
			draw_exitinfo(g,x,y,orientation.RIGHT);
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
	public void draw_exitinfo(Graphics g, int x, int y, orientation dir) {
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
		g.setColor(Color.WHITE);
		int[] tpx = new int[3]; 
		int[] tpy = new int[3];
		switch(dir) {
			case UP:
				g.drawLine(x * cellSizeW + cellSizeW/2, y*cellSizeH + (cellSizeH/4), x * cellSizeW + cellSizeW/2, y*cellSizeH + (3*cellSizeH/4));
				tpx[0] = x * cellSizeW + cellSizeW/2;
				tpy[0] = y * cellSizeH + (cellSizeH/4);
				tpx[1] = x * cellSizeW + cellSizeW/3;
				tpy[1] = y * cellSizeH + (cellSizeH/2);
				tpx[2] = x * cellSizeW + 2*(cellSizeW/3);
				tpy[2] = y * cellSizeH + (cellSizeH/2);
				g.fillPolygon(tpx, tpy, 3);
				break;
			case DOWN:
				g.drawLine(x * cellSizeW + cellSizeW/2, y*cellSizeH + (cellSizeH/4), x * cellSizeW + cellSizeW/2, y*cellSizeH + (3*cellSizeH/4));
				tpx[0] = x * cellSizeW + cellSizeW/2;
				tpy[0] = y * cellSizeH + 3*(cellSizeH/4);
				tpx[1] = x * cellSizeW + cellSizeW/3;
				tpy[1] = y * cellSizeH + (cellSizeH/2);
				tpx[2] = x * cellSizeW + 2*(cellSizeW/3);
				tpy[2] = y * cellSizeH + (cellSizeH/2);
				g.fillPolygon(tpx, tpy, 3);
				break;
			case LEFT:
				g.drawLine(x * cellSizeW, y*cellSizeH + (cellSizeH/2), x * cellSizeW + cellSizeW, y*cellSizeH + (cellSizeH/2));
				tpx[0] = x * cellSizeW;
				tpy[0] = y * cellSizeH + cellSizeH/2;
				tpx[1] = x * cellSizeW + cellSizeW/2;
				tpy[1] = y * cellSizeH + (cellSizeH/4);
				tpx[2] = x * cellSizeW + (cellSizeW/2);
				tpy[2] = y * cellSizeH + 3*(cellSizeH/4);
				g.fillPolygon(tpx, tpy, 3);
				break;
			case RIGHT:
				g.drawLine(x * cellSizeW, y*cellSizeH + (cellSizeH/2), x * cellSizeW + cellSizeW, y*cellSizeH + (cellSizeH/2));
				tpx[0] = x * cellSizeW + cellSizeW;
				tpy[0] = y * cellSizeH + cellSizeH/2;
				tpx[1] = x * cellSizeW + cellSizeW/2;
				tpy[1] = y * cellSizeH + (cellSizeH/4);
				tpx[2] = x * cellSizeW + (cellSizeW/2);
				tpy[2] = y * cellSizeH + 3*(cellSizeH/4);
				g.fillPolygon(tpx, tpy, 3);
				break;
			default:
				
				break;
		}
	}
	
	public static void main(String[] args) throws Exception {
        EvacuationPlanet env = new EvacuationPlanet();
        env.init(new String[] {"5","50","yes"});
    }

}

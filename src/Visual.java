import java.awt.Color; 
import java.awt.Dimension; 
import java.awt.Graphics; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JFrame; 
import javax.swing.JPanel; 
import javax.swing.Timer; 
 
 
public class Visual implements ActionListener, KeyListener, MouseListener, MouseMotionListener, Constants {
 
    public JFrame frame;        //REQUIRED! The outside shell of the window
    public DrawingPanel panel;  //REQUIRED! The interior window
    public Timer visualtime;    //REQUIRED! Runs/Refreshes the screen. 

    //any other variables needed, go here.
    public boolean paused;
    public boolean working;
    public int counter;

    
    public Visual()
    {
        frame = new JFrame("The Pond...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DrawingPanel();
        panel.setPreferredSize(new Dimension(WIDE, HIGH)); 
        frame.getContentPane().add(panel);
        panel.setFocusable(true);
        panel.requestFocus();
        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 
 
        
        //Initialize all global variables here:  

        Initialize();

        
        visualtime = new Timer(20, this);     
        visualtime.start();
    } 

    public void Initialize()
    {
        paused = true;
        working = true;
        counter = 0;
    }

    public void actionPerformed(ActionEvent e)
    {    
        if(!paused && working)
        {
            counter++;
            if(counter > DELAY)
            {
                counter = 0;
                Control.Action();
                Control.NextTurn();
            }
        }
        if(Control.critters.size() == 0) 
        {
            working = false;
        }
        
        panel.repaint();  
    }
 
    public void keyPressed(KeyEvent e)  
    {            
        if(e.getKeyCode() == KeyEvent.VK_HOME) 
        {
            Control.SetUpPond();
            Initialize();
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
            paused = !paused;
       
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0); 
    }
    public void mouseClicked(MouseEvent e) 
    {
    }
    public void mouseMoved(MouseEvent e) 
    {

    }
    
    
    public void keyTyped(KeyEvent e) {} 
    public void keyReleased(KeyEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
        
//BIG NOTE:  The coordinate system for the output screen is as follows:     
//  (x,y) = (0,0) is the TOP LEFT corner of the output screen;    
//  (x,y) = (800,0) is the TOP RIGHT corner of the output screen;     
//  (x,y) = (0,500) is the BOTTOM LEFT corner of the screen;   
//  (x,y) = (800,500) is the BOTTOM RIGHT corner of the screen;
    private class DrawingPanel extends JPanel implements Constants{ 
        public void paintComponent(Graphics g)         
        {
            super.paintComponent(g);
            panel.setBackground(Color.BLACK);

            g.setColor(Color.YELLOW);
            g.setFont(LARGE_FONT);
            g.drawString("Day "+Control.day, MARGIN, MARGIN - 30);
            g.drawString("Hour "+Control.hour, PONDW - 25, MARGIN - 30);
            if(paused) g.drawString("PAUSED <SPACEBAR TO CONTINUE>", MARGIN*3, HIGH - 10);
            if(!working) g.drawString("All of the Critters have Died!", MARGIN*3, HIGH - 10);
            
            g.setColor(BEACH_TINT);
            if(Control.Daytime()) g.setColor(BEACH_DAY_TINT);
            if(Control.Nighttime()) g.setColor(BEACH_NIGHT_TINT);
            g.fillRect(MARGIN-20, MARGIN-20, PONDW+40, PONDH+40);
            
            g.setColor(WATER_TINT);
            if(Control.Daytime()) g.setColor(WATER_DAY_TINT);
            if(Control.Nighttime()) g.setColor(WATER_NIGHT_TINT);
            g.fillRect(MARGIN, MARGIN, PONDW, PONDH);
            
            
            Control.PaintPond(g);
            
            
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(STATSBAR, 0, STATSBAR, HIGH);
            g.setFont(MEDIUM_FONT);
            g.drawString("Pond Statistics", STATSBAR + MARGIN, HIGH/5);
            g.setFont(SMALL_FONT);
            
            
            //YOUR POND STATS GO HERE.
            //LIST IMPORTANT DATA PIECES -- either in Strings or Graphically
            //Some info we may be concerned with:
            //   -- Current Pop
            //   -- Max pop (day)
            //   -- Oldest Critter
            //   -- Birth Rate...
            //   -- Current Ave Health
            //   -- Current Ave Age
            //   ......
            
            String temp = "Current Population: " + Control.critters.size();
            g.drawString(temp, STATSBAR + 6*MARGIN/5, HIGH/3);
            temp = "Max population (day):" + Animal.maxPopulation;
            g.drawString(temp, STATSBAR + 6*MARGIN/5, HIGH/3 - 20);
            int oldestAge = 0;
            for (int i = 0; i < Control.critters.size(); i++) {
            	if (Control.critters.get(i).age > oldestAge) {
            		oldestAge = Control.critters.get(i).age;
            	}
            }
            temp = "Oldest Critter Age: " + oldestAge;
            g.drawString(temp, STATSBAR + 6*MARGIN/5, HIGH/3 + 20);
            //birth rate
            temp = "Birth Rate: " + Animal.numBirthed;
            g.drawString(temp, STATSBAR + 6*MARGIN/5, HIGH/3 + 40);
            //current average health
            int avgHealth = 0;
            int avgAge = 0;
            for (int i = 0; i < Control.critters.size(); i++) {
            	avgHealth += Control.critters.get(i).health; //get the total health
            	avgAge += Control.critters.get(i).age; //get total age
            }
            if (Control.critters.size() != 0) {
                avgHealth /= Control.critters.size(); //get avg health by dividing by current population
            }
            temp = "Current Average Health: " + avgHealth;
            g.drawString(temp, STATSBAR + 6*MARGIN/5, HIGH/3 + 60);
            if (Control.critters.size() != 0) {
                avgAge /= Control.critters.size();
            }
            temp = "Current Average Age: " + avgAge;
            g.drawString(temp, STATSBAR + 6*MARGIN/5, HIGH/3 + 80);
        }
        
        // <editor-fold defaultstate="collapsed" desc="There's nothing in here...">   
        
        //</editor-fold>
    }
}
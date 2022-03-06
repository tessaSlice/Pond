import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class MagiKarp extends Animal {
	 public static final int MINAGE = 20;
	    public static final int OLDAGE = 12;
	    public static final int RANGE = PIP;
	    public static int oldest = 0;
	   
	public MagiKarp(int x, int y)
	{
		super(x,y);
		type = "MagiKarp";
		pic = new ImageIcon("MagiKarp.png");
		
		
	}

	 public void move() 
    {
		for(int i = 0; i < 2;i++)
		{
        if(!alive) return;      //dead frogs don't move
        if(health < 10) return; //unhealthy frogs don't move
        
        

        //Adult frogs don't move around much in the daytime...
        if(gender > 0 && Control.Daytime() && Math.random() < .9) return;  
        
        //Baby frogs are a bit more active in the daytime...
        if(gender == 0 && Control.Daytime() && Math.random() < .4) return;  
        
        //If it's not daytime, all frogs are always active:
        
        //pick a random direction... and it moves one "frog length"
        double ang = Math.random()*Math.PI*2;
        int x = (int)(size*Math.cos(ang));
        int y = (int)(size*Math.sin(ang));
        
        //check if that space is open
        Rectangle attempt = new Rectangle(posx + x, posy + y, size, size);
        boolean empty = true;
        if(posx + x < MARGIN + 2) empty = false;
        if(posy + y < MARGIN + 2) empty = false;
        if(posx + x > MARGIN + PONDW - size - 2) empty = false;
        if(posy + y > MARGIN + PONDH - size - 2) empty = false;
        if(type.equals("MagiKarp")) {
        	
        
        //make sure you don't move onto a Rock
        	if(type.equals("MagiKarp")) {
        		
        	
        for(int n = 0; empty && n < Control.bits.size(); n++)
            if(!Control.bits.get(n).edible && Control.bits.get(n).space.intersects(attempt))
                empty = false;
        }
        }
        if(type.equals("MagiKarp")) {
        for(int n = 0; empty && n < Control.critters.size(); n++)
            if(Control.critters.get(n) != this && Control.critters.get(n).alive && Control.critters.get(n).space.intersects(attempt))
                empty = false;
        }
        
        if(empty)
        {   //If that space is open, move to the new location
           
            
            posx += x;
            posy += y;
            space = new Rectangle(posx, posy, size, size);
        }
		}
    }
	
	public void eating()
	{
		if(type.equals("Gyrados")) return;
		if(Control.critters.size()<1) return;
		double ang = Math.random()*Math.PI*2;
        int x = (int)(size*Math.cos(ang));
        int y = (int)(size*Math.sin(ang));
        
		
//        if(posx + x < MARGIN + 2) empty = false;
//        if(posy + y < MARGIN + 2) empty = false;
//        if(posx + x > MARGIN + PONDW - size - 2) empty = false;
//        if(posy + y > MARGIN + PONDH - size - 2) empty = false;
        Rectangle attempt = new Rectangle(posx + x, posy + y, size, size);
        for(int n = 0;  n < Control.critters.size(); n++)
            if(Control.critters.get(n) != this && Control.critters.get(n).alive && Control.critters.get(n).space.intersects(attempt))
            {
            	health = 100;
            	Control.critters.get(n).alive = false;
            }
        
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		if(!alive) return;  //dead frogs don't act
        //Build a perception Rectangle to look around:
        Rectangle perception = new Rectangle(posx - RANGE, posy - RANGE, size+2*RANGE, size+2*RANGE);
        
        for(int n = Control.bits.size()-1; n >= 0; n--)
            if(Control.bits.get(n).edible && perception.intersects(Control.bits.get(n).space))
            {
                //Find food, eat food...
                health += Control.bits.get(n).nutrients;
                if(health > 100) health = 100;
                Control.bits.remove(n);
            }
        if(type.equals("Gyrados")) eating();
        
        
        
        
        
        //Baby Frogs and Male Frogs are done, return back.
 if(gender != 2) return;
        
        //female frogs look for male frogs...
        if(pregnant) return;  
        
        boolean found = false;
        for(int n = 0; !found && n < Control.critters.size(); n++)
        {
            Animal current = Control.critters.get(n);
            //checks to make sure it is a Live Male Frog within perception distance
            if(current.alive && current.gender == 1 && current.type.equals("MagiKarp") && current.space.intersects(perception))
                found = true;
        }
        if(found) pregnant = true;
        
        
	}

	
        
        /*
         
         for(int n = 0; empty && n < Control.critters.size(); n++)
            if(Control.critters.get(n) != this && Control.critters.get(n).alive && Control.critters.get(n).space.intersects(attempt))
         */
        
	
	@Override
	public void age() {
		if(!alive) return;
		
        health -= DAILY_HUNGER;
        
        if(pregnant)
        {
            pregnant = false;
            boolean morebabies = true;
            int attempts = 0;
            while(morebabies)
            {
                attempts++;
                //pick a random direction... and place a baby if its open
                double ang = Math.random()*Math.PI*2;
                int x = (int)(size*Math.cos(ang));
                int y = (int)(size*Math.sin(ang));
                Animal baby = new MagiKarp(posx+x, posy+y);
                
                //Checking for the border and rocks and other critters...
                boolean empty = true;
                if(posx + x < MARGIN + 2) empty = false;
                if(posy + y < MARGIN + 2) empty = false;
                if(posx + x > MARGIN + PONDW - size - 2) empty = false;
                if(posy + y > MARGIN + PONDH - size - 2) empty = false;
                
                for(int n = 0; empty && n < Control.bits.size(); n++)
                    if(!Control.bits.get(n).edible && Control.bits.get(n).space.intersects(baby.space))
                        empty = false;
                
                for(int n = 0; empty && n < Control.critters.size(); n++)
                    if(Control.critters.get(n) != this && Control.critters.get(n).alive && Control.critters.get(n).space.intersects(baby.space))
                        empty = false;
                
                if(empty)
                {
                    Control.critters.add(baby);
                    if(Math.random() < .25) morebabies = false;
                }
                if(attempts > 100) morebabies = false;
            }
            
        }
        
        
        age++;
        if(gender == 0 && age > MINAGE) //Baby Frog is growing up
        {
            //gets bigger
            size = PIP;
            space = new Rectangle(posx, posy, size, size);
            
            gender++;
            if(Math.random() < .5) gender++;  //50% chance of female
            type = "Gyrados";
            pic = new ImageIcon("Gyrados.png");
            size = 100;


            
        }
        
        if(age > OLDAGE)
        {
            if( Math.random()*100 > health)
                alive = false; /// DEAD!!!!
            
            	 for(int i = 0; i < ADD_FOOD; i++)
     	        {
            		 double ang = Math.random()*Math.PI*2;
            		 int x = (int)(size*Math.cos(ang));
            	        int y = (int)(size*Math.sin(ang));
            	        
            		 boolean empty = true;
            	        if(posx + x < MARGIN + 2) empty = false;
            	        if(posy + y < MARGIN + 2) empty = false;
            	        if(posx + x > MARGIN + PONDW - size - 2) empty = false;
            	        if(posy + y > MARGIN + PONDH - size - 2) empty = false;
            		 
            		 
            		 
     	            int a = (int)(Math.random()*(PONDW - 2*PIP)) + MARGIN + PIP/2;
     	            int b = (int)(Math.random()*(PONDH - 2*PIP)) + MARGIN + PIP/2;
//     	            boolean empty = true;
     	            for(int c = 0; c < Control.bits.size(); c++)
     	                if(Control.bits.get(c).space.contains(a, b))
     	                    empty = false;
     	            
     	            for(int f = 0; f < 30;f++)
     	            {
     	            if(empty)
     	                Control.bits.add(new Mineral(a, b, true));
     	            }
     	        }
            }
            
        
        
        
        // Checking for oldest frog
        
        for(int n = 0; n < Control.critters.size();n++)
        {
        	if(Control.critters.get(n).age > oldest)
        		oldest = n;
        		
        }
	}

}

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Fly extends Animal {
    public static final int MINAGE = 2;
    public static final int OLDAGE = 5;
    public static final int RANGE = PIP/2;
    
    public Fly(int x, int y)
    {
        super(x, y);
        type = "Fly";
        pic = new ImageIcon("fly.png");
        size = 4*PIP/5;
        space = new Rectangle(x, y, size, size);
    }
    
    public void move() 
    {
        if(!alive) return;      //dead ducks don't move

        //flies only move around in nighttime
        if(Control.Daytime()) return;
        
        //pick a random direction... and it moves one "fly length"
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
        
        //make sure you don't move onto another critter
        for(int n = 0; empty && n < Control.critters.size(); n++)
            if(Control.critters.get(n) != this && Control.critters.get(n).alive && Control.critters.get(n).space.intersects(attempt))
                empty = false;
        
        if(empty)
        {
            posx += x;
            posy += y;
            space = new Rectangle(posx, posy, size, size);
        }
    }

    public void act() 
    {
        if(!alive) return;  //dead flies don't act
        //Build a perception Rectangle to look around:
        Rectangle perception = new Rectangle(posx - RANGE, posy - RANGE, size+2*RANGE, size+2*RANGE);
        
        for(int n = Control.critters.size()-1; n >= 0; n--) {
        	if (Control.critters.get(n).alive == false && perception.intersects(Control.critters.get(n).space) && !Control.critters.get(n).type.equals("mutant")) {
        		health += Control.critters.get(n).FOODVAL;
        		Control.critters.remove(n);
        	}
        }
        
        //Baby flies and Male flies are done, return back.
        if(gender != 2) return;
        
        //female flies look for male frogs...
        if(pregnant) return;
        
        boolean found = false;
        for(int n = 0; !found && n < Control.critters.size(); n++)
        {
            Animal current = Control.critters.get(n);
            //checks to make sure it is a Live Male fly within perception distance
            if(current.alive && current.gender == 1 && current.type.equals("Fly") && current.space.intersects(perception))
                found = true;
        }
        if(found) pregnant = true;
    }
    
    public void age() 
    {
        if(!alive) return;
        //flies only start losing health after they develop/mature
        if (age >= MINAGE) {
            health -= DAILY_HUNGER;
        }
        
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
                Animal baby = new Fly(posx+x, posy+y);
                
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
                    //MY OWN CODE: ADD TO DAY COUNT
                    Animal.numBirthed++;
                    if(Math.random() < .25) morebabies = false;
                }
                if(attempts > 100) morebabies = false;
            }
        }
        
        
        age++;
        if(gender == 0 && age > MINAGE) //Baby Duck is growing up
        {
            //gets bigger
            size = PIP;
            space = new Rectangle(posx, posy, size, size);
            
            gender++;
            if(Math.random() < .5) gender++;  //50% chance of female
        }
        
        if(age > OLDAGE)
        {
            if(Math.random()*100 > health)
                alive = false;
        }
        
        if(health <= 0) {
        	alive = false;
        }
        
    }
    

}
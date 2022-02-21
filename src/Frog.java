package newpond;

//Mine!

import java.awt.Rectangle;
import javax.swing.ImageIcon;


public class Frog extends Animal {
    public static final int MINAGE = 3;
    public static final int OLDAGE = 12;
    public static final int RANGE = PIP/2;
    
    public Frog(int x, int y)
    {
        super(x, y);
        type = "Frog";
        pic = new ImageIcon("babyfrog.png");
        size = 4*PIP/5;
        space = new Rectangle(x, y, size, size);
    }

    
    
    public void move() 
    {
        if(!alive) return;      //dead frogs don't move
        if(health < 10) return; //unhealthy frogs don't move
        
        if(Math.random() < .25) updown();

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
        
        //make sure you don't move onto a Rock
        for(int n = 0; empty && n < Control.bits.size(); n++)
            if(!Control.bits.get(n).edible && Control.bits.get(n).space.intersects(attempt))
                empty = false;
        
        //make sure you don't move onto another Frog
        for(int n = 0; empty && n < Control.critters.size(); n++)
            if(Control.critters.get(n) != this && Control.critters.get(n).alive && Control.critters.get(n).space.intersects(attempt))
                empty = false;
        
        if(empty)
        {   //If that space is open, move to the new location
            if(posz >=0) health--; //swimming is easier, being on the water costs health
            
            posx += x;
            posy += y;
            space = new Rectangle(posx, posy, size, size);
        }
    }

    public void act() 
    {
        if(!alive) return;  //dead frogs don't act
        //Build a perception Rectangle to look around:
        Rectangle perception = new Rectangle(posx - RANGE, posy - RANGE, size+2*RANGE, size+2*RANGE);
        
        for(int n = Control.bits.size()-1; n >= 0; n--)
            if(Control.bits.get(n).edible && perception.intersects(Control.bits.get(n).space))
            {
                //Find food, eat food...
                health += Control.bits.get(n).nutrients;
                Control.bits.remove(n);
            }
        
        //Baby Frogs and Male Frogs are done, return back.
        if(gender != 2) return;
        
        //female frogs look for male frogs...
        if(pregnant) return;  
        
        boolean found = false;
        for(int n = 0; !found && n < Control.critters.size(); n++)
        {
            Animal current = Control.critters.get(n);
            //checks to make sure it is a Live Male Frog within perception distance
            if(current.alive && current.gender == 1 && current.type.equals("Frog") && current.space.intersects(perception))
                found = true;
        }
        if(found) pregnant = true;
    }
    
    private void updown()
    {
        if(gender == 0) return;
        
        if(posz == 0) posz = -1;
        else posz = 0;
        
        if(posz == 0) pic = new ImageIcon("adultfrog.png");
        else pic = new ImageIcon("frogunderwater.png");
    }
    
    public void age() 
    {
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
                Animal baby = new Frog(posx+x, posy+y);
                
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
            
            pic = new ImageIcon("adultfrog.png");
            if(Math.random() < .5) updown();
        }
        
        if(age > OLDAGE)
        {
            if(Math.random()*100 > health)
                alive = false;
        }
        
        
    }
    

}

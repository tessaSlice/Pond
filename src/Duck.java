import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Duck extends Animal {
    public static final int MINAGE = 5;
    public static final int OLDAGE = 17;
    public static final int RANGE = PIP/2;
    public static final boolean CONTAINS_MUTANTS = true;
    
    public boolean flying;
    public boolean mutant;
    
    public Duck(int x, int y)
    {
        super(x, y);
        type = "Duck";
        pic = new ImageIcon("duck.png");
        size = 4*PIP/5;
        space = new Rectangle(x, y, size, size);
        flying = false;
        //standard is to make mutants false
        mutant = false;
        if (CONTAINS_MUTANTS) {
        	//make ducks have a chance of being a "mutant"
        	double rand = Math.random();
        	if (rand < .1) {
        		mutant = true;
//        		System.out.println("Created a mutant.");
        		pic = new ImageIcon("mutantduck.png");
        	}
        }
    }
    
    public void move() 
    {
        if(!alive) return;      //dead ducks don't move

        //half of adult ducks should move during daytime
        if(gender > 0 && Control.Daytime() && Math.random() < .5) return;
        
        //Baby ducks are a bit more active in the daytime...
        if(gender == 0 && Control.Daytime() && Math.random() < .1) return;
        
        //pick a random direction... and it moves one "duck length"
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
        
        for(int n = 0; empty && n < Control.bits.size(); n++)
            if(!Control.bits.get(n).edible && Control.bits.get(n).space.intersects(attempt))
                //it can move onto a Rock, it just has to enable the flying movement you know?
            	flying = true;
            	changeFlyingStatus();
            	flying = false;
        
        if(empty)
        {
            posx += x;
            posy += y;
            space = new Rectangle(posx, posy, size, size);
        }
    }
    
    public void changeFlyingStatus() {
    	if (flying) {
    		if (!mutant) {
        		pic = new ImageIcon("flyingduck.png");
    		} else {
    			pic = new ImageIcon("mutantduckflying.png");
    		}
    	} else {
    		//it's not flying
    		if (!mutant) {
        		pic = new ImageIcon("duck.png");
    		} else {
    			pic = new ImageIcon("mutantduck.png");
    		}
    	}
    }

    public void act() 
    {
        if(!alive) return;  //dead ducks don't act
        //Build a perception Rectangle to look around:
        Rectangle perception = new Rectangle(posx - RANGE, posy - RANGE, size+2*RANGE, size+2*RANGE);
        
        for(int n = Control.bits.size()-1; n >= 0; n--)
            if(Control.bits.get(n).edible && perception.intersects(Control.bits.get(n).space))
            {
                //Find food, eat food...
                health += Control.bits.get(n).nutrients;
                Control.bits.remove(n);
            }
        
        for(int n = Control.critters.size()-1; n >= 0; n--) {
        	if (Control.critters.get(n).type == "Frog" && perception.intersects(Control.critters.get(n).space)) {
        		health += Control.critters.get(n).FOODVAL; //should be nutrients ngl for frog, update this later?
        		Control.critters.remove(n);
        	}
        }
        
        //Baby Frogs and Male Ducks are done, return back.
        if(gender != 2) return;
        
        //female frogs look for male frogs...
        if(pregnant) return;
        
        boolean found = false;
        for(int n = 0; !found && n < Control.critters.size(); n++)
        {
            Animal current = Control.critters.get(n);
            //checks to make sure it is a Live Male Duck within perception distance
            if(current.alive && current.gender == 1 && current.type.equals("Duck") && current.space.intersects(perception))
                found = true;
        }
        if(found) pregnant = true;
    }
    
    public void age() 
    {
        if(!alive) return;
        //ducks only start losing health after they develop/mature
        if (age >= MINAGE) {
        	health += 3; //to counteract for daily hunger
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
                Animal baby = new Duck(posx+x, posy+y);
                
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
        
        if (!alive && mutant) {
//        	dead mutants "explode" and distribute nutrients, distributes 10 nutrients max
//        	loops 10 times
        	for (int i = 0; i < 10; i++) {
        		//pick a random direction... and it moves one "nutrient length"
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
                
                //Food is only placed in open spaces
                if(empty)
                {
                	Control.bits.add(new Mineral(posx + x,  posy + y, true));
                }
        	}
        	
        	Control.critters.remove(this);
        }
        
    }
    

}
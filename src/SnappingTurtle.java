import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class SnappingTurtle extends Animal {
	 public static final int MINAGETURTLE = 2;
	 public static final int OLDAGETURTLE = 20;
	 public static final int RANGE = PIP/2;
	 public static int gooseCounter = 0;
	 public static boolean explode = false;
			 
	public SnappingTurtle(int x, int y)
	{
		 super(x, y);
	     type = "Turtle";
	     pic = new ImageIcon("babyturtle.png");
	     size = PIP*6/5;
	     space = new Rectangle(x, y, size, size);
	}
	public void move()
	{
		if(!alive) return;      //dead turtles don't move
        if(health < 10) return; //unhealthy turtles don't move

        //Adult turtles don't move around much in the daytime...
        if(gender > 0 && Control.Daytime() && Math.random() < .9) return;  
        
        //Baby turtles don't move much in the daytime...
        if(gender == 0 && Control.Daytime() && Math.random() < .4) return;  
        
        //If it's not daytime, all turtles are always active:
        
        //pick a random direction... and it moves one "turtle length"
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
        
        // don't go on a rock if you're a baby
        for(int n = 0; empty && n < Control.bits.size(); n++)
            if(!Control.bits.get(n).edible && this.gender == 0 && Control.bits.get(n).space.intersects(attempt))
                empty = false;
        
        // move rocks around if you're not a baby and you're "strong enough"
        for(int n = 0; empty && n < Control.bits.size(); n++)
          if(!Control.bits.get(n).edible && Control.bits.get(n).space.intersects(attempt) && this.gender != 0 && this.health > 50)
          {
        	  Control.bits.get(n).posx = this.posx + 5;
    		  Control.bits.get(n).posy = this.posy;
    		  Rectangle rock = new Rectangle(Control.bits.get(n).posx, Control.bits.get(n).posy, size, size);
        	  // if you move a rock on top of another animal --> that animal dies because it gets crushed
        	  for(int j = 0; empty && j < Control.critters.size(); j++)
        	  {	  
        		 if(Control.critters.get(j) != this && Control.critters.get(j).alive && !Control.critters.get(j).type.contentEquals("Turtle") && Control.critters.get(j).space.intersects(rock))
        			 Control.critters.remove(j);
        	  }
          }
        
        // ATTACK ONE ANOTHER
        gooseCounter = 0;
        for(int n = 0; n < Control.critters.size(); n++)
        	if(Control.critters.get(n).type == "Goose") gooseCounter++;
        
        //make sure you don't move onto another Turtle
        for(int n = 0; empty && n < Control.critters.size(); n++)
        { 
        	if(gooseCounter > 5 && Control.critters.get(n) != this && Control.critters.get(n).alive && Control.critters.get(n).space.intersects(attempt) && Control.critters.get(n).type.equals("Turtle"))
                empty = false;
        	else if(gooseCounter < 5 && Control.critters.get(n) != this && this.gender != 0 && Control.critters.get(n).alive && Control.critters.get(n).space.intersects(attempt) && Control.critters.get(n).type.equals("Turtle"))
        		attack();
        }
        
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
		if(!alive) return;  //dead turtles don't act
        //Build a perception Rectangle to look around:
        Rectangle perception = new Rectangle(posx - Frog.RANGE, posy - Frog.RANGE, size+2*Frog.RANGE, size+2*Frog.RANGE);
        
        for(int n = Control.bits.size()-1; n >= 0; n--)
            if(Control.bits.get(n).edible && perception.intersects(Control.bits.get(n).space) && gender == 0)
            {
                //Find food, eat minerals if you're a baby...
                health += Control.bits.get(n).nutrients;
                Control.bits.remove(n);
            }
        for(int n = Control.critters.size()-1; n >= 0; n--)
      	  if(Control.critters.get(n) != this && this.gender != 0 && Control.critters.get(n).gender == 0 && Control.critters.get(n).alive && this.space.intersects(Control.critters.get(n).space) && Control.critters.get(n).type.equals("Goose"))
            {
                //Find food, eat baby geese regardless of health
      		  
                health += 100;
                Control.critters.remove(n);
            }
      	  else if(Control.critters.get(n) != this && this.gender != 0 && Control.critters.get(n).gender != 0 && this.health > 20 && Control.critters.get(n).alive && this.space.intersects(Control.critters.get(n).space) && Control.critters.get(n).type.equals("Goose"))
      		{
      		    //Find and eat adult geese if you're an adult and strong/healthy enough to process a bigger animal
      		  	health += 150;
      		    Control.critters.remove(n);
      		}
        
        //Baby turtles and Male turtles are done, return back.
        if(gender != 2) return;
        
        //female turtles look for male turtles...
        if(pregnant) return;  
        
        boolean found = false;
        for(int n = 0; !found && n < Control.critters.size(); n++)
        {
            Animal current = Control.critters.get(n);
            //checks to make sure it is a Live Male turtle within perception distance
            if(current.alive && current.gender == 1 && current.type.equals("Turtle") && current.space.intersects(perception))
                found = true;
        }
        if(found) pregnant = true;
       
	}
	private void attack()
	{
        pic = new ImageIcon("finalexplode.png");
        alive = false;
        explode = true;
	}
	
	public void age()
	{
    	if(!alive) return;
        health -= DAILY_HUNGER;
        
        // give birth (lay eggs) during the day on rocks
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
                Animal baby = new SnappingTurtle(posx+x, posy+y);
                
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
//                    Animal.numBirthed++; may have to add this to the magikarp one as well??? 
                    if(Math.random() < .25) morebabies = false;
                }
                if(attempts > 100) morebabies = false;
            }
        }
        
        age++;
        
        if(gender == 0 && age > MINAGETURTLE) //Baby Turtle is growing up
        {
            //gets bigger
            size = PIP;
            space = new Rectangle(posx, posy, size, size);
            
            gender++;
            size = PIP * 7/5;
            if(Math.random() < .5) gender++;  //50% chance of female
            
            pic = new ImageIcon("adultturtle.png");
        }
        if(this.age > OLDAGETURTLE)
        {
            if(Math.random()*100 > health)
            {
                alive = false;
            }
        }
	}
	
	public void draw(Graphics g)
    {
        if(!alive && !explode) return;
        
        if(!alive && explode)
        	g.drawImage(pic.getImage(), posx, posy, size*2 - 5, size*2 - 5, null);
        else if(alive)
        	g.drawImage(pic.getImage(), posx, posy, size, size, null);
    }
	
}
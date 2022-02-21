//Mine!

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;


public class Control implements Constants{

    public static ArrayList<Animal> critters;
    public static ArrayList<Mineral> bits;
    
    public static int day;
    public static int hour;
    
    public static void SetUpPond()
    {
        critters = new ArrayList<Animal>();
        bits = new ArrayList<Mineral>();
        day = 1;
        hour = 0;
        
        int cellcount = (PONDW/PIP)*(PONDH/PIP);
        
        //<editor-fold defaultstate="collapsed" desc="Placing Rocks Code...">   
        
        //Rocks
        //First rock is near the middle of the pond...
        int x = (int)(Math.random()*(PONDW/2)) + MARGIN + PONDW/4;
        int y = (int)(Math.random()*(PONDH/2)) + MARGIN + PONDH/4;
        
        for(int n = 0; n < cellcount; n++)
        {
            if(Math.random() < ROCK_PERC)  //Chance of new rock
            {
                boolean found = false;
                while(!found)
                {
                    //random position and temporary rock made
                    x = (int)(Math.random()*(PONDW - 2*PIP)) + MARGIN + PIP/2;
                    y = (int)(Math.random()*(PONDH - 2*PIP)) + MARGIN + PIP/2;
                    Mineral temp = new Mineral(x, y, false);
                    
                    boolean goodplacement;
                    
                    //checks on the temp rock based on whether it should be connected to other rocks or not
                    if(Math.random() < CONNECTED_ROCK_PERC)
                    {
                        goodplacement = false;
                        for(int c = 0; c < bits.size(); c++)
                            if(bits.get(c).space.intersects(temp.space) && !bits.get(c).space.contains(temp.space) && !temp.space.contains(bits.get(c).space))
                                goodplacement = true;
                        //if the rocks touch but are not inside each other, it's a good rock and ready to be placed
                    }
                    else
                    {
                        goodplacement = true;
                        for(int c = 0; c < bits.size(); c++)
                            if(bits.get(c).space.intersects(temp.space))
                                goodplacement = false;
                        //if it's not supposed to be connected, just check if it touches other rocks
                    }
                    
                    if(goodplacement) 
                    {
                        found = true;
                        bits.add(temp);
                    }
                }
            }               
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Placing Food Code...">   
        
        //Place Food:
        int n = 0;
        while(n < cellcount)
        {
            //random spot, that is not a rock... 
            x = (int)(Math.random()*(PONDW - 2*PIP)) + MARGIN + PIP/2;
            y = (int)(Math.random()*(PONDH - 2*PIP)) + MARGIN + PIP/2;
            boolean empty = true;
            for(int c = 0; c < bits.size(); c++)
                if(bits.get(c).space.contains(x, y))
                    empty = false;
            //Food is only placed in open spaces
            if(empty)
            {
                n++;
                if(Math.random() < ROCK_PERC)
                    bits.add(new Mineral(x, y, true));
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Placing Animals Code...">   
        
        //Place Animals:
        for(int count = 0; count < NOA; count++)
        {
            boolean placed = false;
            while(!placed)
            {
                x = (int)(Math.random()*(PONDW-2*PIP)) + MARGIN + PIP;
                y = (int)(Math.random()*(PONDH-2*PIP)) + MARGIN + PIP;

                //MY OWN CODE:
                Animal temp;
                int randInt = (int) (Math.random() * 2);
                if (randInt == 0) {
                    temp = new Frog(x, y);
                } else {
                	temp = new Duck(x, y);
                }
                placed = true;
                for(int c = 0; placed && c < bits.size(); c++)
                    if(!bits.get(c).edible && bits.get(c).space.intersects(temp.space))
                        placed = false;
                
                for(int c = 0; placed && c < critters.size(); c++)
                    if(critters.get(c).space.intersects(temp.space))
                        placed = false;
                
                //If placed is still true here, add the animal to the pond
                if(placed) critters.add(temp);
                //otherwise, run the while loop again, until a proper critter is added.
            }
        }
        
        //</editor-fold>
    }
    
    public static void Action()
    {
        for(int n = critters.size()-1; n >= 0; n--)
            critters.get(n).move();
        
        for(int n = critters.size()-1; n >= 0; n--)
            critters.get(n).act();
    }
    
    public static void NextTurn()
    {
        hour++;
        if(hour == 24) 
        {
        	//MY OWN CODE: reset the number birthed by resetting
            Animal.numBirthed = 0;
            hour = 0;
            day++;
            NextDay();
        }
    }
    
    private static void NextDay()
    {
        for(int n = critters.size()-1; n >= 0; n--)
            critters.get(n).age();
        
        for(int n = critters.size()-1; n >= 0; n--)
            if(!critters.get(n).alive) critters.remove(n);
        
        //MY OWN CODE: reset the max population to size of critters ArrayList
        if (critters.size() > Animal.maxPopulation) {
            Animal.maxPopulation = critters.size();
        }
        
        //Adding new food...
        for(int n = 0; n < ADD_FOOD; n++)
        {
            int x = (int)(Math.random()*(PONDW - 2*PIP)) + MARGIN + PIP/2;
            int y = (int)(Math.random()*(PONDH - 2*PIP)) + MARGIN + PIP/2;
            boolean empty = true;
            for(int c = 0; c < bits.size(); c++)
                if(bits.get(c).space.contains(x, y))
                    empty = false;
            if(empty)
                bits.add(new Mineral(x, y, true));
        }
        
    }
    
    public static boolean Daytime()
    {
        if(hour > 6 && hour < 18) return true;
        else return false;
    }
    public static boolean Nighttime()
    {
        if(hour < 5 || hour > 19) return true;
        else return false;
    }
    
    public static void PaintPond(Graphics g)
    {
        //Paint all of the items (food and rocks) first:
        for(int n = 0; n < bits.size(); n++)
            bits.get(n).draw(g);
        
        //Then paint all of the Animals on top:
        for(int n = 0; n < critters.size(); n++)
            critters.get(n).draw(g);
    }
}
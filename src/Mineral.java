package newpond;

//Mine!

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Mineral implements Constants {

    public int posx;
    public int posy;
    public int size;
    public Color tint;
    public Rectangle space;
    
    public boolean edible;
    public int nutrients;
    
    public Mineral(int x, int y, boolean food)
    {
        posx = x;
        posy = y;
        
        edible = food;
        if(food) 
        {
            tint = FOOD_TINT;
            size = PIP/3;
            nutrients = FOODVAL;
        }
        else
        {
            tint = ROCK_TINT;
            size = (int)(Math.random()*PIP) + PIP;
            nutrients = -1;
        }
        space = new Rectangle(posx, posy, size, size);
    }
    public void draw(Graphics g)
    {
        g.setColor(tint);
        g.fillOval(posx, posy, size, size);
    }
}

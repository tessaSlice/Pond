package newpond;

//Mine!

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;


public abstract class Animal implements Constants {

    public int posx;
    public int posy;
    public int posz;
    public int size;
    
    public String type;
    public int age;
    public int health;
    public int gender;  //0 = baby, 1 = male, 2 = female
    
    public ImageIcon pic;
    public Rectangle space;
    public boolean alive;
    public boolean pregnant;
    
    public Animal(int x, int y)
    {
        posx = x;
        posy = y;
        posz = 0;
        size = PIP;
        
        type = "Animal";
        age = 1;
        health = 100;
        gender = 0;
        
        space = new Rectangle(x, y, size, size);
        alive = true;
        pregnant = false;
    }
    
    public void draw(Graphics g)
    {
        if(!alive) return;
        g.drawImage(pic.getImage(), posx, posy, size, size, null);
    }
    
    
    public abstract void move();
    public abstract void act();
    public abstract void age();
}

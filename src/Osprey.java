import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Osprey extends Animal{

	public boolean divebomb = false;
	public int direction;
	public int speed;
	public static boolean produceOsprey = false;
	public static int factoryCooldown = 0;
	public static final int RANGE = PIP/2;
	public static int numOspreys = 0;
	
	public Osprey(int x, int y)
	{
		super(x, y);
		type = "Osprey";
		pic = new ImageIcon("OspreyUp.png");
        size = 15*PIP/5;
        space = new Rectangle(x, y, size, size);
        pregnant = false; //Ospreys will not reproduce
        posz = 1;
        direction = (int)(Math.random()*5);
        speed = 20;
	}
	
	public Osprey(int x, int y, int s)
	{
		super(x, y);
		type = "Osprey";
		pic = new ImageIcon("OspreyUp.png");
        size = 15*PIP/5;
        space = new Rectangle(x, y, size, size);
        pregnant = false; //Ospreys will not reproduce
        posz = 1;
        direction = (int)(Math.random()*5);
        speed = s;
	}
	
	public void move()
	{
		speed = (int)(Math.random() * 30) + 10;
		divebomb = false;
		int x;
		int y;
		if (Math.random() < 0.1)
		{
			direction = (direction + 1) % 4;			
		}
		else if (Math.random() < 0.2)
		{
			direction = (direction - 1) % 4;
		}
		if (direction == 0)
        {
        	x = 0;
        	y = -speed;
        	pic = new ImageIcon("OspreyForward.png");
        }
        else if (direction == 1)
        {
        	x = speed;
        	y = 0;
        	pic = new ImageIcon("OspreyRight.png");
        }
        else if (direction == 2)
        {
        	x = 0;
        	y = speed;
        	pic = new ImageIcon("OspreyBack.png");
        }
        else if (direction == 3)
        {
        	x = -speed;
        	y = 0;
        	pic = new ImageIcon("OspreyLeft.png");
        }
        else
        {
        	x = 0;
        	y = 0;
        	pic = new ImageIcon("OspreyUp.gif");
        	divebomb = true;
        }
		boolean empty = true;
        if(posx + x < MARGIN + 2) empty = false;
        if(posy + y < MARGIN + 2) empty = false;
        if(posx + x > MARGIN + PONDW - size - 2) empty = false;
        if(posy + y > MARGIN + PONDH - size - 2) empty = false;
        if (empty)
        {
        	posx += x;
            posy += y;
            space = new Rectangle(posx, posy, size, size);
        }
        else
        {
        	direction = (direction + 2) % 4;
        }
        
        if (produceOsprey && factoryCooldown > 5 && Math.random() < 0.02)
        {
        	Control.critters.add(new Osprey(MARGIN + 50, HIGH - MARGIN - 100, 40));
        	factoryCooldown = 0;
        }
	}
	
	public void age()
	{
		factoryCooldown++;
		numOspreys = 0;
		for (int n = Control.critters.size() - 1; n >= 0; n--)
		{
			if (Control.critters.get(n).type.equals("Osprey"))
			{
				numOspreys++;
			}
		}
//		if (numOspreys > 5)
//		{
//			produceOsprey = false;
//		}
//		else
//		{
//			produceOsprey = true;
//		}
		
		age++;
		
		
		if (age > 10)
		{
			alive = false;
		}
	}
	
	public void act()
	{
		Rectangle perception = new Rectangle(posx - RANGE, posy - RANGE, size+2*RANGE, size+2*RANGE);
		if (divebomb && Control.critters.size() > 50 || Control.critters.size() > 100)
		{
			for(int n = Control.bits.size()-1; n >= 0; n--)
	            if(perception.intersects(Control.bits.get(n).space) && Control.bits.get(n).nutrients > -2)
	            {
	                Control.bits.remove(n);
	            }
			for(int n = Control.critters.size()-1; n >= 0; n--)
	            if(Control.critters.get(n).type != "Osprey" && perception.intersects(Control.critters.get(n).space))
	            {
	                Control.critters.get(n).alive = false;
	            }
			
			Control.bits.add(new Mineral(posx, posy, true));
			Control.bits.get(Control.bits.size()-1).tint = Color.orange;
			Control.bits.get(Control.bits.size()-1).size = size;
//			Control.critters.add(new Osprey(posx, posy));
;		}
		
		for(int n = Control.bits.size()-2; n >= 0; n--)
		{
			if (Control.bits.get(n).size == size)
			{
				Control.bits.remove(n);
			}
		}
	}
}

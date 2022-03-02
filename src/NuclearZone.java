import java.awt.Color;
import java.awt.Graphics;

public class NuclearZone {
	int posx;
	int posy;
	int size;
	
	public NuclearZone(int x, int y, int s) {
		posx = x;
		posy = y;
		size = s*2;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(posx-(size/4), posy-(size/4), size, size);
	}
}
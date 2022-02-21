//Mine!

import java.awt.Color;
import java.awt.Font;


public interface Constants {
    //Screen Values:
    public static final int WIDE = 1100;   //screen size
    public static final int HIGH = 800;
    public static final int STATSBAR = 3*WIDE/4;  //where the vertical separation is located
    public static final int MARGIN = 60;          //margin around the pond water
    public static final int PONDW = STATSBAR - 2*MARGIN;   //pond dimensions
    public static final int PONDH = HIGH - 2*MARGIN;       //  does not include the "beach" boarder
    
    //Fonts:
    public static final Font LARGE_FONT = new Font("Times New Roman", Font.BOLD, 26);
    public static final Font MEDIUM_FONT = new Font("Times New Roman", Font.BOLD, 20);
    public static final Font SMALL_FONT = new Font("Times New Roman", Font.PLAIN, 14);
    
    //Colors:
    public static final Color WATER_DAY_TINT = new Color(0, 195, 200);
    public static final Color WATER_TINT = new Color(0, 175, 165);
    public static final Color WATER_NIGHT_TINT = new Color(0, 130, 120);
    public static final Color ROCK_TINT = new Color(100, 80, 40);
    public static final Color FOOD_TINT = new Color(50, 255, 50);
    public static final Color BEACH_DAY_TINT = new Color(200, 140, 0);
    public static final Color BEACH_TINT = new Color(190, 120, 0);
    public static final Color BEACH_NIGHT_TINT = new Color(150, 80, 0);
    
    
    //Program Constants:
    public static final int DELAY = 5;    //number of cycles between hours
    
    public static final int NOA = 30;     //initial Number Of Animals in the pond.
    public static final int PIP = 18;     //standard unit size
    public static final int FOODVAL = 8;  //standard nutritional food value
    public static final int DAILY_HUNGER = 6;  //how hungry you get at the end of the day.
    
    public static final double FOOD_PERC = .2;      //these are the percentages that 
    public static final double ROCK_PERC = .05;     //  are used in the initial creation
    public static final double CONNECTED_ROCK_PERC = .80;   //   of the pond...
    
    public static final int ADD_FOOD = 10;  //Roughly the amount of new food added each day.
        
}
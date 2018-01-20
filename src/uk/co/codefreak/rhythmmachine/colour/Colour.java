package uk.co.codefreak.rhythmmachine.colour;

import java.awt.*;
import java.util.Random;

public class Colour {

    public static final Color WHITE = new Color(0xffffff, false);
    public static final Color GREY_E0 = new Color(0xe0e0e0, false);
    public static final Color GREY_D0 = new Color(0xd0d0d0, false);
    public static final Color GREY_70 = new Color(0x707070, false);
    public static final Color GREY_40 = new Color(0x404040, false);
    public static final Color GREY_30 = new Color(0x303030, false);
    public static final Color BLACK = new Color(0x000000, false);
    public static final Color SADDLE_BROWN = new Color(0x8b4513, false);
    public static final Color PURPLE = new Color(0xa020f0,false);
    public static final Color PURPLE_4 = new Color(0x551a8b, false);
    public static final Color GREEN_FF = new Color(0x00ff00, false);
    public static final Color GREEN_BB = new Color(0x00bb00, false);
    public static final Color GREEN_AA = new Color(0x00aa00, false);
    public static final Color GREEN_99 = new Color(0x009900, false);
    public static final Color GREEN_77 = new Color(0x007700, false);
    public static final Color RED_FF = new Color(0xff0000, false);
    public static final Color RED_BB = new Color(0xbb0000, false);
    public static final Color BLUE_FF = new Color(0x0000ff, false);
    public static final Color BLUE_DD = new Color(0x0000dd, false);
    public static final Color BLUE_CC = new Color(0x0000cc, false);
    public static final Color BLUE_BB = new Color(0x0000bb, false);
    public static final Color BLUE_AA = new Color(0x0000aa, false);
    public static final Color CYAN_FF = new Color(0x00ffff, false);
    public static final Color CYAN_BB = new Color(0x00bbbb, false);
    public static final Color YELLOW = new Color(0xffff00, false);
    public static final Color YELLOW_BB = new Color(0xbbbb00, false);

    public static Color randomBlue() {
        int blue = new Random().nextInt(9);

        if(blue == 0 || blue == 3 || blue == 6) {
            return Colour.BLUE_BB;
        } else if(blue == 1 || blue == 4 || blue == 7) {
            return Colour.BLUE_CC;
        } else if(blue == 2 || blue == 5 || blue == 8) {
            return Colour.BLUE_AA;
        }
        return Color.BLACK;
    }

}

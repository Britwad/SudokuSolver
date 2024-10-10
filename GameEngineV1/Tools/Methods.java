package GameEngineV1.Tools;

import java.awt.*;

public class Methods {

    public static Color brighter(Color color, double factor) {
        //Directly Taken and modified from Color.class
        int var1 = color.getRed();
        int var2 = color.getGreen();
        int var3 = color.getBlue();
        int var4 = color.getAlpha();
        byte var5 = 3;
        if (var1 == 0 && var2 == 0 && var3 == 0) {
            return new Color(var5, var5, var5, var4);
        } else {
            if (var1 > 0 && var1 < var5) {
                var1 = var5;
            }

            if (var2 > 0 && var2 < var5) {
                var2 = var5;
            }

            if (var3 > 0 && var3 < var5) {
                var3 = var5;
            }

            return new Color(Math.min((int)((double)var1 / factor), 255), Math.min((int)((double)var2 / factor), 255), Math.min((int)((double)var3 / factor), 255), var4);
        }
    }
    public static Color darker(Color color, double factor) {
        return new Color(Math.max((int)((double)color.getRed() * factor), 0), Math.max((int)((double)color.getGreen() * factor), 0), Math.max((int)((double)color.getBlue() * factor), 0), color.getAlpha());
    }
    public static Color changeAlpha(Color color, int amount) {
        return new Color (color.getRed(),color.getBlue(),color.getGreen(), Math.max(0,Math.min(color.getAlpha()+amount,255)));
    }

    public static boolean arrayContains(Object[] a, Object n) {
        for (Object i: a) {
            if (i==n) return true;
        }
        return false;
    }
    public static boolean arrayContains(int[] a, int n) {
        for (int i: a) {
            if (i==n) return true;
        }
        return false;
    }

    public static void drawCenteredString(Graphics g, String s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(s))/2;
        int y = rect.y + ((rect.height - metrics.getHeight())/2) + metrics.getAscent();

        g.drawString(s,x,y);
    }
    public static void drawCenteredString(Graphics g, String[] s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        int gap = (rect.height-metrics.getHeight()*s.length)/(1+s.length);
        for (int i = 0;i<s.length;i++) {
            int x = rect.x + (rect.width - metrics.stringWidth(s[i])) / 2;

            int y = rect.y + gap*(i+1) + metrics.getHeight()*(i+1);

            g.drawString(s[i], x, y);
        }
    }
    public static void drawCenteredString(Graphics g, String[] s, Rectangle rect, int gap) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        for (int i = 0;i<s.length;i++) {
            int x = rect.x + (rect.width - metrics.stringWidth(s[i])) / 2;

            int y = rect.y + gap*(i+1) + metrics.getHeight()*(i+1);

            g.drawString(s[i], x, y);
        }
    }
    public static void drawCenteredString(Graphics g, String[][] s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        int yGap = (rect.height-metrics.getHeight()*s.length)/(1+s.length);
        for (int y = 0;y<s.length;y++) {
            int xSum = 0;
            for (String str: s[y]) {
                xSum+=metrics.stringWidth(str);
            }
            int xGap = (rect.width-xSum)/(1+s[y].length);
            for (int x = 0;x<s[y].length;x++) {
                int xp = rect.x + (xGap * (x + 1)) + (metrics.stringWidth(s[y][x]) * (x + 1));
                int yp = rect.y + yGap*(y+1) + metrics.getHeight()*(y+1);

                g.drawString(s[y][x], xp, yp);
            }

        }
    }
    public static void drawCenteredString(Graphics g, String[][] s, Rectangle rect, int xGap, int yGap) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        for (int y = 0;y<s.length;y++) {
            for (int x = 0;x<s[y].length;x++) {
                int xp = rect.x + (xGap * (x + 1)) + (metrics.stringWidth(s[y][x]) * (x + 1));
                int yp = rect.y + yGap*(y+1) + metrics.getHeight()*(y+1);

                g.drawString(s[y][x], xp, yp);
            }
        }
    }
}

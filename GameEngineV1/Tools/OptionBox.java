package GameEngineV1.Tools;

import GameEngineV1.Game;
import GameEngineV1.GameObject;

import java.awt.*;

public class OptionBox extends GameObject {

    private int x, y, length, height;
    private Runnable clickBlock, tickBlock;
    private String text;
    private Color color, borderColor, fontColor;
    private Font font;
    private int borderThickness;
    private boolean borderOn;
    public boolean mouseOver;
    public boolean clickable;


    public OptionBox(int x, int y, int length, int height) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
        this.text = "";
        this.color = Color.white;
        this.borderColor = Color.black;
        this.borderThickness = 1;
        this.borderOn = false;
        this.fontColor = Color.black;
        this.font = new Font("Century Gothic",Font.BOLD,20);
        this.mouseOver = false;
        this.clickable = true;
    }
    public OptionBox(int x, int y, int length, int height, String text) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
        this.text = text;
        this.color = Color.white;
        this.borderColor = Color.black;
        this.borderThickness = 1;
        this.borderOn = false;
        this.fontColor = Color.black;
        this.font = new Font("Century Gothic",Font.BOLD,20);
        this.mouseOver = false;
        this.clickable = true;
    }

    public void tick(Game game) {
        if (tickBlock!=null) {
            tickBlock.run();
        }
    }

    public void render(Graphics g) {
        g.setColor(this.color);
        g.fillRoundRect(x,y,length,height,10,10);
        if (borderOn) {
            ((Graphics2D) g).setStroke(new BasicStroke(borderThickness));
            g.setColor(this.borderColor);
            g.drawRoundRect(x+((borderThickness-1)/2),y+((borderThickness-1)/2),length-borderThickness+1,height-borderThickness+1,10,10);
        }
        g.setColor(fontColor);
        g.setFont(font);
        drawCenteredString(g,text,new Rectangle(x,y,length,height));
    }

    public void mouseOver() {
        mouseOver = true;
    }
    public void mouseNotOver() {
        mouseOver = false;
    }
    public void clicked() {
        if (clickBlock!=null&clickable) {
            clickBlock.run();
        }
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y=y;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setClickBlock(Runnable r) {
        this.clickBlock = r;
    }
    public void setTickBlock(Runnable r) {
        this.tickBlock = r;
    }
    public void setText(String s) {
        this.text = s;
    }
    public void setColor(Color color) {this.color = color;}
    public void setBorderColor(Color color) {this.borderColor = color;}
    public void setBorderThickness(int t) {this.borderThickness = t;}
    public void setFontColor(Color color) {this.fontColor = color;}
    public void borderOn(Boolean b) {this.borderOn=b;}
    public void setFont(Font font) {this.font = font;}

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getLength() {
        return this.length;
    }
    public int getHeight() {
        return this.height;
    }
    public Color getColor() {
        return this.color;
    }
    public Color getBorderColor() {
        return this.borderColor;
    }
    public Color getFontColor() {
        return this.fontColor;
    }

    public static void drawCenteredString(Graphics g, String s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(s))/2;
        int y = rect.y + ((rect.height - metrics.getHeight())/2) + metrics.getAscent();

        g.drawString(s,x,y);
    }
}

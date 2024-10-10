package GameEngineV1;

import GameEngineV1.Tools.OptionBox;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private int windowWidth, windowHeight;
    private GameInterface gameInterface;

    private int tps = 60;
    private Color backgroundColor;

    private Thread thread;
    private boolean running;

    ArrayList<GameObject> gameObjects = new ArrayList<>();
    ArrayList<OptionBox> boxes = new ArrayList<>();

    public boolean[] keysDown = new boolean[255];
    public int mouseX, mouseY;
    public boolean[] mouseDown = new boolean[5];

    public Game(GameInterface g, int width, int height, String title) {
        this.gameInterface = g;
        windowWidth = width;
        windowHeight = height;

        backgroundColor = Color.WHITE;

        thread = new Thread(this);

        new Window(width,height,title,this);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double ns = 1000000000/tps;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta+= (now-lastTime)/ns;
            lastTime = now;
            while (delta>=1) {
                tick();
                delta--;
            }
            if (running) render();
        }
        stop();
    }


    private void tick() {
        for (int i = 0;i<gameObjects.size();i++) {
            gameObjects.get(i).tick(this);
        }
        for (int i = 0;i<boxes.size();i++) {
            boxes.get(i).tick(this);
        }
        gameInterface.tick(this);

    }
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs==null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(backgroundColor);
        g.fillRect(0,0,windowWidth,windowHeight);

        for (int i = 0;i<gameObjects.size();i++) {
            gameObjects.get(i).render(g);
        }
        for (int i = 0;i<boxes.size();i++) {
            boxes.get(i).render(g);
        }
        gameInterface.render(g);

        g.dispose();
        bs.show();
    }
    void start() {
        thread.start();
        running = true;
    }
    private void stop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        running = false;
    }

    public void addObject(GameObject o) {
        gameObjects.add(o);
    }
    public void removeObject(GameObject o) {
        gameObjects.remove(o);
    }
    public void addBox(OptionBox box) {
        boxes.add(box);
    }
    public void removeBox(OptionBox box) { boxes.remove(box);}
    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }
    public ArrayList<OptionBox> getBoxes() { return boxes;}

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }


    //-------------------------------------------- Key and Mouse Input --------------------------------------------

    public void keyTyped(KeyEvent keyEvent) {

    }

    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if (key<=255) {
            keysDown[key] = true;
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if (key<=255) {
            keysDown[key] = false;
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        for (int i = 0;i<boxes.size();i++) {
            if (mouseX>boxes.get(i).getX()&mouseX<boxes.get(i).getX()+boxes.get(i).getLength()&
                    mouseY>boxes.get(i).getY()&mouseY<boxes.get(i).getY()+boxes.get(i).getHeight()) {
                boxes.get(i).clicked();
            }
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        mouseDown[mouseEvent.getButton()-1]=true;

    }

    public void mouseReleased(MouseEvent mouseEvent) {
        mouseDown[mouseEvent.getButton()-1]=false;
    }

    public void mouseEntered(MouseEvent mouseEvent) {

    }

    public void mouseExited(MouseEvent mouseEvent) {

    }

    public void mouseDragged(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
        for (int i = 0;i<boxes.size();i++) {
            if (mouseX>boxes.get(i).getX()&mouseX<boxes.get(i).getX()+boxes.get(i).getLength()&
                    mouseY>boxes.get(i).getY()&mouseY<boxes.get(i).getY()+boxes.get(i).getHeight()) {
                boxes.get(i).mouseOver();
            }
            else {
                boxes.get(i).mouseNotOver();
            }
        }
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
        for (int i = 0;i<boxes.size();i++) {
            if (mouseX>boxes.get(i).getX()&mouseX<boxes.get(i).getX()+boxes.get(i).getLength()&
                    mouseY>boxes.get(i).getY()&mouseY<boxes.get(i).getY()+boxes.get(i).getHeight()) {
                boxes.get(i).mouseOver();
            }
            else {
                boxes.get(i).mouseNotOver();
            }
        }
    }
}

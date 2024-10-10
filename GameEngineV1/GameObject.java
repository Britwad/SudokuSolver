package GameEngineV1;

import java.awt.*;

public abstract class GameObject {

    public float px = 0, py = 0, vx = 0, vy = 0, ax = 0, ay = 0, width = 0, height = 0;
    public Color color;

    public abstract void tick(Game game);
    public abstract void render(Graphics g);

}

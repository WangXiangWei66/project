package Tank;

import strategy.FireStrategy;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.UUID;

public class Player extends AbstractGameObject{


    public static final int SPEED = 5;
    private int x,y;
    private Dir dir;
    private boolean bL,bU,bR,bD;
    private boolean moving = false;
    private Group group;
    private boolean live = true;
    private UUID id = UUID.randomUUID();
    private FireStrategy strategy = null;

    public Player(int x,int y,Dir dir,Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

        this.initFireStrategy();
    }

    public UUID getId() {
        return id;
    }

    public boolean isMoving() {
        return moving;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public void paint(Graphics g) {

    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private void initFireStrategy() {
        return;
    }

    public void keyPressed(KeyEvent e) {
        return;
    }

    public void keyReleased(KeyEvent e) {
        return;
    }
}

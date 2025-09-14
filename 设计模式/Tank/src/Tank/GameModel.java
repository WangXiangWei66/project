package Tank;

import Tank.chainofresponsibility.ColliderChain;

import java.awt.*;
import java.io.Serializable;

public class GameModel implements Serializable {
    private Player myTank;
    ColliderChain chain = new ColliderChain();

    public Player getMyTank() {
        return myTank;
    }

    public void paint(Graphics g) {
        return;
    }
}

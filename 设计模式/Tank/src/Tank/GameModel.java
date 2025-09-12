package Tank;

import Tank.chainofresponsibility.ColliderChain;

import java.io.Serializable;

public class GameModel implements Serializable {
    private Player myTank;
    ColliderChain chain = new ColliderChain();
}

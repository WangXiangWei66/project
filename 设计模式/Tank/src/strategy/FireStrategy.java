package strategy;
//规范坦克的开火策略，体现了设计模式中的策略模式
import Tank.Player;

import java.io.Serializable;

public interface FireStrategy extends Serializable {
    public void fire(Player p);
}

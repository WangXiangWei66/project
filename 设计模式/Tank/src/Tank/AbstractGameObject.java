package Tank;

import java.awt.*;//用于绘图
import java.io.Serializable;
//所有游戏对象的基类，为坦克大战的各种游戏元素提供了同一的接口规范
public abstract class AbstractGameObject implements Serializable {

    public abstract void paint(Graphics g);

    public abstract boolean isLive();
}

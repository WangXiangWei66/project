package Tank;

import java.util.Random;
//枚举类型，用于表示坦克、子弹等元素的移动方向
public enum Dir {

    L, U, R, D;
    //使用了静态方法，保证了整个枚举类型共享来了以哦个Random实例
    private static Random r = new Random();

    public static Dir randomDir() {
        return values()[r.nextInt(values().length)];
    }
}

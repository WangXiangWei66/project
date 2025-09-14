package Tank;
//坦克大战游戏的主窗口类，负责游戏界面显示、用于输入处理和游戏状态管理


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//继承Frame类，作为游戏的主窗口
public class TankFrame extends Frame {
    //单例模式，创建TankFrame的唯一实例
    public static final TankFrame INSTANCE = new TankFrame();
    //定义游戏窗口的宽高
    public static final int GAME_WIDTH = 800, GAME_HEIGHT = 600;
    //游戏模型对象，管理游戏种的所有元素（坦克、子弹、墙壁等）
    private GameModel gm = new GameModel();

    //私有构造方法（这是单例模式的要求），初始化窗口的属性
    private TankFrame() {
        this.setTitle("tank war");
        this.setLocation(400, 100);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        //为窗口添加键盘监听器，处理用户输入
        this.addKeyListener(new TankKeyListener());
    }

    //对paint方法重写，负责游戏画面的绘制
    @Override
    public void paint(Graphics g) {
        gm.paint(g);//绘制所有的游戏元素
    }

    //双缓冲技术来解决画面闪烁问题
    Image offScreenImage = null;//离屏图像的对象

    //实现了双缓冲的update方法
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        //获取离屏图像的绘图上下文
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();//保存当前的颜色
        //绘制黑色背景
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        //恢复之前的颜色并绘制游戏元素到离屏对象
        gOffScreen.setColor(c);
        paint(gOffScreen);//保证所有的绘制操作现在离屏图像上进行显示
        //将离屏图像一次性绘制到窗口，减少闪烁
        g.drawImage(offScreenImage, 0, 0, null);
    }

    //处理键盘事件的监听器
    private class TankKeyListener extends KeyAdapter {

        private GameModel gm;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();//获取按下的键码
            //按下S键，保存游戏的状态
            if (key == KeyEvent.VK_S)
                save();
                //按下L键，加载游戏状态
            else if (key == KeyEvent.VK_L)
                load();
                //其他的按键传递给玩家坦克来进行处理
            else gm.getMyTank().keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gm.getMyTank().keyReleased(e);
        }

        //保存游戏状态到文件
        private void save() {
            ObjectOutputStream oos = null;
            try {
                File f = new File("d:/test/tank.dat");
                FileOutputStream fos = new FileOutputStream(f);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(gm);//将游戏模型对象序列化到文件
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (oos != null) {
                        oos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //从文件加载到游戏状态
        private void load() {
            try {
                File f = new File("d:/test/tank.dat");
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                //从文件反序列化游戏模型对象
                this.gm = (GameModel) (ois.readObject());
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public GameModel getGm() {
            return this.gm;
        }
    }
}

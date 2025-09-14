package Tank;

import Tank.net.Client;//网络客户端，用于网络连接功能

public class Main {

    public static void main(String[] args) {
        //1.获取TankFrame的单例实例
        //2.调用setVisible显示游戏主窗口
        TankFrame.INSTANCE.setVisible(true);
        //1.创建新线程，避免音频播放阻塞
        //2.使用Lambda表达式简化线程任务，创建音频对象并循环播放背景音乐
        //3.启动线程开始播放
        //这种设计使得音频与游戏主线程分离，保证游戏的流程性
        new Thread(()->new Audio("audio/war1.wav").loop()).start();
        //该线程用于游戏主循环
        new Thread(()->{
            for(;;) {
                try{
                    Thread.sleep(25);//控制游戏的帧率
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TankFrame.INSTANCE.repaint();//调用重绘方法来调整游戏的画面
            }
        }).start();
        Client.INSTANCE.connect();//获取客户端的单例并建立网络连接
    }
}

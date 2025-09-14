package Tank;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;

public class Audio {
    //音频数据缓冲区
    byte[] b = new byte[1024 * 1024 * 15];

    //循环播放音频
    public void loop() {
        try {
            //无限循环，实现持续播放
            while (true) {
                int len = 0;//每次读取的音频数据长度
                //打开音频输出线路，指定音频格式和缓冲区大小
                sourceDataLine.open(audioFormat, 1024 * 1024 * 15);
                sourceDataLine.start();//启动音频输出线路
                //打印当前音频输入流是否支持标记功能
                System.out.println(audioInputStream.markSupported());
                //给音频输入流做标记，指定可读取的最大字节数
                audioInputStream.mark(12358946);
                //循环读取音频数据并写入输出线路播放
                while ((len = audioInputStream.read(b)) > 0) {
                    sourceDataLine.write(b, 0, len);
                }
                //重置音频输入流到之前标记的位置，准备下一次播放
                audioInputStream.reset();
                sourceDataLine.drain();//清空输出线路缓冲区
                sourceDataLine.close();//关闭输出线路
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //音频格式对象，包含音频的编码、采样率、声道数等信息
    private AudioFormat audioFormat = null;
    //音频输出线路，用于将音频数据输出到扬声器
    private SourceDataLine sourceDataLine = null;
    //数据线路信息对象，用于描述音频线路的特性
    private DataLine.Info dataLine_info = null;
    //音频输入流，用于从音频文件读取数据
    private AudioInputStream audioInputStream = null;
    //接收音频文件路径并初始化音频相关资源
    public Audio(String fileName) {
        try {
            //从类路径中加载音频文件，获取音频输入流
            audioInputStream = AudioSystem.getAudioInputStream(Audio.class.getClassLoader().getResource(fileName));
            audioFormat = audioInputStream.getFormat();//获取音频格式信息
            //创建数据线路信息对象
            dataLine_info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLine_info);
            FloatControl volctrl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            volctrl.setValue(-40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //单次播放音频
    public void play() {
        try {
            byte[] b = new byte[1024 * 5];
            int len = 0;
            sourceDataLine.open(audioFormat, 1024 * 5);
            sourceDataLine.start();
            System.out.println(audioInputStream.markSupported());
            audioInputStream.mark(12358946);//标记音频输入流当前位置
            while ((len = audioInputStream.read(b)) > 0) {
                sourceDataLine.write(b, 0, len);
            }
            audioInputStream.reset();
            sourceDataLine.drain();
            sourceDataLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //关闭音频输入流的方法
    public void close() {
        try {
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Audio a = new Audio("audio/explode.wav");
        a.loop();
    }
}

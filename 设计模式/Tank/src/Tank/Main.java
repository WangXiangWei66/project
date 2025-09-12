package Tank;

public class Main {

    public static void main(String[] args) {
        TankFrame.INSTANCE.setVisible(true);

        new Thread(()->new Audio("audio/war1.wav").loop()).start();
    }
}

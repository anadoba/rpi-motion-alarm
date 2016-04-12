package pl.nadoba.rpi.motion.alarm;

public class App {


    public static void main(String[] args) throws InterruptedException {
        MotionAlarm motionAlarm = new MotionAlarm();
        motionAlarm.loop();
    }


}

class MobileApp {

    void powerOn() {
        System.out.println("Power On");
    }

    private void accessSettings() {
        System.out.println("Opened Settings");
    }

    void shutdown() {
        System.out.println("Shutdown");
    }

    void launchSettings() {
        accessSettings();
    }
}
public class Lab5_task2 {
    public static void main(String[] args) {
        MobileApp myPhone = new MobileApp();
        myPhone.powerOn();
        myPhone.launchSettings();
        myPhone.shutdown();
    }
}
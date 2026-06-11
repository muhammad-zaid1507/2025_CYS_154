public class Lab12_task1 {
    public static void main(String[] args) {
        Navigation nav = new Tablet();
        nav.showDestination();
        nav.showLocation();
    }
}

class BaseDevice {
    public void makeCall() {
        System.out.println("You can call on this number...");
    }
    public void sendMessage() {
        System.out.println("You can send messages through this device...");
    }
}

interface CameraFeature {
    void capturePhoto();
    void recordVideo();
}

interface MediaFeature {
    void playAudio();
    void openMediaPlayer();
}

interface Navigation {
    void showLocation();
    void showDestination();
}

class Tablet extends BaseDevice implements CameraFeature, MediaFeature, Navigation {

    @Override
    public void capturePhoto() {
        System.out.println("I can take a Photo");
    }

    @Override
    public void recordVideo() {
        System.out.println("I can record a Video");
    }

    @Override
    public void playAudio() {
        System.out.println("I can play audio");
    }

    @Override
    public void openMediaPlayer() {
        System.out.println("I have my own media player");
    }

    @Override
    public void showLocation() {
        System.out.println("I can show location");
    }

    @Override
    public void showDestination() {
        System.out.println("This is my destination");
    }
}
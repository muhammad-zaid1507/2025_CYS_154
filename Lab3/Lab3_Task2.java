import java.util.Scanner;

public class Lab3_Task2 {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        Time z = new Time();
        System.out.println("==========================\nThe Time Format will be\nHH:MM:SS\n============");
        System.out.println("Enter Time in Seconds: ");
        z.Conversion(input.nextInt());
        System.out.println("Days: " + z.getDays());
        System.out.printf("%02d:%02d:%02d", z.getHours() , z.getMins() , z.getRem_secs());
    }
}

class Time {
    private long Remaining_secs, Hours, minutes , days;

    public void Conversion(int sec) {
        Hours = sec / 3600;

        // Controlling overflow (If user enters more than 86399 it will increase value in Days)
        if (Hours > 23) {
            days = Hours / 24;
            Hours = Hours % 24;
        }
        Remaining_secs = sec % 3600;
        minutes = Remaining_secs / 60;
        Remaining_secs = Remaining_secs % 60;
    }
    // Setters

    public long setDay(){
        return days;
    }
    public long setHour(){
        return Hours;
    }
    public long setMin(){
        return minutes;
    }
    public long setSec(){
        return Remaining_secs;
    }
    // Getters

    public long getDays(){
        return days;
    }
    public long getHours(){
        return Hours;
    }
    public long getMins(){
        return minutes;
    }
    public long getRem_secs(){
        return Remaining_secs;
    }
}

package Lab_4;

import java.util.*;

//  Main Class

public class Lab4_task1 {
    public static void main(String[] args){

        Scanner x = new Scanner(System.in);

        // Input for first Time Stamp

        System.out.println("Enter Time of 1st Time Stamp (in Seconds): ");
        int T1 = x.nextInt();
        Time time1 = new Time(T1);

        // Input for second Time Stamp

        System.out.println("Enter Time of 2nd Time Stamp (in Seconds): ");
        int T2 = x.nextInt();
        Time time2 = new Time(T2);

        System.out.printf("\nTime Stamp 1: %02d:%02d:%02d", time1.getHours(), time1.getMins(), time1.getRem_secs());
        System.out.printf("\nTime Stamp 2: %02d:%02d:%02d\n", time2.getHours(), time2.getMins(), time2.getRem_secs());

        // Time Difference in seconds

        System.out.println("\nDifference in Seconds: " + time1.Difference_in_seconds(time2));

        // Time Difference as Time Stamps

        Time diffTime = time1.Difference_in_timestamp(time2);
        System.out.printf("Difference as Time: %02d:%02d:%02d\n", diffTime.getHours(), diffTime.getMins(), diffTime.getRem_secs());

        // Time After 12 A.M.

        System.out.println("\nEnter seconds after 12 AM: ");
        int sec = x.nextInt();
        Time time3 = new Time(sec);
        time3.Display_in_12hour_format();
    }
}

class Time {

    //  Private attributes for calculation

    private long rem_secs, hours, mins , days;
    long Hour;
    String Am_Pm;
    public Time(long hours, long mins, long rem_secs) {
        this.hours = hours;
        this.mins = mins;
        this.rem_secs = rem_secs;
    }

    //  Testing constructor

    public Time(int sec) {
        Conversion(sec);
    }

    //  Time after 12 A.M.

    public void Display_in_12hour_format() {
        Hour = hours % 12;
        if(Hour == 0){
            Hour = 12;
        }
        if(hours < 12){
            Am_Pm = "AM";
        }
        else {
            Am_Pm = "PM";
        }

        System.out.println("Days: " + days);
        System.out.printf("%02d:%02d:%02d %s\n", Hour, mins, rem_secs, Am_Pm);
    }

    //  Method for Calculation of Days, Hours and Minutes

    public void Conversion(int sec) {
        hours = sec / 3600;

        // Controlling Overflow

        if (hours > 23) {
            days = hours / 24;
            hours = hours % 24;
        }

        rem_secs = sec % 3600;
        mins = rem_secs / 60;
        rem_secs = rem_secs % 60;
    }

    // Method for converting time into seconds

    public long seconds() {
        return (hours * 3600) + (mins * 60) + rem_secs;
    }

    //  Method for calculating difference between two times in seconds

    public long Difference_in_seconds(Time t) {
        return Math.abs(this.seconds() - t.seconds());
    }

    // Method for calculating difference between two time stamps

    public Time Difference_in_timestamp(Time t) {

        // Using absolute function to handle negative values

        long difference = Math.abs(this.seconds() - t.seconds());

        // After difference of two Time objects showing there answer as a new Time object

        return new Time((int) difference);
    }

    //  Getters

    public long getDays(){
        return days;
    }

    public long getHours(){
        return hours;
    }

    public long getMins(){
        return mins;
    }

    public long getRem_secs() {
        return rem_secs;
    }
}

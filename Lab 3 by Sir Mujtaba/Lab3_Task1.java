import java.util.Scanner;

public class Lab3_Task1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Point z = new Point();

        System.out.println("Enter your 3 values (x, y, z): ");
        z.setPoint(input.nextInt(), input.nextInt(), input.nextInt());

        System.out.println("After Assignment Of Values:");
        System.out.println(z.getPoint());
        input.close();
    }
}
class Point {
    private int x , y , z ;
    public void setPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public String getPoint() {
        return "X: " + x + "\nY: " + y + "\nZ: " + z;
    }
}
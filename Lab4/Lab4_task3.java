import java.util.*;

// Main class

public class Lab4_task3 {
    public static void main(String[] args){
        boolean status;
        Scanner x = new Scanner(System.in);
        Random random = new Random();

        // Two Persons (Objects)

        Person Ali = new Person();
        Person Hasan = new Person();

        // Arraylist for Hurdles

        ArrayList<Hurdle_Points> Hurdles = new ArrayList<>();

        // Storing random hurdles into the arraylist using for loop

        for(int i = 1; i <= 10; i++) {
            int hurdle_x = random.nextInt(21) - 10;      // range from -10 to 10
            int hurdle_y = random.nextInt(21) - 10;      // range from -10 to 10
            Hurdles.add(new Hurdle_Points(hurdle_x, hurdle_y));
        }

        // Starting of the game

        System.out.println("-----------------------------------");
        System.out.println("\n\t STARTING OF THE GAME\t\n");
        System.out.println("-----------------------------------");
        System.out.println("\nBoundary of the screen = (-10 , 10)");
        System.out.println("Ali's Initial Position = (0 , 0)");
        System.out.println("Hasan's Initial Position = (0 , 0)");
        System.out.println("Hasan has random movement whereas Ali will move according to your instructions");

        // Infinite loop

        boolean running = true;
        while(running) {

            System.out.print("Ali: Where should I go now \"W (up),  S (down), A (right), D (left)\" and for quit enter \"Q\": ");
            char move = x.next().charAt(0);
            if(move == 'Q' || move == 'q'){
                running = false;
                break;
            }
            status = Ali.Position_changing(move, Hurdles, "Ali");
            if(status == false){
                running = false;
                break;
            }

            // Storing moves in an array and using random for getting them

            char[] directions = {'W','S','A','D'};
            char Random_direction = directions[random.nextInt(4)];
            Hasan.Position_changing(Random_direction, Hurdles, "Hasan");
            System.out.println("Ali is at = (" + Ali.getX() + " : " + Ali.getY() + ")");
            System.out.println("Hasan is at = (" + Hasan.getX() + " : " + Hasan.getY() + ")\n");
        }
    }
}

// Class for Hurdles or Obstacles

class Hurdle_Points{
    private int x , y;

    // Constructor

    Hurdle_Points(int x , int y){
        this.x = x;
        this.y = y;
    }

    // Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

// Class for making persons

class Person{

    // Instance variables for Ali and Hasan's Positions

    private int x = 0;
    private int y = 0;

    // Method for changing positions of Persons

    public boolean Position_changing(char direction, ArrayList<Hurdle_Points> Hurdles, String name) {

        // Variables for storing the change in positons after user's input

        int x_after = x;
        int y_after = y;

        // Conditions for changing positions of the person according to user's input

        if(direction == 'W' || direction == 'w') {
            y_after += 3;
        }
        else if(direction == 'S' || direction == 's') {
            y_after -= 3;
        }
        else if(direction == 'D' || direction == 'd') {
            x_after += 3;
        }
        else if(direction == 'A' || direction == 'a') {
            x_after -= 3;
        }

        // Condition for checking if the person is hitting the boundary

        if(x_after < -10 || x_after > 10 || y_after < -10 || y_after > 10) {
            System.out.println("\n========== GAME OVER!!! " + name + " is hitting the boundary ==========");
            return false;
        }

        for(Hurdle_Points p : Hurdles) {
            if(p.getX() == x_after && p.getY() == y_after) {
                System.out.println(name + " is hitting the hurdle at (" + x_after + " : " + y_after + ") Hurry Up!, change it's position!!!");
            }
        }

        // Storing the changed positions

        x = x_after;
        y = y_after;

        return true;
    }

    // Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
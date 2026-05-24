package game;

public class Pacman {
    int x, y;

    public Pacman(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(char direction, char[][] board) {
        int newX = x;
        int newY = y;

        if (direction == 'w') newX--;
        else if (direction == 's') newX++;
        else if (direction == 'a') newY--;
        else if (direction == 'd') newY++;

        if (board[newX][newY] != '#') {
            x = newX;
            y = newY;
        }
    }
}
package game;

import java.util.Random;

public class Ghost {
    int x, y;
    Random rand = new Random();

    public Ghost(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(char[][] board) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        int dir = rand.nextInt(4);
        int newX = x + dx[dir];
        int newY = y + dy[dir];

        if (board[newX][newY] != '#') {
            x = newX;
            y = newY;
        }
    }
}

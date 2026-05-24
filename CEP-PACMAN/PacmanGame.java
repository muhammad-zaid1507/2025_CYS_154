package game;

import java.util.Scanner;

public class PacmanGame {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        GameBoard board = new GameBoard();
        Pacman pacman = new Pacman(1, 1);
        Ghost ghost = new Ghost(6, 5);

        int score = 0;

        while (true) {

            System.out.println("\nScore: " + score);
            board.printBoard(pacman, ghost);

            System.out.print("Move (W/A/S/D or R=Restart): ");
            char move = sc.next().toLowerCase().charAt(0);

            if (move == 'r') {
                pacman = new Pacman(1, 1);
                ghost = new Ghost(6, 5);
                board = new GameBoard();
                score = 0;
                System.out.println("Game Restarted!");
                continue;
            }

            pacman.move(move, board.board);
            ghost.move(board.board);

            if (board.board[pacman.x][pacman.y] == '.') {
                score++;
                board.board[pacman.x][pacman.y] = ' ';
            }

            if (pacman.x == ghost.x && pacman.y == ghost.y) {
                System.out.println("Game Over!");
                System.out.println("Final Score: " + score);
                break;
            }
        }

        sc.close();
    }
}

package wumpusworld;

public class GameOverException extends Exception {
    public boolean win;
    public GameOverException(boolean win) {
        this.win = win;
    }
}

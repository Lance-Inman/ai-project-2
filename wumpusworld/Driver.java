package wumpusworld;

public class Driver {

    public static void main(String[] args) {
        Agent a = new ReasoningAgent();
        Game g = new Game(10, a, 0.1, 0.1, 0.1);
        try {
            a.start(g);
        } catch (GameOverException goe) {
            if(goe.win) {
                System.out.println("The agent won the game!");
            } else {
                System.out.println("The agent lost the game!");
            }
        }
    }
    
}

package wumpusworld;

import java.util.ArrayList;

public class ReasoningAgent extends Agent
{
    public ReasoningAgent()
    {
        super();
    }

    private boolean backtrack(ArrayList<int[]> moveStack, int riskFactor) throws GameOverException{
        int i = lookBack(riskFactor);
        if(i < 0) {
            return false;
        }

        int[] cell = knowledgeBase.moveStack.get(i);
        System.out.println("backtracking to ["+cell[0]+","+cell[1]+"]");
        ArrayList<int[]> tempMoveStack = new ArrayList(knowledgeBase.moveStack);
        ArrayList tempTurnStack = new ArrayList(knowledgeBase.turnStack);
        System.out.println("moveStack.size:"+tempMoveStack.size()+"\ttempTurnStack.size:"+tempMoveStack.size());
        try {
            turn(LEFT);
            turn(LEFT);
            move();
            while (position[0] != cell[0] || position[1] != cell[1]) {
                int[] nextMove = tempMoveStack.get(tempMoveStack.size() - 1);
                if (position[0] + direction[0] == nextMove[0] && position[1] + direction[1] == nextMove[1]) {
                    move();
                    tempMoveStack.remove(tempMoveStack.size() - 1);
                } else {
                    int turn = (int)tempTurnStack.get(tempTurnStack.size() - 1);
                    if (turn == LEFT) {
                        turn(RIGHT);
                        tempTurnStack.remove(tempTurnStack.size() - 1);
                    } else if (turn == RIGHT) {
                        turn(LEFT);
                        tempTurnStack.remove(tempTurnStack.size() - 1);
                    }
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            return false;
        }
    }

    private int lookBack(int riskFactor) {
        for(int i = knowledgeBase.moveStack.size()-1; i >= 0; i--) {
            int[] m = knowledgeBase.moveStack.get(i);
            for(int[] d: knowledgeBase.DIRECTIONS) {
                int x = m[0]+d[0];
                int y = m[1]+d[1];
                // If the cell hasn't been traveled to yet
                if(knowledgeBase.askPath(x, y) == 0 && knowledgeBase.askObstacle(x, y) <= 0) {
                    // And it match
                    if(knowledgeBase.askWumpus(x, y) + knowledgeBase.askPit(x, y) <= riskFactor){
                        System.out.println("lookback succeeded");
                        return i;
                    }
                }
            }
        }
        System.out.println("lookback failed");
        return -1;
    }
    
    public void infer() throws GameOverException{
        // if there be gold, don't do shit. grab dat gold
        if(knowledgeBase.askGlimmer(position[0], position[1])) {
            pickup();
            return;
        }

        // start the inference process
        int riskFactor = -2;
        while(true) {
            System.out.println("infer()\n\tpos:["+position[0]+","+position[1]+"]\n\tdirection:{"+direction[0]+","+direction[1]+"}\n\triskFactor:"+riskFactor);

            // get dangerScore for each surrounding cell
            int forwardScore = Integer.MAX_VALUE;
            int leftScore = Integer.MAX_VALUE;
            int rightScore = Integer.MAX_VALUE;
            if (direction.equals(knowledgeBase.NORTH)) {
                forwardScore = knowledgeBase.askWumpus(position[0], position[1] + 1) + knowledgeBase.askPit(position[0], position[1] + 1) + knowledgeBase.askObstacle(position[0], position[1] + 1) + knowledgeBase.askPath(position[0], position[1] + 1);
                rightScore = knowledgeBase.askWumpus(position[0] + 1, position[1]) + knowledgeBase.askPit(position[0] + 1, position[1]) + knowledgeBase.askObstacle(position[0] + 1, position[1])+ knowledgeBase.askPath(position[0]+1, position[1]);
                leftScore = knowledgeBase.askWumpus(position[0] - 1, position[1]) + knowledgeBase.askPit(position[0] - 1, position[1]) + knowledgeBase.askObstacle(position[0] - 1, position[1])+ knowledgeBase.askPath(position[0]-1, position[1]);
            } else if (direction.equals(knowledgeBase.SOUTH)) {
                forwardScore = knowledgeBase.askWumpus(position[0], position[1] - 1) + knowledgeBase.askPit(position[0], position[1] - 1) + knowledgeBase.askObstacle(position[0], position[1] - 1)+ knowledgeBase.askPath(position[0], position[1]-1);
                rightScore = knowledgeBase.askWumpus(position[0] - 1, position[1]) + knowledgeBase.askPit(position[0] - 1, position[1]) + knowledgeBase.askObstacle(position[0] - 1, position[1])+ knowledgeBase.askPath(position[0]-1, position[1]);
                leftScore = knowledgeBase.askWumpus(position[0] + 1, position[1]) + knowledgeBase.askPit(position[0] + 1, position[1]) + knowledgeBase.askObstacle(position[0] + 1, position[1])+ knowledgeBase.askPath(position[0]+1, position[1]);
            } else if (direction.equals(knowledgeBase.EAST)) {
                forwardScore = knowledgeBase.askWumpus(position[0] + 1, position[1]) + knowledgeBase.askPit(position[0] + 1, position[1]) + knowledgeBase.askObstacle(position[0] + 1, position[1])+ knowledgeBase.askPath(position[0]+1, position[1]);
                rightScore = knowledgeBase.askWumpus(position[0], position[1] - 1) + knowledgeBase.askPit(position[0], position[1] - 1) + knowledgeBase.askObstacle(position[0], position[1] - 1)+ knowledgeBase.askPath(position[0], position[1]-1);
                leftScore = knowledgeBase.askWumpus(position[0], position[1] + 1) + knowledgeBase.askPit(position[0], position[1] + 1) + knowledgeBase.askObstacle(position[0], position[1] + 1)+ knowledgeBase.askPath(position[0], position[1]+1);
            } else if (direction.equals(knowledgeBase.WEST)) {
                forwardScore = knowledgeBase.askWumpus(position[0] - 1, position[1]) + knowledgeBase.askPit(position[0] - 1, position[1]) + knowledgeBase.askObstacle(position[0]-1, position[1])+ knowledgeBase.askPath(position[0]-1, position[1]);
                rightScore = knowledgeBase.askWumpus(position[0], position[1] + 1) + knowledgeBase.askPit(position[0], position[1] + 1) + knowledgeBase.askObstacle(position[0], position[1]+1)+ knowledgeBase.askPath(position[0], position[1]+1);
                leftScore = knowledgeBase.askWumpus(position[0], position[1] - 1) + knowledgeBase.askPit(position[0], position[1] - 1) + knowledgeBase.askObstacle(position[0], position[1]-1)+ knowledgeBase.askPath(position[0], position[1]-1);
            } else {
                System.out.println("direction did not match in infer()");
            }

            System.out.println("\tforwardDanger:"+forwardScore+"\n\trightDanger:"+rightScore+"\n\tleftDanger:"+leftScore);

            // if a move forward/right/left is less than the risk factor, make the move.
            if(forwardScore<=riskFactor && forwardScore<=leftScore && forwardScore<=rightScore) {
                move();
                knowledgeBase.print();
                return;
            } else if(leftScore<=riskFactor && leftScore<=rightScore) {
                turn(LEFT);
                move();
                knowledgeBase.print();
                return;
            } else if(rightScore <= riskFactor) {
                turn(RIGHT);
                move();
                knowledgeBase.print();
                return;
            } else {
                boolean backtracked = backtrack(knowledgeBase.moveStack, riskFactor);
                if(backtracked) {
                    System.out.println("\tbacktracked");
                    return;
                } else {
                    System.out.println("\tNo suitable backtrack found");
                }
            }
            riskFactor++;
        }

    }
}
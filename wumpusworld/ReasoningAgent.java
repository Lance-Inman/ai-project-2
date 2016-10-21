package wumpusworld;

public class ReasoningAgent extends Agent
{
    public ReasoningAgent()
    {
        super();
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
                forwardScore = knowledgeBase.askWumpus(position[0], position[1] + 1) + knowledgeBase.askPit(position[0], position[1] + 1) + knowledgeBase.askObstacle(position[0], position[1] + 1);
                rightScore = knowledgeBase.askWumpus(position[0] + 1, position[1]) + knowledgeBase.askPit(position[0] + 1, position[1]) + knowledgeBase.askObstacle(position[0] + 1, position[1]);
                leftScore = knowledgeBase.askWumpus(position[0] - 1, position[1]) + knowledgeBase.askPit(position[0] - 1, position[1]) + knowledgeBase.askObstacle(position[0] - 1, position[1]);
            } else if (direction.equals(knowledgeBase.SOUTH)) {
                forwardScore = knowledgeBase.askWumpus(position[0], position[1] - 1) + knowledgeBase.askPit(position[0], position[1] - 1) + knowledgeBase.askObstacle(position[0], position[1] - 1);
                rightScore = knowledgeBase.askWumpus(position[0] - 1, position[1]) + knowledgeBase.askPit(position[0] - 1, position[1]) + knowledgeBase.askObstacle(position[0] - 1, position[1]);
                leftScore = knowledgeBase.askWumpus(position[0] + 1, position[1]) + knowledgeBase.askPit(position[0] + 1, position[1]) + knowledgeBase.askObstacle(position[0] + 1, position[1]);
            } else if (direction.equals(knowledgeBase.EAST)) {
                forwardScore = knowledgeBase.askWumpus(position[0] + 1, position[1]) + knowledgeBase.askPit(position[0] + 1, position[1]) + knowledgeBase.askObstacle(position[0] + 1, position[1]);
                rightScore = knowledgeBase.askWumpus(position[0], position[1] - 1) + knowledgeBase.askPit(position[0], position[1] - 1) + knowledgeBase.askObstacle(position[0], position[1] - 1);
                leftScore = knowledgeBase.askWumpus(position[0], position[1] + 1) + knowledgeBase.askPit(position[0], position[1] + 1) + knowledgeBase.askObstacle(position[0], position[1] + 1);
            } else if (direction.equals(knowledgeBase.WEST)) {
                forwardScore = knowledgeBase.askWumpus(position[0] - 1, position[1]) + knowledgeBase.askPit(position[0] - 1, position[1]) + knowledgeBase.askObstacle(position[0]-1, position[1]);
                rightScore = knowledgeBase.askWumpus(position[0], position[1] + 1) + knowledgeBase.askPit(position[0], position[1] + 1) + knowledgeBase.askObstacle(position[0], position[1]+1);
                leftScore = knowledgeBase.askWumpus(position[0], position[1] - 1) + knowledgeBase.askPit(position[0], position[1] - 1) + knowledgeBase.askObstacle(position[0], position[1]-1);
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
            }

            // check if a move on the moveStack passed by a cell <= riskfactor
            // if it is, backtrack to that spot and make the move
            // PUT BACKTRACK HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            // increase the riskfactor and repeat
            riskFactor++;
        }

    }
}
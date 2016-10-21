package wumpusworld;

public class ReasoningAgent extends Agent{
    public void infer() {
        // if there be gold, don't do shit. grab dat gold
        if(glitter) {
            pickup();
            return;
        }

        // start the inference process
        int riskFactor = -2;
        while(true) {
            System.out.println("infer()\n\tpos:["+postion[0]+","+position[1]+"]\n\triskFactor:"+riskFactor);

            // get dangerScore for each surrounding cell
            int forwardScore = Integer.MAX_VALUE;
            int leftScore = Integer.MAX_VALUE;
            int rightScore = Integer.MAX_VALUE;
            switch(direction) {
                case knowledgeBase.NORTH:
                    forwardScore = knowledgeBase.askWumpus(position[0], position[1]+1) + knowledgeBase.askPit(position[0], position[1]+1);
                    rightScore = knowledgeBase.askWumpus(position[0]+1, position[1]) + knowledgeBase.askPit(position[0]+1, position[1]);
                    leftScore = knowledgeBase.askWumpus(position[0]-1, position[1]) + knowledgeBase.askPit(position[0]-1, position[1]);
                    break;
                case knowledgeBase.SOUTH:
                    forwardScore = knowledgeBase.askWumpus(position[0], position[1]-1) + knowledgeBase.askPit(position[0], position[1]-1);
                    rightScore = knowledgeBase.askWumpus(position[0]-1, position[1]) + knowledgeBase.askPit(position[0]-1, position[1]);
                    leftScore = knowledgeBase.askWumpus(position[0]+1, position[1]) + knowledgeBase.askPit(position[0]+1, position[1]);
                    break;
                case(knowledgeBase.EAST):
                    forwardScore = knowledgeBase.askWumpus(position[0]+1, position[1]) + knowledgeBase.askPit(position[0]+1, position[1]);
                    rightScore = knowledgeBase.askWumpus(position[0], position[1]-1) + knowledgeBase.askPit(position[0], position[1]-1);
                    leftScore = knowledgeBase.askWumpus(position[0], position[1]+1) + knowledgeBase.askPit(position[0], position[1]+1);
                    break;
                case(knowledgeBase.WEST):
                    forwardScore = knowledgeBase.askWumpus(position[0]-1, position[1]) + knowledgeBase.askPit(position[0]-1, position[1]);
                    rightScore = knowledgeBase.askWumpus(position[0], position[1]+1) + knowledgeBase.askPit(position[0], position[1]+1);
                    leftScore = knowledgeBase.askWumpus(position[0], position[1]-1) + knowledgeBase.askPit(position[0], position[1]-1);
                    break;
                default:
                    System.err.println("direction not recognized in infer()");
            }

            // if a move forward/right/left is less than the risk factor, make the move.
            if(forwardScore<=riskFactor && forwardScore<=leftScore && forwardScore<=rightScore) {
                move();
                return;
            } else if(leftScore<=riskFactor && leftScore<=rightScore) {
                turn(LEFT);
                move();
                return;
            } else if(rightScore <= riskFactor) {
                turn(RIGHT);
                move();
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

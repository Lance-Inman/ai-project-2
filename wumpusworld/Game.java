package wumpusworld;

import java.util.Arrays;

/*
 * @author Ethan Peterson
 * @since Oct 20, 2016
 */
public class Game 
{
    private int[][] world;
    private int score;
    private Agent agent;
    public int size;
    private boolean gameOver = false;
    
    public Game(int size, Agent agent, double wumpusProb, double pitProb, double obsProb)
    {
        world = WumpusWorldGenerator.generateWorld(size, wumpusProb, pitProb, obsProb);
        this.agent = agent;
        this.agent.position = WumpusWorldGenerator.startingPosition;
        this.size = size;
        this.score = 1000;
    }

    public int getScore() {
        return score;
    }
    
    public boolean moveAgent() throws GameOverException
    {
        int oldPos[] = {agent.position[0], agent.position[1]};
        //check if the attempted move will actually be a bump
        if(world[oldPos[0] + agent.direction[0]][oldPos[1] + agent.direction[1]] == 3)
            return false;
        else if(world[oldPos[0] + agent.direction[0]][oldPos[1] + agent.direction[1]] == 1 || world[oldPos[0] + agent.direction[0]][oldPos[1] + agent.direction[1]] == 2)
        {
            score -= 1000;
            agent.died = true;
            gameOver = true;
            throw new GameOverException(false);
        }
        
        agent.position[0] = agent.position[0] + agent.direction[0];
        agent.position[1] = agent.position[1] + agent.direction[1];
        
        //set percepts
        if(world[agent.position[0] - 1][agent.position[1]] == 1 || world[agent.position[0] + 1][agent.position[1]] == 1 || world[agent.position[0]][agent.position[1] + 1] == 1 || world[agent.position[0]][agent.position[1] - 1] == 1)
            agent.stench = true;
        else
            agent.stench = false;
        if(world[agent.position[0] - 1][agent.position[1]] == 2 || world[agent.position[0] + 1][agent.position[1]] == 2 || world[agent.position[0]][agent.position[1] + 1] == 2 || world[agent.position[0]][agent.position[1] - 1] == 2)
            agent.breeze = true;
        else
            agent.breeze = false;
        if(world[agent.position[0]][agent.position[1]] == 4)
            agent.glimmer = true;
        else
            agent.glimmer = false;
        
        score--;        
        return true;
    }
    
    public void turnAgent(int direction)
    {
        if(direction == agent.LEFT)
        {        
            System.out.println("Agent turned left.");
            if(Arrays.equals(agent.direction, agent.knowledgeBase.NORTH))
                agent.direction = agent.knowledgeBase.WEST;
            else if(Arrays.equals(agent.direction, agent.knowledgeBase.EAST))
                agent.direction = agent.knowledgeBase.NORTH;
            else if(Arrays.equals(agent.direction, agent.knowledgeBase.SOUTH))
                agent.direction = agent.knowledgeBase.EAST;
            else if(Arrays.equals(agent.direction, agent.knowledgeBase.WEST))
                agent.direction = agent.knowledgeBase.SOUTH;
            else
            {
                System.out.println("Error. Shouldnt be reached (Turn LEFT).");
                System.exit(1);
            }
        }
        else if(direction == agent.RIGHT)
        {
            System.out.println("Agent turned right.");
            if(Arrays.equals(agent.direction, agent.knowledgeBase.NORTH))
                agent.direction = agent.knowledgeBase.EAST;
            else if(Arrays.equals(agent.direction, agent.knowledgeBase.EAST))
                agent.direction = agent.knowledgeBase.SOUTH;
            else if(Arrays.equals(agent.direction, agent.knowledgeBase.SOUTH))
                agent.direction = agent.knowledgeBase.WEST;
            else if(Arrays.equals(agent.direction, agent.knowledgeBase.WEST))
                agent.direction = agent.knowledgeBase.NORTH;
            else
            {
                System.out.println("Error. Shouldnt be reached (Turn RIGHT).");
                System.exit(1);
            }
        }
        else
        {
            System.out.println("ERROR. Else should not be reached.");
            System.exit(1);
        }
        score--;
    }
    
    
    public void processShot()
    {
        if(Arrays.equals(agent.direction, agent.knowledgeBase.NORTH))
        {
            //fire north. (0, 0) is in the bottom left corner
            for(int i = agent.position[1]; i < world.length; i++)
            {
                if(world[agent.position[0]][i] == 1)
                {
                    world[agent.position[0]][i] = 0;
                    agent.scream = true;
                    return;
                }
                //arrow hits obstacle
                else if(world[agent.position[0]][i] == 1)
                {
                    agent.scream = false;
                    break;
                }
            }
        }
        else if(Arrays.equals(agent.direction, agent.knowledgeBase.EAST))
        {
            //fire East. (0, 0) is in the bottom left corner
            for(int i = agent.position[0]; i < world.length; i++)
            {
                if(world[i][agent.position[1]] == 1)
                {
                    world[i][agent.position[1]] = 0;
                    agent.scream = true;
                    return;
                }
                //arrow hits obstacle
                else if(world[i][agent.position[1]] == 1)
                {
                    agent.scream = false;
                    break;
                }
            }
        }
        else if(Arrays.equals(agent.direction, agent.knowledgeBase.SOUTH))
        {
            //fire south. (0, 0) is in the bottom left corner
            for(int i = agent.position[1]; i > -1; i--)
            {
                if(world[agent.position[0]][i] == 1)
                {
                    world[agent.position[0]][i] = 0;
                    agent.scream = true;
                    return;
                }
                //arrow hits obstacle
                else if(world[agent.position[0]][i] == 1)
                {
                    agent.scream = false;
                    break;
                }
            }
        }
        else if(Arrays.equals(agent.direction, agent.knowledgeBase.WEST))
        {
            //fire West. (0, 0) is in the bottom left corner
            for(int i = agent.position[0]; i > -1; i--)
            {
                if(world[i][agent.position[1]] == 1)
                {
                    world[i][agent.position[1]] = 0;
                    agent.scream = true;
                    return;
                }
                //arrow hits obstacle
                else if(world[i][agent.position[1]] == 1)
                {
                    agent.scream = false;
                    break;
                }
            }
        }
        else
        {
            System.out.println("Error. Shouldnt be reached (Shoot arrow).");
            System.exit(1);
        }
    }
    
    public void agentGrabsGold() throws GameOverException
    {
        if(world[agent.position[0]][agent.position[1]] == 4)
        {
            world[agent.position[0]][agent.position[1]] = 0;
            gameOver = true;
            score += 1000;
            throw new GameOverException(true);
        }
        else
        {
            System.out.println("There's no gold here!");
            System.exit(1);
        }
    }
}

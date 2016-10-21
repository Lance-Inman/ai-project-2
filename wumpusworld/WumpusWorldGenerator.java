package wumpusworld;

/*
 * @author Ethan Peterson
 * @since Oct 20, 2016
 */
public class WumpusWorldGenerator 
{
    public static int numWumpi = 0;
    public static int[] startingPosition = new int[2];
    
    public static int[][] generateWorld(int size, double wumpusProb, double pitProb, double obstacleProb)
    {
        int[][] wumpusWorld = new int[size + 2][size + 2]; //add room for walls in the world
        numWumpi = 0;
        
        //populate the world
        populateWorld(wumpusWorld, wumpusProb, pitProb, obstacleProb);
        
        //place the gold
        placeGold(wumpusWorld);
        
        setStartLocation(wumpusWorld);
        
        return wumpusWorld;
    }
    
    private static void setStartLocation(int[][] wumpusWorld)
    {
        startingPosition = getRandomEmptyLocation(wumpusWorld);        
    }
    
    private static void placeGold(int[][] wumpusWorld)
    {
        int[] goldPosition = getRandomEmptyLocation(wumpusWorld);
        wumpusWorld[goldPosition[0]][goldPosition[1]] = 4;
    }
    
    private static int[] getRandomEmptyLocation(int[][] wumpusWorld)
    {
        boolean found = false;
        while(!found)
        {
            int randX = (int) (wumpusWorld.length * Math.random()),
                randY = (int) (wumpusWorld.length * Math.random());
            
            if(wumpusWorld[randX][randY] == 0)
            {
                int[] empty = {randX, randY};
                return empty;
            }
        }
        System.out.println("Problem in generating random location in getRandomEmptyLocation(). This should not have been reached.");
        System.exit(1);
        return null;
    }
    
    private static void populateWorld(int[][] wumpusWorld, double wumpusProb, double pitProb, double obstacleProb)
    {
        //iterate thru x-axis
        for(int i = 0; i < wumpusWorld.length; i++)
        {
            //iterate thru y-axis
            for(int j = 0; j < wumpusWorld.length; j++)
            {
                if(i == 0 || j == 0 || i == wumpusWorld.length - 1 || j == wumpusWorld.length - 1)
                {
                    wumpusWorld[i][j] = 3; //obstacle borders, representing the walls of the World
                    continue; //move to the next loop, since an object has been placed
                }
                
                double rand = Math.random();
                
                if(rand <= wumpusProb)
                {
                    wumpusWorld[i][j] = 1; //make space (i, j) a wumpus
                    numWumpi++;
                    continue;
                }
                rand = Math.random();
                
                if(rand <= pitProb)
                {
                    wumpusWorld[i][j] = 2; //make space (i, j) a pit
                    continue;
                }
                rand = Math.random();
                
                if(rand <= obstacleProb)
                {
                    wumpusWorld[i][j] = 3; //make space (i, j) an obstacle
                    continue;
                }
                
                //If the space is not a wumpus, pit or obstacle, it is empty (and safe), since ints default to 0.
            }
        }
    }
}

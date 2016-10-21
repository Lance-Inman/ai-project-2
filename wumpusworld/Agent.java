package wumpusworld;

/*
 * @author Ethan Peterson
 * @since Oct 20, 2016
 */
public abstract class Agent 
{
    public final int LEFT = 0,
                     RIGHT = 1;
    //location and orientation details
    public int[] position,
                 direction;
    //percepts
    public boolean breeze,
                   bump,
                   died,
                   glimmer,
                   haveGold,
                   scream,
                   stench;
    private int quiver; //# of remaining arrows
    private Game game;
    public KnowledgeBase knowledgeBase;
    
    public Agent()
    {
        quiver = WumpusWorldGenerator.numWumpi;
    }

    public void start(Game game) throws GameOverException{
        this.game = game;
        this.knowledgeBase = new KnowledgeBase(game.size);
        this.direction = knowledgeBase.NORTH;
        knowledgeBase.registerMove(position[0], position[1]);
        while(true) {
            infer();
        }
    }
    
    public void move() throws GameOverException
    {
        if(!game.moveAgent())
        {            
            knowledgeBase.tellBump(position[0], position[1], direction);
            return;
        }
        int x = position[0],
            y = position[1];
        knowledgeBase.registerMove(x, y);
        
        processPercepts(x, y);
    }
    
    public void processPercepts(int x, int y)
    {
        //add percepts to knowledge base
        if(glimmer)
            knowledgeBase.tellGlimmer(x, y);
        if(!breeze && !stench)
        {
            knowledgeBase.tellClear(x, y);
            return;
        }
        if(breeze)
            knowledgeBase.tellBreeze(x, y);
        if(stench)
            knowledgeBase.tellStench(x, y);
    }
    
    public void turn(int direction)
    {
        game.turnAgent(direction);
    }
    
    public void shoot()
    {
        quiver--;
        System.out.println("Agent loosed an arrow.");
        game.processShot();
        
        processPercepts(position[0], position[1]);
    }
    
    public void pickup() throws GameOverException
    {
        game.agentGrabsGold();
        System.out.println("Agent picked up the gold.");
    }
    
    public abstract void infer() throws GameOverException;
}

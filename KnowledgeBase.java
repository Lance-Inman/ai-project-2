package wumpusworld;

public class KnowledgeBase {
   private final int CLEAR = -1;
   public final int[] NORTH = {0, 1};
   public final int[] SOUTH = {0, -1};
   public final int[] EAST = {-1, 0};
   public final int[] WEST = {1, 0};
   private final int[][] DIRECTIONS = {NORTH, SOUTH, EAST, WEST};
   private int[][] wumpusMap;
   private int[][] pitMap;
   private int[][] obstacleMap;
   private int[][] pathMap;
   private int steps;
   
   public static void main(String[] args) {
       KnowledgeBase kb = new KnowledgeBase(5);
       kb.tellClear(0, 0);
       kb.tellClear(0, 1);
       kb.tellStench(0, 2);
       kb.tellClear(0, 1);
       kb.tellClear(0, 0);
       kb.tellClear(1, 0);
       kb.tellClear(2, 0);
       kb.tellBreeze(3, 0);
       kb.tellClear(3, 1);
       kb.tellStench(3, 2);
       kb.print();
   }
   
   public KnowledgeBase(int size) {
       wumpusMap = new int[size][size];
       pitMap = new int[size][size];
       obstacleMap = new int[size][size];
       pathMap = new int[size][size];
       steps = 0;
       fillArray(wumpusMap, 0);
       fillArray(pitMap, 0);
       fillArray(obstacleMap, 0);
       fillArray(pathMap, 0);
   }
   
   /**
    * Increments the values around a location unless they are known to be CLEAR
    * @param x      the x location to update
    * @param y      the y location to update
    * @param map    the map to update
    */
   private void perceive(int x, int y, int[][] map) {
       // for each cell neighboring cell[x,y]
       for(int[] d: DIRECTIONS) {
           // ensure that the neighboring cell is on the map
           if(x + d[0] >=0 && x + d[0] < map.length && y + d[1] >=0 && y + d[1] < map.length) {
               map[x + d[0]][y + d[1]] = CLEAR;
           }
       }
   }
   
   /**
    * Marks the given location and its neighbors as clear
    * @param x  the x location that is clear
    * @param y  the y location that is clear
    */
   public void tellClear(int x, int y) {
       wumpusMap[x][y] = CLEAR;
       // for each cell neighboring cell[x,y]
       for(int[] d: DIRECTIONS) {
           // ensure that the neighboring cell is on the map
           if(x + d[0] >=0 && x + d[0] < pathMap.length && y + d[1] >=0 && y + d[1] < pathMap.length) {
               wumpusMap[x + d[0]][y + d[1]] = CLEAR;
               pitMap[x + d[0]][y + d[1]] = CLEAR;
           }
       }
       obstacleMap[x][y] = CLEAR;
       pathMap[x][y] = steps++;
   }
   
   public int askPath(int x, int y) {
       return pathMap[x][y];
   }
   
   /**
    * Report a stench at location (x,y)
    * @param x  the x location of the stench
    * @param y  the y location of the stench
    */
   public void tellStench(int x, int y) {
       pathMap[x][y] = steps++;
       perceive(x, y, wumpusMap);
   }
   
   public int askWumpus(int x, int y) {
       return wumpusMap[x][y];
   }
   
   public void tellBreeze(int x, int y) {
       pathMap[x][y] = steps++;
       perceive(x, y, pitMap);
   }
   
   public int askPit(int x, int y) {
       return pitMap[x][y];
   }
   
   public void tellBump(int x, int y) {
       obstacleMap[x][y]++;
       wumpusMap[x][y] = CLEAR;
       pitMap[x][y] = CLEAR;
   }
   
   /*
   public void tellScream(int x, int y, int[] direction) {
       // ASSUMPTION: Clears all wumpus' until it hits an obstacle or a wall
       if(direction == NORTH || SOUTH || EAST || WEST) {
           while(x < wumpusMap.length && y < wumpusMap.length) {
               if(obstacleMap[x][y] > 0) {
                   wumpusMap[x][y] = CLEAR;
                   return;
               }else {
                   wumpusMap[x][y] = CLEAR;
                   x = x + direction[0];
                   y = y + direction[1];
               }
           }
       } else {
           System.out.println("ERROR: invalid direction");
       }
   }
*/
   
   public void print() {
       int x = 0;
       int y = 0;
       
       // Print top bar and numbers
       System.out.print(" ");
       for(int i = 0; i < pathMap.length; i++) {
        System.out.print(" " + i);
       }
       System.out.print("\n ");
       for(int i = 0; i < pathMap.length; i++) {
        System.out.print("--");
       }
       System.out.println();
       
       // print the values for each cell
       for(y = 0; y < pathMap.length; y++) {
        if(x == 0) {
            System.out.print(y + "|");
        }
        for(x = 0; x < pathMap.length; x++) {
            if(pathMap[x][y] > 0) {
                System.out.print(pathMap[x][y] + " ");
            } else if(obstacleMap[x][y] > 0) {
                System.out.print("# ");
            } else if (wumpusMap[x][y] < 0 && pitMap[x][y] < 0) {
                System.out.print("  ");
            } else if (wumpusMap[x][y] == 0 && pitMap[x][y] == 0) {
                System.out.print("? ");
            } else if(wumpusMap[x][y] >= pitMap[x][y]) {
                System.out.print("w ");
            } else if(pitMap[x][y] > 0) {
                System.out.print("p ");
            } else {
                System.out.print("! ");
            }
        }
        x = 0;
        System.out.println();
       }
   }
   
   private void fillArray(int[][] array, int val) {
       for(int x = 0; x < array.length; x++) {
           for(int y = 0; y < array.length; y++) {
               array[x][y] = val;
           }
       }
   }
}

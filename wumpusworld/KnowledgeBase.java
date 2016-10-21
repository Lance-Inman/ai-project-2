package wumpusworld;

import java.util.ArrayList;

public class KnowledgeBase {
    public final int[] NORTH = {0, 1};
    public final int[] SOUTH = {0, -1};
    public final int[] EAST = {-1, 0};
    public final int[] WEST = {1, 0};
    private final int CLEAR = -1;
    private final int[][] DIRECTIONS = {NORTH, SOUTH, EAST, WEST};
    public ArrayList<int[]> moveStack;
    private int[][] wumpusMap;
    private int[][] pitMap;
    private int[][] obstacleMap;
    private int[][] pathMap;
    private int[] glimmer;
    private int steps;

    public KnowledgeBase(int size) {
        wumpusMap = new int[size][size];
        pitMap = new int[size][size];
        obstacleMap = new int[size][size];
        pathMap = new int[size][size];
        glimmer = new int[2];
        moveStack = new ArrayList<>();
        steps = 0;
        fillArray(wumpusMap, 0);
        fillArray(pitMap, 0);
        fillArray(obstacleMap, 0);
        fillArray(pathMap, 0);
    }

    public static void main(String[] args) {
        KnowledgeBase kb = new KnowledgeBase(5);
        kb.registerMove(0,0);
        kb.tellBump(0,0,kb.NORTH);
        kb.registerMove(1,0);
        kb.tellClear(1, 0);
        kb.registerMove(2,0);
        kb.tellStench(2, 0);
        kb.print();

        /*kb.tellClear(0, 0);
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
        */
    }

    /**
     * Increments the values around a location unless they are known to be CLEAR
     *
     * @param x   the x location to update
     * @param y   the y location to update
     * @param map the map to update
     */
    private void perceive(int x, int y, int[][] map) {
        // for each cell neighboring cell[x,y]
        for (int[] d : DIRECTIONS) {
            // ensure that the neighboring cell is on the map
            if (x + d[0] >= 0 && x + d[0] < map.length && y + d[1] >= 0 && y + d[1] < map.length) {
                map[x + d[0]][y + d[1]] = CLEAR;
            }
        }
    }

    public void registerMove(int x, int y) {
        int[] move = {x, y};
        moveStack.add(move);
        wumpusMap[x][y] = CLEAR;
        pitMap[x][y] = CLEAR;
        obstacleMap[x][y] = CLEAR;
        pathMap[x][y]++;
    }

    public void tellClear(int x, int y) {
        // for each cell neighboring cell[x,y]
        for (int[] d : DIRECTIONS) {
            // ensure that the neighboring cell is on the map
            if (x + d[0] >= 0 && x + d[0] < pathMap.length && y + d[1] >= 0 && y + d[1] < pathMap.length) {
                wumpusMap[x + d[0]][y + d[1]] = CLEAR;
                pitMap[x + d[0]][y + d[1]] = CLEAR;
            }
        }
    }

    public int askPath(int x, int y) {
        return pathMap[x][y];
    }

    public void tellStench(int x, int y) {
        if (pathMap[x][y] <= 1) {
            perceive(x, y, wumpusMap);
        }
    }

    public int askWumpus(int x, int y) {
        return wumpusMap[x][y];
    }

    public void tellBreeze(int x, int y) {
        if (pathMap[x][y] <= 1) {
            perceive(x, y, pitMap);
        }
    }

    public int askPit(int x, int y) {
        return pitMap[x][y];
    }

    public void tellBump(int x, int y, int[] direction) {
        obstacleMap[x + direction[0]][y + direction[1]]++;
    }

    public void tellGlimmer(int x, int y) {
        glimmer[0] = x;
        glimmer[1] = y;
    }

    public boolean askGlimmer(int x, int y) {
        if(glimmer[0] == x && glimmer[1] == y) {
            return true;
        } else {
            return false;
        }
    }

    public void tellScream(int x, int y, int[] direction) {
        // ASSUMPTION: Clears all wumpus' until it hits an obstacle or a wall
        while (x < wumpusMap.length && y < wumpusMap.length) {
            if (obstacleMap[x][y] > 0) {
                wumpusMap[x][y] = CLEAR;
                return;
            } else {
                wumpusMap[x][y] = CLEAR;
                x = x + direction[0];
                y = y + direction[1];
            }
        }
    }

    public void print() {
        int x = 0;
        int y = 0;

        // print the values for each cell
        for (y = pathMap.length-1; y >= 0; y--) {
            if (x == 0) {
                System.out.print(y + "|");
            }
            for (x = 0; x < pathMap.length; x++) {
                int lastX = moveStack.get(moveStack.size()-1)[0];
                int lastY = moveStack.get(moveStack.size()-1)[1];
                if (lastX == x && lastY == y) {
                    System.out.print("o ");
                } else if (pathMap[x][y] > 0) {
                    System.out.print("+ ");
                } else if (obstacleMap[x][y] > 0) {
                    System.out.print("# ");
                } else if (wumpusMap[x][y] < 0 && pitMap[x][y] < 0) {
                    System.out.print("  ");
                } else if (wumpusMap[x][y] == 0 && pitMap[x][y] == 0) {
                    System.out.print("? ");
                } else if (wumpusMap[x][y] >= pitMap[x][y]) {
                    System.out.print("w ");
                } else if (pitMap[x][y] > 0) {
                    System.out.print("p ");
                } else {
                    System.out.print("! ");
                }
            }
            x = 0;
            System.out.println();
        }
        System.out.print(" ");
        for (int i = 0; i < pathMap.length; i++) {
            System.out.print("--");
        }
        System.out.print("\n ");
        for (int i = 0; i < pathMap.length; i++) {
            System.out.print(" " + i);
        }
    }

    private void fillArray(int[][] array, int val) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array.length; y++) {
                array[x][y] = val;
            }
        }
    }
}

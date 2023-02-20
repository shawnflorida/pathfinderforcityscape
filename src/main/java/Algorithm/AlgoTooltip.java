package Algorithm;

import Strategy.Techniques.*;
import Strategy.Explorer.AStarStrategy;
import Strategy.Explorer.DijkstraStrategy;
import Strategy.Explorer.Algo;

public class AlgoTooltip
{
    public static Algo getPathAlgo(Algo.Algorithms algorithmStrategy, Tactics tactics)
    {
        return switch (algorithmStrategy) {
            case Dijkstra -> new DijkstraStrategy();
            case AStar -> new AStarStrategy(false, tactics);
            default -> throw new IllegalArgumentException("Pathfinding algorithm not found!");
        };
    }
    
    public static Tactics getTactics(AStarStrategy.Heuristic strategy)
    {
        return switch (strategy) {
            case Euclidean -> new Euclid();
            case Manhattan -> new Manhattan();
            default -> throw new IllegalArgumentException("Heuristic strategy not found!");
        };
    }

}

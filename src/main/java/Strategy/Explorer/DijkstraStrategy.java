package Strategy.Explorer;
import com.example.demo1.BoxGroupGrid;
import com.example.demo1.Boxes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DijkstraStrategy extends Algo
{
    public DijkstraStrategy()
    {
        super();
    }

    @Override
    public int runPathfinder(BoxGroupGrid boxGroupGrid, List<Boxes> path)
    {
        Boxes root = boxGroupGrid.getRoot();
        Boxes target = boxGroupGrid.getTarget();
        
        HashMap<Boxes, Boxes> parents = new HashMap<>();
        HashMap<Boxes, Integer> weights = new HashMap<>();
        path.clear();
        
        executeDijkstra(boxGroupGrid, parents, weights);
        
        int cost = weights.get(target);
        
        Boxes boxes = target;
        
        if(cost != Integer.MAX_VALUE)
        {
            do{
                path.add(0, boxes);
                boxes = parents.get(boxes);
            } while (boxes != root);
            
            Boxes.setPathFound(true);
            Boxes.isDone = true;
        }
        
        return cost;
    }

    private void executeDijkstra(BoxGroupGrid boxGroupGrid,
                                 HashMap<Boxes, Boxes> parents,
                                 HashMap<Boxes, Integer> w)
    {
        Boxes root = boxGroupGrid.getRoot();
        
        Set<Boxes> unvisited = new HashSet<>();
        
        boxGroupGrid.getTiles().stream().filter((tile) -> !(tile.isWall())).peek(unvisited::add).peek((tile) ->
                w.put(tile, Integer.MAX_VALUE)).forEachOrdered((tile) ->
        {
            parents.put(tile, null);
        });
        w.put(root, 0);
        
        while(!unvisited.isEmpty())
        {
            Boxes lowCostBoxes = getMinWeight(unvisited, w);
            
            if(w.get(lowCostBoxes) == Integer.MAX_VALUE)
                break;
            
            pathGenerator.drawTile(lowCostBoxes, boxGroupGrid.getTarget(), root, Boxes.Type.HIGHLIGHT, 1);
            this.statistics.incrementVisited();
            
            unvisited.remove(lowCostBoxes);
            
            List<Boxes> neighbors = boxGroupGrid.getNext(lowCostBoxes);
            
            for(Boxes boxes : neighbors)
            {
                if(unvisited.contains(boxes))
                {
                    int weight = boxes.getWeight() + w.get(lowCostBoxes);
                    if(w.get(boxes) > weight)
                    {
                        w.put(boxes, weight);
                        parents.put(boxes, lowCostBoxes);
                    }
                }
            }
            pathGenerator.drawTile(lowCostBoxes, boxGroupGrid.getTarget(), root, Boxes.Type.VISITED, 1);
        }
    }

    private Boxes getMinWeight(Set<Boxes> unvisited, HashMap<Boxes, Integer> weights)
    {
        double minWeight = Integer.MAX_VALUE;
        Boxes minWeightBoxes = null;
        
        for(Boxes boxes : unvisited)
        {
            if(weights.get(boxes) <= minWeight)
            {
                minWeightBoxes = boxes;
                minWeight = weights.get(boxes);
            }
        }
        
        return minWeightBoxes;
    }
}
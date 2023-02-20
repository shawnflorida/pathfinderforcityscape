
package Strategy.Explorer;
import com.example.demo1.BoxGroupGrid;
import com.example.demo1.PathGenerator;
import com.example.demo1.Boxes;
import com.example.demo1.resultGenerator;

import java.util.List;

public abstract class Algo
{
    protected PathGenerator pathGenerator;
    protected resultGenerator statistics;
    
    public enum Algorithms{
        Dijkstra,
        AStar
    }
    public Algo()
    {
        this.pathGenerator = PathGenerator.getInstance();
    }
    public final int algorithm(BoxGroupGrid model, List<Boxes> path)
    {
        long start = System.nanoTime();
        this.statistics = new resultGenerator(model);
        this.statistics.setWallSize(model.getImagePosition());
        
        int cost = this.runPathfinder(model, path);

        long end = System.nanoTime();
        Boxes.setElapsedTime(end - start);
        this.statistics.updateObservers();
        this.pathGenerator.drawPath(path, model);
        return cost;
    }

    protected abstract int runPathfinder(BoxGroupGrid model, List<Boxes> path);
}
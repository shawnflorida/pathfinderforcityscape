package Strategy.Explorer;
import com.example.demo1.BoxGroupGrid;
import com.example.demo1.Boxes;
import Strategy.Techniques.Tactics;
import java.util.*;

public class AStarStrategy extends Algo
{
    private final boolean findOptimalSolution;
    private final Tactics heuristic;
    public enum Heuristic
    {
        Euclidean,
        Manhattan
    }
    public AStarStrategy(boolean findOptimalSolution, Tactics heuristic)
    {
        super();
        this.findOptimalSolution = findOptimalSolution;
        this.heuristic = heuristic;
    }

    @Override
    public int runPathfinder(BoxGroupGrid model, List<Boxes> path)
    {
        List<currentBoxToolTip> currentBoxToolTips = new ArrayList<>();
        currentBoxToolTip rootCurrentBoxToolTip = null;
        currentBoxToolTip targetCurrentBoxToolTip = null;
        for(Boxes boxes : model.getTiles())
        {
            currentBoxToolTip currentBoxToolTip = new currentBoxToolTip(boxes);
            
            if(model.getRoot() == boxes)
                rootCurrentBoxToolTip = currentBoxToolTip;
            else if(model.getTarget() == boxes)
                targetCurrentBoxToolTip = currentBoxToolTip;

            currentBoxToolTips.add(currentBoxToolTip);
        }

        currentBoxToolTips.forEach((currentBoxToolTip) ->
                currentBoxToolTip.setNeighbors(currentBoxToolTips, model));

        assert rootCurrentBoxToolTip != null;
        assert targetCurrentBoxToolTip != null;
        executeAStar(rootCurrentBoxToolTip, targetCurrentBoxToolTip, this.heuristic);

        this.addPath(currentBoxToolTips, path, model.getTarget());

        int cost = this.calculateCost(path) + 1;

        if(!path.isEmpty())
        {
           Boxes.setPathFound(true);
           Boxes.isDone = true;
        }

        return cost;
    }

    private void executeAStar(currentBoxToolTip rootCurrentBoxToolTip, currentBoxToolTip targetCurrentBoxToolTip, Tactics heuristic)
    {
        currentBoxToolTip currentCurrentBoxToolTip = rootCurrentBoxToolTip;

        currentCurrentBoxToolTip.setLocalGoal(0.0);
        currentCurrentBoxToolTip.setGlobalGoal(heuristic.aStarTactics(rootCurrentBoxToolTip.getTile(), targetCurrentBoxToolTip.getTile()));

        Queue<currentBoxToolTip> notTestedCurrentBoxToolTips = new PriorityQueue<>();
        notTestedCurrentBoxToolTips.add(currentCurrentBoxToolTip);

        while (!notTestedCurrentBoxToolTips.isEmpty())
        {
            if(!this.findOptimalSolution && currentCurrentBoxToolTip == targetCurrentBoxToolTip) break;

            while(!notTestedCurrentBoxToolTips.isEmpty() && notTestedCurrentBoxToolTips.peek().isIsVisited())
                notTestedCurrentBoxToolTips.poll();

            if(notTestedCurrentBoxToolTips.isEmpty())
                break;

            currentCurrentBoxToolTip = notTestedCurrentBoxToolTips.poll();
            this.statistics.incrementVisited();
            pathGenerator.drawTile(currentCurrentBoxToolTip.getTile(), rootCurrentBoxToolTip.getTile(), targetCurrentBoxToolTip.getTile(), Boxes.Type.HIGHLIGHT, 2);
            currentCurrentBoxToolTip.setIsVisited(true);

            for(currentBoxToolTip currentBoxToolTipNeighbor : currentCurrentBoxToolTip.getNeighbors())
            {
                if(!currentBoxToolTipNeighbor.isIsVisited() && !currentBoxToolTipNeighbor.isWall())
                    notTestedCurrentBoxToolTips.add(currentBoxToolTipNeighbor);

                double goal = currentCurrentBoxToolTip.getLocalGoal() + currentBoxToolTipNeighbor.getTile().getWeight();

                if(goal < currentBoxToolTipNeighbor.getLocalGoal())
                {
                    getNodeNeighbor(currentBoxToolTipNeighbor).setParent(currentCurrentBoxToolTip);
                    currentBoxToolTipNeighbor.setLocalGoal(goal);

                    currentBoxToolTipNeighbor.setGlobalGoal(currentBoxToolTipNeighbor.getLocalGoal() + heuristic.aStarTactics(currentBoxToolTipNeighbor.getTile(), targetCurrentBoxToolTip.getTile()));
                }
            }
            pathGenerator.drawTile(currentCurrentBoxToolTip.getTile(), rootCurrentBoxToolTip.getTile(), targetCurrentBoxToolTip.getTile(), Boxes.Type.VISITED, 0);
        }
    }

    private static currentBoxToolTip getNodeNeighbor(currentBoxToolTip currentBoxToolTipNeighbor) {
        return currentBoxToolTipNeighbor;
    }

    private int calculateCost(List<Boxes> path)
    {
        int total = -1;
        total = path.stream().map(Boxes::getWeight).reduce(total, Integer::sum);
        return total;
    }

    private void addPath(List<currentBoxToolTip> currentBoxToolTips, List<Boxes> path, Boxes target)
    {
        currentBoxToolTip parentCurrentBoxToolTip = null;

        for(currentBoxToolTip currentBoxToolTip : currentBoxToolTips)
        {
            if(currentBoxToolTip.getTile() == target)
            {
                parentCurrentBoxToolTip = currentBoxToolTip;
                break;
            }
        }

        while(parentCurrentBoxToolTip != null)
        {
            path.add(parentCurrentBoxToolTip.getTile());
            parentCurrentBoxToolTip = parentCurrentBoxToolTip.getParent();
        }

        path.remove(path.size() - 1);

        Collections.reverse(path);

    }

    private static class currentBoxToolTip implements Comparable<currentBoxToolTip>
    {
        private final Boxes boxes;
        private final List<currentBoxToolTip> neighbors;
        private currentBoxToolTip parent;
        private boolean isVisited;
        private double globalGoal;
        private double localGoal;

        public currentBoxToolTip(Boxes boxes)
        {
            this.boxes = boxes;
            this.parent = null;
            this.neighbors = new ArrayList<>();
            this.isVisited = false;
            this.globalGoal = Double.MAX_VALUE;
            this.localGoal = Double.MAX_VALUE;
        }

        public double getGlobalGoal()
        {
            return globalGoal;
        }

        public void setGlobalGoal(double globalGoal)
        {
            this.globalGoal = globalGoal;
        }

        public double getLocalGoal()
        {
            return localGoal;
        }
        public void setLocalGoal(double localGoal)
        {
            this.localGoal = localGoal;
        }

        public boolean isIsVisited()
        {
            return isVisited;
        }

        public void setIsVisited(boolean isVisited)
        {
            this.isVisited = isVisited;
        }

        public List<currentBoxToolTip> getNeighbors()
        {
            return neighbors;
        }

        public void setNeighbors(List<currentBoxToolTip> currentBoxToolTips, BoxGroupGrid model)
        {
            List<Boxes> boxesNeighbors = model.getNext(this.getTile());

            for(currentBoxToolTip currentBoxToolTip : currentBoxToolTips)
            {
                if(boxesNeighbors.contains(currentBoxToolTip.getTile()))
                {
                    this.neighbors.add(currentBoxToolTip);
                }
            }
        }

        public Boxes getTile()
        {
            return boxes;
        }

        public currentBoxToolTip getParent()
        {
            return parent;
        }

        public void setParent(currentBoxToolTip parent)
        {
            this.parent = parent;
        }

        public boolean isWall()
        {
            return this.boxes.isWall();
        }

        @Override
        public int compareTo(currentBoxToolTip other)
        {
            return Double.compare(this.getGlobalGoal(), other.getGlobalGoal());
        }

        @Override
        public String toString()
        {
            return "Node{" + "parent=" + parent + ", isVisited=" + isVisited + ", globalGoal=" + globalGoal + '}';
        }
    }
}
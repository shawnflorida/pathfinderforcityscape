
package com.example.demo1;

import Strategy.Explorer.Algo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PathGenerator
{
    private static final PathGenerator INSTANCE = new PathGenerator();
    private final Executor executor;
    private PathGenerator()
    {
        executor = Executors.newSingleThreadExecutor();
    }
    public static PathGenerator getInstance()
    {
        return INSTANCE;
    }
    public void drawPath(List<Boxes> path, BoxGroupGrid model)
    {
        this.executor.execute(
        () ->
                path.stream().filter((tile) -> !(tile == model.getTarget() || tile == model.getRoot())).peek((tile) ->
                        tile.changeBoxAttributes(Boxes.Type.PATH, tile.getWeight())).forEachOrdered((_item) ->
                {
                    try
                    {
                        Thread.sleep(20);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(BoxGroupGrid.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }));
    }
    public void drawTile(Boxes boxes, Boxes target, Boxes root, Boxes.Type type, long sleep)
    {
        this.executor.execute(()->
        {
            if(boxes != target && boxes != root)
                boxes.changeBoxAttributes(type, boxes.getWeight());

            try
            {
                Thread.sleep(sleep);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(Algo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    public void removeP(BoxGroupGrid model)
    {
        this.executor.execute(()->
        {
            Boxes boxes;
            for(int y = 0; y < model.getYSize(); y++)
            {
                for(int x = 0; x < model.getXSize(); x++)
                {
                    boxes = model.getGrid()[x][y];
                    if(boxes.getType() == Boxes.Type.PATH || boxes.getType() == Boxes.Type.VISITED)
                    {
                        boxes.changeBoxAttributes(Boxes.Type.ROAD, boxes.getWeight());
                    }
                }
            }
        });
    }
}

package com.example.demo1;

import Strategy.Explorer.Algo;

import java.io.FileNotFoundException;
import java.util.*;

public class BoxGroupGrid extends Observable implements Observer{
    private int x_size;
    private int y_size;
    private Boxes[][] grid;
    Boxes root, target;
    private  Boxes.Type clickType;

    private final PathGenerator pathGenerator;

    public BoxGroupGrid()
    {
        this.root = null;
        this.target = null;
        this.clickType = Boxes.Type.START;
        pathGenerator = PathGenerator.getInstance();
    }
    public void gridInit(int x_tiles, int y_tiles, int tile_size) throws FileNotFoundException {
        this.x_size = x_tiles;
        this.y_size = y_tiles;
        this.grid = new Boxes[x_tiles][y_tiles];

        for(int y = 0; y < y_tiles; y++)
        {
            for(int x = 0; x < x_tiles; x++)
            {
                Boxes boxes = new Boxes(x, y, tile_size);
                boxes.addObserver(this);
                grid[x][y] = boxes;
            }
        }
    }
    public void toggleCoords(boolean toAdd)
    {
        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                grid[x][y].coordinates(toAdd);
            }
        }
    }

    public List<Boxes> getTiles()
    {
        List<Boxes> gridboxes = new ArrayList<>();

        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                gridboxes.add(grid[x][y]);
            }
        }

        return gridboxes;
    }

    public int getYSize()
    {
        return this.y_size;
    }
    public int getXSize()
    {
        return this.x_size;
    }


    public Boxes[][] getGrid()
    {
        return this.grid;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(o instanceof Boxes boxes)
        {
            if(boxes.isWall())
            {
                if(this.clickType == Boxes.Type.ROAD)
                    boxes.changeBoxAttributes(clickType, boxes.getDefaultWeight());
                return;
            }

            switch (this.clickType) {
                case START, END -> {
                    if (clickType == Boxes.Type.START) {
                        if (this.root != null) root.clearTile();
                        this.root = boxes;
                    } else {
                        if (this.target != null) target.clearTile();
                        this.target = boxes;
                    }
                    boxes.changeBoxAttributes(clickType, boxes.getDefaultWeight());
                }
                default -> boxes.changeBoxAttributes(clickType, boxes.getWeight());
            }
        }
        if(o instanceof resultGenerator stats)
        {
            setChanged();
            notifyObservers(stats);
        }
    }

    public Boxes getTarget()
    {
        return this.target;
    }
    public void changeClickType(Boxes.Type type)
    {
        this.clickType = type;
    }
    public Boxes getRoot()
    {
        return this.root;
    }

    public Boxes getN(Boxes boxes)
    {
        return (boxes.getY() - 1 >= 0) ? grid[boxes.getX()][boxes.getY() - 1] : null;
    }

    public Boxes getS(Boxes boxes)
    {
        return (boxes.getY() + 1 <= y_size - 1) ? grid[boxes.getX()][boxes.getY() + 1] : null;
    }
    public Boxes getW(Boxes boxes)
    {
        return (boxes.getX() - 1 >= 0) ? grid[boxes.getX() - 1][boxes.getY()] : null;
    }

    public Boxes getE(Boxes boxes)
    {
        return (boxes.getX() + 1  <= x_size - 1) ? grid[boxes.getX() + 1][boxes.getY()] : null;
    }

    public void executePathfinding(Algo algo) throws InterruptedException
    {
        if(root == null || target == null) return;
        this.pathGenerator.removeP(this);

        List<Boxes> path = new ArrayList<>();

        algo.algorithm(this, path);
    }
    public List<Boxes> getNext(Boxes boxes)
    {
        List<Boxes> next = new ArrayList<>();

        next.add(this.getN(boxes));
        next.add(this.getS(boxes));
        next.add(this.getE(boxes));
        next.add(this.getW(boxes));

        return next;
    }
    public int getImagePosition()
    {
        int totalWalls = 0;

        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                if(grid[x][y].isWall()) totalWalls++;
            }
        }

        return totalWalls;
    }
    public void addRandomWalls()
    {
        Random random = new Random();
        Boxes boxes;

        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                boxes = grid[x][y];
                if(boxes.getType() == Boxes.Type.ADD_IMAGE)
                    boxes.changeBoxAttributes(Boxes.Type.ROAD, boxes.getWeight());

                if((random.nextInt(2 + 1)) == 1)
                    boxes.changeBoxAttributes(Boxes.Type.ADD_IMAGE, boxes.getWeight());
            }
        }
    }


}




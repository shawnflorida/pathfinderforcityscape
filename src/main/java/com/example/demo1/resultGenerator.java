package com.example.demo1;

import java.util.Observable;
public class resultGenerator extends Observable
{
    private int tilesVisited;
    private int wallSize;
    
    public resultGenerator(BoxGroupGrid model)
    {
        this.addObserver(model);

        int tilesTotal = model.getTiles().size();
        this.tilesVisited = 0;
        boolean pathFound = false;
        int pathCost = -1;
        long elapsedTime = 0;
        this.wallSize = 0;
    }
    public void incrementVisited()
    {
        this.tilesVisited++;
    }
    public void setWallSize(int wallSize)
    {
        this.wallSize = wallSize;
    }

    public void updateObservers()
    {
        setChanged();
        notifyObservers();
    }

}

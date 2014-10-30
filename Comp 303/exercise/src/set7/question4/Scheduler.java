package set7.question4;

import java.util.ArrayList;
import java.util.List;

/**
 * Scheduler abstract class
 * Created by tim on 14-10-30.
 */
public abstract class Scheduler
{
    private List<Biker> aAvailableBikers = new ArrayList<>();
    private List<ScheduleObserver> aObservers = new ArrayList<>();

    public void add(Biker pBiker)
    {
        aAvailableBikers.add(pBiker);
    }

    public void addObserver(ScheduleObserver pObserver)
    {
        aObservers.add(pObserver);
    }

    public void schedule(Location pLocation) throws ScheduleException
    {
        if (!isBikerAvailable())
        {
            throw new ScheduleException("No biker available");
        }

        notifyAll();
    }

    private boolean isBikerAvailable()
    {
        return !aAvailableBikers.isEmpty();
    }

    private void notifyAll(Biker pBiker, Location pLocation)
    {
        for (ScheduleObserver observer : aObservers)
        {
            observer.update(pBiker, pLocation);
        }
    }

    protected abstract void run(Location pLocation) throws ScheduleException;
}

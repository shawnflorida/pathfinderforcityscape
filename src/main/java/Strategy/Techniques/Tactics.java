
package Strategy.Techniques;

import com.example.demo1.Boxes;

public abstract class Tactics
{
    public Tactics()
    {
    }
    public abstract double aStarTactics(Boxes root, Boxes target);
}

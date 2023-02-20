
package Strategy.Techniques;
import com.example.demo1.Boxes;
public class Manhattan extends Tactics
{
    public Manhattan()
    {
        super();
    }
    @Override
    public double aStarTactics(Boxes root, Boxes target)
    {
        double D = 1.0;
        double dx = Math.abs(root.getX() - target.getX());
        double dy = Math.abs(root.getY() - target.getY());
        return D * (dx + dy);
    }
}

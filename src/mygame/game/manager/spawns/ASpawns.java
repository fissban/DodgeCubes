package mygame.game.manager.spawns;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import mygame.game.manager.PlayerManager;
import mygame.game.manager.level.ALevel;
import mygame.game.manager.spawns.type.StarManager;

/**
 *
 * @author fissban
 */
public abstract class ASpawns
{
    protected static final int DISTANCE_TO_SPAWN = 100;
    protected static final float CUBIC_SIZE = 1.0f;

    protected final List<Geometry> spawnsGeometry = new CopyOnWriteArrayList();
    protected final List<Spatial> spawnsSpatial = new CopyOnWriteArrayList();

    public abstract void spawn(ALevel levelManager, Node worldNode, Node rootNode);

    public abstract void createModel();

    public void clearAllSpawns()
    {
        spawnsGeometry.forEach((spawn) -> spawn.removeFromParent());
        spawnsGeometry.clear();

        spawnsSpatial.forEach((spawn) -> spawn.removeFromParent());
        spawnsSpatial.clear();
    }

    public boolean hasSpawns()
    {
        return !spawnsGeometry.isEmpty();
    }

    public float cubicSize()
    {
        return CUBIC_SIZE;
    }

    /**
     * Chequea si algun spawn colisiona con el player.
     *
     * @param worldNode
     * @return
     */
    public boolean checkCollision(Node worldNode)
    {
        // Geometry ---------------------------------------------------------------------------------
        Iterator<Geometry> geoIt = spawnsGeometry.iterator();
        while (geoIt.hasNext())
        {
            Geometry cModel = geoIt.next();

            if (PlayerManager.getInstance().checkCollision(cModel))
            {
                if (this instanceof StarManager)
                {
                    StarManager sm = (StarManager) this;
                    sm.removeSpatial(cModel);
                    spawnsGeometry.remove(cModel);
                }

                return true;
            }

            // Remove cube if 10 world units behind player
            if (cModel.getLocalTranslation().getX() + 10 < worldNode.getLocalTranslation().getX())
            {
                cModel.removeFromParent();
                spawnsGeometry.remove(cModel);
            }
        }
        return false;
    }
}

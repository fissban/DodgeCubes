package mygame.game.manager.spawns.type;

import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import mygame.game.enums.ESpawn;
import mygame.game.manager.level.ALevel;
import mygame.game.manager.spawns.ASpawns;
import mygame.managers.GameAssetManager;

/**
 *
 * @author fissban
 */
public class CubicManager extends ASpawns
{
    // cube model
    private Geometry fCube;

    private CubicManager()
    {
        //
    }

    /**
     * Se crea el primer objeto con textura para luego ser clonado
     */
    @Override
    public void createModel()
    {
        Box box = new Box(CUBIC_SIZE * 2, CUBIC_SIZE, CUBIC_SIZE);
        Geometry geo = new Geometry("Box", box);
        Material mat = new Material(GameAssetManager.getInstance().get(), "Common/MatDefs/Misc/Unshaded.j3md");
        geo.setMaterial(mat);
        fCube = geo;
    }

    /**
     *
     * @param levelManager
     * @param worldNode
     * @param rootNode
     */
    @Override
    public void spawn(ALevel levelManager, Node worldNode, Node rootNode)
    {
        if (levelManager.hasMoreLines())
        {
            // Son 5 lugares a ocupar, desde el -4 al 4 saltando de 2 en 2.
            // Esto cambiaria de aumentar el tamaño de la calle.

            float x = worldNode.getLocalTranslation().getX() + DISTANCE_TO_SPAWN;
            float y = worldNode.getLocalTranslation().getY();
            float z = -4;

            for (ESpawn cubic : levelManager.getLine())
            {
                if (cubic == ESpawn.CUBIC)
                {
                    // se clona el modelo
                    Geometry clone = fCube.clone();
                    // se asigna una textura al azar.
                    int rnd = FastMath.nextRandomInt(1, 9);
                    Texture texture = GameAssetManager.getInstance().get().loadTexture(new TextureKey("Textures/cubes/" + rnd + ".png", false));
                    clone.getMaterial().setTexture("ColorMap", texture);
                    // se asigna la posicion
                    clone.getLocalTranslation().set(x, y, z);
                    // se agrega al node root
                    rootNode.attachChild(clone);
                    // se agrega a nuestro listado
                    spawnsGeometry.add(clone);
                }
                z += 2;
            }
        }
    }

    public static CubicManager getInstance()
    {
        return CubicManagerHolder.INSTANCE;
    }

    private static class CubicManagerHolder
    {
        private static final CubicManager INSTANCE = new CubicManager();
    }
}

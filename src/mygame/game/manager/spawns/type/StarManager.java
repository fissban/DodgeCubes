package mygame.game.manager.spawns.type;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.Iterator;
import mygame.game.enums.ESpawn;
import mygame.game.manager.level.ALevel;
import mygame.game.manager.spawns.ASpawns;
import mygame.managers.GameAssetManager;

/**
 * @author fissban
 */
public class StarManager extends ASpawns
{
    private static final String MODEL_HEART = "Models/star/Star.obj";

    // cube model
    private Spatial heartModel;
    private Geometry heartCube;

    private int count = 0;

    private StarManager()
    {
    }

    @Override
    public void createModel()
    {
        // create spatial model ---------------------------------------------------------------------------------
        heartModel = GameAssetManager.getInstance().get().loadModel(MODEL_HEART);
        float scale = 5.0f;
        heartModel.scale(scale, scale, scale);
        heartModel.rotate(0, 90, 0);
        //Material m = new Material(GameAssetManager.getInstance().get(), "Common/MatDefs/Misc/Unshaded.j3md");
        //m.setColor("Color", ColorRGBA.Yellow);
        //m.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        //heartModel.setMaterial(m);
        // create geometry model --------------------------------------------------------------------------------
        Box box = new Box(0.001f, CUBIC_SIZE, CUBIC_SIZE);
        heartCube = new Geometry("Box", box);
        Material material = new Material(GameAssetManager.getInstance().get(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", new ColorRGBA(0, 1, 0, 0.0f));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        heartCube.setMaterial(material);
    }

    /**
     *
     * @param levelManager
     * @param playerNode
     * @param rootNode
     */
    @Override
    public void spawn(ALevel levelManager, Node playerNode, Node rootNode)
    {
        if (levelManager.hasMoreLines())
        {
            float x = playerNode.getLocalTranslation().getX() + DISTANCE_TO_SPAWN;
            float y = playerNode.getLocalTranslation().getY();
            float z = -4;

            // Son 5 lugares a ocupar, desde el -4 al 4 saltando de 2 en 2.
            // Esto cambiaria de aumentar el tamaño de la calle.
            for (ESpawn cubic : levelManager.getLine())
            {
                if (cubic == ESpawn.HEART)
                {
                    count++;
                    // se clona el modelo
                    Geometry cloneCube = heartCube.clone();
                    cloneCube.setName("star" + count);
                    // se asigna la posicion
                    cloneCube.getLocalTranslation().set(x, y, z);
                    // se agrega al node root
                    rootNode.attachChild(cloneCube);
                    // se agrega a nuestro listado
                    spawnsGeometry.add(cloneCube);
                    // ---------------------------------------------
                    Spatial cloneSpatial = heartModel.clone();
                    cloneSpatial.setName("star" + count);
                    // se asigna la posicion
                    cloneSpatial.getLocalTranslation().set(x, y - 1, z);
                    // se agrega al node root
                    rootNode.attachChild(cloneSpatial);
                    // se agrega a nuestro listado
                    spawnsSpatial.add(cloneSpatial);
                }
                z += 2;
            }
        }
    }

    public void removeSpatial(Geometry cModel)
    {
        String name = cModel.getName();

        Iterator<Spatial> it = spawnsSpatial.iterator();
        while (it.hasNext())
        {
            Spatial s = it.next();
            if (s.getName().equals(name))
            {
                s.removeFromParent();
                spawnsSpatial.remove(s);
            }
        }
    }

    public void rotateStars(float tpf)
    {
        spawnsSpatial.forEach(star -> star.rotate(0, 2 * tpf, 0));
    }

    public static StarManager getInstance()
    {
        return HeartManagerHolder.INSTANCE;
    }

    private static class HeartManagerHolder
    {
        private static final StarManager INSTANCE = new StarManager();
    }
}

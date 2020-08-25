package mygame.game.manager;

import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import mygame.managers.GameAssetManager;

/**
 * Clase encargada de administrar el "player" y todo lo referido a el.
 *
 * @author fissban
 */
public class PlayerManager
{
    // Se usan cuadros invisibles para generar las colisiones
    private final static float ALPHA_INVISIVLE_OBJECT = 0.0f;
    // 
    private final static String MODEL = "Models/player/Spaceship.obj";

    private Spatial playerModel;
    private Geometry playerCube;

    public PlayerManager()
    {
        //
    }

    public void createPlayer(Node worldNode)
    {
        playerModel = GameAssetManager.getInstance().get().loadModel(MODEL);
        float scale = 0.1f;
        playerModel.scale(scale, scale, scale);
        playerModel.getLocalTranslation().set(-4.5f, 0, 0);
        playerModel.rotate(.0f, 3.15f, 0);

        worldNode.attachChild(playerModel);
    }

    /**
     * Se define un box invisible en la misma posicion del player para las
     * collisiones. El sistema es algo precario pero para este casio funciona.
     *
     * @param worldNode
     */
    public void createInvisiblePlayer(Node worldNode)
    {
        playerCube = createInvisibleCube();
        playerCube.getLocalTranslation().set(-4.5f, 0, 0);
        playerCube.scale(0.1f, 0.8f, 0.2f);
        worldNode.attachChild(playerCube);
    }

    /**
     * Cubo invisible paraque el objeto (player) pueda tener colision
     *
     * @return
     */
    private Geometry createInvisibleCube()
    {
        Box b = new Box(1, 1, 1);
        Geometry geo = new Geometry("Box", b);
        Material material = new Material(GameAssetManager.getInstance().get(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", new ColorRGBA(0, 1, 0, ALPHA_INVISIVLE_OBJECT));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geo.setMaterial(material);
        return geo;
    }

    /**
     * Se chequea si el player y "cube" colisionan entre si.
     *
     * @param cube
     * @return
     */
    public boolean checkCollision(Geometry cube)
    {
        BoundingVolume pVol = playerCube.getWorldBound();
        BoundingVolume cVol = cube.getWorldBound();

        // se chequea si el player collisiona con el cubo
        return pVol.intersects(cVol);
    }

    /**
     * Se chequea si el player sale del camino de ser asi, devuelve "true"
     *
     * @param worldNode
     * @return
     */
    public boolean checkPlayerOutFloor(Node worldNode)
    {
        float z = playerModel.getLocalTranslation().getZ() - worldNode.getLocalTranslation().getZ();

        if (z > 5 || z < -5)
        {
            return true;
        }
        return false;
    }

    public static PlayerManager getInstance()
    {
        return NewSingletonHolder.INSTANCE;
    }

    private static class NewSingletonHolder
    {
        private static final PlayerManager INSTANCE = new PlayerManager();
    }
}

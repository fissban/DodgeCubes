package mygame.game.manager;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import mygame.managers.GameAssetManager;

/**
 * Clase encargada de administrar el piso del juego.
 *
 * @author fissban
 */
public class FloorManager
{
    private static final String TEXTURE_FLOOR = "Textures/floor/floor.png";
    private static final String[] MODEL_TREES =
    {
        "Models/tree/Prop_Tree_Oak_1.obj",
        "Models/tree/Prop_Tree_Oak_2.obj",
        "Models/tree/Prop_Tree_Oak_3.obj",
    };
    // cantidad de pisos que se pondran
    private static final int FLOOR_COUNT = 14;

    private static final float FLOOR_SIZE = 5.0f;

    // piso modelo a usarse para ser clonado.
    private Geometry floorModel;

    private Geometry grassModel;
    private final List<Spatial> treeModels = new ArrayList<>();

    // lista con todos los pisos clonados
    private final List<Geometry> floorsField = new CopyOnWriteArrayList();
    private final List<Spatial> treesField = new CopyOnWriteArrayList();

    // nodo que contiene todos los pisos para ser administrados de igual forma
    private final Node floorNode = new Node();

    public FloorManager()
    {
        //createFloorModel();
    }

    public void createTreeModel()
    {
        for (String tree : MODEL_TREES)
        {
            Spatial model = GameAssetManager.getInstance().get().loadModel(tree);
            float scale = 1.0f;
            model.scale(scale, scale, scale);
            model.getLocalTranslation().set(-5, 0, 0);
            treeModels.add(model);
        }

    }

    public void createFloorModel()
    {
        Box box = new Box(FLOOR_SIZE, 0.01f, FLOOR_SIZE);
        Geometry geometry = new Geometry("Box", box);
        Material material = new Material(GameAssetManager.getInstance().get(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = GameAssetManager.getInstance().get().loadTexture(TEXTURE_FLOOR);
        material.setTexture("ColorMap", texture);
        geometry.setMaterial(material);

        floorModel = geometry;
    }

    public void createGlassModel()
    {
        Box box = new Box(FLOOR_SIZE, 0.01f, FLOOR_SIZE);
        Geometry geometry = new Geometry("Box", box);
        Material material = new Material(GameAssetManager.getInstance().get(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = GameAssetManager.getInstance().get().loadTexture("Textures/grass/ground_grass_gen_08.png");
        material.setTexture("ColorMap", texture);
        geometry.setMaterial(material);
        geometry.setLocalTranslation(0, -1.0f, 0);
        grassModel = geometry;
    }

    /**
     * Se crean "FLOOR_SIZE" pisos en la misma direccion
     *
     * @param rootNode
     */
    public void initFloors(Node rootNode)
    {
        for (int i = 0; i < FLOOR_COUNT; i++)
        {
            // FLOOR ------------------------------------------------------------------------------------
            // se clona el modelo
            cloneGeometry(floorModel, (10.0f * i), 0);

            // GRASS ------------------------------------------------------------------------------------
            cloneGeometry(grassModel, (10.0f * i), FLOOR_SIZE * 2);
            cloneGeometry(grassModel, (10.0f * i), -FLOOR_SIZE * 2);

            cloneGeometry(grassModel, (10.0f * i), FLOOR_SIZE * 4);
            cloneGeometry(grassModel, (10.0f * i), -FLOOR_SIZE * 4);

            cloneGeometry(grassModel, (10.0f * i), FLOOR_SIZE * 6);
            cloneGeometry(grassModel, (10.0f * i), -FLOOR_SIZE * 6);

            cloneGeometry(grassModel, (10.0f * i), FLOOR_SIZE * 8);
            cloneGeometry(grassModel, (10.0f * i), -FLOOR_SIZE * 8);

            // TREE --------------------------------------------------------------------------------------
            cloneSpatial(treeModels, (10.0f * i), FLOOR_SIZE + 0.5f);
            cloneSpatial(treeModels, (10.0f * i), -FLOOR_SIZE - 0.5f);

        }

        rootNode.attachChild(floorNode);

    }

    private void cloneSpatial(List<Spatial> list, float x, float z)
    {
        int rnd = FastMath.nextRandomInt(0, list.size() - 1);
        // clone
        Spatial sh = list.get(rnd).clone();
        // se define la ubicacion inicial
        sh.setLocalTranslation(x, -1, z);
        treesField.add(sh);
        floorNode.attachChild(sh);
    }

    private void cloneGeometry(Geometry glassOrFloor, float x, float z)
    {
        // clone geometry
        Geometry geo = glassOrFloor.clone();
        // se define la ubicacion inicial
        geo.setLocalTranslation(x, -1, z);
        floorsField.add(geo);
        floorNode.attachChild(geo);
    }

    private void checkGeometry(List<Geometry> list, Node worldNode)
    {
        Iterator<Geometry> ff = list.iterator();
        while (ff.hasNext())
        {
            Geometry fModel = ff.next();
            // Se chequea si  se encuentra 20 unidades por detras.
            if (fModel.getLocalTranslation().getX() + 20 < worldNode.getLocalTranslation().getX())
            {
                // Se remueve el piso que quedo atras
                fModel.removeFromParent();
                float z = fModel.getLocalTranslation().getZ();
                list.remove(fModel);

                // Se crea uno nuevo en el horizonte
                Geometry m = fModel.clone();
                float x = worldNode.getLocalTranslation().getX();
                m.setLocalTranslation(x + (10.0f * (FLOOR_COUNT - 2)), -1, z);
                list.add(m);
                floorNode.attachChild(m);
            }
        }
    }

    private void checkSpatial(List<Spatial> list, Node worldNode, Spatial model)
    {
        Iterator<Spatial> ff = list.iterator();
        while (ff.hasNext())
        {
            Spatial fModel = ff.next();
            // Se chequea si  se encuentra 20 unidades por detras.
            if (fModel.getLocalTranslation().getX() + 20 < worldNode.getLocalTranslation().getX())
            {
                // Se remueve el piso que quedo atras
                fModel.removeFromParent();
                float z = fModel.getLocalTranslation().getZ();
                list.remove(fModel);

                // Se crea uno nuevo en el horizonte
                Spatial m = model.clone();
                float x = worldNode.getLocalTranslation().getX();
                m.setLocalTranslation(x + (10.0f * (FLOOR_COUNT - 2)), -1, z);
                list.add(m);
                floorNode.attachChild(m);
            }
        }
    }

    public void checkPathFloor(Node worldNode)
    {
        checkGeometry(floorsField, worldNode);
        treeModels.forEach((s) -> checkSpatial(treesField, worldNode, s));

    }

    public static FloorManager getInstance()
    {
        return NewSingletonHolder.INSTANCE;
    }

    private static class NewSingletonHolder
    {
        private static final FloorManager INSTANCE = new FloorManager();
    }
}

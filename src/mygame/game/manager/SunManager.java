package mygame.game.manager;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author fissban
 */
public class SunManager
{
    private DirectionalLight sun;

    private SunManager()
    {
    }

    public void create(Node rootNode)
    {
        sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1.1f, -1.3f, 1.1f));
        sun.setColor(new ColorRGBA(1.0f, 0.94f, 0.8f, 1f).multLocal(1.3f));
        rootNode.addLight(sun);
        rootNode.addLight(new AmbientLight());

    }

    public DirectionalLight get()
    {
        return sun;
    }

    public static SunManager getInstance()
    {
        return SunManagerHolder.INSTANCE;
    }

    private static class SunManagerHolder
    {
        private static final SunManager INSTANCE = new SunManager();
    }
}

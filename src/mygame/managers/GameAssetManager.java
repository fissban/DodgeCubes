package mygame.managers;

import com.jme3.asset.AssetManager;

/**
 * Clase encargada de almacenar el "GameAssetManager" propio de
 * SimpleApplication que se ejecuta en el juego. Esto nos permite poder leer
 * imagenes/texturas dentro de otras clases y asi poder organizar mas el codigo.
 *
 * @author fissban
 */
public class GameAssetManager
{
    private static AssetManager assetManager;

    public GameAssetManager()
    {
        //
    }

    public void init(AssetManager asset)
    {
        assetManager = asset;
    }

    public AssetManager get()
    {
        return assetManager;
    }

    public static GameAssetManager getInstance()
    {
        return NewSingletonHolder.INSTANCE;
    }

    private static class NewSingletonHolder
    {
        private static final GameAssetManager INSTANCE = new GameAssetManager();
    }
}

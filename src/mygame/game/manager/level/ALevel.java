package mygame.game.manager.level;

import java.util.ArrayList;
import java.util.List;
import mygame.game.enums.ESpawn;

/**
 * Clase abstract encargada de darnos un formato y herramientas para desarrollar
 * cada nivel del juego.
 *
 * @author fissban
 */
public abstract class ALevel
{
    // ancho del camino donde apareceran los cubics
    public static final int SIZE_FLOOR = 5;
    // lista con los spawns.
    private final List<ESpawn[]> spawnList = new ArrayList<>();

    private int pos = 0;

    /**
     * Devuelve los spawns de la linea de spawns.
     *
     * @return
     */
    public ESpawn[] getLine()
    {
        return spawnList.get(pos);
    }

    /**
     * Usado para pasar a la proxima linea de spawns.
     */
    public void nextLine()
    {
        pos++;
    }

    /**
     * Devuelve si aun quedan mas lineas.
     *
     * @return
     */
    public boolean hasMoreLines()
    {
        return spawnList.size() > pos + 1;
    }

    /**
     * Inicializa los spawns.
     */
    public abstract void init();

    /**
     * Usado para generar la posicion de los spawns
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     */
    public void add(ESpawn a, ESpawn b, ESpawn c, ESpawn d, ESpawn e)
    {
        ESpawn[] add = new ESpawn[SIZE_FLOOR];
        int i = 0;
        add[i++] = a;
        add[i++] = b;
        add[i++] = c;
        add[i++] = d;
        add[i++] = e;
        spawnList.add(add);
    }

    /**
     * Usado para generar "size" cantidad de spawns similares uno detras del
     * otro.
     *
     * @param size
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     */
    public void forAdd(int size, ESpawn a, ESpawn b, ESpawn c, ESpawn d, ESpawn e)
    {
        for (int i = 0; i < size; i++)
        {
            add(a, b, c, d, e);
        }
    }

}

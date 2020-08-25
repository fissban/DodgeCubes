package mygame.util;

/**
 *
 * @author fissban
 */
public class Util
{
    /**
     * Se agregan los ceros necesarios. TODO: este metodo solo esta preparado
     * para valores hasta 100.
     *
     * @param level
     * @return
     */
    public static String addCeros(int level)
    {
        if (level == 100)
        {
            return level + "";
        }
        if (level >= 10)
        {
            return "0" + level;
        }

        return "00" + level;
    }
}

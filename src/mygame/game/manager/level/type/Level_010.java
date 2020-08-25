package mygame.game.manager.level.type;

import mygame.game.enums.ESpawn;
import mygame.game.manager.level.ALevel;

/**
 * @author fissban
 */
public class Level_010 extends ALevel
{
    public Level_010()
    {

    }

    @Override
    public void init()
    {
        forAdd(4, ESpawn.CUBIC, ESpawn.NONE, ESpawn.NONE, ESpawn.HEART, ESpawn.NONE);

        forAdd(8, ESpawn.CUBIC, ESpawn.NONE, ESpawn.HEART, ESpawn.NONE, ESpawn.CUBIC);

        forAdd(4, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE);// Empty

        forAdd(10, ESpawn.HEART, ESpawn.CUBIC, ESpawn.HEART, ESpawn.CUBIC, ESpawn.NONE);

        forAdd(4, ESpawn.NONE, ESpawn.NONE, ESpawn.HEART, ESpawn.NONE, ESpawn.NONE);

        forAdd(10, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE, ESpawn.HEART, ESpawn.NONE);

        forAdd(8, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE, ESpawn.HEART);

        forAdd(4, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE);// Empty

        forAdd(8, ESpawn.HEART, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE, ESpawn.NONE);

        forAdd(8, ESpawn.NONE, ESpawn.CUBIC, ESpawn.CUBIC, ESpawn.CUBIC, ESpawn.NONE);
    }
}

package mygame.game.enums;

/**
 *
 * @author fissban
 */
public enum EGameState
{
    /**
     * Estado inicial donde aun no tiene movimiento la camara nmi ninguna otra
     * accion.
     */
    INIT,
    /**
     * Sin desarrollar.
     */
    PAUSE,
    /**
     * El juego inicia.
     */
    START,
    /**
     *
     */
    WIN_LEVEL,
    /**
     * Tiempo de espera al ganar un nivel y de espera al proximo nivel.
     */
    WAITING_FOR_NEXT_LEVEL,
    /**
     * Proximo a ejecutarse luego de <b>WAITING_FOR_NEXT_LEVEL</b>, donde se
     * realizan las acciones para iniciar el proximo nivel.
     */
    NEXT_LEVEL,
    /**
     * EL jugador perdio al colisionar con un cubo.
     */
    LOSE,
    /**
     * Sin desarrollar.
     */
    END,
}

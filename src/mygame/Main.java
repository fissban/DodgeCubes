package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.game.enums.EGameState;
import mygame.game.enums.EState;
import mygame.game.manager.FloorManager;
import mygame.game.manager.PlayerManager;
import mygame.game.manager.SunManager;
import mygame.game.manager.UserInterfaceManager;
import mygame.game.manager.level.ALevel;
import mygame.game.manager.spawns.type.CubicManager;
import mygame.game.manager.spawns.type.StarManager;
import mygame.managers.GameAssetManager;
import mygame.managers.ThreadPoolManager;
import mygame.util.Util;

/**
 * Game bases on "CubeFields" for JMonkeyEngine examples.
 *
 * @fissban
 */
public class Main extends SimpleApplication implements AnalogListener
{
    private BitmapFont defaultFont;

    private EGameState gameState = EGameState.INIT;

    // node que se encarga de mantener en linea objetos como el personaje.
    private final Node playerNode = new Node();

    private final static float GAME_SPEED = .015f;
    private float camAngle = 0;

    private BitmapText pressStart;

    private final static float FPS_RATE = 1000f / 1f;

    // clase encargada de almacenar la informacion de cada nivel
    private ALevel levelManager;

    // cantidad de puntos obtenidos por el player
    // ultima posicion registrada para el spawns de cubics
    private float lastPos = 0;

    public Main()
    {
        super();
    }

    /**
     * Initializes game
     */
    @Override
    public void simpleInitApp()
    {
        Logger.getLogger("com.jme3").setLevel(Level.WARNING);

        ThreadPoolManager.getInstance();
        GameAssetManager.getInstance().init(assetManager);

        setDisplayStatView(false);
        setDisplayFps(false);

        flyCam.setEnabled(false);

        keyboards();

        defaultFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        pressStart = new BitmapText(defaultFont, false);

        loadText(pressStart, "PRESS ENTER", defaultFont, 0, 5, 0);

        // ***** SE CREA EL PERSONAJE ******************************************
        PlayerManager.getInstance().createPlayer(playerNode);
        PlayerManager.getInstance().createInvisiblePlayer(playerNode);
        //  ***** SE CREA EL SUELO *********************************************
        FloorManager.getInstance().createFloorModel();
        FloorManager.getInstance().createGlassModel();
        FloorManager.getInstance().createTreeModel();

        // ***** SE CREA LA LUZ ************************************************
        SunManager.getInstance().create(rootNode);
        //  ***** SE CREAN LOS SPAWNS ******************************************
        CubicManager.getInstance().createModel();
        StarManager.getInstance().createModel();
        // ***** SE INICIALIZA LA INTERFACE ************************************
        UserInterfaceManager.getInstance().init(settings, guiNode);
        // *********************************************************************
        rootNode.attachChild(playerNode);

        gameInit();
    }

    /**
     * Used to init game or reset
     */
    private void gameInit()
    {
        // se borran todos los spawns
        StarManager.getInstance().clearAllSpawns();
        CubicManager.getInstance().clearAllSpawns();
        // se crea el primer camino
        FloorManager.getInstance().initFloors(rootNode);

        // se inicializa los valores de la interface
        UserInterfaceManager.getInstance().initLevel();
        UserInterfaceManager.getInstance().initScore();
        UserInterfaceManager.getInstance().initStar();

        // se inicializa la posicion del player
        playerNode.setLocalTranslation(0, 0, 0);

        try
        {
            levelManager = (ALevel) Class.forName("mygame.game.manager.level.type.Level_00" + 1).getDeclaredConstructor().newInstance();
            levelManager.init();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        camTakeOver(tpf);

        StarManager.getInstance().rotateStars(tpf);

        /**
         * Acciones a tomar durante cada estado del juego.
         */
        switch (gameState)
        {
            case WIN_LEVEL:
            {
                moveCam(tpf);
                FloorManager.getInstance().checkPathFloor(playerNode);
                UserInterfaceManager.getInstance().showState(EState.WIN, guiNode);
                gameState = EGameState.WAITING_FOR_NEXT_LEVEL;

                ThreadPoolManager.schedule(() ->
                {
                    if (gameState != EGameState.LOSE)
                    {
                        gameState = EGameState.NEXT_LEVEL;
                    }
                }, 3000);

                break;
            }
            case WAITING_FOR_NEXT_LEVEL:
            {
                moveCam(tpf);
                FloorManager.getInstance().checkPathFloor(playerNode);
                break;
            }
            case NEXT_LEVEL:
            {
                moveCam(tpf);
                FloorManager.getInstance().checkPathFloor(playerNode);
                nextLevel();
                break;
            }
            case START:
            {
                moveCam(tpf);
                FloorManager.getInstance().checkPathFloor(playerNode);
                gameLogic();
                break;
            }
            case LOSE:
            {
                UserInterfaceManager.getInstance().showState(EState.LOSE, guiNode);
                gameLost();
                break;
            }

        }
    }

    /**
     * Forzamos la camara a estar siempre detras del personaje
     *
     * @param tpf Tickes Per Frame
     */
    private void camTakeOver(float tpf)
    {
        cam.setLocation(playerNode.getLocalTranslation().add(-8, 2, 0));
        cam.lookAt(playerNode.getLocalTranslation(), Vector3f.UNIT_Y);

        Quaternion rot = new Quaternion();
        rot.fromAngleNormalAxis(camAngle, Vector3f.UNIT_Z);
        cam.setRotation(cam.getRotation().mult(rot));
        camAngle *= FastMath.pow(.299f, FPS_RATE * tpf);
    }

    @Override
    public void requestClose(boolean esc)
    {
        //TODO No se pero no sirve xD
        ThreadPoolManager.shutdown();
        context.destroy(false);
        destroy();
        super.requestClose(esc);
    }

    @Override
    public void destroy()
    {
        ThreadPoolManager.shutdown();
        super.destroy();
    }

    /**
     * Acciones a tomar cuando el jugador pierde.
     * <li> Se informa por pantalla.</li>
     * <li> Se envia un mensaje por pantalla e informando de la proxima accion.
     * <li> Se reinician todos los datos del juego.
     */
    private void gameLost()
    {
        UserInterfaceManager.getInstance().showState(EState.LOSE, guiNode);

        loadText(pressStart, "You lost! Press enter to try again.", defaultFont, 0, 5, 0);
        gameInit();
    }

    private void nextLevel()
    {
        UserInterfaceManager.getInstance().incLevel();

        try
        {
            String level = Util.addCeros(UserInterfaceManager.getInstance().getLevel());
            levelManager = (ALevel) Class.forName("mygame.game.manager.level.type.Level_" + level).getDeclaredConstructor().newInstance();
            levelManager.init();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        gameState = EGameState.START;
    }

    private void moveCam(float tpf)
    {
        playerNode.move(GAME_SPEED * tpf * FPS_RATE, 0, 0);
    }

    /**
     * Logica del juego.
     * <li> se da spawn a los cubos.
     * <li> se verifica si el player se sale del camino.
     * <li> se verifica si los cubos sufren de colision con el player.
     * <li> se verifica si ya se superaron todos los cubos para pasar al proximo
     * nivel.
     * <li> se incrementan los puntos.
     */
    private void gameLogic()
    {
        // se chquea si el player se sale del camino.S
        if (PlayerManager.getInstance().checkPlayerOutFloor(playerNode))
        {
            gameState = EGameState.LOSE;
            return;
        }

        // si el listado de cubos esta vacio y ya no ahi mas por spawnear quiere decir
        // que supero todos los cubics y esta listo para el proximo nivel.
        if (!CubicManager.getInstance().hasSpawns() && !levelManager.hasMoreLines())
        {
            gameState = EGameState.WIN_LEVEL;
            return;
        }

        // se verifica la ultima posicion y se dan spawn a los cubics uno detras del otro.
        float newPos = playerNode.getLocalTranslation().getX();
        if (newPos - lastPos > CubicManager.getInstance().cubicSize() * 4)
        {
            lastPos = newPos;
            CubicManager.getInstance().spawn(levelManager, playerNode, rootNode);
            StarManager.getInstance().spawn(levelManager, playerNode, rootNode);
            levelManager.nextLine();
        }

        // se chequea si algun star colisiona con el player.
        if (StarManager.getInstance().checkCollision(playerNode))
        {
            UserInterfaceManager.getInstance().incStar();
        }

        // se chequea si algun cubic colisiona con el player.
        if (CubicManager.getInstance().checkCollision(playerNode))
        {
            gameState = EGameState.LOSE;
            return;
        }

        // se incrementan los puntos.
        UserInterfaceManager.getInstance().incScore();
    }

    /**
     * Sets up the keyboard bindings
     */
    private void keyboards()
    {
        inputManager.addMapping("START", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(this, "START", "Left", "Right");
    }

    @Override
    public void onAnalog(String binding, float value, float tpf)
    {
        if (binding.equals("START") && gameState == EGameState.INIT)
        {
            gameState = EGameState.START;
            guiNode.detachChild(pressStart);
            System.out.println("START");
        }
        else if (gameState == EGameState.START && binding.equals("Left"))
        {
            playerNode.move(0, 0, -(GAME_SPEED / 2f) * value * FPS_RATE);
            camAngle -= value * tpf;
        }
        else if (gameState == EGameState.START && binding.equals("Right"))
        {
            playerNode.move(0, 0, (GAME_SPEED / 2f) * value * FPS_RATE);
            camAngle += value * tpf;
        }
    }

    /**
     * Sets up a BitmapText to be displayed
     *
     * @param txt  the Bitmap Text
     * @param text the
     * @param font the font of the text
     * @param x
     * @param y
     * @param z
     */
    private void loadText(BitmapText txt, String text, BitmapFont font, float x, float y, float z)
    {
        txt.setSize(font.getCharSet().getRenderedSize());
        txt.setLocalTranslation(txt.getLineWidth() * x, txt.getLineHeight() * y, z);
        txt.setText(text);
        guiNode.attachChild(txt);
    }

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }
}

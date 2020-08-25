/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.game.manager;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import mygame.game.enums.EState;
import mygame.managers.GameAssetManager;
import mygame.managers.ThreadPoolManager;

/**
 * Sencilla interface para el juego.
 *
 * @author fissban
 */
public class UserInterfaceManager
{
    // tiempo que mostrara el cartel de "Ganastes" o "Perdistes".
    private static final int TIME = 2000;

    // Imagen a mostrar en el estado del juego con "Ganastes" o "Perdistes"..
    private Picture winPicture;
    private Picture losePicture;

    // Font default que usaremos para toda la interface
    private final BitmapFont defaultFont;

    // score
    private int score = 0;
    private BitmapText scoreBitMap;

    // star
    private int star = 0;
    private BitmapText starBitMap;

    // level
    private int level = 1;
    private BitmapText levelBitMap;

    private UserInterfaceManager()
    {
        defaultFont = GameAssetManager.getInstance().get().loadFont("Interface/Fonts/SegoeUIBlack.fnt");

    }

    /**
     * Se inicializa las imagenes y textos de la interface del juego.
     *
     * @param settings
     * @param guiNode
     */
    public void init(AppSettings settings, Node guiNode)
    {
        Picture upPic = new Picture("up");
        upPic.setImage(GameAssetManager.getInstance().get(), "Textures/interface/up.png", true);
        upPic.setWidth(settings.getWidth() / 2);
        upPic.setHeight(settings.getHeight() / 8);
        upPic.setPosition(settings.getWidth() / 4, settings.getHeight() - 80);
        guiNode.attachChild(upPic);

        Picture downPic = new Picture("down");
        downPic.setImage(GameAssetManager.getInstance().get(), "Textures/interface/down.png", true);
        downPic.setWidth(settings.getWidth() / 2);
        downPic.setHeight(settings.getHeight() / 8);
        downPic.setPosition(settings.getWidth() / 4, 0);
        guiNode.attachChild(downPic);

        Picture starPic = new Picture("star");
        starPic.setImage(GameAssetManager.getInstance().get(), "Textures/interface/star.png", true);
        starPic.setWidth(100);
        starPic.setHeight(100);
        starPic.setPosition(0, settings.getHeight() - 100);
        guiNode.attachChild(starPic);

        scoreBitMap = new BitmapText(defaultFont, false);
        scoreBitMap.setSize(30);
        scoreBitMap.setColor(ColorRGBA.Green);
        scoreBitMap.setText("Puntos: " + score);
        scoreBitMap.setLocalTranslation(settings.getWidth() / 2 - 70, 50, 0);
        guiNode.attachChild(scoreBitMap);

        starBitMap = new BitmapText(defaultFont, false);
        starBitMap.setSize(30);
        starBitMap.setColor(ColorRGBA.White);
        starBitMap.setText(star + "");
        starBitMap.setLocalTranslation(40, settings.getHeight() - 30, 0);
        guiNode.attachChild(starBitMap);

        levelBitMap = new BitmapText(defaultFont, false);
        levelBitMap.setSize(40);
        levelBitMap.setColor(ColorRGBA.Green);
        levelBitMap.setText("Nivel: " + level);
        levelBitMap.setLocalTranslation(settings.getWidth() / 2 - 70, settings.getHeight(), 0);
        guiNode.attachChild(levelBitMap);

        // ----------------------------------------------------
        winPicture = new Picture("HUD Picture");
        winPicture.setImage(GameAssetManager.getInstance().get(), "Textures/interface/state/win.png", true);
        winPicture.setWidth(settings.getWidth() / 2);
        winPicture.setHeight(settings.getHeight() / 8);
        winPicture.setPosition(settings.getWidth() / 4, settings.getHeight() - 250);
        //
        losePicture = new Picture("HUD Picture");
        losePicture.setImage(GameAssetManager.getInstance().get(), "Textures/interface/state/lose.png", true);
        losePicture.setWidth(settings.getWidth() / 2);
        losePicture.setHeight(settings.getHeight() / 8);
        losePicture.setPosition(settings.getWidth() / 4, settings.getHeight() - 250);
    }

    /**
     * Inicializa en 0 los puntos del juego.
     */
    public void initScore()
    {
        score = 0;
        scoreBitMap.setText("Puntos: 0");
    }

    /**
     * Incrementa en 1 la cantidad de puntos del juego.
     */
    public void incScore()
    {
        score++;
        scoreBitMap.setText("Puntos: " + score);
    }

    public void initStar()
    {
        star = 0;
        starBitMap.setText("0");
    }

    public void incStar()
    {
        star++;
        starBitMap.setText(star + "");
    }

    /**
     * Muestra el cartel informando que el player ah ganado o perdido.
     *
     * @param state
     * @param guiNode
     */
    public void showState(EState state, Node guiNode)
    {
        switch (state)
        {
            case WIN:
                guiNode.attachChild(winPicture);
                ThreadPoolManager.schedule(() -> winPicture.removeFromParent(), TIME);
                break;
            case LOSE:
                guiNode.attachChild(losePicture);
                ThreadPoolManager.schedule(() -> losePicture.removeFromParent(), TIME);
                break;
        }

    }

    /**
     * Se obtiene el nivel actual del juego.
     *
     * @return
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Inicializa en 0 el nivel del juego.
     */
    public void initLevel()
    {
        level = 1;
        levelBitMap.setText("Nivel: " + level);
    }

    /**
     * Incrementa el nivel actual del juego.
     */
    public void incLevel()
    {
        level++;
        levelBitMap.setText("Nivel: " + level);
    }

    public static UserInterfaceManager getInstance()
    {
        return UserInterfaceHolder.INSTANCE;
    }

    private static class UserInterfaceHolder
    {
        private static final UserInterfaceManager INSTANCE = new UserInterfaceManager();
    }
}

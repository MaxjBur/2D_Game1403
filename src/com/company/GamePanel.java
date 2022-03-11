package com.company;

import com.company.entity.Entity;
import com.company.entity.Player;
import com.company.tile.TileManager;
import object.SuperObject;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile - Default size of Characters/NPCs
    final int scale = 3;

    public final int tileSize = originalTileSize*scale; // 48x48 tile - Actual tile size that is displayed on game screen
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize*maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize*maxScreenRow; // 576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;  //can delete
    public final int worldHeight = tileSize * maxWorldRow; //can delete


    // FPS
    int FPS = 60;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this,keyH);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc[]= new Entity[10];

    // GAME STATE
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;



    // SET PLAYER DEFAULT POSITION
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();
        playMusic(0);
        gameState = playState;
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; // 1 Billion Nanoseconds = 1 second
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            long currentTime = System.nanoTime();

            // 1 UPDATE: Update information such as character positions
            update();

            // 2 DRAW: Draw the screen with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();

                remainingTime /= 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (gameState == playState) {

            player.update();
        }
        if (gameState == pauseState){


        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

    // TILE
        tileM.draw(g2);
    //OBJECT
        for (int i = 0; i < obj.length; i++) {
            if(obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }
        //NPC
        for (int i = 0; i < npc.length; i++) {
            if (npc[i]!=null){
                npc[i].draw(g2);
            }

        }

    // PLAYER
        player.draw(g2);

        //UI
        ui.draw(g2);

        g2.dispose();
    }

    public void playMusic (int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSE (int i){
        se.setFile(i);
        se.play();
    }


}

package com.company.entity;


import com.company.GamePanel;
import com.company.KeyHandler;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Player extends Entity {


    KeyHandler KeyH;

    int oldRoom;
    public final int screenX;
    public final int screenY;
    public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler KeyH) {
        super(gp);


        this.KeyH= KeyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        //PLAYER HITBOX
        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/walk 1 up.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/walk 2 up.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/walk 1 down.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/walk 2 down.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/walk 1 left.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/walk 2 left.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/walk 1 right.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/walk 2 right.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void update() {

        if (KeyH.upPressed == true || KeyH.downPressed == true || KeyH.leftPressed == true || KeyH.rightPressed == true) {
            if (KeyH.upPressed == true) {
                direction = "up";
            }
            else if (KeyH.downPressed == true) {
                direction = "down";
            }
            else if (KeyH.leftPressed == true) {
                direction = "left";
            }
            else if (KeyH.rightPressed == true) {
                direction = "right";
            }

            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this,true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false) {
                switch(direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                }
                else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject (int i){

        if (i != 999){ //id not 999 then have touched an object
            String objectName = gp.obj[i].name;

            switch (objectName){
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i]= null;
                    gp.ui.showMessage("You got a key!");
                    break;
                case "Door":
                    if (hasKey >0){
                        gp.playSE(3);
                        Random random = new Random();
                        int nextRoom = random.nextInt(2); // CHANGE 2 -> (APPROPRIATE NUMBER), WHEN ADDING NEW DUNGEON ROOMS

                        while (nextRoom == oldRoom) { // MAKES IT SO THAT YOU CANNOT GENERATE THE SAME ROOM TWICE IN A ROW
                            nextRoom = random.nextInt(2);
                        }

                        if (nextRoom == 0) {
                            for (int j = 0; j < 8; j++) {
                                gp.obj[j]= null; // REMOVES ALL OLD ROOM OBJECTS
                            }

                            gp.obj[0]= new OBJ_Key(); // CREATES NEW KEY
                            gp.obj[0].worldX = 23* gp.tileSize;
                            gp.obj[0].worldY = 19*gp.tileSize;

                            gp.obj[4]  = new OBJ_Door(); // CREATES NEW ROOM DOOR
                            gp.obj[4].worldX = 26 * gp.tileSize;
                            gp.obj[4].worldY = 21*gp.tileSize;

                            gp.obj[7]  = new OBJ_Boots(); // CREATES NEW ROOM BOOTS
                            gp.obj[7].worldX = 23 * gp.tileSize;
                            gp.obj[7].worldY= 23 *gp.tileSize;

                            gp.tileM.loadMap("/maps/START1.txt"); // LOADS NEW ROOM
                            oldRoom = 0;
                        }
                        else if (nextRoom == 1) {
                            for (int j = 0; j < 8; j++) {
                                gp.obj[j]= null; // REMOVES ALL OLD ROOM OBJECTS
                            }

                            gp.obj[0]= new OBJ_Key();
                            gp.obj[0].worldX = 23* gp.tileSize;
                            gp.obj[0].worldY = 23*gp.tileSize;

                            gp.obj[4]  = new OBJ_Door();
                            gp.obj[4].worldX = 20 * gp.tileSize;
                            gp.obj[4].worldY = 21*gp.tileSize;

                            gp.obj[6]  = new OBJ_Chest();
                            gp.obj[6].worldX = 23 * gp.tileSize;
                            gp.obj[6].worldY = 19 *gp.tileSize;

                            gp.tileM.loadMap("/maps/START2.txt");
                            oldRoom = 1;
                        }
                        hasKey--;
                        gp.ui.showMessage("You opened the door!");
                    }
                    else{
                        gp.ui.showMessage("You need a key");
                    }
                    System.out.println("Key: "+ hasKey);
                    break;
                case "Boots":
                    gp.playSE(2);
                    speed += 2; //change this number for different speed boost
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed up!");
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;

            }
        }
    }
    public void interactNPC(int i){
        if(i != 999){
            gp.gameState = gp.dialogueState;
            gp.npc[i].speak();

        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch(direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
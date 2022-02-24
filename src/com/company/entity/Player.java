package com.company.entity;


import com.company.GamePanel;
import com.company.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler KeyH;

    public final int screenX;
    public final int screenY;
    int hasKey = 0;

    public Player(GamePanel gp, KeyHandler KeyH) {

        this.gp = gp;
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
                    hasKey++;
                    gp.obj[i]= null;
                    System.out.println("Key: "+ hasKey);
                    break;
                case "Door":
                    if (hasKey >0){
                        gp.obj[i] = null;
                        hasKey--;
                    }
                    System.out.println("Key: "+ hasKey);
                    break;
            }
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
package com.company.entity;

import com.company.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 1;
        getImage();
        setDialogue();

    }
    public void getImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/npc/npc.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/npc/npc2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/npc/npc3.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/npc/npc4.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/npc/npc5.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/npc/npc6.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/npc/npc7.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/npc/npc8.png"));
        }

        catch(IOException e){
            e.printStackTrace();
        }


    }
    public void setDialogue(){

        dialogues[0]="Hello, lad.";
        dialogues[1]="So you've come to this dungeon to \nfind the treasure?";
        dialogues[2]="I used to be a great wizard but now... \nI think I've got lost.";
        dialogues[3]="Well, good luck on you.";
    }
    public void  speak(){

        super.speak();


    }
}

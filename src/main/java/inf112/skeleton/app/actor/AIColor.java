package inf112.skeleton.app.actor;

import com.badlogic.gdx.graphics.Color;

/**
 * Class which holds the different AI Colors. computer 1 has Colors[0] and so forth.
 * To a maximum of 7 AI's
 */

public class AIColor {

    public final Color[] Colors = {Color.BLUE, Color.GREEN, Color.BROWN,
            Color.CORAL, Color.MAGENTA, Color.GOLD, Color.PINK};

    public final String[] ModelColors = {
            "assets/pictures/AiModels/BlueInvader.png", "assets/pictures/AiModels/GreenInvader.png",
            "assets/pictures/AiModels/BrownInvader.png", "assets/pictures/AiModels/CoralInvader.png",
            "assets/pictures/AiModels/MagentaInvader.png", "assets/pictures/AiModels/GoldInvader.png",
            "assets/pictures/AiModels/PinkInvader.png"};

}

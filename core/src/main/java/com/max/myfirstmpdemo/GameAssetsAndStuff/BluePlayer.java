package com.max.myfirstmpdemo.GameAssetsAndStuff;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class BluePlayer {
    public BluePlayer() {
    }

    public Sprite keyframe;

    public Animation<Sprite> redRunningAnimation;
    public Animation<Sprite> redKickingAnimation;
    public Animation<Sprite> redIdleAnimation;

    public Vector2 position;
}

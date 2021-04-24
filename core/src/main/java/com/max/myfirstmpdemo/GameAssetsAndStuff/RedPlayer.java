package com.max.myfirstmpdemo.GameAssetsAndStuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.max.myfirstmpdemo.LoadingPathsAndScreen.AnimationAtlasPaths;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class RedPlayer {
    MyFirstMpDemoMain game;
    Animation<TextureRegion> redIdleAnimation;
    Animation<TextureRegion> redRunningAnimation;
    Animation<TextureRegion> redKickingAnimation;
    TextureAtlas textureAtlas;

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public Animation<TextureRegion> animation;
    public RedPlayer(MyFirstMpDemoMain game) {
        this.game = game;
        textureAtlas = game.getAssetManager().get(AnimationAtlasPaths.PLAYERS_ATLAS, TextureAtlas.class);
        redIdleAnimation = new Animation<TextureRegion>(1/15f, textureAtlas.findRegions("RedIdle"));
        redRunningAnimation = new Animation<TextureRegion>(1/15f, textureAtlas.findRegions("RedRun"));
        redKickingAnimation = new Animation<TextureRegion>(1/15f, textureAtlas.findRegions("RedKick"));
    }

    public void setKeyframe(Sprite keyframe) {
        this.keyframe = keyframe;
    }

    public Sprite keyframe = new Sprite(redIdleAnimation.getKeyFrames()[0]);

    public Vector2 position;

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    float statetime = 0f;
    public void resetStatetime() {
        statetime = 0f;
    }
    public void update(float delta){
        keyframe = (Sprite)animation.getKeyFrame(statetime);
        this.statetime += delta;
        //keyframe.setPosition();
        keyframe.draw(game.getBatch());
    }

}

package com.max.myfirstmpdemo.GameAssetsAndStuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class RedPlayer {
    MyFirstMpDemoMain game;
    public Sprite keyframe;
    public static Animation<TextureRegion> redIdleAnimation;
    public static Animation<TextureRegion> redRunningAnimation;
    public static Animation<TextureRegion> redKickingAnimation;
    public Animation<TextureRegion> animation;
    public Animation<TextureRegion> previousAnimation;
 //   public Texture keyframeinit = new Texture(Gdx.files.internal("badlogic.png"));


    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }


    public RedPlayer(MyFirstMpDemoMain game) {
        this.game = game;

        keyframe = new Sprite(game.splashScreen.gameAssets.textureAtlas.createSprites().get(0));
        keyframe.setFlip(true, false);

        redIdleAnimation = new Animation<TextureRegion>(1/10f, game.splashScreen.gameAssets.textureAtlas.findRegions("RedIdle"));
        redRunningAnimation = new Animation<TextureRegion>(1/10f, game.splashScreen.gameAssets.textureAtlas.findRegions("RedRun"));
        redKickingAnimation = new Animation<TextureRegion>(1/10f, game.splashScreen.gameAssets.textureAtlas.findRegions("RedKick"));

        redIdleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        redRunningAnimation.setPlayMode(Animation.PlayMode.LOOP);
        redKickingAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void setKeyframe(Sprite keyframe) {
        this.keyframe = keyframe;
    }



    public Vector2 position = new Vector2();
    private Vector2 previousPosition = new Vector2();

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    boolean flipX = false;
    boolean flipY = false;
    float statetime = 0f;
    public void resetStatetime() {
        statetime = 0f;
    }

    public void update(float delta){
        if(previousAnimation != animation){Gdx.app.log(this.toString(),"new animation " + String.valueOf(animation));}
        if(animation != null){
            keyframe.setRegion(animation.getKeyFrame(statetime));
            keyframe.setPosition(position.x, position.y);
            keyframe.setRegionWidth(24);
            keyframe.setRegionHeight(24);
        }else {Gdx.app.log(this.toString(), "animation is null");}
        this.statetime += delta;

        if(previousPosition != position){
            if (previousPosition.x - position.x > 0){
                flipX = true;
            } else if (previousPosition.x - position.x < 0){
                flipX = false;
            }
        }
        keyframe.flip(flipX, flipY);
        keyframe.draw(game.getBatch());
        previousAnimation = animation;
        //previousPosition = position; this doesn't work, because it points to the same memory reference
        previousPosition.x = position.x;
        previousPosition.y = position.y;
    }

}

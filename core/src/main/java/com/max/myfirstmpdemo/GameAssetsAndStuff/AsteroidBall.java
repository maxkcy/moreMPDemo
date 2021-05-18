package com.max.myfirstmpdemo.GameAssetsAndStuff;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class AsteroidBall {
    MyFirstMpDemoMain game;
    public static Animation<TextureRegion> asteroidAnimation;
    public Sprite keyframe;
    private Sprite unInitSpriteTest;


    public Vector2 position = new Vector2(); //null position is (0,0) by default
    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public AsteroidBall(MyFirstMpDemoMain game) {
        this.game = game;
        asteroidAnimation = new Animation<TextureRegion>(1/5f, game.splashScreen.gameAssets.asteroidTextureAtlas.findRegions("asteroid"));
        //asteroidAnimation = new Animation<TextureRegion>(1/5f, game.splashScreen.gameAssets.asteroidNewAtlas.findRegions("a10"));
        asteroidAnimation.setPlayMode(Animation.PlayMode.LOOP);
        keyframe = new Sprite(game.splashScreen.gameAssets.asteroidTextureAtlas.createSprites().get(0));
        unInitSpriteTest = keyframe;
    }

    public float stateTime = 0;

    public void update(float delta){
        // -->this to be done later along with rotation asteroidAnimation.setFrameDuration();
        keyframe.setRegion(asteroidAnimation.getKeyFrame(stateTime));
        keyframe.setSize(40, 40);
        keyframe.setPosition(position.x - 4, position.y - 4);
        if(true){ //true will be false when state on server side is static
        stateTime += delta;}
        //unInitSpriteTest.setBounds(10, 10, 32, 32);
        //unInitSpriteTest.draw(game.getBatch());
        keyframe.draw(game.getBatch());
    }

}

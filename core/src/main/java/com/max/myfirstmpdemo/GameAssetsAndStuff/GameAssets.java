package com.max.myfirstmpdemo.GameAssetsAndStuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.max.myfirstmpdemo.LoadingPathsAndScreen.LoadingPaths;
import com.max.myfirstmpdemo.LoadingPathsAndScreen.SkinPaths;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class GameAssets {
    MyFirstMpDemoMain game;

    private Skin cleanCrispy;
    private Skin sgx;

    public GameAssets(MyFirstMpDemoMain game) {
        this.game = game;
        cleanCrispy = game.getAssetManager().get(SkinPaths.SKIN_1_CLEANCRISPY);
        if(game.getAssetManager().isLoaded(SkinPaths.Skin_2_SGX)){
        sgx = game.getAssetManager().get(SkinPaths.Skin_2_SGX);
        } else {game.getAssetManager().finishLoadingAsset(SkinPaths.Skin_2_SGX);}
    }
    public Skin getCleanCrispy() { return cleanCrispy; }
    public Skin getSgx() { return sgx; }
}

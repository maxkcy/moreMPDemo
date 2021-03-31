package com.max.myfirstmpdemo.LoadingPathsAndScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.max.myfirstmpdemo.GameAssetsAndStuff.GameAssets;
import com.max.myfirstmpdemo.MyFirstMpDemoMain;

public class Loader {
    private LoadingPaths loadingPaths;
    private MyFirstMpDemoMain game;

    public Loader(MyFirstMpDemoMain game) {
       this.game = game;
        loadingPaths = new LoadingPaths();
        loadSkinPaths();
        //this.game.getAssetManager().finishLoading();
    }

    public void loadSkinPaths() {
        for (String skinPath : loadingPaths.getSkinPaths()) {
            if (skinPath != null) {
                //System.out.println(game.getAssetManager());
                game.getAssetManager().load(skinPath, Skin.class);

                if (skinPath == SkinPaths.SKIN_1_CLEANCRISPY) {
                    game.getAssetManager().finishLoadingAsset(SkinPaths.SKIN_1_CLEANCRISPY);
                }
            }
            }

        }
    }


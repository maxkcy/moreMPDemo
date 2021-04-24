package com.max.myfirstmpdemo.LoadingPathsAndScreen;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
        loadSpritePaths();
        loadAtlasPaths();
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

        public void loadSpritePaths(){
            for (String spritePath : loadingPaths.getSpritePaths()){
                if (spritePath != null){
                    game.getAssetManager().load(spritePath, Texture.class);
                }
            }
        }

        public void loadAtlasPaths(){
            for (String atlasPath : loadingPaths.getAnimationAtlasPaths()){
                if (atlasPath != null){
                    game.getAssetManager().load(atlasPath, TextureAtlas.class);
                }
            }
        }

}



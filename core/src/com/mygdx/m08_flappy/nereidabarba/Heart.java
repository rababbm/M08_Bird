package com.mygdx.m08_flappy.nereidabarba;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Heart extends Actor {

    Rectangle bounds; //
    AssetManager assetManager;

    Heart()
    {

        setSize(64, 64);
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        setVisible(false);
    }
    @Override
    public void act(float delta)
    {
        moveBy(-200 * delta, 0);
        bounds.setPosition(getX(), getY());
        if(!isVisible())
            setVisible(true);
        if (getX() < -64)
            remove();
    }

       @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw( assetManager.get( "heart.png", Texture.class), getX(), getY() );

    }

    // Método para obtener los límites del corazón
    public Rectangle getBounds() {
        return bounds;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
}
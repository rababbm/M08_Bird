package com.mygdx.m08_flappy.nereidabarba;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;


public class Player extends Actor {
    Rectangle bounds;
    AssetManager manager;
    float speedy, gravity;
    //TEST29/02 Inmunidad
    private boolean immune;
    private long immunityEndTime;
    private static final long IMMUNITY_DURATION = 3000; // Duración de la inmunidad en milisegundos (por ejemplo, 3 segundos)

    // TEST29 Métodos para establecer y comprobar la inmunidad
    public void setImmune(boolean immune) {
        this.immune = immune;
        if (immune) {
            immunityEndTime = TimeUtils.millis() + IMMUNITY_DURATION;
        }
    }

    public boolean isImmune() {
        return immune && TimeUtils.millis() < immunityEndTime;
    }


    Player()
    {
        setX(200);
        setY(280 / 2 - 64 / 2);
        setSize(64,45);
        bounds = new Rectangle();

        speedy = 0;
        gravity = 850f;
    }
    @Override
    public void act(float delta)
    {
        //Actualitza la posició del jugador amb la velocitat vertical
        moveBy(0, speedy * delta);
//Actualitza la velocitat vertical amb la gravetat
        speedy -= gravity * delta;
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }
    // process user input
void impulso()
{
speedy = 400f;
}
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(manager.get("bird.png", Texture.class), getX(), getY());
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void setManager(AssetManager manager) {
        this.manager = manager;
    }
}

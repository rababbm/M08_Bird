package com.mygdx.m08_flappy.nereidabarba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final Bird game;
    OrthographicCamera camera;
    Stage stage;
    Player player;
    boolean dead;
    int numHearts;
    Array<Pipe> obstacles;
    long lastObstacleTime;
    float score;
    private Heart heart;
    private Array<Heart> hearts;
    private boolean addHeart;
    private long lastHeartAddTime;
    private long lastHeartSpawnTime;
    int heartCounter = 0; // Contador de corazones creados
    private final float heartAddIntervalSeconds = 10f; // Intervalo de tiempo entre generación de corazones en segundos
    float heartSpawnIntervalSeconds = 5f;
    long nextHeartSpawnTime = TimeUtils.nanoTime() + (long) (heartSpawnIntervalSeconds * 1000000000);


    public GameScreen(final Bird gam) {

        this.game = gam;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        player = new Player();
        player.setManager(game.manager);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        stage.addActor(player);
        // create the obstacles array and spawn the first obstacle
        obstacles = new Array<Pipe>();
        spawnObstacle();
        score = 0;

        //CORAZON
        hearts = new Array<Heart>();
        spawnHeart();

        numHearts = 0;

        // Inicializa el corazón con la textura correspondiente
        Heart heart = new Heart();



    }
    private void spawnHeart() {
        // Obtener las coordenadas y del pico de la tubería superior y la base de la tubería inferior
        float pipe1Top = obstacles.get(0).getY() + obstacles.get(0).getHeight();
        float pipe2Bottom = obstacles.get(1).getY();
        float heartY = (pipe1Top + pipe2Bottom) / 2;
        float heartHeight = 64; // Altura del corazón
        float adjustedHeartY = heartY - heartHeight / 2;

        Heart heart = new Heart();
        heart.setX(800);
        heart.setY(adjustedHeartY);
        hearts.add(heart);
        stage.addActor(heart);
        heart.setAssetManager(game.manager);
        lastHeartSpawnTime = TimeUtils.nanoTime();
    }


    private void spawnObstacle() {
        float holey = MathUtils.random(50, 230);
        Pipe pipe1 = new Pipe();
        pipe1.setX(800);
        pipe1.setY(holey - 230);
        pipe1.setUpsideDown(true);
        pipe1.setManager(game.manager);
        obstacles.add(pipe1);
        stage.addActor(pipe1);
        Pipe pipe2 = new Pipe();
        pipe2.setX(800);
        pipe2.setY(holey + 200);
        pipe2.setUpsideDown(false);
        pipe2.setManager(game.manager);
        obstacles.add(pipe2);
        stage.addActor(pipe2);
        lastObstacleTime = TimeUtils.nanoTime();
    }
    @Override
    public void render(float delta) {
        // Render Pintado de pantalla ==============

        // clear the screen with a color
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch
        game.batch.begin();
        game.batch.draw(game.manager.get("background.png", Texture.class), 0, 0);
        game.batch.end();

        // Stage batch: Actors
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.draw(); //Pintar los actores que hay en el screen

        game.batch.begin();
        game.smallFont.draw(game.batch, "Score: " + (int)score, 10, 470);
        game.batch.end();

        //Recuento de lifes
        game.batch.begin();
        game.smallFont.draw(game.batch, "Life:" + numHearts, 10, 420);
        game.batch.end();

        //Actualización de la lógica ===============
        stage.act();

        //SCORE- La puntuació augmenta amb el temps de joc
        score += Gdx.graphics.getDeltaTime();

        // process user input
        if (Gdx.input.justTouched()) {  //Si alguien toco la pantalla en el ultimo frame
            player.impulso();
            game.manager.get("flap.wav", Sound.class).play();
        }

        dead = false;

        // Comprova que el jugador no es surt de la pantalla.
        // Si surt per la part inferior, game over
        if (player.getBounds().y > 480 - 45)
            player.setY( 480 - 45 );
        if (player.getBounds().y < 0 - 45) {
            dead = true;
        }

        // Comprova si cal generar un obstacle nou
        if (TimeUtils.nanoTime() - lastObstacleTime > 1500000000) spawnObstacle();





        // Comprova si les tuberies colisionen amb el jugador
        /*Iterator<Pipe> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next(); //corazon

           if (pipe.getX() + pipe.getWidth() < player.getX() && !pipe.isScored()) {
                // El jugador ha pasado entre las tuberías, incrementa la puntuación
                score += 1;
                pipe.setScored(true);
            }
            /*if (pipe.getBounds().overlaps(player.getBounds())) {
                dead = true;
            }
        }*/
        //CORAZON
        // pajaro toca el corazon
        Iterator<Heart> iterator = hearts.iterator();
        while (iterator.hasNext()) {
            Heart heart = iterator.next();
            if (heart.getBounds().overlaps(player.getBounds())) {
                numHearts ++;
                iterator.remove(); //elimina el corazón de la lista
                heart.remove();
            }
        }


        // Treure de l'array les tuberies que estan fora de pantalla
        Iterator<Pipe>iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getX() < -64) {
                obstacles.removeValue(pipe, true);
            }
        }

        if(dead) {
            game.manager.get("fail.wav", Sound.class).play();
            game.lastScore = (int)score;
            if(game.lastScore > game.topScore) game.topScore = game.lastScore;

            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        //CONDICION DE LOS CORAZONES AL APARECER
        // pajaro toca el corazon
        Iterator<Heart> iterato = hearts.iterator();
        while (iterato.hasNext()) {
            Heart heart = iterato.next();
            if (heart.getBounds().overlaps(player.getBounds())) {
                numHearts++; // Incrementa el contador de corazones
                iterato.remove(); // Elimina el corazón de la lista
                heart.remove(); // Elimina el corazón de la pantalla
            }
        }

        if (TimeUtils.nanoTime() > nextHeartSpawnTime) {
            spawnHeart(); // Crea un nuevo corazón
            nextHeartSpawnTime = TimeUtils.nanoTime() + (long) (heartSpawnIntervalSeconds * 1000000000); // Actualiza el tiempo del próximo corazón
        }
        //TEST29
        // Comprobar la colisión con tuberías
        Iterator<Pipe> iterr = obstacles.iterator();
        while (iterr.hasNext()) {
            Pipe pipe = iterr.next();

            if (pipe.getBounds().overlaps(player.getBounds())) {
                if (numHearts > 0 && !player.isImmune()) {
                    player.setImmune(true);
                    numHearts--;
                } else if (numHearts <= 0 && !player.isImmune()) {
                    dead = true;
                }
                // Eliminar la tubería de la colección después de verificar la colisión
                iterr.remove();
            }
        }
        // Si el jugador está muerto y no tiene vidas, mostrar la pantalla de Game Over
        if (dead && numHearts <= 0) {
            game.manager.get("fail.wav", Sound.class).play();
            game.lastScore = (int)score;
            if(game.lastScore > game.topScore) game.topScore = game.lastScore;

            game.setScreen(new GameOverScreen(game));
            dispose();
            return; // Agregar este return para salir del método render
        }

    }


    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void show() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose() {
    }






}
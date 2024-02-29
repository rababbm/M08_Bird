package com.mygdx.m08_flappy.nereidabarba;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Bird extends Game {

	SpriteBatch batch;
	AssetManager manager;
	BitmapFont smallFont, bigFont;
	int topScore;
	int lastScore;
	public void create() {
		batch = new SpriteBatch();
// Create bitmap fonts from TrueType font
		FreeTypeFontGenerator generator = new
				FreeTypeFontGenerator(Gdx.files.internal("8bitOperatorPlus-Bold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new
				FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 22;
		params.borderWidth = 2;
		params.borderColor = Color.BLACK;
		params.color = Color.WHITE;
		smallFont = generator.generateFont(params); // font size 22 pixels
		params.size = 50;
		params.borderWidth = 5;
		bigFont = generator.generateFont(params); // font size 50 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		manager = new AssetManager();
		manager.load("bird.png", Texture.class);
		manager.load("pipe_up.png", Texture.class);
		manager.load("pipe_down.png", Texture.class);
		manager.load("background.png", Texture.class);
		manager.load("heart.png", Texture.class);
		manager.load("flap.wav", Sound.class);
		manager.load("fail.wav", Sound.class);
		manager.finishLoading();


		topScore = 0;
		lastScore = 0;
		this.setScreen(new MainMenuScreen(this));


	}
	//corazon de vidas
	private int lives;

	public Bird() {
		// Inicializa el contador de vidas
		lives = 3; // Por ejemplo, comienza con 3 vidas
	}

	public void incrementLives() {
		lives++;
	}

	public void decrementLives() {
		lives--;
		if (lives <= 0) {
			// Aquí podrías manejar la lógica de fin de juego si el jugador se queda sin vidas
		}
	}

	public void render() {
		super.render(); // important!
	}
	public void dispose() {
	}
}
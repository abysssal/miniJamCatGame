package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// <summary>
// Idea:
// cat game where you deliver coffee to cats first thing in the morning, but you are also tired, so you may have to drink
// your customer's coffee to gain energy
// if you fall asleep completely, the game is over
// </summary>

public class catGame extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	Viewport viewport;
	Sound pickupSfx, happyCat;
	Music newWindow, pillowFort, sleepless;

	String itemHeld = "nothing";
	Boolean canPickup = true;

	Texture cat;
	Rectangle catRect;
	int energy;

	Texture blackCat;
	Rectangle blackCatRect;
	String blackOrder;

	Texture orangeCat;
	Rectangle orangeCatRect;
	String orangeOrder;

	Texture coffee;
	Rectangle coffeeRect;

	Texture espresso;
	Rectangle espressoRect;

	Texture blackCoffee;
	Rectangle blackCoffeeRect;

	@Override
	public void create () {
		batch = new SpriteBatch();
		viewport = new ExtendViewport(1280, 720);
		camera = (OrthographicCamera) viewport.getCamera();
		pickupSfx = Gdx.audio.newSound(Gdx.files.internal("pickup.wav"));
		happyCat = Gdx.audio.newSound(Gdx.files.internal("happyCat.wav"));
		newWindow = Gdx.audio.newMusic(Gdx.files.internal("New-Window.mp3"));
		pillowFort = Gdx.audio.newMusic(Gdx.files.internal("Pillow-Fort.mp3"));
		sleepless = Gdx.audio.newMusic(Gdx.files.internal("Sleepless.mp3"));

		energy *= 60;

		int random = ThreadLocalRandom.current().nextInt(0, 3);

		switch (random) {
			case 0:
				newWindow.play();
				break;
			case 1:
				pillowFort.play();
				break;
			case 2:
				sleepless.play();
				break;
		}

		System.out.println(random);

		blackOrder = makeOrder();
		orangeOrder = makeOrder();

		if (Objects.equals(blackOrder, orangeOrder)) {
			blackOrder = makeOrder();
			orangeOrder = makeOrder();
		}

		System.out.println(blackOrder + ", " + orangeOrder);

		cat = new Texture("tiredPlayerCat.png");
		blackCat = new Texture("blackTiredCat.png");
		orangeCat = new Texture("orangeTiredCat.png");
		coffee = new Texture("coffee.png");
		espresso = new Texture("espresso.png");
		blackCoffee = new Texture("blackCoffee.png");

		catRect = new Rectangle(viewport.getWorldWidth() / 2 - 32, viewport.getWorldHeight() / 2 - 32, 64, 64);
		blackCatRect = new Rectangle(ThreadLocalRandom.current().nextInt(0, 1280 - 64), ThreadLocalRandom.current().nextInt(0, 720 - 64), 64, 64);
		orangeCatRect = new Rectangle(ThreadLocalRandom.current().nextInt(0, 1280 - 64), ThreadLocalRandom.current().nextInt(0, 720 - 64), 64, 64);
		coffeeRect = new Rectangle(ThreadLocalRandom.current().nextInt(0, 1280 - 48), ThreadLocalRandom.current().nextInt(0, 720 - 48), 48, 48);
		espressoRect = new Rectangle(ThreadLocalRandom.current().nextInt(0, 1280 - 48), ThreadLocalRandom.current().nextInt(0, 720 - 48), 48, 48);
		blackCoffeeRect = new Rectangle(ThreadLocalRandom.current().nextInt(0, 1280 - 48), ThreadLocalRandom.current().nextInt(0, 720 - 48), 48, 48);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.15f, 0.15f, 0.15f, 1);

		camera.position.x = catRect.x;
		camera.position.y = catRect.y;

		viewport.apply();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(cat, catRect.x, catRect.y, 64 , 64);
		batch.draw(blackCat, blackCatRect.x, blackCatRect.y, 64, 64);
		batch.draw(orangeCat, orangeCatRect.x, orangeCatRect.y, 64, 64);

		batch.draw(coffee, coffeeRect.x, coffeeRect.y, 48, 48);
		batch.draw(espresso, espressoRect.x, espressoRect.y, 48, 48);
		batch.draw(blackCoffee, blackCoffeeRect.x, blackCoffeeRect.y, 48, 48);
		batch.end();

		if (!newWindow.isPlaying() && !pillowFort.isPlaying() && !sleepless.isPlaying()) {
			int random = ThreadLocalRandom.current().nextInt(0, 3);

			switch (random) {
				case 0:
					newWindow.play();
					break;
				case 1:
					pillowFort.play();
					break;
				case 2:
					sleepless.play();
					break;
			}

			System.out.println(random);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			catRect.setPosition(catRect.x + 200 * Gdx.graphics.getDeltaTime(), catRect.y);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			catRect.setPosition(catRect.x - 200 * Gdx.graphics.getDeltaTime(), catRect.y);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			catRect.setPosition(catRect.x, catRect.y + 200 * Gdx.graphics.getDeltaTime());
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			catRect.setPosition(catRect.x, catRect.y - 200 * Gdx.graphics.getDeltaTime());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && canPickup) {
			canPickup = false;

			if (catRect.overlaps(coffeeRect)) {
				itemHeld = "coffee";
				System.out.println("picked up coffee");
				coffeeRect.setPosition(999999999, 999999999);
				pickupSfx.play(1);
				canPickup = true;
			} else if (catRect.overlaps(espressoRect)) {
				itemHeld = "espresso";
				System.out.println("picked up espresso");
				espressoRect.setPosition(999999999, 999999999);
				pickupSfx.play(1);
				canPickup = true;
			} else if (catRect.overlaps(blackCoffeeRect)) {
				itemHeld = "blackCoffee";
				System.out.println("picked up black coffee");
				blackCoffeeRect.setPosition(999999999, 999999999);
				pickupSfx.play(1);
				canPickup = true;
			} else if (catRect.overlaps(blackCatRect) && itemHeld == blackOrder) {
				itemHeld = "nothing";
				blackCat = new Texture("happyBlackCat.png");
				System.out.println("the black cat is happy!");
				happyCat.play(1);
				canPickup = true;
			} else if (catRect.overlaps(orangeCatRect) && itemHeld == orangeOrder) {
				itemHeld = "nothing";
				orangeCat = new Texture("happyOrangeCat.png");
				System.out.println("the orange cat is happy!");
				happyCat.play(1);
				canPickup = true;
			} else {
				System.out.println("picked up air, currently have " + itemHeld);
				canPickup = true;
				pickupSfx.play(1);
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		pickupSfx.dispose();
		newWindow.dispose();
		pillowFort.dispose();
		sleepless.dispose();
		cat.dispose();
		blackCat.dispose();
		orangeCat.dispose();
		coffee.dispose();
		espresso.dispose();
		blackCoffee.dispose();
	}

	public String makeOrder() {
		int randomizer = ThreadLocalRandom.current().nextInt(0, 3);
		String order = null;

		switch (randomizer) {
			case 0:
				order = "coffee";
				break;
			case 1:
				order = "espresso";
				break;
			case 2:
				order = "blackCoffee";
				break;
		}

		return order;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.concurrent.ThreadLocalRandom;

// <summary>
// Idea:
// cat game where you deliver coffee to cats first thing in the morning, but you are also tired, so you may have to drink
// your customer's coffee to gain energy
// if you fall asleep completely, the game is over
// </summary>

public class catGame extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;

	String itemHeld = "nothing";
	Boolean canPickup = true;

	Texture cat;
	Rectangle catRect;

	Texture coffee;
	Rectangle coffeeRect;

	Texture espresso;
	Rectangle espressoRect;

	Texture blackCoffee;
	Rectangle blackCoffeeRect;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(1280, 720);
		camera.setToOrtho(false, 1280, 720);

		cat = new Texture("tiredPlayerCat.png");
		coffee = new Texture("coffee.png");
		espresso = new Texture("espresso.png");
		blackCoffee = new Texture("blackCoffee.png");

		catRect = new Rectangle(Gdx.graphics.getWidth() / 2 - 32, Gdx.graphics.getHeight() / 2 - 32, 64, 64);
		coffeeRect = new Rectangle(ThreadLocalRandom.current().nextInt(0, 1280 - 48), ThreadLocalRandom.current().nextInt(0, 720 - 48), 48, 48);
		espressoRect = new Rectangle(ThreadLocalRandom.current().nextInt(0, 1280 - 48), ThreadLocalRandom.current().nextInt(0, 720 - 48), 48, 48);
		blackCoffeeRect = new Rectangle(ThreadLocalRandom.current().nextInt(0, 1280 - 48), ThreadLocalRandom.current().nextInt(0, 720 - 48), 48, 48);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.15f, 0.15f, 0.15f, 1);
		batch.begin();
		batch.draw(cat, catRect.x, catRect.y, 64 , 64);
		batch.draw(coffee, coffeeRect.x, coffeeRect.y, 48, 48);
		batch.draw(espresso, espressoRect.x, espressoRect.y, 48, 48);
		batch.draw(blackCoffee, blackCoffeeRect.x, blackCoffeeRect.y, 48, 48);
		batch.end();

		camera.project(new Vector3(catRect.x, catRect.y, -10));
		camera.update();

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
				canPickup = true;
			} else if (catRect.overlaps(espressoRect)) {
				itemHeld = "espresso";
				System.out.println("picked up espresso");
				espressoRect.setPosition(999999999, 999999999);
				canPickup = true;
			} else if (catRect.overlaps(blackCoffeeRect)) {
				itemHeld = "blackCoffee";
				System.out.println("picked up black coffee");
				blackCoffeeRect.setPosition(999999999, 999999999);
				canPickup = true;
			} else {
				System.out.println("picked up air, currently have " + itemHeld);
				canPickup = true;
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		cat.dispose();
		coffee.dispose();
		espresso.dispose();
		blackCoffee.dispose();
	}
}

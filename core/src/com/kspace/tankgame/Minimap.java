package com.kspace.tankgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;;

public class Minimap
{
	public Viewport viewport;
	public Sprite bg;
	public Sprite ico;
	public Player player;
	public Background grid;
	public BitmapFont font;
	
	private int borderThickness = 3;
	
	Minimap(Background grid, Player player)
	{
		this.grid = grid;
		this.player = player;
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ui/font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 12;
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 2;
		parameter.spaceX = -2;
		font = generator.generateFont(parameter);
		generator.dispose();
		
		viewport = new ScreenViewport();
		
		Pixmap bgpm = new Pixmap(Gdx.graphics.getHeight() / 5 + borderThickness, Gdx.graphics.getHeight() / 5 + borderThickness, Pixmap.Format.RGBA8888);
		bgpm.setColor(Color.BLACK);
		bgpm.fillRectangle(0, 0, bgpm.getWidth(), bgpm.getHeight());
		bgpm.setColor(Color.WHITE);
		bgpm.fillRectangle(0, 0, bgpm.getWidth() - borderThickness, bgpm.getHeight() - borderThickness);
		
		Texture background = new Texture(bgpm);
		Texture icon = new Texture("data/ui/minimap.png");
		
		bg = new Sprite(background);
		bg.setColor(Color.DARK_GRAY);
		bg.setPosition(Gdx.graphics.getWidth() / -2, Gdx.graphics.getHeight() / 2 - bg.getHeight());
		
		ico = new Sprite(icon);
		
		ico.setColor(player.color);
		ico.setOriginBasedPosition(bg.getX() + bg.getOriginX(), bg.getY() + bg.getOriginY());
		ico.setScale(.5f);
	}

	public void draw(SpriteBatch batch)
	{
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		float relx = player.position.x / grid.mapWidth;
		float rely = player.position.y / grid.mapHeight;	
		
		ico.setOriginBasedPosition(bg.getX() + bg.getOriginX() + relx * (bg.getWidth() - borderThickness) - borderThickness / 2, bg.getY() + bg.getOriginY() + rely * (bg.getHeight() - borderThickness) + borderThickness / 2);
		ico.setRotation(player.direction);
		
		batch.setProjectionMatrix(viewport.getCamera().combined);
		
		bg.draw(batch);
		ico.draw(batch);
		
		font.draw(batch, "FPS:" + Gdx.graphics.getFramesPerSecond(), -Gdx.graphics.getWidth() / 2 + bg.getWidth(), Gdx.graphics.getHeight() / 2 - 2);
		font.draw(batch, "X:" + Math.round(player.position.x), -Gdx.graphics.getWidth() / 2 + bg.getWidth(), Gdx.graphics.getHeight() / 2 - 14);
		font.draw(batch, "Y:" + Math.round(player.position.y), -Gdx.graphics.getWidth() / 2 + bg.getWidth(), Gdx.graphics.getHeight() / 2 - 26);
	}
	
	public void dispose()
	{
		font.dispose();
	}
}

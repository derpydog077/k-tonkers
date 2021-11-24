package com.kspace.tankgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Background
{
	public Array<Sprite> tiles = new Array<Sprite>(); //Texture Sprite
	public Vector2 position;
	public int sizeX;
	public int sizeY;
	public int mapWidth;
	public int mapHeight;
	public int tileX;
	public int tileY;
	public int renderRadius;

	Background(int sizeX, int sizeY)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		Texture texture = new Texture("data/world/tile.png");
		tileX = texture.getWidth();
		tileY = texture.getHeight();
		mapWidth = sizeX * tileX;
		mapHeight = sizeY * tileY;
		
		for (int y = 0; y < sizeY; y++)
		{
			for (int x = 0; x < sizeX; x++)
			{
				Sprite sprite = new Sprite(texture);
				sprite.setColor(Color.DARK_GRAY);
				sprite.setOriginCenter();
				sprite.setPosition(x * tileX - mapWidth/2, y * tileY - mapHeight/2);
				tiles.add(sprite);
			}
		}
	}

	public void draw(SpriteBatch batch, OrthographicCamera cam)
	{
		int px = (int) cam.position.x + mapWidth/2;
		int py = (int) cam.position.y + mapHeight/2;
		
		int scanColumn = (int) (px / tileX);
		int scanLine = (int) (py / tileY);

		for (int x = -renderRadius; x < renderRadius + 1; x++)
		{
			for (int y = -renderRadius; y < renderRadius + 1; y++)
			{
				if ((((scanLine + y) < sizeY) && ((scanLine + y) > -1)) && (((scanColumn + x) < sizeX) && ((scanColumn + x) > -1)))
				{
					tiles.get((scanLine + y) * sizeY + scanColumn + x).draw(batch);
				}
			}
		}
	}
	
	public void dispose()
	{

	}
}

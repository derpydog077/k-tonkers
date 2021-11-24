package com.kspace.tankgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Projectile
{
	public Sprite sprite; //Texture Sprite
	public Vector2 position;
	public float direction = 0;
	public float velocity = 0;
	public Weapon parent;
	public float life = 3;

	Projectile(Weapon parent, float direction, float velocity)
	{
		this.direction = direction;
		this.velocity = velocity;
		this.parent = parent;
		
		float xOffset = (float) Math.cos(Math.toRadians(direction + 90)) * -parent.length;
		float yOffset = (float) Math.sin(Math.toRadians(direction + 90)) * -parent.length;
		
		Texture texture = new Texture("data/weapons/cannon/prj_s0.png");
		
		sprite = new Sprite(texture);
		sprite.setColor(parent.parent.color);
		sprite.setOriginCenter();
		sprite.setRotation(direction - 180);
		sprite.setOriginBasedPosition(parent.position.x + xOffset, parent.position.y + yOffset);
		
		position = new Vector2(parent.position.x + xOffset, parent.position.y + yOffset);
	}

	public void draw(SpriteBatch batch)
	{
		float x = (float) Math.cos(Math.toRadians(direction + 90)) * -velocity * Gdx.graphics.getDeltaTime();
		float y = (float) Math.sin(Math.toRadians(direction + 90)) * -velocity * Gdx.graphics.getDeltaTime();
		
		position.add(x, y);
		
		life -= Gdx.graphics.getDeltaTime();
		
		System.out.println(life);
		
		if (life < 0)
		{
			parent.despawn(this);
		}
		
		sprite.setOriginBasedPosition(position.x, position.y);
		
		if (Vector2.dst(position.x, position.y, parent.parent.position.x, parent.parent.position.y) < 2000)
		{
			sprite.draw(batch);
		}
	}
	
	public void dispose()
	{

	}
}

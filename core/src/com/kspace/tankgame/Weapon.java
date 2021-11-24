package com.kspace.tankgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;

public class Weapon
{
	public Sprite sprite; //Texture Sprite
	public Player parent; //Parent Player
	public float offset = 0; //Distance from Turret Center
	public float direction = 0; //Rotation Offset in Degrees
	public float rotation = 0;
	public float length = 0;
	public float reload = 0.2f;
	public float timer = 0;
	public Vector2 position = new Vector2();
	public Array<Projectile> active = new Array<Projectile>();

	Weapon(Player parent)
	{
		this.parent = parent;
		Texture texture = new Texture("data/weapons/cannon/wpn_s0.png");
		sprite = new Sprite(texture);
		sprite.setOriginCenter();
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight());
		this.length = sprite.getHeight();
	}

	public void draw(SpriteBatch batch)
	{
		float xOffset = (float) Math.cos(Math.toRadians(parent.rotation + direction + 90)) * -offset;
		float yOffset = (float) Math.sin(Math.toRadians(parent.rotation + direction + 90)) * -offset;
		
		this.position.x = parent.position.x + xOffset;
		this.position.y = parent.position.y + yOffset;
		
		if (timer > 0) timer -= Gdx.graphics.getDeltaTime();
		else if (timer < 0) timer = 0;
		
		sprite.setOriginBasedPosition(parent.position.x + xOffset, parent.position.y + yOffset);
		sprite.setRotation(parent.rotation + direction + rotation);
		
		for (int i = 0; i < active.size; i++)
		{
			active.get(i).draw(batch);
		}
		
		sprite.draw(batch);
	}
	
	public void fire()
	{
		if (timer == 0)
		{
			Projectile p = new Projectile(this, parent.rotation + direction + rotation, 256);
			active.add(p);
			timer = reload;
		}
	}
	
	public void despawn(Projectile p)
	{
		int i = active.indexOf(p, false);
		active.set(i, null);
		active.removeIndex(i);
	}
	
	public void dispose()
	{

	}
}

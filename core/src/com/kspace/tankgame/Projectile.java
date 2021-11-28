package com.kspace.tankgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Projectile
{
	public Sprite sprite; //Texture Sprite
	public Vector2 position;
	public float direction = 0;
	public float velocity = 0;
	public ModelInstance instance;
	public Weapon parent;
	public float life = 3;

	Projectile(Weapon parent, Model mdl, float direction, float velocity)
	{
		this.direction = direction;
		this.velocity = velocity;
		this.parent = parent;
		
		float xOffset = (float) Math.cos(Math.toRadians(direction + 90)) * -parent.length;
		float yOffset = (float) Math.sin(Math.toRadians(direction + 90)) * -parent.length;
		
		instance = new ModelInstance(mdl);
		instance.transform.setToRotation(0, 0, 1, direction);
		instance.transform.setTranslation(parent.position.x + xOffset, parent.position.y + yOffset, 0);
		instance.materials.get(0).set(ColorAttribute.createDiffuse(parent.parent.color));
		
		//Texture texture = new Texture("data/weapons/cannon/prj_s0.png");
		
		//sprite = new Sprite(texture);
		//sprite.setColor(parent.parent.color);
		//sprite.setOriginCenter();
		//sprite.setRotation(direction - 180);
		//sprite.setOriginBasedPosition(parent.position.x + xOffset, parent.position.y + yOffset);
		
		position = new Vector2(parent.position.x + xOffset, parent.position.y + yOffset);
	}

	public void update()
	{
		float x = (float) Math.cos(Math.toRadians(direction + 90)) * -velocity * Gdx.graphics.getDeltaTime();
		float y = (float) Math.sin(Math.toRadians(direction + 90)) * -velocity * Gdx.graphics.getDeltaTime();
		
		position.add(x, y);
		
		life -= Gdx.graphics.getDeltaTime();
		
		if (life < 0)
		{
			parent.despawn(this);
		}
		
		instance.transform.setTranslation(position.x, position.y, 0);
		//sprite.setOriginBasedPosition(position.x, position.y);
		
		//if (Vector2.dst(position.x, position.y, parent.parent.position.x, parent.parent.position.y) < 2048)
	}
	
	public void dispose()
	{
		instance.model.dispose();
	}
}

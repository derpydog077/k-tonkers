package com.kspace.tankgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector2;

public class Projectile
{
	public Vector2 position;
	public Weapon parent;
	public ModelInstance instance;
	public float direction = 0;
	public float velocity = 0;
	public float life = 3;

	Projectile(Weapon parent, Vector2 position, Color color, Model mdl, float direction, float velocity)
	{
		this.parent = parent;
		this.direction = direction;
		this.velocity = velocity;
		
		instance = new ModelInstance(mdl);
		instance.transform.setToRotation(0, 0, 1, direction);
		//instance.transform.scale(0.7f, 0.7f, 0.7f);
		instance.transform.setTranslation(position.x, position.y, 0f);
		instance.materials.get(0).set(ColorAttribute.createDiffuse(color));
		
		this.position = new Vector2(position.x, position.y);
	}

	public void update()
	{
		float x = (float) Math.cos(Math.toRadians(direction + 90)) * -velocity * Gdx.graphics.getDeltaTime();
		float y = (float) Math.sin(Math.toRadians(direction + 90)) * -velocity * Gdx.graphics.getDeltaTime();
		
		position.add(x, y);
		
		life -= Gdx.graphics.getDeltaTime();
		
		instance.transform.setTranslation(position.x, position.y, 0);

		//if (Vector2.dst(position.x, position.y, parent.parent.position.x, parent.parent.position.y) < 2048)
	}
	
	public void dispose()
	{
		//instance.model.dispose();
	}
}

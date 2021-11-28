package com.kspace.tankgame;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player
{
	public Sprite sprite;
	public Sprite turret;
	public Color color = new Color(0.5f, 0.8f, 0f, 1f);
	public Vector2 position = new Vector2();
	public float rotation;
	public float direction = 0;
	public Array<Array<Weapon>> weapons = new Array<Array<Weapon>>();
	public Projectile p;
	public float velocity = 0;
	public Array<ModelInstance> renderQueue = new Array<ModelInstance>();
	public Array<ModelInstance> modelInstances = new Array<ModelInstance>();
	
	Player(AssetManager amgr)
	{	
		Model tank = amgr.get("data/player/tank.g3db", Model.class);
		Model turret = amgr.get("data/player/turret.g3db", Model.class);
		Model cannon = amgr.get("data/weapons/cannon/mdl_s0.g3db", Model.class);
		Model proj = amgr.get("data/weapons/cannon/prj_s0.g3db", Model.class);
		
		ModelInstance body_i = new ModelInstance(tank);
		body_i.materials.get(0).set(ColorAttribute.createDiffuse(color));
		body_i.transform.rotate(1, 0, 0, 90);
		modelInstances.add(body_i);
		
		ModelInstance turret_i = new ModelInstance(turret);
		turret_i.materials.get(0).set(ColorAttribute.createDiffuse(color));
		//turret_i.transform.scale(10,10,10);
		turret_i.transform.rotate(1, 0, 0, 90);
		modelInstances.add(turret_i);
		
		Array<Weapon> firegroupLeft = new Array<Weapon>();
		Array<Weapon> firegroupRight = new Array<Weapon>();
			
		Weapon w1 = new Weapon(this, cannon, proj);
		w1.offset = 30;
		w1.direction = -30;
		w1.rotation = -w1.direction;
		w1.muzzle = 256;
		w1.reload = 0.2f;
		firegroupLeft.add(w1);
		
		Weapon w2 = new Weapon(this, cannon, proj);
		w2.offset = 30;
		w2.direction = 30;
		w2.rotation = -w2.direction;
		w2.muzzle = 256;
		w2.reload = 0.2f;
		firegroupRight.add(w2);
		
		weapons.add(firegroupLeft);
		weapons.add(firegroupRight);
		
		//tank.dispose();
		//cannon.dispose();
		//turret.dispose();
	}

	public void render(ModelBatch batch, Environment environment)
	{
		renderQueue.clear();
		
		modelInstances.get(0).transform.setToRotation(0, 0, 1, direction);
		modelInstances.get(0).transform.rotate(1, 0, 0, 90);
		modelInstances.get(0).transform.setTranslation(position.x, position.y, -1);
		renderQueue.add(modelInstances.get(0));
		
		for (int i = 0; i < weapons.size; i++)
		{
			for (int w = 0; w < weapons.get(i).size; w++)
			{
				weapons.get(i).get(w).update();
				renderQueue.add(weapons.get(i).get(w).instance);
				
				for (int a = 0; a < weapons.get(i).get(w).active.size; a++)
				{
					renderQueue.add(weapons.get(i).get(w).active.get(a).instance);
				}
			}
		}
		
		modelInstances.get(1).transform.setToRotation(0, 0, 1, rotation);
		modelInstances.get(1).transform.rotate(1, 0, 0, 90);
		modelInstances.get(1).transform.setTranslation(position.x, position.y, 1);
		renderQueue.add(modelInstances.get(1));
		
		batch.render(renderQueue, environment);
	}
	
	public void move(float target)
	{
		if ((velocity < target + 0.1) && (velocity > target - 0.1)) velocity = target;
		else if (Math.round(target) == 0.0f) velocity += (target - velocity) / 10;
		else velocity += (target - velocity) / 50;
		
		float x = (float) Math.cos(Math.toRadians(direction + 90)) * velocity * Gdx.graphics.getDeltaTime();
		float y = (float) Math.sin(Math.toRadians(direction + 90)) * velocity * Gdx.graphics.getDeltaTime();
		
		position.x += x;
		position.y += y;
	}
	
	public void fire(int firegroup) //true = primary, false = secondary
	{
		for (int i = 0; i < weapons.get(firegroup).size; i++)
		{
			weapons.get(firegroup).get(i).fire();
		}
	}

	public void dispose()
	{
		for (int i = 0; i < weapons.size; i++)
		{
			for (int w = 0; w < weapons.get(i).size; w++)
			{
				weapons.get(i).get(w).dispose();
			}
		}
	}
}

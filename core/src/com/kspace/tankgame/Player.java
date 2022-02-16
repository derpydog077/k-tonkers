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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class Player
{
	public Sprite sprite;
	public Sprite turret;
	public BoundingBox hitbox = new BoundingBox();
	public Color color;
	public Vector2 position = new Vector2();
	public float rotation;
	public float direction = 0;
	public float velocity = 0;
	public float health = 100;
	public Array<Array<Weapon>> weapons = new Array<Array<Weapon>>();
	public Array<ModelInstance> renderQueue = new Array<ModelInstance>();
	public Array<ModelInstance> modelInstances = new Array<ModelInstance>();
	
	Player(AssetManager amgr, Color color)
	{	
		this.color = color;
		
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
		turret_i.transform.rotate(1, 0, 0, 90);
		modelInstances.add(turret_i);
		
		Array<Weapon> firegroup0 = new Array<Weapon>();
		
		Weapon w1 = new Weapon(this, cannon, proj);
		w1.offset = 30;
		w1.direction = 0;
		w1.rotation = -w1.direction;
		w1.muzzle = 256;
		w1.reload = 0.2f;
		w1.length = 64;
		firegroup0.add(w1);
		
		/*Weapon w2 = new Weapon(this, cannon, proj);
		w2.offset = 30;
		w2.direction = 30;
		w2.rotation = -w2.direction;
		w2.muzzle = 256;
		w2.reload = 0.2f;
		w2.length = 64;
		firegroupRight.add(w2);*/
		
		weapons.add(firegroup0);
	}

	public void render(ModelBatch batch, Environment environment)
	{
		modelInstances.get(1).calculateBoundingBox(hitbox);		
		hitbox.set(hitbox.min.add(position.x, 0, position.y), hitbox.max.add(position.x, 0, position.y));
		
		
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
				
				for (int a = 0; a < weapons.get(i).get(w).projectiles.size; a++)
				{
					renderQueue.add(weapons.get(i).get(w).projectiles.get(a).instance);
				}
			}
		}
		
		modelInstances.get(1).transform.setToRotation(0, 0, 1, rotation);
		modelInstances.get(1).transform.rotate(1, 0, 0, 90);
		modelInstances.get(1).transform.setTranslation(position.x, position.y, 1);
		renderQueue.add(modelInstances.get(1));
		
		batch.render(renderQueue, environment);
	
		renderQueue.clear();
	}
	
	public void checkIfHit(Array<Projectile> projectiles)
	{
		for (Projectile p : projectiles)
		{
			if (getImpact(p.position))
			{
				p.parent.despawn(p);
				health -= 1;
			}
		}
	}
	
	public boolean getImpact(Vector2 loc)
	{
		return hitbox.contains(new Vector3(loc.x, 0, loc.y));
	}
	
	public Array<Projectile> getProjectiles()
	{
		Array<Projectile> projectiles = new Array<Projectile>();
				
		for (Array<Weapon> a : weapons)
		{
			for (Weapon w : a)
			{
				for (Projectile p : w.projectiles)
				{
					projectiles.add(p);
				}
			}
		}
		return projectiles;
	}
	
	public void move(Map map, float target)
	{
		if ((velocity < target + 0.1) && (velocity > target - 0.1)) velocity = target;
		else if (Math.round(target) == 0.0f) velocity += (target - velocity) / 10;
		else velocity += (target - velocity) / 50;
		
		float x = (float) Math.cos(Math.toRadians(direction + 90)) * velocity * Gdx.graphics.getDeltaTime();
		float y = (float) Math.sin(Math.toRadians(direction + 90)) * velocity * Gdx.graphics.getDeltaTime();
		
		if (map.mapBox.contains(position.x + x, position.y + y))
		{
			position.x += x;
			position.y += y;
		}
		
		//System.out.println(hitbox.contains(position.x, position.y));
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

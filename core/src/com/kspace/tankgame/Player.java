package com.kspace.tankgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	public AssetManager assets;
	public Array<ModelInstance> modelInstances = new Array<ModelInstance>();
	
	Player()
	{
		assets = new AssetManager();
		assets.load("data/player/tank.g3db", Model.class);
		assets.load("data/player/turret.g3db", Model.class);
		assets.finishLoading();
		
		Model tank = assets.get("data/player/tank.g3db", Model.class);
		Model turret = assets.get("data/player/turret.g3db", Model.class);
		
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
			
		//Texture bodyTex = new Texture("data/player/player.png");
		//Texture turretTex = new Texture("data/player/turret.png");
		//sprite = new Sprite(bodyTex);
		//sprite.setColor(color);
		//turret = new Sprite(turretTex);
		//turret.setColor(color);
		
		Weapon w1 = new Weapon(this);
		w1.offset = 30;
		w1.direction = -30;
		w1.rotation = -w1.direction;
		firegroupLeft.add(w1);
		
		Weapon w2 = new Weapon(this);
		w2.offset = 30;
		w2.direction = 30;
		w2.rotation = -w2.direction;
		firegroupLeft.add(w2);
		
		Weapon w3 = new Weapon(this);
		w3.offset = 30;
		w3.direction = 0;
		w3.rotation = 0;
		firegroupLeft.add(w3);
		
		Weapon w4 = new Weapon(this);
		w4.offset = 30;
		w4.direction = 135;
		w4.rotation = 0;
		firegroupRight.add(w4);
		
		Weapon w5 = new Weapon(this);
		w5.offset = 30;
		w5.direction = 225;
		w5.rotation = 0;
		firegroupRight.add(w5);
		
		Weapon w6 = new Weapon(this);
		w6.offset = 32;
		w6.direction = 105;
		w6.rotation = 30;
		firegroupRight.add(w6);
		
		Weapon w7 = new Weapon(this);
		w7.offset = 30;
		w7.direction = 255;
		w7.rotation = -30;
		firegroupRight.add(w7);
		
		weapons.add(firegroupLeft);
		weapons.add(firegroupRight);
	}

	public void render(ModelBatch batch, Environment environment)
	{
		modelInstances.get(0).transform.setToRotation(0, 0, 1, direction);
		modelInstances.get(0).transform.rotate(1, 0, 0, 90);
		modelInstances.get(0).transform.setTranslation(position.x, position.y, 0);
		
		modelInstances.get(1).transform.setToRotation(0, 0, 1, rotation);
		//modelInstances.get(1).transform.scale(2, 2, 2);
		modelInstances.get(1).transform.rotate(1, 0, 0, 90);
		modelInstances.get(1).transform.setTranslation(position.x, position.y, 1);
		
		batch.render(modelInstances, environment);
	}
	
	public void draw(SpriteBatch batch)
	{
		//sprite.setRotation(direction);
		//sprite.setOriginBasedPosition(this.position.x, this.position.y);
		
		for (int i = 0; i < weapons.size; i++)
		{
			for (int w = 0; w < weapons.get(i).size; w++) weapons.get(i).get(w).draw(batch);
		}
		
		//turret.setRotation(rotation);
		//turret.setOriginBasedPosition(this.position.x, this.position.y);
		//turret.draw(batch);
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

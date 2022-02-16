package com.kspace.tankgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;

public class Weapon
{
	public Player parent; //Parent Player
	public float offset = 0; //Distance from Turret Center
	public float direction = 0; //Rotation Offset in Degrees
	public float rotation = 0;
	public float length = 0;
	public float reload = 0f;
	public float timer = 0;
	public float muzzle = 0;
	public Model proj;
	public Vector2 position = new Vector2();
	public ModelInstance instance;
	public Array<Projectile> projectiles = new Array<Projectile>();

	Weapon(Player parent, Model mdl, Model prj)
	{
		this.parent = parent;
		
		proj = prj;
		
		instance = new ModelInstance(mdl);
	}

	public void update()
	{
		float xOffset = (float) Math.cos(Math.toRadians(parent.rotation + direction + 90)) * -offset;
		float yOffset = (float) Math.sin(Math.toRadians(parent.rotation + direction + 90)) * -offset;
		
		this.position.x = parent.position.x + xOffset;
		this.position.y = parent.position.y + yOffset;
		
		if (timer > 0) timer -= Gdx.graphics.getDeltaTime();
		else if (timer < 0) timer = 0;
		
		instance.transform.setToRotation(0, 0, 1, parent.rotation + direction + rotation);
		instance.transform.setTranslation(position.x, position.y, 1);
		
		for (Projectile projectile : projectiles)
		{
			projectile.update();
		}
	}
	
	public void fire()
	{
		if (timer == 0)
		{
			float rot = parent.rotation + direction + rotation;
			float sx = (float) Math.cos(Math.toRadians(rot + 90)) * -length;
			float sy = (float) Math.sin(Math.toRadians(rot + 90)) * -length;
			Vector2 pos = new Vector2(this.position.x + sx, this.position.y + sy);
			Projectile p = new ProjectileKinetic(this, pos, parent.color, proj, rot, muzzle);
			projectiles.add(p);
			timer = reload;
		}
	}
	
	public void despawn(Projectile p)
	{
		int i = projectiles.indexOf(p, false);
		projectiles.set(i, null);
		projectiles.removeIndex(i);
	}
	
	public void dispose()
	{
		//instance.model.dispose();
		
		for (int i = 0; i < projectiles.size; i++)
		{
			projectiles.get(i).dispose();
		}
	}
}

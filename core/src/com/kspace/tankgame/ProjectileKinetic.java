package com.kspace.tankgame;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class ProjectileKinetic extends Projectile
{
	ProjectileKinetic(Weapon parent, Vector2 position, Color color, Model mdl, float direction, float velocity)
	{
		super(parent, position, color, mdl, direction, velocity);
	}

	public void update()
	{
		super.update();
	}
	
	public void dispose()
	{
		super.dispose();
	}
}

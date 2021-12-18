package com.kspace.tankgame;

import com.badlogic.gdx.graphics.g3d.Model;

public class ProjectileKinetic extends Projectile
{
	ProjectileKinetic(Weapon parent, Model mdl, float direction, float velocity)
	{
		super(parent, mdl, direction, velocity);
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

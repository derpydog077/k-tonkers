package com.kspace.tankgame;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Entity
{
	public Sprite sprite;
	public Sprite turret;
	public Vector2 position = new Vector2();
	public float direction = 0;
	public Array<ModelInstance> modelInstances = new Array<ModelInstance>();
	
	Entity(AssetManager amgr)
	{	
		Model entity = amgr.get("data/player/npc/test_enemy.g3dj", Model.class);
		
		ModelInstance instance = new ModelInstance(entity);
		
		instance.transform.rotate(1, 0, 0, 90);
		modelInstances.add(instance);
		
	}

	public void render(ModelBatch batch, Environment environment)
	{
		modelInstances.get(0).transform.setToRotation(0, 0, 1, direction);
		modelInstances.get(0).transform.rotate(1, 0, 0, 90);
		modelInstances.get(0).transform.setTranslation(position.x, position.y, 0);
		
		batch.render(modelInstances, environment);
	}
	
	public void dispose()
	{
		
	}
}

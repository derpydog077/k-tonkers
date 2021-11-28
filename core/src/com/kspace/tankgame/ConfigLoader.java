package com.kspace.tankgame;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.files.FileHandle;

public class ConfigLoader
{
	public JsonReader json;
	public JsonValue val;
	
	ConfigLoader()
	{
		json = new JsonReader();
	}

	public void parse(FileHandle file)
	{
		val = json.parse(file);
	}
	
	public JsonValue retrieve(String name)
	{
		JsonValue comp = val.get(name);
		return comp;
	}
	
	public static JsonValue find(String name, JsonValue obj)
	{
		JsonValue comp = obj.get(name);
		return comp;
	}
	
	public void dispose()
	{
		
	}
}

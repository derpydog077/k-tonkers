package com.kspace.tankgame;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.files.FileHandle;
import java.util.Map;
import java.util.HashMap;

public class ConfigLoader
{
	public JsonReader json;
	public JsonValue temp;
	
	public Map<String, JsonValue> loadedConfigs = new HashMap<String, JsonValue>();
	
	ConfigLoader()
	{
		json = new JsonReader();
		temp = null;
	}

	public void load(String key, FileHandle file)
	{
		temp = json.parse(file);
		
		loadedConfigs.put(key, temp);
	}
	
	public JsonValue get(String cfg)
	{
		return loadedConfigs.get(cfg);
	}
	
	public JsonValue find(JsonValue cfg, String key)
	{
		return cfg.get(key);
	}
	
	public void delete(String cfg)
	{
		loadedConfigs.remove(cfg);
	}
}

package com.kspace.tankgame;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;

public class UIRenderer
{
	public BitmapFont font;
	public String activeUILayer = "none";
	public Array<Object> uiElements = new Array<Object>();
	public JsonValue regionDefs;
	public Viewport viewport;
	
	UIRenderer(Viewport viewport, JsonValue regionDefs)
	{
		this.viewport = viewport;
		this.regionDefs = regionDefs;
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ui/font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		
		parameter.size = 16;
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 2;
		parameter.spaceX = -2;
		
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	public void draw(SpriteBatch batch)
	{
		batch.setProjectionMatrix(viewport.getCamera().combined);
		
		font.draw(batch, "sample tank", 0, 0);

		for (Object element : uiElements)
		{
			try 
			{
				Sprite s = (Sprite) element.getClass().getField("sprite").get(element);
				s.draw(batch);
			}
			catch (IllegalAccessException e) {}
			catch (NoSuchFieldException e) {}
		}
	}
	
	public void handleInput(int mx, int my)
	{
		for (Object element : uiElements)
		{
			if (element.getClass() == UIButton.class);
			{
				Vector2 mwc = viewport.unproject(new Vector2(mx, my));
				System.out.println(((UIButton) element).checkHitbox((int) mwc.x, (int) mwc.y));
			}
		}
	}
	
	public boolean gameUI()
	{
		activeUILayer = "game";
		uiElements.clear();
		
		if (!Float.isNaN(viewport.unproject(new Vector2(1, 1)).x))
		{
			String name = "editorButton";
			uiElements.add(new UIButton(getRegionFromConfig(name, regionDefs), (int) viewport.unproject(new Vector2(0, 420)).x, (int) viewport.unproject(new Vector2(0, 420)).y, regionDefs.get(name).get("scale").asFloat()));
			return true;
		}
		else return false;
		//uiElements.add(new UIButton(getRegionFromConfig("editorButton", regionDefs), 420, 0, 1));
	}
	
	public TextureRegion getRegionFromConfig(String name, JsonValue cfgfile)
	{
		JsonValue cfg = cfgfile.get(name);
		TextureRegion tr = new TextureRegion(new Texture(Gdx.files.internal(cfgfile.get("texture").asString())), cfg.get("x").asInt(), cfg.get("y").asInt(), cfg.get("width").asInt(), cfg.get("height").asInt());
		return tr;
	}
	
	public void dispose()
	{
		font.dispose();
	}
}

class UIButton
{
	public Sprite sprite;
	public String tag;
	
	UIButton(TextureRegion tex, int x, int y, float scale)
	{
		sprite = new Sprite(tex);
		sprite.setScale(scale);
		sprite.setPosition(x, y);
	}
	
	public boolean checkHitbox(int x, int y)
	{
		if (sprite.getBoundingRectangle().contains(x, y))
		{
			return true;
		}
		else return false;
	}
}

class UILabel
{
	UILabel(TextureRegion tex)
	{

	}
}

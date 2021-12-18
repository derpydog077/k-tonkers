package com.kspace.tankgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;

public class TankGame extends ApplicationAdapter implements InputProcessor
{
	private Array<Entity> entities = new Array<Entity>();
	
	private SpriteBatch batch;
	private Environment environment;
	private ModelBatch modelBatch;
	private Minimap map;
	private OrthographicCamera camera;
	private Player player;
	private ConfigLoader cfgl;
	private Background background;
	private AssetManager assets;
	private UIRenderer ui;
	private Viewport uiViewport;
	
	public boolean loading;
	
	private float[] zoomLimits = {0.5f, 3f};
	
	@Override
	public void create()
	{
		loading = true;
		
		cfgl = new ConfigLoader();
		loadConfigs();
		
		uiViewport = new ScreenViewport();
		
		assets = new AssetManager();
		assets.load("data/player/tank.g3db", Model.class);
		assets.load("data/player/turret.g3db", Model.class);
		assets.load("data/weapons/cannon/mdl_s0.g3db", Model.class);
		assets.load("data/weapons/cannon/prj_s0.g3db", Model.class);
		assets.load("data/player/npc/test_enemy.g3dj", Model.class);
		assets.finishLoading();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		modelBatch = new ModelBatch();
		environment = new Environment();
		batch = new SpriteBatch();
		
		ui = new UIRenderer(uiViewport, cfgl.get("KEY_REGIONS_UI"));
		player = new Player(assets);
		background = new Background(8, 8);
		map = new Minimap(uiViewport, background, player);
		
		background.renderRadius = 2;
		
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
		
		camera.position.set(player.position, 20);
		
		camera.zoom = 1f;
		camera.near = 0.1f;
		camera.far = 100f;
				
		Gdx.input.setInputProcessor(this);
		
		entities.add(new Entity(assets));
	}

	@Override
	public void render()
	{
		float dpos = 0;
		float drot = 0;
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
		
		camera.position.set(player.position, 5f);
		camera.update();
		
		if (loading)
		{
			if (ui.gameUI()) loading = false;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) dpos += 128;
		if (Gdx.input.isKeyPressed(Input.Keys.S)) dpos -= 128;
		
		if (Gdx.input.isKeyPressed(Input.Keys.A)) drot += 128;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) drot -= 128;
		
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) player.fire(0);
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) player.fire(1);
		
		player.move(dpos);
		player.direction += drot * Gdx.graphics.getDeltaTime();
		
		player.rotation = (float) Math.toDegrees(Math.atan2(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, Gdx.input.getY() - Gdx.graphics.getHeight() / 2));

		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		background.draw(batch, camera);
		batch.end();
		
		modelBatch.begin(camera);
		player.render(modelBatch, environment);
		for (Entity entity : entities)
		{
			entity.render(modelBatch, environment);
		}
		modelBatch.end();
		
		batch.begin();
		ui.draw(batch);
		map.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose()
	{
		batch.dispose();
		player.dispose();
		map.dispose();
		background.dispose();
		ui.dispose();
		
		modelBatch.dispose();
		assets.dispose();
	}
	
	private void loadConfigs()
	{
		cfgl.load("KEY_REGIONS_UI", Gdx.files.internal("data/ui/REGION_DEFS.json"));
	}
	
	@Override
	public boolean keyDown(int keycode) {return false;}
	
	@Override
    public boolean keyUp(int keycode) {return false;}

    @Override
    public boolean keyTyped(char character) {return false;}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
    	ui.handleInput(screenX, screenY);
    	return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}

    @Override
    public boolean mouseMoved(int screenX, int screenY) {return false;}

    @Override
    public boolean scrolled(float amountX, float amountY)
    {
    	camera.zoom += amountY * 0.1f;
    	if (camera.zoom >= zoomLimits[1]) camera.zoom = zoomLimits[1];
    	if (camera.zoom <= zoomLimits[0]) camera.zoom = zoomLimits[0];
        return true;
    }
}
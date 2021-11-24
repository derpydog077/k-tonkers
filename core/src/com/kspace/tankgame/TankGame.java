package com.kspace.tankgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class TankGame extends ApplicationAdapter implements InputProcessor
{
	private SpriteBatch batch;
	private Environment environment;
	private ModelBatch modelBatch;
	private Minimap map;
	private OrthographicCamera camera;
	private Player player;
	private Background background;
	
	private float[] zoomLimits = {0.5f, 3f};
	
	@Override
	public void create()
	{
		player = new Player();
		modelBatch = new ModelBatch();
		environment = new Environment();
		background = new Background(8, 8);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		map = new Minimap(background, player);
		
		background.renderRadius = 1;
		
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
		
		camera.position.set(player.position, 20);
		
		camera.zoom = 1f;
		camera.near = 0.1f;
		camera.far = 300f;
		
		Gdx.input.setInputProcessor(this);
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
		player.draw(batch);
		map.draw(batch);
		batch.end();
		
		modelBatch.begin(camera);
		player.render(modelBatch, environment);
		modelBatch.end();
		
		//batch.end();
	}
	
	@Override
	public void dispose()
	{
		batch.dispose();
		modelBatch.dispose();
		player.dispose();
		map.dispose();
		background.dispose();
	}
	
	
	@Override
	public boolean keyDown(int keycode) {return false;}
	
	@Override
    public boolean keyUp(int keycode) {return false;}

    @Override
    public boolean keyTyped(char character) {return false;}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}

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
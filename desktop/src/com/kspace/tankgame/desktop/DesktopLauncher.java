package com.kspace.tankgame.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kspace.tankgame.TankGame;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "TonkGame�";
		config.width = 1152; 
		config.height = 648;
		config.resizable = false;
		config.samples = 8;
		config.foregroundFPS = -1;
		config.vSyncEnabled = false;
		config.addIcon("data/ui/icon.png", Files.FileType.Internal);
		new LwjglApplication(new TankGame(), config);
	}
}
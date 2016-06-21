package com.mygdx.time.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.Files;
import com.mygdx.time.TimeGame;

public class DesktopLauncher {
	
	//Timestamp: Jun 20, 2016, 12:54:48 PM
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	    	config.title = "cat plant";  
	    	config.width = 1280;
	    	config.height = 720;
	    	config.addIcon("img/kittenTransparent3.png", Files.FileType.Internal);
	    	config.resizable = false;
	    	config.foregroundFPS = 60; //game engine stops working if you lag
//	    	config.vSyncEnabled = true; 
		new LwjglApplication(new TimeGame(), config);
	}
}
package com.mygdx.time.desktop;

import java.awt.geom.Area;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.time.TimeGame;

public class DesktopLauncher {
	
	//Timestamp: Jun 22, 2016, 12:02:36 PM
	//game engine stops working if you lag
	
	//https://docs.google.com/document/d/1AjTXf05EsB-Mj5XNurko3xhLNufK9FHJZLycDPozr5Y/edit design/idea doc
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	    	config.title = "cat plant";  
	    	config.width = 1280;
	    	config.height = 720;
	    	config.addIcon("img/kittenTransparent3.png", Files.FileType.Internal);
	    	config.resizable = false;
	    	
	    	config.vSyncEnabled = true; //vsync enabled by default, but can be changed in settings
	    	
		new LwjglApplication(new TimeGame(), config);
	}
	
}
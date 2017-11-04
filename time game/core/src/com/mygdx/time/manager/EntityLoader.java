package com.mygdx.time.manager;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mygdx.time.Game;

public class EntityLoader {
	public static String getValue(String entityName, String key, int type){
		String jsonString;
		String pathString = "";
		switch(type){
			case Game.MOB:
				pathString = "entities/";
				break;
			case Game.ATTACK:
				pathString = "attacks/";
				break;
			case Game.PROJECTILE:
				pathString = "projectiles/";
				break;
		}
		try {
			jsonString = FileReader.readFile(pathString + entityName + ".json");
			JSONObject jsonObject = new JSONObject(jsonString);
			return (String) jsonObject.get(key);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}catch (JSONException e) {
			return null;
		}
	}
	
	public static JSONArray getJSONArray(String entityName, String key, int type){
		String jsonString;
		String pathString = "";
		switch(type){
		case Game.MOB:
			pathString = "entities/";
			break;
		case Game.ATTACK:
			pathString = "attacks/";
			break;
		case Game.PROJECTILE:
			pathString = "projectiles/";
			break;
		}
		try {
			jsonString = FileReader.readFile(pathString + entityName + ".json");
			JSONObject jsonObject = new JSONObject(jsonString);
			return (JSONArray) jsonObject.get(key);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static short getCategory(String entityName){
		short category = 0;
		for(int i=0; i< EntityLoader.getJSONArray(entityName, "category", Game.MOB).length(); i++){
			switch(EntityLoader.getJSONArray(entityName, "category", Game.MOB).getString(i)){
				case "TERRAIN": category += Game.CATEGORY_TERRAIN; break;
				case "NONCOLLIDABLE": category += Game.CATEGORY_NONCOLLIDABLE; break;	
				case "TERRAIN_GROUND": category += Game.CATEGORY_TERRAIN_GROUND; break;
				case "TERRAIN_AERIAL": category += Game.CATEGORY_TERRAIN_AERIAL; break;
	
				case "ALLY": category += Game.CATEGORY_ALLY; break;
				case "ENEMY": category += Game.CATEGORY_ENEMY; break;
				case "ALLY_ATTACK": category += Game.CATEGORY_ALLY_ATTACK; break;
				case "ENEMY_ATTACK": category += Game.CATEGORY_ENEMY_ATTACK; break;
				
			}
		}
		return category;
	}
	
	public static short getMask(String entityName){
		short mask = 0;
		for(int i=0; i< EntityLoader.getJSONArray(entityName, "mask", Game.MOB).length(); i++){
			switch(EntityLoader.getJSONArray(entityName, "mask", Game.MOB).getString(i)){
				case "NONCOLLIDABLE": mask += Game.MASK_NONCOLLIDABLE; break;
				case "TERRAIN": mask += Game.MASK_TERRAIN; break;
						
				case "ALLY": mask += Game.MASK_ALLY; break;
				case "ENEMY": mask += Game.MASK_ENEMY; break;
				case "ALLY_ATTACK": mask += Game.MASK_ALLY_ATTACK; break;
				case "ENEMY_ATTACK": mask += Game.MASK_ENEMY_ATTACK; break;
				
				case "WARP": mask += Game.CATEGORY_WARP; break;
			}
		}
		return mask;
	}
	
	
}

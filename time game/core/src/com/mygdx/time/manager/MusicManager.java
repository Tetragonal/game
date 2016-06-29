package com.mygdx.time.manager;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.time.TimeGame;

public class MusicManager {
	
	private static Music music;
	
	private static FileHandle currentTrack, newTrack;
	private static float fadeTimer;
	private static float originalVolume;
	private static float newVolume;
	private static float duration;
	private static boolean isFadingOut = false;
	private static boolean isTransitioning = false;
	
	public static boolean isMuted = false;
	
	// Singleton: unique instance
	private static MusicManager instance;
	
	// Singleton: private constructor
	private MusicManager() {
		super();
	}
	
	// Singleton: retrieve instance
	public static MusicManager getInstance() {
		if (instance == null) {
			instance = new MusicManager();
		}
		return instance;
	}
	
	/** Disposes old track and loads new one at specified volume */
	public void setMusic(FileHandle newTrack, float volume) {
		if(!isMuted){
			if(currentTrack != null){
				TimeGame.assets.unload(currentTrack + "");
			}
			TimeGame.assets.load(newTrack + "", Music.class);
			TimeGame.assets.finishLoadingAsset(newTrack + "");
			music = TimeGame.assets.get(newTrack + "");
			music.play();
			setVolume(volume);
			currentTrack = newTrack;
			newTrack = null;
		}
	}
	
	/** Sets the old track to be faded and the new one to be faded into a specified volume over the duration.
	 *	<br> -The old track is disposed in the process. 
	 *  <br><b> -Must call update(delta) every frame. </b> */
	public void setTransitionMusic(FileHandle newTrack, float duration, float volume) {
		if(!isMuted){
			fadeTimer = 0;
			if(!newTrack.equals(currentTrack)){
				isTransitioning = true;
				MusicManager.duration = duration;
				MusicManager.newTrack = newTrack;
				if(currentTrack != null){
					//					MusicManager.newTrack = newTrack;
					isFadingOut = true;
					originalVolume = getVolume();
				}else{
					setMusic(newTrack, 0);
					fadeTimer = duration/2;
				}
				newVolume = volume;
			}
		}
	}
	
	/** Handles volume control set by calling setTransitionMusic/setFadeMusic
	 * <br> -Does nothing otherwise
	 */
	public void update(float delta){
		if(!isMuted){
			if(isTransitioning == true){ //setTransitionMusic
				fadeTimer += delta;
				if(fadeTimer < duration/2){
					setVolume(((duration/2)-fadeTimer)*(originalVolume/(duration/2)));
				}else if(fadeTimer < duration){
					if(isFadingOut){
						isFadingOut = false;
						setMusic(newTrack, 0);
					}
					setVolume((fadeTimer-(duration/2))*(newVolume/(duration/2)));
				}else{
					setVolume(newVolume);
					isFadingOut = false;
					isTransitioning = false;
				}
			}else if (isFadingOut){ //setFadeMusic
				fadeTimer += delta;
				if(fadeTimer < duration){
					setVolume(MathUtils.clamp(((duration/2)-fadeTimer)*(originalVolume/(duration/2)), 0, 1));
				}else{
					currentTrack = null;
					isFadingOut = false;
				}
			}
		}else if(music != null && currentTrack != null){
			isFadingOut = false;
			isTransitioning = false;
			fadeTimer = 0;
			duration = 0;
			
			TimeGame.assets.unload(currentTrack + "");
			music = null;
		}
	}
	
	public void setFadeMusic(float duration){ //overrides setTransitionMusic
		fadeTimer = 0;
		isFadingOut = true;
		isTransitioning = false;
		MusicManager.duration = duration;
		originalVolume = getVolume();
	}
	
	public void setVolume(float volume){
		if(music != null){
			music.setVolume(volume);
		}
	}
	
	public float getVolume(){
		if(music != null){
			return music.getVolume();
		}else return 0;
	}
	
	public void pauseMusic(){
		music.pause();
	}
	
	public void dispose(){
		if(music != null){
			music.dispose();
		}
	}
}

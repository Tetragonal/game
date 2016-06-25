package com.mygdx.time.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public class MusicManager {
	
	private static Music music;
	
	private static FileHandle currentTrack;
	private static float fadeTimer;
	private static float originalVolume;
	private static float newVolume;
	private static float duration;
	private static boolean isFadingOut = false;
	private static boolean isTransitioning = false;
	
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
	
	/** Disposes old track and loads new one */
	public void setMusic(FileHandle newTrack) {
		if(currentTrack != null){
			music.dispose();
			music = Gdx.audio.newMusic(newTrack);
			music.play();
		}else{
			music = Gdx.audio.newMusic(newTrack);
			music.play();
		}
		currentTrack = newTrack;
	}
	
	/** Disposes old track and loads new one at specified volume */
	public void setMusic(FileHandle newTrack, float volume) {
		if(currentTrack != null){
			music.dispose();
			music = Gdx.audio.newMusic(newTrack);
			music.play();
		}else{
			music = Gdx.audio.newMusic(newTrack);
			music.play();
		}
		setVolume(volume);
		currentTrack = newTrack;
	}
	
	/** Sets the old track to be faded and the new one to be faded in over the duration.
	 *	<br> -The old track is disposed in the process. 
	 *  <br><b> -Must call update(delta) every frame. </b> */
	public void setTransitionMusic(FileHandle newTrack, float duration) {
		fadeTimer = 0;
		if(!newTrack.equals(currentTrack)){ 
			isTransitioning = true;
			MusicManager.duration = duration;
			newVolume = originalVolume;
			if(currentTrack != null){
				currentTrack = newTrack;
				isFadingOut = true;
				originalVolume = getVolume();
				newVolume = originalVolume;
			}else{
				setMusic(newTrack, 0);
				fadeTimer = duration/2;
				newVolume = 1;
			}
		}
	}
	
	/** Sets the old track to be faded and the new one to be faded into a specified volume over the duration.
	 *	<br> -The old track is disposed in the process. 
	 *  <br><b> -Must call update(delta) every frame. </b> */
	public void setTransitionMusic(FileHandle newTrack, float duration, float volume) {
		fadeTimer = 0;
		System.out.println(!newTrack.equals(currentTrack));
		if(!newTrack.equals(currentTrack)){
			isTransitioning = true;
			MusicManager.duration = duration;
			if(currentTrack != null){
				currentTrack = newTrack;
				isFadingOut = true;
				originalVolume = getVolume();
			}else{
				setMusic(newTrack, 0);
				fadeTimer = duration/2;
			}
			newVolume = volume;
		}
	}
	
	/** Handles volume control set by calling setTransitionMusic/setFadeMusic
	 * <br> -Does nothing otherwise
	 */
	public void update(float delta){
		if(isTransitioning == true){ //setTransitionMusic
			fadeTimer += delta;
			if(fadeTimer < duration/2){
				setVolume(((duration/2)-fadeTimer)*(originalVolume/(duration/2)));
			}else if(fadeTimer < duration){
				if(isFadingOut){
					isFadingOut = false;
					setMusic(currentTrack, 0);
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
	}
	
	public void setFadeMusic(float duration){ //overrides setTransitionMusic
		fadeTimer = 0;
		isFadingOut = true;
		isTransitioning = false;
		MusicManager.duration = duration;
		originalVolume = getVolume();
	}
	
	public void setVolume(float volume){
		music.setVolume(volume);
	}
	
	public float getVolume(){
		return music.getVolume();
	}
	public void pauseMusic(){
		music.pause();
	}
	
	public void dispose(){
		music.dispose();
	}
}

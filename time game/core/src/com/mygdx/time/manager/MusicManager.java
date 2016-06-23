package com.mygdx.time.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicManager {
	
	private static Music music;
	
	private static FileHandle currentTrack;
	private static float fadeTimer;
	private static float originalVolume;
	private static float newVolume;
	private static float duration;
	private static boolean isFadingOut = false;
	private static boolean isFadingIn = false;
	
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
	
	/** Stops old track and starts new one 
	 *	<br> -The old track is disposed. */
	public void setMusic(FileHandle newTrack) {
		if(music != null){
			music.dispose();
			music = Gdx.audio.newMusic(newTrack);
			music.play();
		}else{
			music = Gdx.audio.newMusic(newTrack);
			music.play();
		}
		currentTrack = newTrack;
	}
	
	/** Stops old track and starts new one at specified volume */
	public void setMusic(FileHandle newTrack, float volume) {
		if(music != null){
			music.dispose();
			music = Gdx.audio.newMusic(newTrack);
			music.play();

			setVolume(volume);
		}else{
			music = Gdx.audio.newMusic(newTrack);
			music.play();
		}
		currentTrack = newTrack;
	}
	
	/** Sets the old track to be faded and the new one to be faded in over the duration.
	 *	<br> -The old track is disposed. 
	 *  <br><b> -Must call update(delta) every frame. </b> */
	public void setFadeMusic(FileHandle newTrack, float duration) {
		fadeTimer = 0;
		if(!newTrack.equals(currentTrack)){ 
			MusicManager.duration = duration;
			newVolume = originalVolume;
			if(music != null){
				currentTrack = newTrack;
				isFadingOut = true;
				originalVolume = getVolume();
				newVolume = originalVolume;
			}else{
				setMusic(newTrack, 0);
				fadeTimer = duration/2;
				isFadingIn = true;
				newVolume = 1;
			}
		}
	}
	
	/** Sets the old track to be faded and the new one to be faded into a specified volume over the duration.
	 *	<br> -The old track is disposed. 
	 *  <br><b> -Must call update(delta) every frame. </b> */
	public void setFadeMusic(FileHandle newTrack, float duration, float volume) {
		fadeTimer = 0;
		if(!newTrack.equals(currentTrack)){
			MusicManager.duration = duration;
			if(music != null){
				currentTrack = newTrack;
				isFadingOut = true;
				originalVolume = getVolume();
			}else{
				setMusic(newTrack, 0);
				fadeTimer = duration/2;
				isFadingIn = true;
			}
			newVolume = volume;
		}
	}
	
	/** Handles volume control set by calling setFadeMusic
	 * <br> -Does nothing otherwise
	 */
	public void update(float delta){
		if(isFadingOut || isFadingIn){
			fadeTimer += delta;
			if(fadeTimer < duration/2){
				setVolume(((duration/2)-fadeTimer)*(originalVolume/(duration/2)));
			}else if(fadeTimer < duration){
				if(isFadingOut){
					isFadingOut = false;
					isFadingIn = true;
					setMusic(currentTrack, 0);
				}
				setVolume((fadeTimer-(duration/2))*(newVolume/(duration/2)));
			}else{
				setVolume(newVolume);
				isFadingIn = false;
				isFadingOut = false;
			}
		}
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

package com.zora.play;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;

public class PlayOperation extends MediaPlayer{
	private MediaPlayer media;

	PlayOperation() {
		this.media = new MediaPlayer();
	}

	public void newPlay(File f) {

		if (media.isPlaying()) {
			media.reset();// ÷ÿ÷√Œ™≥ı º◊¥Ã¨
		}
		try {

			media.setDataSource(f.getPath());
			media.prepare();
			media.start();
		} catch (Exception e) {
		}

	}

	public void pause() {
		if (media.isPlaying())
			media.pause();
	}

	public void repeat(int start,int end) {
        media.seekTo(start);
        while(end!=media.getCurrentPosition())
        {
        	try {
				media.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	media.start();
        	
        	
        }
	}

	public void continuePlay() {
		if (media != null)
			media.start();
	}

	protected void onDestroy() {
		if (media != null)
			media.release();
            
	}
	
	
}

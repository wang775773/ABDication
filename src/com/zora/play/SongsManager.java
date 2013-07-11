package com.zora.play;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;

public class SongsManager {
	public static String FirstFolder = "RepeatPlayer";// 一级目录

	public static String SecondFolder = "SongFiles";// 二级目录
    public final static String FIRST_PATH=Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ java.io.File.separator
			+ FirstFolder;
    public final static String SECOND_PATH=FIRST_PATH+java.io.File.separator + SecondFolder+java.io.File.separator;
	// SDCard Path
	final String MEDIA_PATH =SECOND_PATH;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	// Constructor
	public SongsManager(){
		
	}
	 
	public String getSecondPath()
	{
		return SECOND_PATH;
	}
	 
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList(){
		File home = new File(MEDIA_PATH);

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getAbsolutePath());
				
				// Adding each song to SongList
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}

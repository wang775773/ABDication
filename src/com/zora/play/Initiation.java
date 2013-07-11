package com.zora.play;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class Initiation {

	public static String FirstFolder = "RepeatPlayer";// 一级目录

	public static String SecondFolder = "SongFiles";// 二级目录
    public final static String FIRST_PATH=Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ java.io.File.separator
			+ FirstFolder;
    public final static String SECOND_PATH=FIRST_PATH+java.io.File.separator + SecondFolder;
    
	public void createDirectory() {

		File file = new File(SECOND_PATH);
		if (!file.exists())
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
				// TODO: handle exception
			}
		File dir = new File(FIRST_PATH+ "/version" + ".dat");  
        if (!dir.exists()) {  
              try {  
                  //在指定的文件夹中创建文件  
                  dir.createNewFile();  
            } catch (Exception e) {  
            }  
        }  
	}

}

package com.zora.play;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class Initiation {

	public static String FirstFolder = "RepeatPlayer";// һ��Ŀ¼

	public static String SecondFolder = "SongFiles";// ����Ŀ¼
    public final static String FIRST_PATH=Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ java.io.File.separator
			+ FirstFolder;
    public final static String SECOND_PATH=FIRST_PATH+java.io.File.separator + SecondFolder;
    
	public void createDirectory() {

		File file = new File(SECOND_PATH);
		if (!file.exists())
			try {
				// ����ָ����·�������ļ���
				file.mkdirs();
			} catch (Exception e) {
				// TODO: handle exception
			}
		File dir = new File(FIRST_PATH+ "/version" + ".dat");  
        if (!dir.exists()) {  
              try {  
                  //��ָ�����ļ����д����ļ�  
                  dir.createNewFile();  
            } catch (Exception e) {  
            }  
        }  
	}

}

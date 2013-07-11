package com.zora.play;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;

import com.zora.repeatplayer.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {
	private TextView songContent, songCurrentDuration, songTotalDuration;
	private ImageButton playList, playbtn, repeater;
	private ImageView repeatPointA,repeatPointB;
	private SeekBar songBar;
	private Initiation init;
	private MediaPlayer mp;
	private Handler mHandler = new Handler();
	private SongsManager songManager;
	private Utilities utils;
	private int currentSongIndex = 0;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private int repeatState = 0;
	private int startA = -1;
	private int endB = -1;
	private int windowWidth=0;
	private int windowHeight=0;
	private int flagPaddingBottom=0;
	private int flagPaddingLeft=0;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_view);
		
		initicate();
		click();
	}

	private void initicate() {
		DisplayMetrics metric = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(metric);  
		windowWidth= metric.widthPixels;     // 屏幕宽度（像素）  
		windowHeight = metric.heightPixels;
		songContent = (TextView) findViewById(R.id.songContentView);
		songCurrentDuration = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDuration = (TextView) findViewById(R.id.songTotalDurationLabel);
		playList = (ImageButton) findViewById(R.id.btnPlayList);
		playbtn = (ImageButton) findViewById(R.id.btnPlay);
		repeater = (ImageButton) findViewById(R.id.btnRepeat);
		songBar = (SeekBar) findViewById(R.id.songProgressBar);
		repeatPointA=(ImageView)findViewById(R.id.repeatPointA);
		repeatPointB=(ImageView)findViewById(R.id.repeatPointB);
		flagPaddingBottom=repeatPointB.getPaddingBottom();
		flagPaddingLeft=repeatPointB.getPaddingLeft();
		init = new Initiation();
		songManager = new SongsManager();
		songsList = songManager.getPlayList();
		mp = new MediaPlayer();
		utils = new Utilities();
		songContent.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		songContent.setScrollbarFadingEnabled(false);
	}


	public void click() {
		
		songBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

			}

			/**
			 * When user starts moving the progress handler
			 * */
			public void onStartTrackingTouch(SeekBar seekBar) {
				mHandler.removeCallbacks(mUpadateTimeTask);

			}

			/**
			 * When user stops moving the progress handler
			 * */

			public void onStopTrackingTouch(SeekBar seekBar) {
				mHandler.removeCallbacks(mUpadateTimeTask);
				int totalDuration = mp.getDuration();
				int currentPosition = utils.progressToTimer(
						seekBar.getProgress(), totalDuration);
				mp.seekTo(currentPosition);
				updateProgressBar();

			}

		});
		playList.setOnClickListener(new ImageButton.OnClickListener() {

			public void onClick(View v) {
				checkCardState();
				init.createDirectory();
				// new Intent(当前页面，要跳转的页面)
				Intent intent = new Intent(PlayActivity.this,
						PlayListActivity.class);
				startActivityForResult(intent, 100);

			}

		});

		playbtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// check for already playing
				if (mp.isPlaying()) {
					if (mp != null) {
						mp.pause();
						// Changing button image to play button
						playbtn.setImageResource(R.drawable.btn_play);
					}
				} else {
					// Resume song
					if (mp != null) {
						mp.start();
						// Changing button image to pause button
						playbtn.setImageResource(R.drawable.btn_pause);
					}
				}

			}
		});

		repeater.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				switch (repeatState) {
				// 还没有选择复读,点击后选择起点A
				case 0: {
					repeater.setImageResource(R.drawable.player_repeater_a);
					repeatState = 1;
					startA = mp.getCurrentPosition();
					setFlagABPosition(repeatPointA);
					


					break;

				}
				// 已经选点了起点A，再点击选择终点B
				case 1: {
					repeater.setImageResource(R.drawable.player_repeater_a_b);
					endB = mp.getCurrentPosition();
					repeatState = 2;
					setFlagABPosition(repeatPointB);
					repeatAtoB();

					break;
				}
				// 再次点击，结束复读
				case 2: {
					repeater.setImageResource(R.drawable.player_repeater_none);
					repeatState = 0;
					repeatPointA.setVisibility(View.GONE);
					repeatPointB.setVisibility(View.GONE);
					break;
				}
				}

			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			currentSongIndex = data.getExtras().getInt("songIndex");
			// play selected song
			playSong(currentSongIndex);
			showContent(currentSongIndex);
		}

	}
	
	/**
	 * 
	 * */
	public void setFlagABPosition(ImageView a)
	{   
		
		int progress=(int) (windowWidth*((1.0*mp.getCurrentPosition())/mp.getDuration()));
		Log.v("assss",progress+"");
		 a.setPadding(  flagPaddingLeft+progress,   a.getPaddingTop(),  
				 a.getPaddingRight(),  flagPaddingBottom-10);
		 a.setVisibility(View.VISIBLE);
		
		
	}

	/**
	 * Function to play a song
	 * 
	 * @param songIndex
	 *            - index of song
	 * */
	public void playSong(int songIndex) {
		// Play song
		try {
			mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			mp.prepare();
			mp.start();
			// Displaying Song title
			String songTitle = songsList.get(songIndex).get("songTitle");
			// songTitleLabel.setText(songTitle);
		
			

			// Changing Button Image to pause image
			playbtn.setImageResource(R.drawable.btn_pause);

			// set Progress bar values
			songBar.setProgress(0);
			songBar.setMax(100);

			// Updating progress bar
			updateProgressBar();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showContent(int songIndex){
		String f=songsList.get(songIndex).get("songTitle")+".txt";
		String fileName=songManager.getSecondPath()+f;
		String res="无对应的文本";
		try{ 

			FileInputStream fin = new FileInputStream(fileName);

			//FileInputStream fin = openFileInput(fileName);  

			//用这个就不行了，必须用FileInputStream

			    int length = fin.available(); 

			    byte [] buffer = new byte[length]; 

			    fin.read(buffer);     

			   res = EncodingUtils.getString(buffer, "UTF-8"); 

			    fin.close();     

			    }catch(Exception e){ 

			           e.printStackTrace(); 

			} 

			
			songContent.setText(res);
		
		
	}

	/**
	 * Background Runnable thread
	 * */

	private Runnable mUpadateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mp.getDuration();
			long currentDuration = mp.getCurrentPosition();
			// 显示当前时间和总时间
			songCurrentDuration.setText(""
					+ utils.milliSecondsToTimer(currentDuration));
			songTotalDuration.setText(""
					+ utils.milliSecondsToTimer(totalDuration));
			// Updating progress bar
			int progress = (int) (utils.getProgressPercentage(currentDuration,
					totalDuration));
			songBar.setProgress(progress);
			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);

		}
	};

	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpadateTimeTask, 100);
	}

	/**
	 * Repeat from pointA to pointB
	 * */
	public void repeatAtoB() {

		if (repeatState == 2) {
			mp.seekTo(startA);
			int duration = endB - startA;
			CountDownTimer timer = new CountDownTimer(duration, 1000) {
                
				@Override
				public void onTick(long millisUntilFinished) {

				}

				@Override
				public void onFinish() {

					repeatAtoB();

				}

			};
			timer.start();
		}

	}

	/**
	 * 检查SD卡状态
	 * */
	public void checkCardState() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			/**
			 * getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
			 * Activity.this的context 返回当前activity的上下文，属于activity ，activity
			 * 摧毁他就摧毁 getBaseContext() 返回由构造函数指定或setBaseContext()设置的上下文
			 * this.getApplicationContext（）取的是这个应
			 * 用程序的Context，Activity.this取的是这个Activity的Context，这两者的生命周期是不同
			 * 的，前者的生命周期是整个应用，后者的生命周期只是它所在的Activity
			 * */
			Toast.makeText(PlayActivity.this, "请插入SD存储卡", Toast.LENGTH_SHORT)
					.show();

		}
	}

}

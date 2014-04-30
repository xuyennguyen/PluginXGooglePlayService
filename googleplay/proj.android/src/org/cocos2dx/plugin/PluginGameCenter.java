package org.cocos2dx.plugin;

import java.util.Hashtable;



import com.gamesv.library.googleplayservice.GameCenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class PluginGameCenter implements InterfaceSocial {
	
	private static final String TAG = "PluginGameCenter";
	private static Activity mActivity = null;
	protected static boolean bDebug = true;
	private static InterfaceSocial mSocialAdapter = null;
	private static String LEADERBOARDID="";
	private static String ACHIEVEMENTSID="";
	private static boolean isInitialized = false;
	private static Hashtable<String, String> mShareInfo = null;
	private  static int REQUEST_ACHIEVEMENTS = 0;
	private  static int REQUEST_LEADERBOARD = 0;
	private  static int PERCENT_COMPLETE = 0;
	private  static int SCORE = 0;
	private static 	GameCenter mGame;
	protected static void LogE(String msg, Exception e)
	{
		Log.e(TAG, msg, e);
		e.printStackTrace();
	}

	protected static void LogD(String msg)
	{
		if (bDebug) {
			Log.d(TAG, msg);
		}
	}
	public PluginGameCenter ( Context context)
	{
		mActivity	 = (Activity)context;
		mSocialAdapter = this;
		
	}
	@Override
	public void configDeveloperInfo(Hashtable<String, String> cpInfo) {
		
		LogD("init DeveloperInfo invoked " + cpInfo.toString());
		Log.d(TAG, "call conficDev ");	
		try 
		{
			if ( isInitialized ){
				return;
			}
			Log.d(TAG, "call conficDev 2");
			isInitialized = true;
			PluginWrapper.runOnMainThread( new Runnable() {
				
				@Override
				public void run() {
					mActivity = (Activity) PluginWrapper.getContext();				
					mGame = new GameCenter(mActivity , REQUEST_ACHIEVEMENTS);
					Log.d(TAG, "call conficDev 3 ");
					
				}
			});	
			
			
		} catch ( Exception e ) {
			LogE(" Developer info is wrong !", e);
		}
		
		
	}
	public static GameCenter getGameCenter(){
		return mGame;
	}

	@Override
	public void share(Hashtable<String, String> cpInfo) {
		
		LogD("share invoked " + cpInfo.toString());
		final String type = cpInfo.get("type");
		LogD("type = " + type);
		PluginGameCenter.ACHIEVEMENTSID = cpInfo.get("achivements");
		PluginGameCenter.LEADERBOARDID = cpInfo.get("leaderboardid");
		PluginGameCenter.REQUEST_ACHIEVEMENTS = Integer.parseInt(cpInfo.get("requestachivements"));
		PluginGameCenter.REQUEST_LEADERBOARD = Integer.parseInt(cpInfo.get("requestleaderboar"));
		PluginGameCenter.SCORE = Integer.parseInt(cpInfo.get("score"));
		PluginGameCenter.PERCENT_COMPLETE = Integer.parseInt(cpInfo.get("percent"));
		
		
		Log.d(TAG, "share nay");
		if (! networkReachable()) {
			shareResult(SocialWrapper.SHARERESULT_FAIL, "Network error!");
			return;
		}

		if (! isInitialized) {
			shareResult(SocialWrapper.SHARERESULT_FAIL, "Initialize failed!");
			return;
		}
		PluginWrapper.runOnMainThread( new Runnable() {
			
			@Override
			public void run() {
				Log.d(TAG, "goi vao ham post achiventments");
				ManagerGameCenter(type);
				
			}
		});
		
	}
	
	// Check network
	
	private boolean networkReachable() {
		boolean bRet = false;
		try {
			ConnectivityManager conn = (ConnectivityManager) mActivity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = conn.getActiveNetworkInfo();
			bRet = (null == netInfo) ? false : netInfo.isAvailable();
		} catch (Exception e) {
			LogE("Fail to check network status", e);
		}
		LogD("NetWork reachable : " + bRet);
		return bRet;
	}
	
	private static void shareResult(int ret, String msg) {
		SocialWrapper.onShareResult(mSocialAdapter, ret, msg);
		LogD("SocialFacebook result : " + ret + " msg : " + msg);
	}
	
	// login
	
	public void onLogin()
	{
			mGame.signIn();
			
	}
	// Logout 
	
	public void onLogout()
	{
		mGame.signOut();
	}
	
	 // show achievenments
	
	public void showAchievenments()
	{
		if (mGame.showAchievements(PluginGameCenter.REQUEST_ACHIEVEMENTS) == false )
		{
			Log.d(TAG, "Chua dang nhap ");
		}
		else
		{
			Log.d(TAG, "show thanh cong");
		}
	}
	
	 // post achievenments
	
	public void postAchievenments()
	{
		mGame.postAchievement(ACHIEVEMENTSID, PERCENT_COMPLETE);
	}
	
	// show score 
	public void showScore()
	{
		if ( mGame.showScores(REQUEST_LEADERBOARD) == false)
		{
			Log.d(TAG, "show score failed");
		}
		else 
		{
			Log.d(TAG, "show score success");
		}
	}
	//post score
	
	public void postScore()
	{
		mGame.postScore(LEADERBOARDID, SCORE);
	}
	 // Clear all achievements 
	
	public void clearAchievements()
	{
		mGame.clearAllAchievements();
	}
	
	public  void onActivityResult ( int requestCode , int resultCode , Intent data )
	{
		mGame.onActivityResult(requestCode, resultCode, data);
	}
	
	protected class PostScore extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			LogD("show score");
				postScore();
		}
	}
	protected class ShowScore extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			LogD("show score");
				showScore();
		}
	}
	protected class PostAchievenments extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			LogD("post achievements");
				postAchievenments();
		}
	}
	protected class ShowAchievenments extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			onLogin();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			LogD("show achieventments");
				showAchievenments();
		}
	}
	public void ManagerGameCenter( String type)
	{
		Log.d(type, "manager game center");
		if (type.equals("postscore")){
			new PostScore().execute();
		}
		else if ( type.equals("showscore")){
			new ShowScore().execute();
		}
		else if ( type.equals("postachievenments")){
			Log.d(type, "manager game center 2");
			new PostAchievenments().execute();
		}
		else if ( type.equals("showachievenments")){
			new ShowAchievenments().execute();
		}
		else if ( type.equals("login")){
			onLogin();
		}
		else if (type.equals("logout")){
			onLogout();
		}
		else if ( type.equals("clearachivements")){
			clearAchievements();
		}
	}
	@Override
	public void setDebugMode(boolean debug) 
	{
		bDebug = debug;
		
	}

	@Override
	public String getSDKVersion() 
	{
		return "2.0";
	}

	@Override
	public String getPluginVersion()
	{
		return "1.0";
	}
	

}

package com.gamesv.library.googleplayservice;





import com.google.android.gms.games.Games;
import com.gamesv.library.googleplayservice.GameHelper;
import com.gamesv.library.googleplayservice.GameHelper.GameHelperListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.AbsListView;

public class GameCenter implements GameHelper.GameHelperListener {
	
	protected static GameHelper mHelper  = null;
	public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
	public static final int CLIENT_APPSTATE = GameHelper.CLIENT_APPSTATE;
	public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;
	public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
	protected static int mRequestedClients = CLIENT_GAMES;
	protected boolean mDebugLog = false;
	public static  Activity mActivity = null;
	private static int REQUEST_ACHIEVEMENTS  = 0;
	private  static int REQUEST_LEADERBOARD  = 0;
	public static String TAG = "GameCenter";
	public static String leaderBoard = "";
	
	public GameCenter (  Context mContext ,  int clientsToUse )
	{
		this.mActivity = (Activity) mContext;
	}
	
	public static  GameHelper getGameHelper() {
		if (mHelper == null) {
			mHelper = new GameHelper(mActivity, mRequestedClients);
			mHelper.enableDebugLog(true);
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mHelper.setup(new GameHelperListener() {
						@Override
						public void onSignInSucceeded() {
							Log.d(TAG, " dang nhap thanh cong ");
						}

						@Override
						public void onSignInFailed() {
							Log.d(TAG, " dang nhap that bai");

						}
					});
				}
			});

		}
		;
		return mHelper;
	}
	
	/**
	 * sign in 
	 */
	public  void signIn() {
		
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				getGameHelper().beginUserInitiatedSignIn();

			}
		});
	}
	 
	 public  void signOut()
	 {
		 if ( mHelper == null ) {
				return;
		 }
		 mActivity.runOnUiThread( new Runnable() {
			
			@Override
			public void run() {
				getGameHelper().signOut();			
			}
		});
		 
	 }
	 
	 // check signed in
	 public boolean isSignedIn()
	 {
		 return ( getGameHelper().isSignedIn());		 
	 }
	 
	// show achievenments
	 public  boolean showAchievements(int idAchivements){
		 REQUEST_ACHIEVEMENTS = idAchivements;
		 if ( !isSignedIn() ){
			 return false;
		 }
		 mActivity.runOnUiThread( new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mActivity.startActivityForResult(Games.Achievements.getAchievementsIntent(getGameHelper().getApiClient()),
						REQUEST_ACHIEVEMENTS);		
			}
		});
		 return true;
	 }
	 
	 // post achievement 
	 public  void postAchievement ( final String idName , int percentComplete) {
		 if ( !isSignedIn() || percentComplete < 100  ) {
			 return;
		 } 
		 mActivity.runOnUiThread( new Runnable() {
			
			@Override
			public void run() {	
				
			Games.Achievements.unlock(getGameHelper().getApiClient(), idName );
				
			}
		});
		 
	 }
	 private  int getResourceId(String idName)
	 {
	        int rId = mActivity.getResources().getIdentifier(idName, "string", mActivity.getPackageName());
	        if (rId == 0) {
	            Log.v(TAG, "Unable to find resource ID for string " + idName);
	        }
	        return rId;

	  }
	 
	 public void clearAllAchievements()
	 {
		 Log.d(TAG , "clear all achievements is not avialable on this platform");
	 }
	 
	 // show scores
	 
	 public  boolean showScores(final int idBoard)
	 {
		 REQUEST_LEADERBOARD = idBoard;
		 if ( !isSignedIn() ) {
			 return false;
		 }
		 mActivity.runOnUiThread( new Runnable() {
			
			@Override
			public void run() {
				
				mActivity.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getGameHelper().getApiClient()), REQUEST_LEADERBOARD);
			}
		});
		 return true;
	 }
	 
	 public  void postScore(final String idName, final int score){
		
		 if ( !isSignedIn()){
			 return;
		 }
		 Log.d("package name ", mActivity.getPackageName() );
		 mActivity.runOnUiThread( new Runnable() {
			
			@Override
			public void run() {
				 Log.d("postsocre", "call post score 2");
				Games.Leaderboards.submitScore(getGameHelper().getApiClient(), idName, score);
				Log.d("postsocre", "post thanh cong");
			}
		});	 
	 }
	 
	 public  void onStar()
	 {
		 getGameHelper().onStart(mActivity);
	 }
	 
	 public  void onStop()
	 {
		 if ( mHelper == null ){
			 return ;
		 }
		 
		 getGameHelper().onStop();
	 }
	 
	 public  void onActivityResult ( int requestCode , int resultCode , Intent data )
	 {
		 if ( mHelper == null ) {
			 return;
		 }
		 getGameHelper().onActivityResult(requestCode, resultCode, data);
	 }
	 
	@Override
	public void onSignInFailed() 
	{
		Log.d(TAG, "signin failed try again");
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getGameHelper().beginUserInitiatedSignIn();
			}
		});
		
	}
	@Override
	public void onSignInSucceeded() {
		Log.d(TAG, "Sign in successed cc");
		
	}
	
}

package dev.shoulongli.picasaplus.auth;

import java.io.IOException;

import org.apache.http.HttpRequest;

import com.google.api.client.googleapis.GoogleTransport;




import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class PicasaAuthenticatorAccountManager implements PicasaAuthenticator
{
	
	/** The authentication token type used for Client Login. */
	public static final String AUTH_TOKEN_TYPE = "lh2";


	public static final int AUTH_ERROR = -102;

	public static final String ACTIVE_ACCOUNT = "aq.account";

	private Activity activity;
	private AccountManager accountManager;
	private PicasaAuthCallback callback;
	private String authToken;
	private Account[] accs;
	private Account acc;
	public PicasaAuthenticatorAccountManager(Activity activity, PicasaAuthCallback callback)
	{
		this.activity = activity;
		accountManager = AccountManager.get(activity);
		this.callback = callback;  
	}

//	private void gotAccount(AccountManager manager, Account account) 
//	{
//		SharedPreferences settings = activity.getSharedPreferences(PREF, 0);
//		SharedPreferences.Editor editor = settings.edit();
//		editor.putString("accountName", account.name);
//		editor.commit();
//		this.auth(account);
//	}
	//	@Override
	//	public void onClick(DialogInterface dialog, int which) 
	//	{
	//		Account acc = accs[which];
	//		setActiveAccount(activity, acc.name);		
	//		auth(acc);
	//	}
	//	@Override
	//	public void onCancel(DialogInterface dialog) 
	//	{		
	//		if(this.callback != null)
	//			callback.failure(AUTH_ERROR, "cancel");
	//	}
	public static void setActiveAccount(Context context, String account)
	{
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(ACTIVE_ACCOUNT, account).commit();		
	}

	public static String getActiveAccount(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context).getString(ACTIVE_ACCOUNT, null);
	}
	@Override
	public GoogleTransport getTransport()
	{
		return null;
	}
	@Override
	public void auth(boolean tokenExpired)
	{
		String accountName = getActiveAccount(activity);
		if(accountName == null)
		{
			accountDialog();
		}
		else{
			Account[] accounts = accountManager.getAccountsByType("com.google");
			for(int i = 0; i < accounts.length; i++) 
			{
				Account account = accounts[i];
				if(accountName.equals(account.name)) 
				{
					if (tokenExpired) 
					{
						accountManager.invalidateAuthToken("com.google", this.authToken);
					}
					auth(account);
					return;
				}
			}
		}
	}
	private void auth(Account account){

		this.acc = account;

		Task task = new Task();
		task.execute();
	}
	
	private void accountDialog() 
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Select a Google account");
		//builder.setTitle("Select a Google account");
		accs = accountManager.getAccountsByType("com.google");
		final int size = accs.length;

		//FIXME
//		if(size == 1)
//		{
//			setActiveAccount(activity, accs[0].name);
//			auth(accs[0]);
//		}		
//		else
		{

			String[] names = new String[size];
			for(int i = 0; i < size; i++) 
			{
				names[i] = accs[i].name;
			}
			builder.setItems(names, new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					setActiveAccount(activity, accs[which].name);
					auth(accs[which]);

				}
			});

			AlertDialog dialog = builder.create();//.show();
			dialog.show();
		}
	}

	public boolean expired(int status) 
	{
		return status == 401 || status == 403;
	}
	@Override
	public void applyToken(HttpRequest request) 
	{
		request.addHeader("Authorization", "GoogleLogin auth=" + authToken);
	}
	public String getCacheUrl(String url){
		return url + "#" + authToken;
	}


	public boolean isAuthenticated() {
		return this.authToken != null;
	}
	private class Task extends AsyncTask<String, String, Bundle>
	{

		@Override
		protected Bundle doInBackground(String... params) 
		{

			AccountManagerFuture<Bundle> future = accountManager.getAuthToken(acc, AUTH_TOKEN_TYPE, null, activity, null, null);

			Bundle bundle = null;

			try 
			{
				bundle = future.getResult();
			} catch (OperationCanceledException e) {
			} catch (AuthenticatorException e) {
			} catch (IOException e) {
			}

			return bundle;
		}


		@Override
		protected void onPostExecute(Bundle bundle)
		{
//			if (bundle.containsKey(AccountManager.KEY_INTENT))
//			{
//				//FIXME
//				//				Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
//				//				int flags = intent.getFlags();
//				//				flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;
//				//				intent.setFlags(flags);
//			}
//			else 
			if(bundle != null && bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) 
			{
				authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
				if(callback != null)
					callback.authenticated();
			}
			else
			{
				if(callback != null)
					callback.failure(AUTH_ERROR, "rejected");
			}

		}

	}

}

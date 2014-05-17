package activities;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snagtag.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class LoginActivity extends Activity {
    private final String TAG = LoginActivity.class.getSimpleName();
    private Button fbLoginButton;
    private Button loginButton;
    private Button register;
    private Button skip;
    private Dialog progressDialog;
    
    private EditText username;
    private EditText password;
    private String mUserEmail;
    private String mPassword;
    private TextView mErrorMessage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.loginUsername);
        password = (EditText) findViewById(R.id.loginPassword);
        mErrorMessage = (TextView) findViewById(R.id.errorMessage);
        
        // Fetch Facebook user info if the session is active
        fbLoginButton = (Button) findViewById(R.id.fbLoginButton);
		fbLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onFBLoginButtonClicked();
			}
		});
		
		register = (Button) findViewById(R.id.regButton);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View V) {
				Intent i = new Intent(LoginActivity.this, RegisterNewAccountActivity.class);
				startActivity(i);
			}
		});
		
		loginButton = (Button) findViewById(R.id.logButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View V) {
				onLoginButtonClicked();
			}
		});
		
		skip = (Button) findViewById(R.id.colors);
		skip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View V) {
				Intent i = new Intent(LoginActivity.this, CartDrawerActivity.class);
				startActivity(i);
			}
		});
		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
		    Intent intent = new Intent(this, CartDrawerActivity.class);
		    startActivity(intent);
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG,"onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
    
    /**
     * Handles regular Login
     */
    private void onLoginButtonClicked() {
        LoginActivity.this.progressDialog = ProgressDialog.show(
                LoginActivity.this, "", "Logging in...", true);
    		if(validateFields()) {
    				mUserEmail = username.getText().toString();
    			    mPassword =  password.getText().toString();
    			if (TextUtils.isEmpty(mUserEmail) || TextUtils.isEmpty(mPassword)) {
    				mErrorMessage.setText("Please enter a valid username and password.");
    			} else {
    				//showProgress();
    				userLogin();
    			}
    		}
    		else {
    			Toast.makeText(getApplicationContext(), "Please Insert Username and Password", Toast.LENGTH_SHORT).show();
    		}
    	}

//TODO: set the limits on sizes of username length and password length
    	private boolean validateFields() {
    		if (username.length()>0 && password.getText().length()>0) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	/**
    	 * Handles parse user login
    	 */
    	public void userLogin() {
    		ParseUser.logInInBackground(mUserEmail, mPassword, new LogInCallback() {
    			  public void done(ParseUser user, ParseException e) {
    			    if (user != null) {
    	    			Toast.makeText(getApplicationContext(), "Welcome, "+user.getString("fname"), Toast.LENGTH_SHORT).show();
    	    			Intent i = new Intent(LoginActivity.this, CartDrawerActivity.class);
    	    			startActivity(i);
    			    } else {
    			      // Signup failed. Look at the ParseException to see what happened.
    	    			Toast.makeText(getApplicationContext(), "Login failed please try again", Toast.LENGTH_SHORT).show();	
    			    }
    			  }
    			});
    	}
    /**
     * Handles facebook login request.
     */
    private void onFBLoginButtonClicked() {
        Log.i(TAG,"onLoginButtonClicked");
        LoginActivity.this.progressDialog = ProgressDialog.show(
                LoginActivity.this, "", "Logging in...", true);
        List<String> permissions = Arrays.asList("basic_info", "user_about_me",
                "user_relationships", "user_birthday", "user_location");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                LoginActivity.this.progressDialog.dismiss();
                if (user == null) {
                    Log.d(TAG,
                            "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(TAG,
                            "User signed up and logged in through Facebook!");
                    startCartDrawerActivity();
                } else {
                    Log.d(TAG,
                            "User logged in through Facebook!");
                    startCartDrawerActivity();
                    
                }
            }
        });
    }
    
    /**
     * Starts the Cart Drawer Activity.
     */
    public void startCartDrawerActivity() {
        Log.i(TAG,"startCartDrawerActivity");
        Intent intent = new Intent(this, CartDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

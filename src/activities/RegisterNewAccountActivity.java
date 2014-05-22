package activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ActionBar;
import com.example.snagtag.R;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Activity for registering new accounts.  This prompts a user for a user name (email address),
 * First and Last name, and a password. After validation, it creates a new ParseUser and redirects 
 * the user back to the login page for Authentication.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RegisterNewAccountActivity extends Activity {


	protected EditText mEditFirstName;
	protected EditText mEditLastName;
	protected EditText mEditEmailAddress;
	protected EditText mEditPassword;
	protected EditText mEditPasswordConfirm;
	protected Button mRegisterAccount;
    private CheckBox male;
    private CheckBox female;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		
		
		mEditFirstName = (EditText) findViewById(R.id.fname);
		mEditLastName = (EditText) findViewById(R.id.lname);
		mEditEmailAddress = (EditText) findViewById(R.id.unEmail);
		mEditPassword = (EditText) findViewById(R.id.pass);
		mEditPasswordConfirm = (EditText) findViewById(R.id.comPass);
		mRegisterAccount = (Button) findViewById(R.id.registerButton);
		male = (CheckBox) findViewById(R.id.checkBox1);
		female = (CheckBox) findViewById(R.id.checkBox2);
		
		mRegisterAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				registerAccount(v);
			}
		});
////TODO: CHeck boxes for male/female
//		male.setOnCheckedChangeListener(new OnCheckedChangeListener()) {
//		}
//		if(male.isChecked()) {
//			if(female.isChecked()) {
//				female.setChecked(false);
//			}
//		}
//		if(female.isChecked()) {
//			if(male.isChecked()) {
//				male.setChecked(false);
//			}
//		}
	}
	
	 public void registerAccount(View view) {
		if (validateFields()) {
			if (validatePasswordMatch()) {
				processSignup(view);
			} else {
				Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
			} 
		} else {
			Toast.makeText(this, "Fields not filled in", Toast.LENGTH_SHORT).show();
		}
	}
	
	// TODO:  Implement Text Listeners to handle this
	private boolean validateFields() {
		if (mEditFirstName.getText().length()>0 && mEditLastName.getText().length()>0 
				&& mEditEmailAddress.length()>0 && mEditPassword.getText().length()>0 
				&& mEditPasswordConfirm.getText().length()>0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validatePasswordMatch() {
		if (mEditPassword.getText().toString().equals(mEditPasswordConfirm.getText().toString())) {
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * Calls the createUserWithUsername method of the Parse to create a new user.
	 * After sign-up is successful, the First and Last name are added to the user, and
	 * an Intent is instantiated to bring the user back to the login page to confirm
	 * authentication.  
	 * 
	 * Note that a user is not Authorized here to the Android Account Manager, but rather
	 * is added to AccountManager upon successful authentication.  
	 */
	public void processSignup(final View view) {
		Toast.makeText(this, "Creating user...", Toast.LENGTH_SHORT).show();
		
		final ParseUser user = new ParseUser();
//username is set to email
		user.setUsername(mEditEmailAddress.getText().toString());
		user.setPassword(mEditPassword.getText().toString());
		user.setEmail(mEditEmailAddress.getText().toString().toLowerCase());
		  
		// other fields can be set just like with ParseObject
		user.put("fname", mEditFirstName.getText().toString());
		user.put("lname", mEditLastName.getText().toString());
		//if(male button pressed) {
		//	user.put("gender", "male");
		//}else if (female button pressed) {
		//	user.put("gender", "female");
		//}
		  
		user.signUpInBackground(new SignUpCallback() {
		@Override
		public void done(ParseException e) {
			// TODO Auto-generated method stub
		    if (e == null) {
		    	Toast.makeText(view.getContext(), "Registration Successful\nSending Confirmation Email", Toast.LENGTH_SHORT).show();
//TODO: Email notification???
		    	user.setEmail(user.getEmail());
		    	Intent i = new Intent(RegisterNewAccountActivity.this, LoginActivity.class);
		    	startActivity(i);
			      // Hooray! Let them use the app now.
			    } else {
			    	Toast.makeText(view.getContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
			      // Sign up didn't succeed. Look at the ParseException
			      // to figure out what went wrong
			    }
		}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
	            this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}

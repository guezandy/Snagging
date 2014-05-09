package activities;

import interfaces.NFCHandler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import utils.NfcUtils;

import com.example.snagtag.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import fragments.AccountTabbedFragment;
import fragments.CartFragment;
import fragments.HomeFragment;
import fragments.SnagFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import android.annotation.TargetApi;
import android.app.SearchManager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class CartDrawerActivity extends ActionBarActivity implements NFCHandler {
    private final String TAG = CartDrawerActivity.class.getSimpleName();

	private static final int HOME_FRAGMENT = 0;
    private static final int ACCOUNT_FRAGMENT = 1;
    private static final int SNAG_FRAGMENT = 2;

    private static final String PLACEHOLDER_STRING = "";
    
    //change to: "application/com.example.snagtag.tag"
    private static final String MIME_TYPE = "application/com.example.snagtag.tag";
    
    public ActionBar actionBar;
	public FrameLayout cartDrawerFrame = null;
    public DrawerLayout cartDrawerLayout = null;
    public ActionBarDrawerToggle cartDrawerToggle = null;
    public CharSequence activityTitle = null;
    public CharSequence cartDrawerTitle = null;
    private NfcAdapter mNfcAdapter;
    
    
	//read or write?
	private boolean isWriteReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_drawer);
        actionBar = getSupportActionBar();
        activityTitle = actionBar.getTitle();

        handleSearchIntent(getIntent());
        
        // Check if there is a currently logged in user
        // and they are linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
         // Fetch Facebook user info if the session is active
            Session session = ParseFacebookUtils.getSession();
            if (session != null && session.isOpened()) {
                makeMeRequest();
        }else{
            // If the user is not logged in, go to the
            // activity showing the login view.
            Log.i(TAG, "User was not logged in.");
            startLoginActivity();
            
            }
        }
        
        prepareCartDrawer();
        if (savedInstanceState == null) {
            updateMainContent(HOME_FRAGMENT,PLACEHOLDER_STRING);
        }
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {//no nfc on phone
			Toast.makeText(this, "Sorry, NFC is not available on this device", Toast.LENGTH_SHORT).show();
			finish();
		}
		
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        
        //Configure the search info and add any event listener
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
    
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(TAG, "onPrepareOptionsMenu");
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = cartDrawerLayout.isDrawerOpen(cartDrawerFrame);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
   
    @Override   
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.i(TAG, "onOptionsItemSelected");
        if (cartDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId())
        {
            case R.id.action_cart:
                Log.i(TAG,"Cart Item Clicked");
                if(cartDrawerLayout.isDrawerOpen(cartDrawerFrame)){
                    cartDrawerLayout.closeDrawer(cartDrawerFrame);
                }else{
                    cartDrawerLayout.openDrawer(cartDrawerFrame);
                }
                return true;
            case R.id.action_search:
                Log.i(TAG,"Search Item Clicked");
                return true;
            case R.id.action_profile:
                Log.i(TAG,"Profile Item Clicked");
                updateMainContent(ACCOUNT_FRAGMENT,PLACEHOLDER_STRING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }     
    }
    /*
     * Sets the activity_fragment to the fragment
     * corresponding to the paramater fragmentID
     */
    private void updateMainContent(int fragmentId,String clothingEntityId) {
        Log.i(TAG, "updateMainContent");
        Fragment fragment = null;
        Bundle args = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        switch(fragmentId){
            case ACCOUNT_FRAGMENT:
                fragment = new AccountTabbedFragment();
                break;
            case SNAG_FRAGMENT:
                fragment = new SnagFragment();
                ((SnagFragment) fragment).setEntityId(clothingEntityId);
                break;
            default:
                //Default sets Home Fragment
                fragment = new HomeFragment();
        }
        
        fragment.setArguments(args);
        // update the main content by replacing fragments
        fragmentManager.beginTransaction().replace(R.id.activity_frame, fragment).addToBackStack( "tag" ).commit();
        cartDrawerLayout.closeDrawer(cartDrawerFrame);
        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        Log.i(TAG, "onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK){
           if (getSupportFragmentManager().getBackStackEntryCount() == 0){
               this.finish();
               return false;
           }else{
               getSupportFragmentManager().popBackStack();
               removeCurrentFragment();
               return false;
           }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void removeCurrentFragment(){
        Log.i(TAG, "removeFragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFrag =  getSupportFragmentManager().findFragmentById(R.id.activity_frame);
    }
    
    @Override
    public void setTitle(CharSequence title) {
        Log.i(TAG, "setTitle");
        actionBar.setTitle(title);
    }
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onPostCreate");
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        cartDrawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        cartDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // Check if the user is currently logged
            // and show any cached content
        } else {
            // If the user is not logged in, go to the
            // activity showing the login view.
            startLoginActivity();
        }
        
        //not sure if to put this inside currentUser != null
		if (isWriteReady && NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
			processWriteIntent(getIntent());
		} else if (!isWriteReady
				&& (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction()) || NfcAdapter.ACTION_NDEF_DISCOVERED
						.equals(getIntent().getAction()))) {
			final String nfcid = processReadIntent(getIntent());
			updateMainContent(SNAG_FRAGMENT, nfcid);
		}
        
    }
    
    public void startLoginActivity() {
        Log.i(TAG, "startLoginActivity");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
	
    /** Facebook request for user info.
     * Stores facebook graph data into Parse user.
     */
    private void makeMeRequest() {
        Log.i(TAG, "makeMeRequest");
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) { 
                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();
                            try {                   
                                // Populate the JSON object 
                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());
                                if (user.getLocation().getProperty("name") != null) {
                                    userProfile.put("location", (String) user
                                            .getLocation().getProperty("name"));    
                                }                           
                                if (user.getProperty("gender") != null) {
                                    userProfile.put("gender",       
                                            (String) user.getProperty("gender"));   
                                }                           
                                if (user.getBirthday() != null) {
                                    userProfile.put("birthday",     
                                            user.getBirthday());                    
                                }                           
                                if (user.getProperty("relationship_status") != null) {
                                    userProfile                     
                                        .put("relationship_status",                 
                                            (String) user                                           
                                                .getProperty("relationship_status"));                               
                                }                           
                                // Now add the data to the UI elements
                                // ...
        
                            } catch (JSONException e) {
                                Log.d(TAG, "Error parsing returned user data.");
                            }
        
                        } else if (response.getError() != null) {
                            // handle error
                        }                  
                    }               
                });
        request.executeAsync();
     
    }
      
    /**
     * Handles a search intent
     * @param intent
     *      the intent that initialized the activity
     * 
     */
    private void handleSearchIntent(Intent intent) {
        Log.i(TAG, "handleSearchIntent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }
    
    /**
     * Adds the cart fragment to the right drawer
     */
    private void prepareCartDrawer(){
        Log.i(TAG, "onPrepareCartDrawer");
        //Set up cart drawer, in progress
        cartDrawerTitle = "Cart";
        cartDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        cartDrawerFrame = (FrameLayout) findViewById(R.id.right_drawer);
        
        // create the cart drawer fragment
        Fragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.right_drawer, fragment)
                       .commit();
    
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        cartDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                cartDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                actionBar.setTitle(activityTitle);
            }
    
            public void onDrawerOpened(View view) {
                actionBar.setTitle(cartDrawerTitle);
            }
        };
    }

	/**
	 * Enable this activity to write to a tag
	 * 
	 * @param isWriteReady
	 */
	public void setTagWriteReady(boolean isWriteReady) {
	    Log.i(TAG, "setTagWriteReady");
		this.isWriteReady = isWriteReady;
		if (isWriteReady) {
			IntentFilter[] writeTagFilters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED) };
			mNfcAdapter.enableForegroundDispatch(CartDrawerActivity.this, NfcUtils.getPendingIntent(CartDrawerActivity.this),
					writeTagFilters, null);
		} else {
			// Disable dispatch if not writing tags
			mNfcAdapter.disableForegroundDispatch(CartDrawerActivity.this);
		}
	}

	/**
	 * Write to an NFC tag; reacting to an intent generated from foreground
	 * dispatch requesting a write
	 * 
	 * @param intent
	 */
	public void processWriteIntent(Intent intent) {
	    Log.i(TAG, "processWriteIntent");
		if (isWriteReady && NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {

			Tag detectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
/*
			String tagWriteMessage = mTextField.getText().toString();
			byte[] payload = new String(tagWriteMessage).getBytes();

			if (detectedTag != null && NfcUtils.writeTag(
					NfcUtils.createMessage(MIME_TYPE, payload), detectedTag)) {
				
				Toast.makeText(this, "Wrote '" + tagWriteMessage + "' to a tag!", 
					Toast.LENGTH_LONG).show();
				setTagWriteReady(false);
			} else {
				Toast.makeText(this, "Write failed. Please try again.", Toast.LENGTH_LONG).show();
			}*/
		}
	}
	
	public String processReadIntent(Intent intent) {
	    Log.i(TAG, "processReadIntent");
		List<NdefMessage> intentMessages = NfcUtils.getMessagesFromIntent(intent);
		List<String> payloadStrings = new ArrayList<String>(intentMessages.size());

		for (NdefMessage message : intentMessages) {
			for (NdefRecord record : message.getRecords()) {
				byte[] payload = record.getPayload();
				String payloadString = new String(payload);

				if (!TextUtils.isEmpty(payloadString))
					payloadStrings.add(payloadString);
			}
		}

		if (!payloadStrings.isEmpty()) {
			String content =  TextUtils.join(",", payloadStrings);
			//printing for testing
			Log.i(TAG,content);
			return content;
		}
		return null;
	}

}

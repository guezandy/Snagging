package jQuery;

import java.util.Arrays;
import java.util.List;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snagtag.R;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class SnagActivityjQuery extends ActionBarActivity {
    private final String TAG = SnagActivityjQuery.class.getSimpleName();
    ActionBar actionBar;
    public CharSequence activityTitle = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snag_jquery);
        actionBar = getSupportActionBar();
        activityTitle = actionBar.getTitle();
        final WebView screen = (WebView) findViewById(R.id.webSnag);
        //screen.getSettings().setPluginState(true);
        WebSettings webSettings = screen.getSettings();
        webSettings.setJavaScriptEnabled(true);
        
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("ClothingEntity");
 //should be nfcid! insread of "wJVgzJPWaA";
    	query.getInBackground("wJVgzJPWaA", new GetCallback<ParseObject>() {
		@Override
		public void done(ParseObject clothingEntity, ParseException e) {
			// TODO Auto-generated method stub
    	    if (e == null) {
    	    	
    	    	System.out.println("Entity:" + clothingEntity);
    	    	//String summary = "<html><body>YOUR JSON HERE</body></html>";
    	    	//screen.loadDataWithBaseURL("file:///android_asset/index.html", summary,  "text/html", null, "file:///android_asset/index.html");
    	    	//screen.loadData(summary, "text/html", null);
    	    } else {
	      // something went wrong
	      //Toast.makeText(thi, "Cannot find item!", Toast.LENGTH_SHORT).show();

	    }
}
});

        screen.loadUrl("file:///android_asset/master/index.html");

    }
  
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

        return super.onPrepareOptionsMenu(menu);
    }
   
    @Override   
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.i(TAG, "onOptionsItemSelected");
       
        switch(item.getItemId())
        {
        	case R.id.action_home:

        		return true;
            case R.id.action_cart:

                return true;
            case R.id.action_search:
            	
            	return true;
            case R.id.action_barcode:

            	return true;
            case R.id.action_profile:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }     
    }

}

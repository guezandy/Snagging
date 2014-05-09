package activities;


import model.CartItem;
import model.ClothingEntity;

import  com.example.snagtag.R;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Application;
import android.util.Log;

public class SnagTagApplication extends Application {
    private final static String TAG = SnagTagApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");

        /*
         * In this tutorial, we'll subclass ParseObject for convenience to
         * create and modify Meal objects
         */
        ParseObject.registerSubclass(ClothingEntity.class);
        ParseObject.registerSubclass(CartItem.class);


        /*
         * Fill in this section with your Parse credentials
         */
        Parse.initialize(this, "84MWD1exP97b7T2NiAGKpEmFXR6t0SEXiMzCd6gB", "8AJ6q1JgGUkbtmXm6XO4cPydJV2tw4OLtDe9Z5DX");
        ParseFacebookUtils.initialize(getString(R.string.facebook_id));

        /*
         * This app lets an anonymous user create and save photos of meals
         * they've eaten. An anonymous user is a user that can be created
         * without a username and password but still has all of the same
         * capabilities as any other ParseUser.
         * 
         * After logging out, an anonymous user is abandoned, and its data is no
         * longer accessible. In your own app, you can convert anonymous users
         * to regular users so that data persists.
         * 
         * Learn more about the ParseUser class:
         * https://www.parse.com/docs/android_guide#users
         */
        ParseUser.enableAutomaticUser();

        /*
         * For more information on app security and Parse ACL:
         * https://www.parse.com/docs/android_guide#security-recommendations
         */
        ParseACL defaultACL = new ParseACL();

        /*
         * If you would like all objects to be private by default, remove this
         * line
         */
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}

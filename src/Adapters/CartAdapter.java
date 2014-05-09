package Adapters;



import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.snagtag.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/*
 * The FavoriteMealAdapter is an extension of ParseQueryAdapter
 * that has a custom layout for favorite meals, including a 
 * bigger preview image, the meal's rating, and a "favorite"
 * star. 
 */

public class CartAdapter extends ParseQueryAdapter<ParseObject> {

    private static final String TAG = CartAdapter.class.getSimpleName();

    public CartAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                Log.i(TAG, "Constructor");

                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                
                // set up the query on the relation
                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<ParseObject> cart = user.getRelation("cart");
                ParseQuery<ParseObject> query = cart.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                query.orderByDescending("date");
                return query;
            }
        });
    }

    @Override
    public View getItemView(ParseObject cartItem, View v, ViewGroup parent) {
        Log.i(TAG, "getItemView");

        if (v == null) {
            v = View.inflate(getContext(), R.layout.row_clothing_entity, null);
        }
        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();
        super.getItemView(cartItem, v, parent);
        Log.i(TAG,"Adde history object");
        ParseObject testingTagHistory = new ParseObject("TagHistory");
        ParseUser user = ParseUser.getCurrentUser();
        testingTagHistory.add("from", user);
        testingTagHistory.add("to", cartItem);
        testingTagHistory.add("date", time);
        testingTagHistory.saveEventually();
        
        ParseImageView itemImage = (ParseImageView) v.findViewById(R.id.item_image);
        ParseFile photoFile = cartItem.getParseFile("image");
        if (photoFile != null) {
            itemImage.setParseFile(photoFile);
            itemImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        TextView descriptionTextView = (TextView) v
                .findViewById(R.id.item_description);
        descriptionTextView.setText(cartItem.getString("description"));

        TextView priceTextView = (TextView) v.findViewById(R.id.item_price);
        priceTextView.setText("Price: "+cartItem.getNumber("price"));
        return v;
    }

}
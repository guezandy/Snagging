package Adapters;

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

public class ClosetAdapter extends ParseQueryAdapter<ParseObject> {
    private static final String TAG = ClosetAdapter.class.getSimpleName();

    /**
     * Constructs a ParseQueryAdapter on the user closet relation 
     */
    public ClosetAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            
            public ParseQuery<ParseObject> create() {
                Log.i(TAG, "Constructor");

                // set up the query on the relation
                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<ParseObject> cart = user.getRelation("closet");
                ParseQuery<ParseObject> query = cart.getQuery();
                query.orderByDescending("createdAt");
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                return query;
            }
        });
        Log.i(TAG,"closet adapter constructor");

    }

    @Override
    public View getItemView(ParseObject clothingEntity, View v, ViewGroup parent) {
        Log.i(TAG, "getItemView");
        if (v == null) {
            v = View.inflate(getContext(), R.layout.row_tag_history_entity, null);
        }
        super.getItemView(clothingEntity, v, parent);

        ParseImageView itemImage = (ParseImageView) v.findViewById(R.id.item_image);
        ParseFile photoFile = clothingEntity.getParseFile("image");
        if (photoFile != null) {
            itemImage.setParseFile(photoFile);
            itemImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        TextView descriptionTextView = (TextView) v.findViewById(R.id.item_description);
        descriptionTextView.setText(clothingEntity.getString("description"));

        TextView priceTextView = (TextView) v.findViewById(R.id.item_price);
        priceTextView.setText("Price: "+clothingEntity.getNumber("price"));
        
        return v;
    }

}
package fragments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.ClothingEntity;

import com.example.snagtag.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import Adapters.CartAdapter;
import activities.LoginActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CartFragment extends ListFragment {
    private final String TAG = CartFragment.class.getSimpleName();
    private CartAdapter cartAdapter;
    private TableLayout cartDetails;
    private double subtotal;
    private double taxRate;
    protected double total;
    protected double shipping;
    public CartFragment() {
        // Empty constructor required for fragment subclasses
        
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getListView().setClickable(false);
        cartAdapter = new CartAdapter(this.getActivity().getApplicationContext());
        setListAdapter(cartAdapter);
        LayoutInflater inflater = this.getLayoutInflater(savedInstanceState);
        cartDetails = (TableLayout)inflater.inflate(
                R.layout.layout_cart_details, null);
        getListView().addFooterView(cartDetails);
        updateCartDetails();
    }
    /**
     * Add item to the cart
     * @param clothingEntity parse object to be added to the cart
     * 
     */
    public void addItem(ClothingEntity clothingEntity){
        Log.i(TAG, "addItem");
        ParseUser  user = ParseUser.getCurrentUser();
        ParseRelation<ClothingEntity> cart = user.getRelation("cart");
        cart.add(clothingEntity);
        user.saveEventually();
        updateCartDetails();

        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();
        
        // create an entity in the CartHistory table
        ParseObject cartHistoryItem = new ParseObject("CartHistoryItem");
        cartHistoryItem.put("from", ParseUser.getCurrentUser());
        cartHistoryItem.put("to", clothingEntity);
        cartHistoryItem.put("date",time);
        cartHistoryItem.saveEventually();
    }
    /**
     * Remove item from the cart
     * @param itemId id of item to be removed from the cart
     */
    public void removeItem(ClothingEntity clothingEntity){
        Log.i(TAG, "removeItem");
        ParseUser  user = ParseUser.getCurrentUser();
        ParseRelation<ClothingEntity> cart = user.getRelation("cart");
        cart.remove(clothingEntity);
        user.saveEventually();
        updateCartDetails();

        
    }
    /**
     * Update cart details
     * sets cart subtotal ,total, shipping, and tax to 
     * correct values.
     * 
     */
    private void updateCartDetails(){
        Log.i(TAG, "updateCartDetails");

        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> cart = user.getRelation("cart");
        ParseQuery<ParseObject> query = cart.getQuery();
        subtotal = (double) 0;
        taxRate = .05;
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> cartItems, ParseException e) {
              if (e == null) {
                // Results were successfully found, looking first on the
                // disk and then on network.
                  for(ParseObject cartItem:cartItems){
                      subtotal+= cartItem.getDouble("price");
                  }
                  shipping = getShipping(subtotal);
                  total = subtotal*(1+taxRate) + shipping;
                  /*
                   *  Must call updateShipping after updating 
                   *  subtotal, before setting
                   *  the cart details and calculating total.
                   * 
                   */
                  updateCartDetailStrings();
              } else {
                // The network was inaccessible and we have no cached data
                // for this query.
              }
            };
        });
        
      
        
        
        
    }

    /**
     * Get shipping based on cart subtotal
     * @param subtotal
     * @return shipping base on companies shipping brackets
     */
    private double getShipping(double subtotal) {
        return 0;
    }

    private void updateCartDetailStrings() {
        Log.i(TAG, "updateCartDetailsStrings");
        TextView subtotalView = (TextView) cartDetails.findViewById(R.id.subtotal);
        TextView shippingView = (TextView) cartDetails.findViewById(R.id.shipping);
        TextView taxView = (TextView) cartDetails.findViewById(R.id.taxRate);
        TextView totalView = (TextView) cartDetails.findViewById(R.id.total);
        DecimalFormat f = new DecimalFormat("#0.00");
        subtotalView.setText(f.format(subtotal));
        shippingView.setText(f.format(shipping));
        taxView.setText(f.format(subtotal));
        double total = subtotal*(1+taxRate)+shipping;
        totalView.setText(f.format(total));
    }

    
}

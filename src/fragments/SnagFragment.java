package fragments;

import java.util.Calendar;
import java.util.Date;

import com.example.snagtag.R;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class SnagFragment extends Fragment {
    private final String TAG = SnagFragment.class.getSimpleName();
    private ParseObject entity ;
    
    private TextView price;
    private TextView store;
    private TextView description;
    private Button styles;
    private Button purchase;
    private Button add2Cart;
    private Button sizes;
    private RatingBar rate;
    private ParseImageView sim1;
    private ParseImageView sim2;
    private PopupMenu sizeMenu;
    private PopupWindow styleWindow;
    private Button closeStyle;
    //private ParseImageView itemImg;
    public String nfcid;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	System.out.println("Running on create");
    	final LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_snag,
                container, false);
      	final ParseImageView itemImg = (ParseImageView) mLinearLayout.findViewById(R.id.imageView);

    	price = (TextView) mLinearLayout.findViewById(R.id.price);
    	store = (TextView) mLinearLayout.findViewById(R.id.store);
    	description = (TextView) mLinearLayout.findViewById(R.id.description);
    	
    	add2Cart = (Button) mLinearLayout.findViewById(R.id.add2Cart);
    	add2Cart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addToCart();
			}
    	});
    	sizes = (Button) mLinearLayout.findViewById(R.id.sizes);
        sizes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //buttonClicked(v);
            	sizeMenu.show();
            	System.out.println("size menu popupp");
            }
        });
    	styles = (Button) mLinearLayout.findViewById(R.id.colors);
    	styles.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initiatePopupWindow();
			}
    	});
    	
    	purchase = (Button) mLinearLayout.findViewById(R.id.purchase);
    	purchase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( entity.getString("purchaseUrl").toString()));
		        startActivity( browse );
				
			}
    	});
    	rate = (RatingBar) mLinearLayout.findViewById(R.id.rate);
    	sim1 = (ParseImageView) mLinearLayout.findViewById(R.id.sim1);
    	sim1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    	});
    	sim2 = (ParseImageView) mLinearLayout.findViewById(R.id.sim2);
    	sim2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    	});
    	
    	//Size menu popup
        sizeMenu = new PopupMenu(this.getActivity(), mLinearLayout.findViewById(R.id.sizes));
        sizeMenu.getMenu().add("Small");
        sizeMenu.getMenu().add("Medium");
        sizeMenu.getMenu().add("Large");
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("ClothingEntity");
 //should be nfcid! insread of "wJVgzJPWaA";
    	query.getInBackground(nfcid, new GetCallback<ParseObject>() {
		@Override
		public void done(ParseObject clothingEntity, ParseException e) {
			// TODO Auto-generated method stub
    	    if (e == null) {
    	    	entity = clothingEntity;
    	    	addToHistory();
      	      // Object will be your clothing item!
    	    	price.setText("$"+clothingEntity.getNumber("price").toString());
    	    	description.setText(clothingEntity.getString("description").toString());
    	    	store.setText(clothingEntity.getString("store"));
    	      	 // The placeholder will be used before and during the fetch, to be replaced by the fetched image
    	      	 // data.

    	        ParseFile photoFile = clothingEntity.getParseFile("image");
    	        if (photoFile != null) {
    	            itemImg.setParseFile(photoFile);
    	            itemImg.loadInBackground(new GetDataCallback() {
    	                @Override
    	                public void done(byte[] data, ParseException e) {
    	                    // nothing to do
    	                	System.out.println("Image loaded");
    	                }
    	            });
    	        }
    	    
    	       	
      	    } else {
      	      // something went wrong
      	      Toast.makeText(mLinearLayout.getContext(), "Cannot find item!", Toast.LENGTH_SHORT).show();

      	    }
		}
    	});

    	return mLinearLayout;
       }

    
    public SnagFragment() {
        // TODO Auto-generated constructor stub
    }
    private void  addToCart(){
        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> cart = user.getRelation("cart");
        cart.add(entity);
        Toast.makeText(this.getActivity(), "Added "+entity.getString("description")+" to cart", Toast.LENGTH_SHORT).show();
        user.saveEventually();
    }
    
    private void  addToHistory(){
    	//creates tag history relation
        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> history = user.getRelation("TagHistory");
        history.add(entity);
        Toast.makeText(this.getActivity(), "Added "+entity.getString("description")+" to history", Toast.LENGTH_SHORT).show();
        user.saveEventually();
        
        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();
        Log.i(TAG,"Added history object");
//        ParseObject testingTagHistory = new ParseObject("TagHistory");
//        testingTagHistory.add("from", user);
//        testingTagHistory.add("to", entity);
//        testingTagHistory.add("date", time);
//        testingTagHistory.saveEventually();
        
    }

    public void setEntityId(String clothingEntityId) {
    	System.out.println("Inside setEntityId");
        // Query parse for specific clothingEntityId
    	
    	nfcid = clothingEntityId;
        
    }
    
    private void initiatePopupWindow() { 
 	   try { 
		       // We need to get the instance of the LayoutInflater 
		       //LayoutInflater inflater = (LayoutInflater) SnagActivity.class 
		       //.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
 		   LayoutInflater inflater = getLayoutInflater(null);
		       View layout = inflater.inflate(R.layout.popup_snag_styles,(ViewGroup)
		
		       getActivity().findViewById(R.layout.popup_snag_styles)); 
		       styleWindow = new PopupWindow(layout, 850, 350, true); 
		       styleWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
		
		       closeStyle = (Button) layout.findViewById(R.id.close_style); 
		       closeStyle.setOnClickListener(cancel_button_click_listener);
		
		   } catch (Exception e) { 
			   e.printStackTrace(); 
		       }
   	   }
	   private OnClickListener cancel_button_click_listener = new OnClickListener() { 
		   public void onClick(View v) { 
			   styleWindow.dismiss();
		   	} 
	   };

}

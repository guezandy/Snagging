package fragments;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.support.v4.app.Fragment;

public class SnagFragment extends Fragment {
    private final String TAG = SnagFragment.class.getSimpleName();
    private ParseObject entity ;

    public SnagFragment() {
        // TODO Auto-generated constructor stub
    }
    private void  addToCart(){
        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> cart = user.getRelation("cart");
        cart.add(entity);
        user.saveEventually();
    }

    public void setEntityId(String clothingEntityId) {
        // TODO Auto-generated method stub
        
    }
}

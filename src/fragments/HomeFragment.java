package fragments;

import com.example.snagtag.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.parse.*;

public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();
    private ImageButton image;
    public int imageInt = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_home,
                container, false);
    	image = (ImageButton) mLinearLayout.findViewById(R.id.homeImage);
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
            	imageInt = (imageInt+1) % 2; //keeps imageInt 0 or 1;
            	changeImage(imageInt);
            }
        });
        // Inflate the layout for this fragment
        return mLinearLayout;
    }
    
    public void changeImage(int imageInt) {
    	switch(imageInt) {
    		case 0:
    			
    			image.setImageResource(R.drawable.snaglogo);
    			break;
    		case 1:
    			image.setImageResource(R.drawable.ic_launcher);
    			break;
    		default:
    			
    			break;
    	}
    }
    
    public HomeFragment() {
        // TODO Auto-generated constructor stub
    	System.out.println("Do we go in here?");
    }
}

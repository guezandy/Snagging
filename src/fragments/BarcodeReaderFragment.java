package fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.ClothingEntity;

import com.example.snagtag.R;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import Adapters.ClosetAdapter;
import activities.CartDrawerActivity;
import activities.LoginActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import utils.IntentIntegrator;
import utils.IntentResult;

public class BarcodeReaderFragment extends Fragment {
    private final String TAG = "Barcode Reader Fragment";

    public BarcodeReaderFragment() {
        // Empty constructor required for fragment subclasses
        
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

		//scan
		IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
		scanIntegrator.initiateScan();
        
        
    }
}

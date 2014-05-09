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
import android.widget.Toast;

public class ClosetFragment extends ListFragment {
    private final String TAG = ClosetFragment.class.getSimpleName();
    private ClosetAdapter closetAdapter;
    public ClosetFragment() {
        // Empty constructor required for fragment subclasses
        
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getListView().setClickable(false);
        closetAdapter = new ClosetAdapter(this.getActivity().getApplicationContext());
        setListAdapter(closetAdapter);
        
    }
    
    
}

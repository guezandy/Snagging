package fragments;

import com.parse.ParseObject;

import Adapters.TagHistoryAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;

public class TagHistoryFragment extends ListFragment{
    private final String TAG = TagHistoryFragment.class.getSimpleName();
    private TagHistoryAdapter cartAdapter;
    public TagHistoryFragment() {
        // TODO Auto-generated constructor stub
        
    }
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getListView().setClickable(false);
        cartAdapter = new TagHistoryAdapter(this.getActivity().getApplicationContext());
        setListAdapter(cartAdapter);
    }
}

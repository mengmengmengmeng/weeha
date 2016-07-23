package ph.codebuddy.weeha.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ph.codebuddy.weeha.R;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class GroupFragment extends Fragment {

    public GroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        return view;
    }
}

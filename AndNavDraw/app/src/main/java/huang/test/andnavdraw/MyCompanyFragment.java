package huang.test.andnavdraw;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shiyanghuang on 15/5/2.
 */
public class MyCompanyFragment extends Fragment {
    public static MyCompanyFragment newInstance() {

        MyCompanyFragment fragment = new MyCompanyFragment();

        return fragment;

    }

    public MyCompanyFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_company, container, false);

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(3);
    }
}

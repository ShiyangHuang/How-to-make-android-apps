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
public class AboutMeFragment extends android.support.v4.app.Fragment {

    public static AboutMeFragment newInstance() {

        AboutMeFragment fragment = new AboutMeFragment();
        return fragment;

    }

    public AboutMeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);
    }
}

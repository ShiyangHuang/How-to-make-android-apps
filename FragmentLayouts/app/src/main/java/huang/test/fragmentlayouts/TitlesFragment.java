package huang.test.fragmentlayouts;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by shiyanghuang on 15/4/30.
 */
public class TitlesFragment extends ListFragment {

    boolean mDuePlane;
    int mCurCheckPosition = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> connectArrayToListView = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, SuperHeroInfo.NAMES);

        setListAdapter(connectArrayToListView);

        View detailsFrame = getActivity().findViewById(R.id.details);

        mDuePlane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {

            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);

        }

        if (mDuePlane) {

            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("curChoice", mCurCheckPosition);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);

    }

    public void showDetails(int index) {

        mCurCheckPosition = index;

        if(mDuePlane) {

            getListView().setItemChecked(index, true);

            DetailsFragment details = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);

            if (details == null || details.getShownIndex() != index) {

                details = DetailsFragment.newInstance(index);

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.details, details);

                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

            } else {

                Intent intent = new Intent();

                intent.setClass(getActivity(), DetailsActivity.class);

                intent.putExtra("index", index);

                startActivity(intent);
            }
        }

    }
}

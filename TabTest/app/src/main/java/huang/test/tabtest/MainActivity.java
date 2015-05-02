package huang.test.tabtest;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    List<View> listView = new ArrayList<View>();

    LinearLayout tab1;
    LinearLayout tab2;
    LinearLayout tab3;
    LinearLayout tab4;

    ImageButton imgButton1;
    ImageButton imgButton2;
    ImageButton imgButton3;
    ImageButton imgButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        tab1 = (LinearLayout) findViewById(R.id.tab1);
        tab2 = (LinearLayout) findViewById(R.id.tab2);
        tab3 = (LinearLayout) findViewById(R.id.tab3);
        tab4 = (LinearLayout) findViewById(R.id.tab4);

        imgButton1 = (ImageButton) findViewById(R.id.img_tab1);
        imgButton2 = (ImageButton) findViewById(R.id.img_tab2);
        imgButton3 = (ImageButton) findViewById(R.id.img_tab3);
        imgButton4 = (ImageButton) findViewById(R.id.img_tab4);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View tab01 = layoutInflater.inflate(R.layout.tab1, null);
        View tab02 = layoutInflater.inflate(R.layout.tab2, null);
        View tab03 = layoutInflater.inflate(R.layout.tab3, null);
        View tab04 = layoutInflater.inflate(R.layout.tab4, null);

        listView.add(tab01);
        listView.add(tab02);
        listView.add(tab03);
        listView.add(tab04);

        pagerAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(listView.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = listView.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return listView.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }
        };

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

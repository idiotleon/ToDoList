package com.example.tek.first.servant;

<<<<<<< HEAD
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
=======
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
>>>>>>> habit
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.EditText;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
=======
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
>>>>>>> habit

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

<<<<<<< HEAD
=======
    EditText editTextSearch;
    Button btnSearch;
    ListView listViewFood, listViewEntertainment, listViewShopping;
    public ArrayList<InfoCollection> food_list = new ArrayList<>();
    public ArrayList<InfoCollection> entertainment_list = new ArrayList<>();
    public ArrayList<InfoCollection> shopping_list = new ArrayList<>();
    double[] currentaddress = new double[2];
    protected LocationManager locationManager;
    protected LocationListener locationListener;

>>>>>>> habit

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
<<<<<<< HEAD
        View view = inflater.inflate(R.layout.fragment_main, container, false);
=======
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        editTextSearch = (EditText) view.findViewById(R.id.et_search);
        btnSearch = (Button) view.findViewById(R.id.btn_search);
        btnSearch.setEnabled(true);
        listViewFood = (ListView) view.findViewById(R.id.listView_food_result);
        listViewEntertainment = (ListView) view.findViewById(R.id.listView_entertainment_result);
        listViewShopping = (ListView) view.findViewById(R.id.listView_shopping_result);
        final TabHost th = (TabHost) view.findViewById(R.id.tabHost);
        th.setup();
        TabWidget tabWidget = th.getTabWidget();
        th.addTab(th.newTabSpec("tab1").setContent(R.id.tab1).setIndicator("Food", null));
        th.addTab(th.newTabSpec("tab2").setContent(R.id.tab2).setIndicator("Entertainment", null));
        th.addTab(th.newTabSpec("tab3").setContent(R.id.tab3).setIndicator("Shopping", null));
        TextView tv1 = (TextView) tabWidget.getChildAt(0).findViewById(android.R.id.title);
        tv1.setTextSize(10);
        TextView tv2 = (TextView) tabWidget.getChildAt(1).findViewById(android.R.id.title);
        tv2.setTextSize(10);
        TextView tv3 = (TextView) tabWidget.getChildAt(2).findViewById(android.R.id.title);
        tv3.setTextSize(10);
        th.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View v) {
            }

            @Override
            public void onViewAttachedToWindow(View v) {
                th.getViewTreeObserver().removeOnTouchModeChangeListener(th);
            }
        });


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            currentaddress[0] = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            currentaddress[1] = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

        } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            currentaddress[0] = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
            currentaddress[1] = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
        } else if (locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null) {
            currentaddress[0] = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
            currentaddress[1] = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentaddress[0] = location.getLatitude();
                currentaddress[1] = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 1, locationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(currentaddress[0]);
        System.out.println(currentaddress[1]);
        //****************************************//
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Yelp yelp = Yelp.getYelp(getActivity());
                String food_json = yelp.search("taco", currentaddress[0], currentaddress[1], "5");
                String entertainment_json = yelp.search("bar", currentaddress[0], currentaddress[1], "5");
                String shopping_json = yelp.search("Macy's", currentaddress[0], currentaddress[1], "5");
                try {
                    ProcessJSON.processJson(food_json, food_list);
                    ProcessJSON.processJson(entertainment_json, entertainment_list);
                    ProcessJSON.processJson(shopping_json, shopping_list);

                } catch (JSONException e) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                listViewFood.setAdapter(new CustomAdapter(MainActivityFragment.this, food_list));
                listViewEntertainment.setAdapter(new CustomAdapter(MainActivityFragment.this, entertainment_list));
                listViewShopping.setAdapter(new CustomAdapter(MainActivityFragment.this, shopping_list));
            }
        }.execute();


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchResult.class);
                Bundle bundle = new Bundle();
                bundle.putString("search", editTextSearch.getText().toString().trim());
                bundle.putDoubleArray("location", currentaddress);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
>>>>>>> habit


        return view;
    }


}

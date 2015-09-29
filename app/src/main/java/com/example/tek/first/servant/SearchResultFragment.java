package com.example.tek.first.servant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchResultFragment extends Fragment {

    private Spinner spinner;
    private EditText editTextSearchAgain;
    private Button btnSearchAgain;
    private ListView listViewSearchResult;
    private double[] location_search;
    private SharedPreferences sp;
    private ArrayList<InfoCollection> result_list = new ArrayList<>();
    private ArrayList<InfoCollection> copy = new ArrayList<>();
    Comparator<InfoCollection> comparator_distance = new Comparator<InfoCollection>() {
        public int compare(InfoCollection s1, InfoCollection s2) {

            if (s1.getDistance() - s2.getDistance() > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    };


    Comparator<InfoCollection> comparator_rating = new Comparator<InfoCollection>() {
        public int compare(InfoCollection s1, InfoCollection s2) {

            if (s1.getRating() - s2.getRating() > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    public SearchResultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        editTextSearchAgain = (EditText) view.findViewById(R.id.et_search_again);
        btnSearchAgain = (Button) view.findViewById(R.id.btn_search_again);
        listViewSearchResult = (ListView) view.findViewById(R.id.listView_search_result);
        spinner = (Spinner) view.findViewById(R.id.spinner_sort);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sp.edit();
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            final String search = bundle.getString("search");
            location_search = bundle.getDoubleArray("location");
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Yelp yelp = Yelp.getYelp(getActivity());
                    String food_json = yelp.search(search, location_search[0], location_search[1], "20");
                    try {
                        ProcessJSON.processJson(food_json, result_list);
                    } catch (JSONException e) {

                    }
                    copy.addAll(result_list);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    initialSort();
                }
            }.execute();
        }
        btnSearchAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result_list.clear();
                final String searchagain = editTextSearchAgain.getText().toString().trim();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Yelp yelp = Yelp.getYelp(getActivity());
                        String food_json = yelp.search(searchagain, location_search[0], location_search[1], "20");
                        try {
                            ProcessJSON.processJson(food_json, result_list);
                        } catch (JSONException e) {

                        }
                        copy.clear();
                        copy.addAll(result_list);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        initialSort();

                    }
                }.execute();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                reSort(i, editor);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    private void reSort(int i, SharedPreferences.Editor editor) {
        switch (i) {
            case 0:
                result_list.clear();
                result_list.addAll(copy);
                listViewSearchResult.setAdapter(new CustomAdapter(SearchResultFragment.this, result_list));
                editor.putInt("sort", 0);
                editor.commit();
                break;
            case 1:
                Collections.sort(result_list, comparator_distance);
                listViewSearchResult.setAdapter(new CustomAdapter(SearchResultFragment.this,result_list));
                editor.putInt("sort", 1);
                editor.commit();
                break;
            case 2:
                Collections.sort(result_list,comparator_rating);
                listViewSearchResult.setAdapter(new CustomAdapter(SearchResultFragment.this, result_list));
                editor.putInt("sort",2);
                editor.commit();
                break;
        }
    }

    private void initialSort() {
        int sort_method= sp.getInt("sort",0);
        switch (sort_method) {
            case 0:
                spinner.setSelection(0);
                listViewSearchResult.setAdapter(new CustomAdapter(SearchResultFragment.this, result_list));
                break;
            case 1:
                spinner.setSelection(1);
                Collections.sort(result_list, comparator_distance);
                listViewSearchResult.setAdapter(new CustomAdapter(SearchResultFragment.this, result_list));
                break;
            case 2:
                spinner.setSelection(2);
                Collections.sort(result_list, comparator_rating);
                listViewSearchResult.setAdapter(new CustomAdapter(SearchResultFragment.this,result_list));
                break;
        }
    }


}

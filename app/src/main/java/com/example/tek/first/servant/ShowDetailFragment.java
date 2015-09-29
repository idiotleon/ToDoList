package com.example.tek.first.servant;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;


/**
 * A placeholder fragment containing a simple view.
 */
public class ShowDetailFragment extends Fragment {

    TextView detailName, detailReviews, detailDistance;
    TextView detailDescription;
    RatingBar detailRating;
    String jsonInfoCollection;
    TextView detaiAdress, detailPhone, detailWebsite;
    TextView detailComments;
    Button btnselect;
    String web_url;
    String phone;
    String mapaddress;

    public ShowDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_detail, container, false);

        detailName = (TextView) view.findViewById(R.id.detail_name);
        detailRating = (RatingBar) view.findViewById(R.id.ratingBar);
        detailReviews = (TextView) view.findViewById(R.id.detail_comment);
        detailDistance = (TextView) view.findViewById(R.id.detail_distance);
        detailDescription = (TextView) view.findViewById(R.id.detail_description);
        detaiAdress = (TextView) view.findViewById(R.id.textViewAddress);
        detailPhone = (TextView) view.findViewById(R.id.textViewPhone);
        detailWebsite = (TextView) view.findViewById(R.id.textViewWebsite);
        detailComments = (TextView) view.findViewById(R.id.textViewCommentDetail);
        btnselect = (Button) view.findViewById(R.id.btnselect);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            jsonInfoCollection = extras.getString("resultObject");
        }
        InfoCollection infoCollectiondetail = new Gson().fromJson(jsonInfoCollection, InfoCollection.class);
        detailName.setText(infoCollectiondetail.getName());
        detailRating.setRating(infoCollectiondetail.getRating());
        detailReviews.setText(infoCollectiondetail.getNumber_comment() + "reviews");
        detailDistance.setText(infoCollectiondetail.getDistance() + "miles");
        detailDescription.setText(infoCollectiondetail.getCategory());
        detaiAdress.setText(infoCollectiondetail.getAddress());
        detailPhone.setText(infoCollectiondetail.getPhone_number());
        detailWebsite.setText(infoCollectiondetail.getMobile_url());
        detailComments.setText(infoCollectiondetail.getSnippet_text());
        mapaddress=infoCollectiondetail.getAddress();
        web_url=infoCollectiondetail.getMobile_url();
        phone=infoCollectiondetail.getPhone_number();

        detaiAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q",mapaddress).build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);
                if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });

        detailWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(web_url);
                Intent it   = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });

        detailPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:"+phone);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });


        return view;
    }
}

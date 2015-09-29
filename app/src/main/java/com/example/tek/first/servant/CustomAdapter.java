package com.example.tek.first.servant;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by HAN KE on 2015/8/29.
 */
public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<InfoCollection> resultInfoCollection;

    private  static LayoutInflater inflater=null;

    public CustomAdapter(Fragment Fragment, ArrayList<InfoCollection> infoCollections){

        context=Fragment.getActivity();
        resultInfoCollection =infoCollections;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return resultInfoCollection.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        SmartImageView imageViewResult;
        TextView textViewName,textViewDistance,textViewDescription,textViewComment;
        RatingBar ratingBar;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder=new Holder();
        View rowView;

        rowView=inflater.inflate(R.layout.listview_detail,null);
        holder.textViewName= (TextView) rowView.findViewById(R.id.detail_name_list);
        holder.textViewDistance= (TextView) rowView.findViewById(R.id.detail_distance_list);
        holder.textViewDescription= (TextView) rowView.findViewById(R.id.detail_description_list);
        holder.textViewComment= (TextView) rowView.findViewById(R.id.detail_comment_list);
        holder.imageViewResult= (SmartImageView) rowView.findViewById(R.id.imageView_list);
        holder.ratingBar= (RatingBar) rowView.findViewById(R.id.ratingBar_list);

        holder.textViewName.setText(resultInfoCollection.get(position).getName());
        holder.textViewDistance.setText(resultInfoCollection.get(position).getDistance() + "miles");
        holder.textViewDescription.setText(resultInfoCollection.get(position).getCategory());
        holder.textViewComment.setText(resultInfoCollection.get(position).getNumber_comment() + "reviews");
        holder.imageViewResult.setImageUrl(resultInfoCollection.get(position).getImage_url());
        holder.ratingBar.setRating(resultInfoCollection.get(position).getRating());

        Gson gson=new Gson();
        final String tests=gson.toJson(resultInfoCollection.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ShowDetail.class);
                Bundle bundle=new Bundle();
                bundle.putString("resultObject",tests);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return rowView;
    }


}
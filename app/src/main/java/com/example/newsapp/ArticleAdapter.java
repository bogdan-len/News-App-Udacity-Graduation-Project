package com.example.newsapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView articleNameView = (TextView) listItemView.findViewById(R.id.article_name);

        articleNameView.setText(currentArticle.getWebTitle());

        String trimedDate = currentArticle.getDate().substring(0, 10);
        TextView articleDate = (TextView) listItemView.findViewById(R.id.date_text_view);
        articleDate.setText(trimedDate);

        TextView articleType = (TextView) listItemView.findViewById(R.id.type_text_view);
        articleType.setText(currentArticle.getSection());


        GradientDrawable magnitudeCircle = (GradientDrawable) articleType.getBackground();

        int magnitudeColor = getSecionColor(currentArticle.getSection());

        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }

    private int getSecionColor(String section) {
        int sectionColorResourceId;
        switch (section) {
            case "Football":
            case "Sport":
                sectionColorResourceId = R.color.sport;
                break;
            case "US news":
            case "Australia news":
            case "UK news":
            case "World news":
                sectionColorResourceId = R.color.news;
                break;
            case "Technology":
            case "Business":
            case "Television & radio":
                sectionColorResourceId = R.color.tech_business_media;
                break;
            case "Fashion":
            case "Culture":
            case "Books":
            case "Art and design":
                sectionColorResourceId = R.color.culture_fashion;
                break;
            case "Politics":
                sectionColorResourceId = R.color.politics;
                break;
            default:
                sectionColorResourceId = R.color.backgroundColorGrey;
                break;
        }

        return ContextCompat.getColor(getContext(), sectionColorResourceId);
    }
}
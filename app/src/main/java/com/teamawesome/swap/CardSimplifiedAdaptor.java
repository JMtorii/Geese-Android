package com.teamawesome.swap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

public class CardSimplifiedAdaptor extends ArrayAdapter<CardSimplified> {
    Context context;
    int layoutResourceId;
    CardSimplified[] data = null;

    public CardSimplifiedAdaptor(Context context, int layoutResourceId, CardSimplified[] data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardSimplifiedHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CardSimplifiedHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.cardImageIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.cardTitleText);
            holder.txtAuthor = (TextView)row.findViewById(R.id.cardAuthorText);
            holder.clockDate = (TextClock)row.findViewById(R.id.cardDate);

            row.setTag(holder);
        } else {
            holder = (CardSimplifiedHolder)row.getTag();
        }

        CardSimplified card = data[position];
        holder.imgIcon.setImageResource(card.icon);
        holder.txtTitle.setText(card.title);
        holder.txtAuthor.setText(card.author);
        return row;
    }

    static class CardSimplifiedHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtAuthor;
        //TODO: Don't use this, this is for clock of current time
        TextClock clockDate;

    }
}

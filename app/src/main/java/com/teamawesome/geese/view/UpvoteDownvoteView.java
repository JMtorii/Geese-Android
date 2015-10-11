package com.teamawesome.geese.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teamawesome.geese.R;

/**
 * Created by MichaelQ on 2015-10-11.
 */
public class UpvoteDownvoteView extends RelativeLayout {

    UpvoteDownvoteListener mListener;

    TextView mVotesText;
    Button mUpvoteButton;
    Button mDownvoteButton;

    public UpvoteDownvoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.upvote_downvote_view, this);
        mVotesText = (TextView)v.findViewById(R.id.upvote_downvote_view_text);
        mUpvoteButton = (Button)v.findViewById(R.id.upvote_downvote_view_upvote);
        mDownvoteButton = (Button)v.findViewById(R.id.upvote_downvote_view_downvote);

        final UpvoteDownvoteView referenceView = this;

        mUpvoteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onUpvoteClicked(referenceView);
                }
            }
        });

        mDownvoteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onDownvoteClicked(referenceView);
                }
            }
        });
    }

    public void setUpvoteDownvoteListener(UpvoteDownvoteListener listener) {
        mListener = listener;
    }

    public void setVotesText(String text) {
        mVotesText.setText(text);
    }
}

package com.teamawesome.geese.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teamawesome.geese.R;

/**
 * Created by MichaelQ on 2015-10-11.
 */
public class UpvoteDownvoteView extends RelativeLayout {

    private enum VoteValue {
        UpVote,
        NoVote,
        DownVote
    }

    UpvoteDownvoteListener mListener;

    TextView mVotesText;
    ImageButton mUpvoteButton;
    ImageButton mDownvoteButton;

    private VoteValue voteValue = VoteValue.NoVote;

    public UpvoteDownvoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.upvote_downvote_view, this);
        mVotesText = (TextView)v.findViewById(R.id.upvote_downvote_view_text);
        mUpvoteButton = (ImageButton)v.findViewById(R.id.upvote_downvote_view_upvote);
        mDownvoteButton = (ImageButton)v.findViewById(R.id.upvote_downvote_view_downvote);

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

    public void setUpVoted() {
        voteValue = VoteValue.UpVote;
        setUpArrowImage(true);
        setTextColour(true);
        setDownArrowImage(false);
    }

    public void setNotVoted() {
        voteValue = VoteValue.NoVote;
        setUpArrowImage(false);
        setTextColour(false);
        setDownArrowImage(false);
    }

    public void setDownVoted() {
        voteValue = VoteValue.DownVote;
        setUpArrowImage(false);
        setTextColour(true);
        setDownArrowImage(true);
    }

    private void setUpArrowImage(boolean coloured) {
        mUpvoteButton.setImageDrawable(getResources().getDrawable(coloured ? R.drawable.ic_keyboard_arrow_up_teal_24dp : R.drawable.ic_keyboard_arrow_up_black_24dp));
    }

    private void setDownArrowImage(boolean coloured) {
        mDownvoteButton.setImageDrawable(getResources().getDrawable(coloured ? R.drawable.ic_keyboard_arrow_down_teal_24dp : R.drawable.ic_keyboard_arrow_down_black_24dp));
    }

    private void setTextColour(boolean coloured) {
        mVotesText.setTextColor(getResources().getColor(coloured ? R.color.primary : R.color.black));
    }
}

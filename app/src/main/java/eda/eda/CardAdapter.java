package eda.eda;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Card> cardDataSet;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mUserName;
        public ImageView mCollectPic;
        public ImageView mProfilePic;

        public ViewHolder( View v ) {
            super(v);
            mUserName = (TextView) v.findViewById(R.id.card_username);
            mCollectPic = (ImageView) v.findViewById(R.id.card_collection_pic);
            mProfilePic = (ImageView) v.findViewById(R.id.card_profile_pic);
        }
    }

    public CardAdapter(Context mContext , List<Card> cardDataSet)
    {
        this.mContext = mContext;
        this.cardDataSet = cardDataSet;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Card mCard = cardDataSet.get(position);
        holder.mUserName.setText(mCard.userName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mCollectPic.setImageDrawable(mContext.getDrawable(mCard.getImageResourceId(mContext, mCard.imageName)));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mProfilePic.setImageDrawable(mContext.getDrawable(mCard.getImageResourceId(mContext, mCard.profileName)));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cardDataSet.size();
    }
}
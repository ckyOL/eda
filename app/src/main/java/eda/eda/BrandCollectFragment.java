package eda.eda;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class BrandCollectFragment extends Fragment{

    /**
     * 卡片
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Card> cardList;

    public static Fragment newInstance(Context context) {
        BrandCollectFragment brandCollectFragment = new BrandCollectFragment();
        return brandCollectFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View brandLayout = inflater.inflate(R.layout.fragment_card, container, false);
        return brandLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inits();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardAdapter(getActivity(),cardList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void inits(){
        cardList= new ArrayList<Card>();
        Card card1 = new Card("user1","card1_collect","card1_profile");
        Card card2 = new Card("User2","card2_collect","card1_profile");
        cardList.add(card1);
        cardList.add(card2);
    }

}



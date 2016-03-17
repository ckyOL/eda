package eda.eda.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import eda.eda.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    Uri uri;
    ImageView image;
    EditText brandText;
    EditText kindText;
    EditText styleText;
    Button postButton;

    private boolean isEmpty(String str)
    {
        return false;
    }

    public PostFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context,Bundle bundle) {
        PostFragment postFragment = new PostFragment();
        postFragment.setArguments(bundle);
        return postFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_post_collect, container, false);
        image=(ImageView) root.findViewById(R.id.userPic2);
        brandText=(EditText) root.findViewById(R.id.brand);
        kindText=(EditText) root.findViewById(R.id.kind);
        styleText=(EditText) root.findViewById(R.id.style);
        postButton=(Button) root.findViewById(R.id.postButton);
        image.setImageURI(uri);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(brandText.getText().toString()))
                {
                    //弹窗
                }
                else if(isEmpty(kindText.getText().toString()))
                {

                    //弹窗
                }
                else if(isEmpty(styleText.getText().toString()))
                {

                    //弹窗
                }
                else
                {
                    //新建json连接
                }
            }
        });
        return root;
    }

}

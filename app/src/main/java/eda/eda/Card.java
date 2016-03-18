package eda.eda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class Card implements Serializable {

    String imageName;
    String userName;
    String profileName;
    String uuid;
    int postId;

    public Card(String userName, String imageName,String profileName,
                String uuid,int postId) {
        this.userName = userName;
        this.imageName = imageName;
        this.profileName = profileName;
        this.uuid = uuid;
        this.postId = postId;
    }

}
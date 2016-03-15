package eda.eda;

import android.content.Context;

public class Card {

    String imageName;
    String userName;
    String profileName;
    String uuid;
    String posterId;
    String postId;

    public Card(String userName, String imageName,String profileName,
                String uuid,String posterId,String postId) {
        this.userName = userName;
        this.imageName = imageName;
        this.profileName = profileName;
        this.uuid = uuid;
        this.posterId = posterId;
        this.postId = postId;
    }

    public int getImageResourceId( Context context , String name) {
        try
        {
            return context.getResources().getIdentifier(name, "drawable", context.getPackageName());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
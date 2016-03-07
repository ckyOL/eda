package eda.eda;

import android.content.Context;

public class Card {

    String imageName;
    String userName;
    String profileName;

    public Card(String userName, String imageName,String profileName) {
        this.userName = userName;
        this.imageName = imageName;
        this.profileName = profileName;
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
package eda.eda;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GlobalValue {
    public static final String ip = "119.29.120.129:8080";//"119.29.120.129"
    public static final String loginUrl = "http://"+ip+"/eda/servlet/Login";
    public static final String signupUrl = "http://"+ip+"/eda/servlet/Register";
    public static final String favoriteUri = "http://"+ip+"/eda/servlet/Favorite";
    public static final String likeUri = "http://"+ip+"/eda/servlet/Like";
    public static final String defaultUri = "http://"+ip+"/eda/servlet/GetDefaultActicle";
    public static final String getActicleUrl = "http://"+ip+"/eda/servlet/GetArticle";
    public static final String UploadPictureUrl = "http://"+ip+"/eda/servlet/UploadPicture";
    public static final String imageUrl = "http://"+ip+"/eda/pic";
    public static final String PostUrl = "http://"+ip+"/eda/servlet/PostArticle";
    public static final String getActicleDetailsUrl = "http://"+ip+"/eda/servlet/PostArticleDetails";

    public static String getAbsoluteImagePath(Context context,Uri uri)
    {
        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query( uri,
                proj,                 // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null);                 // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public static String getUuid(Context context){
        SharedPreferences data =
                context.getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        String uuid = data.getString("uuid", "");

        return uuid;
    }

    public static boolean isEmpty(String str)
    {
        if(str==null) return true;
        if("".equals(str)) return true;
        return false;
    }

    public static JSONObject AsyncConnect(String url,JSONObject jsonObject)
    {
        ExecutorService executor = Executors.newCachedThreadPool();
        GetJsonTask task=new GetJsonTask(jsonObject,url);
        Future<JSONObject> result=executor.submit(task);
        executor.shutdown();
        JSONObject json=null;
        try
        {
            json=result.get();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return json;
    }

    static class GetJsonTask implements Callable<JSONObject>
    {

        JSONObject jsonObject;
        String url;

        public GetJsonTask(JSONObject jsonObject,String url)
        {
            this.jsonObject=jsonObject;
            this.url=url;
        }

        @Override
        public JSONObject call() throws Exception {

            JsonConnection jc = new JsonConnection(url, jsonObject, "POST");
            if (jc.connectAndGetJson()) {
                return jc.getJson();
            } else return null;

        }
    }
}

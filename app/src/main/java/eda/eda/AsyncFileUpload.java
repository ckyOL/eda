package eda.eda;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by piekey1994 on 2016/3/18.
 */
public class AsyncFileUpload {

    private String uid;
    private String fileName=null;
    private String msg=null;

    public AsyncFileUpload(String uid)
    {
        this.uid=uid;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getMessage()
    {
        return msg;
    }

    public boolean pictureUpload(String uploadFile,String actionUrl)
    {
        ExecutorService executor = Executors.newCachedThreadPool();
        FileUploadTask task=new FileUploadTask(uploadFile,actionUrl,uid);
        Future<String> result=executor.submit(task);
        executor.shutdown();
        try
        {
            fileName=result.get();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        if(fileName!=null) return true;
        else
        {
            msg=task.getMessage();
            return false;
        }
    }
}

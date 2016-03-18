package eda.eda;

/**
 * Created by piekey1994 on 2016/3/18.
 */
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;


import org.json.JSONObject;

public class FileUploadTask implements Callable<String> {

    private String uid;
    private String fileName=null;
    private String msg=null;
    String uploadFile=null;
    String actionUrl=null;

    public FileUploadTask(String uploadFile,String actionUrl,String uid)
    {
        this.uid=uid;
        this.uploadFile=uploadFile;
        this.actionUrl=actionUrl;
    }

    public String getMessage()
    {
        return msg;
    }

    public String call() throws Exception {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String newName = uploadFile.substring(uploadFile.lastIndexOf("\\")+1);
        try {
            URL url = new URL(actionUrl+"?uid="+uid);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
		    /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
		    /* 设置传送的method=POST */
            con.setRequestMethod("POST");
		    /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            con.connect();
            /* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);
            /* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 关闭DataOutputStream */
            ds.close();
            System.out.print(b.toString());
            JSONObject json=new JSONObject(b.toString());
            if(json.getInt("code")==1)
            {
                fileName=json.getString("name");
                return fileName;
            }
            else
            {
                msg=json.getString("msg");
                return null;
            }

        } catch (Exception e) {
            msg=e.toString();
            return null;
        }
    }
}

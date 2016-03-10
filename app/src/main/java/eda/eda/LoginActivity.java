package eda.eda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity{
    private EditText loginUserName,loginPassWord;
    private Button loginButton,signupButton;
    private SharedPreferences data;
    private SharedPreferences.Editor editor;
    private Dialog emptyDialog,successDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);

        if (data.getBoolean("login",true)){
            Intent main = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(main);
            finish();
        }
        else {

            loginUserName = (EditText) findViewById(R.id.login_user_name);
            loginPassWord = (EditText) findViewById(R.id.login_password);

            loginButton = (Button) findViewById(R.id.login_button);
            signupButton = (Button) findViewById(R.id.login_signup);

            loginButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userName = loginUserName.getText().toString().trim();
                    String passWord = loginPassWord.getText().toString().trim();
                    if(!(userName.equals("") || passWord.equals(""))) {

                        JSONObject json = new JSONObject();
                        try {
                            json.put("name", userName);
                            json.put("password", Coder.toMd5(passWord));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        conTask(GlobalValue.loginUrl, json);

                    } else {
                        emptyDialog = new Dialog(
                                LoginActivity.this,
                                "用户名或密码为空，请重新输入！",
                                "好");
                    }

                }

            });

            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userName = loginUserName.getText().toString().trim();
                    String passWord = loginPassWord.getText().toString().trim();
                    if(!(userName.equals("") || passWord.equals(""))) {

                        JSONObject json = new JSONObject();
                        try {
                            json.put("name", userName);
                            json.put("password", passWord);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        signupConTask(GlobalValue.signupUrl, json);

                    } else {
                        emptyDialog = new Dialog(
                                LoginActivity.this,
                                "用户名或密码为空，请重新输入！",
                                "好");
                    }
                }
            });
        }
    }

    public void conTask(String url, final JSONObject json) {
        JsonConnection jsonConn = new JsonConnection(url, json, "POST");
        new AsyncTask<JsonConnection, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(JsonConnection... params) {
                JsonConnection jc = params[0];
                if (jc.connectAndGetJson()) {
                    return jc.getJson();
                } else return null;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                if (jsonObject != null) {
                    try {
                        switch (jsonObject.getInt("code")) {

                            case -1:

                                Toast.makeText(getApplicationContext(),
                                        "服务器异常",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 0:

                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                editor= data.edit();
                                editor.putBoolean("login", true);
                                editor.putString("uuid", json.getString("uuid"));
                                editor.commit();
                                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(main);
                                finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "连接失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(jsonConn);
    }

    public void signupConTask(String url, final JSONObject json) {
        JsonConnection jsonConn = new JsonConnection(url, json, "POST");
        new AsyncTask<JsonConnection, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(JsonConnection... params) {
                JsonConnection jc = params[0];
                if (jc.connectAndGetJson()) {
                    return jc.getJson();
                } else return null;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                if (jsonObject != null) {
                    try {
                        switch (jsonObject.getInt("code")) {
                            case -1:

                                Toast.makeText(getApplicationContext(),
                                        "服务器异常",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                successDialog = new Dialog(LoginActivity.this,
                                        "注册成功！",
                                        "好");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(jsonConn);
    }
}

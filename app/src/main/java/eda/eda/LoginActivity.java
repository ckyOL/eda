package eda.eda;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{
    private EditText loginUserName,loginPassWord;
    private Button loginButton;
    private SharedPreferences data;
    private SharedPreferences.Editor editor;

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

            loginButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userName = loginUserName.getText().toString().trim();
                    String passWord = loginPassWord.getText().toString().trim();
                    if (userName.equals("test") && passWord.equals("test")) {
                        editor = data.edit();
                        editor.putBoolean("login", true);
                        editor.putString("userName", userName);
                        editor.putString("passWord", passWord);
                        editor.commit();

                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名密码不正确，请重新输入！", Toast.LENGTH_LONG).show();
                    }

                }

            });
        }
    }

}

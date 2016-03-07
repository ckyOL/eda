package eda.eda;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity{
    EditText loginUserName,loginPassWord;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUserName=(EditText) findViewById(R.id.login_user_name);
        loginPassWord=(EditText) findViewById(R.id.login_password);

        loginButton=(Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                
            }
        });
    }
}

package eda.eda;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity{
    EditText userName,passWord;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName=(EditText) findViewById(R.id.login_user_name);
        passWord=(EditText) findViewById(R.id.login_password);

        login=(Button) findViewById(R.id.login_button);




    }
}

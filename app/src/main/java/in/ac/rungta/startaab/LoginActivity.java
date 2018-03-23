package in.ac.rungta.startaab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btn_signin;
    private EditText et_email,et_password;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbAdapter=DBAdapter.getInstance(this);

        btn_signin=findViewById(R.id.btn_signin);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=et_email.getText().toString().trim();
                String pass=et_password.getText().toString().trim();
                if(dbAdapter.CheckIDnPass(email,pass))
                {
                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Invalid Login Detail",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}


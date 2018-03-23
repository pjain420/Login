package in.ac.rungta.startaab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_signup;
    private EditText et_name,et_email,et_password,et_conpassword;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbAdapter=dbAdapter.getInstance(this);


        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        et_conpassword=findViewById(R.id.et_conpassword);
        btn_signup=findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= et_password.getText().toString().trim();
                String email= et_email.getText().toString().trim();
                String pass= et_password.getText().toString().trim();
                String conpass= et_conpassword.getText().toString().trim();
                if (pass.length() != 0 && conpass.length() != 0) {
                    if (pass.equals(conpass)) {
                        if (pass.length() <= 14 && pass.length() >= 6) {
                            if (!dbAdapter.CheckEmailID(email)) {
                                long rowInserted = dbAdapter.insertIntoLogin(name, email, pass);
                                if (rowInserted == -1) {
                                    Toast.makeText(SignUpActivity.this, "Something went Wrong..", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Registered Successfully..", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(SignUpActivity.this, "Email already Exists", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, pass.length(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Password didnt matched =" + conpass + " " + pass, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Password null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

package com.example.onlineshooping.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshooping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {
    private TextView sellerLogin;
    private EditText selLoginEmail, sellLoginPassword;
    private Button sellerLoginBtn;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        mAuth= FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        sellerLogin=(TextView)findViewById(R.id.seller_login_text);
        selLoginEmail=findViewById(R.id.seller_login_email);
        sellLoginPassword=findViewById(R.id.seller_login_password);
        sellerLoginBtn=findViewById(R.id.seller_login);

        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             loginSeller();
            }
        });

        sellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerLoginActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginSeller()
    {
        final String email=selLoginEmail.getText().toString();
        final String password=sellLoginPassword.getText().toString();

        if (!email.equals("") && !password.equals("")) {
            loadingBar.setTitle("Seller Account Login ");
            loadingBar.setMessage("PLease wait,while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                Intent intent=new Intent(SellerLoginActivity.this,SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

        }

    });

        }
        else
        {
            Toast.makeText(this,"Please complete the registration form.",Toast.LENGTH_SHORT).show();
        }

    }
}

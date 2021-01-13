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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private TextView sellerRegister;
    private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    private Button sellerRegBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth= FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        sellerRegister=findViewById(R.id.seller_reg_text);
        nameInput=findViewById(R.id.seller_name);
        phoneInput=findViewById(R.id.seller_phone);
        emailInput=findViewById(R.id.seller_email);
        passwordInput=findViewById(R.id.seller_password);
        addressInput=findViewById(R.id.seller_address);
        sellerRegBtn=findViewById(R.id.seller_register_btn);




        sellerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        sellerRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller()
    {
        final String name=nameInput.getText().toString();
        final String phone=phoneInput.getText().toString();
        final String email=emailInput.getText().toString();
        String password=passwordInput.getText().toString();
        final String address=addressInput.getText().toString();

        if (!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals(""))
        {
            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("PLease wait,while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                final DatabaseReference rootRef;
                                rootRef= FirebaseDatabase.getInstance().getReference();

                                String sid=mAuth.getCurrentUser().getUid();

                                HashMap<String,Object> sellerMap=new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("name",name);
                                sellerMap.put("phone",phone);
                                sellerMap.put("email",email);
                                sellerMap.put("address",address);

                                rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                loadingBar.dismiss();
                                                Toast.makeText(SellerRegistrationActivity.this,"You are Registered Successfully.",Toast.LENGTH_SHORT).show();

                                                Intent intent=new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                            }

                        }
                    });

        }else
        {
            Toast.makeText(this,"Please complete the registration form.",Toast.LENGTH_SHORT).show();
        }
    }
}

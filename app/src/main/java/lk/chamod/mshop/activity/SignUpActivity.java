package lk.chamod.mshop.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.chamod.mshop.R;
import lk.chamod.mshop.databinding.ActivitySignUpBinding;
import lk.chamod.mshop.model.User;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        binding.signupBtnSignup.setOnClickListener(view -> {

            String name = binding.signupInputName.getText().toString().trim();
            String email = binding.signupInputEmail.getText().toString().trim();
            String password = binding.signupInputPassword.getText().toString().trim();
            String repassword = binding.signupReTypePassword.getText().toString().trim();
            if (name.isEmpty()) {
                binding.signupInputName.setError("Email is requried");
                binding.signupInputEmail.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                binding.signupInputEmail.setError("Email is requried");
                binding.signupInputEmail.requestFocus();
                return;
            }
            if (password.length() < 6) {
                binding.signupInputPassword.setError("password must be at least 6 characters");
                binding.signupInputPassword.requestFocus();
                return;
            }
            if (!repassword.equals(password)) {
                binding.signupReTypePassword.setError("password and retype password must be the same");
                binding.signupReTypePassword.requestFocus();
                return;
            }


            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();

                                User user = User.builder().uid(uid).name(name).email(email).build();


                                firebaseFirestore.collection("users").document(uid).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "saved success", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                startActivity(intent);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("FirestoreError", "Error saving user: " + e.getMessage());
                                            }
                                        });
                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(Tag, "createUserWithEmail:success");
//                                FirebaseUser user = firebaseAuth.getCurrentUser();
//                                updateUI(user);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Auth Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                            }
                        }
                    });

        });
    }
}
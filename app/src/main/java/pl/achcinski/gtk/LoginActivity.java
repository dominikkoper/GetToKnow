package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.achcinski.gtk.databinding.ActivityLogRegBinding;
import pl.achcinski.gtk.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
                    startActivity(intent);
                }
            } // sprawdzanie czy dany użytkownik jest już zalogowany, jeśli tak to aktywność zmienia się od razu na mainacitivity
        };

        binding.Login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = binding.emailRegister.getText().toString();
                final String password = binding.passwordRegister.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Błąd logowania", Toast.LENGTH_SHORT).show();
                        }
                    } // logowowanie na przycisku i sprawdzanie czy w bazie danych istnieją określone dane logowania
                });

            }
        });

        binding.emailRegister.addTextChangedListener(loginTextWatcher);                                            // będziemy sprawdzali czy pola email i password są puste czy nie
        binding.passwordRegister.addTextChangedListener(loginTextWatcher);                                         // jesli nie to pozwalamy kliknąć przycisk

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = binding.emailRegister.getText().toString().trim();                                 // trim po to żeby białych znaków nie zaliczało jako napisu
            String passwordInput = binding.passwordRegister.getText().toString().trim();

            binding.Login2.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);  // rozpoczyna nasłuchiwanie zmian uwierzytelniania, daje znać po tym jak nastąpi rejestracja, logowanie, wylogowanie, obceny użytkownik się zmieni
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener); // zatrzymuje nasłuchiwanie zmian uwierzytelniania
    }
}

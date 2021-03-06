package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.achcinski.gtk.databinding.ActivityChatBinding;
import pl.achcinski.gtk.databinding.ActivityLogRegBinding;

public class LogRegActivity extends AppCompatActivity {

    ActivityLogRegBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogRegBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogRegActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogRegActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LogRegActivity.this, LoadingActivity.class);
                    startActivity(intent);
                }
            } // sprawdzanie czy dany u??ytkownik jest ju?? zalogowany, je??li tak to aktywno???? zmienia si?? od razu na mainacitivity
        };

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);  // rozpoczyna nas??uchiwanie zmian uwierzytelniania, daje zna?? po tym jak nast??pi rejestracja, logowanie, wylogowanie, obceny u??ytkownik si?? zmieni
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener); // zatrzymuje nas??uchiwanie zmian uwierzytelniania
    }
}

// Ekran pocz??tkowy do logowania lub rejestracji

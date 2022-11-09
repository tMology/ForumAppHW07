package edu.uncc.hw07;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener, ForumsFragment.ForumsListener, CreateForumFragment.CreateForumListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new ForumsFragment())
                    .commit();
        } else {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();
        }
    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .commit();
    }

    @Override
    public void goToForums() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ForumsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void login() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void logout() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void createForum() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateForumFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToForumAndComments(Forum forum) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ForumFragment.newInstance(forum))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
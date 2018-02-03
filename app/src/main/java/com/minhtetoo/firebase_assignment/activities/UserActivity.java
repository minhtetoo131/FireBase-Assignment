package com.minhtetoo.firebase_assignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhtetoo.firebase_assignment.R;
import com.minhtetoo.firebase_assignment.adapters.UserListAdapter;
import com.minhtetoo.firebase_assignment.data.UserVO;
import com.minhtetoo.firebase_assignment.delegates.UserItemViewDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity implements UserItemViewDelegate ,GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_GOOGLE_SIGN_IN = 101;
    @BindView(R.id.rv_users_list)
    RecyclerView rvUserList;

    UserListAdapter mUserListAdapter;

    FirebaseAuth auth;
    FirebaseUser currentLoginUser;

    protected GoogleApiClient mGoogleApiClient;

    private UserVO mtapedUser;
    private List<UserVO> allUser;
    private UserVO currentUser ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);





        mUserListAdapter = new UserListAdapter(getApplicationContext(),this);
        rvUserList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        rvUserList.setAdapter(mUserListAdapter);
        allUser = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentLoginUser = auth.getCurrentUser();

        if (currentLoginUser != null){
            toolbar.setTitle("Login as" + currentLoginUser.getDisplayName());
        }

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("739990367593-4d1u0t5hh5ost3psq9olo6d9c2rkb40r.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this /*FragmentActivity*/, this /*OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                             allUser.clear();
                        for (DataSnapshot dss: dataSnapshot.getChildren()) {
                            UserVO user = dss.getValue(UserVO.class);
                            allUser.add(user);


                        }

                        mUserListAdapter.setNewData(allUser);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTapItemView(UserVO tapedUser) {
        mtapedUser = tapedUser;

        Toast.makeText(getApplicationContext(),"onTapItemView",Toast.LENGTH_LONG).show();
        if (currentLoginUser == null){
            signInGoogle();
            Toast.makeText(getApplicationContext(),"user is null google signIn",Toast.LENGTH_LONG).show();
        }else {
            startActivity(ChatActivity.newIntent(getApplicationContext(),mtapedUser.uid,mtapedUser.name));
            Toast.makeText(getApplicationContext(),"user is not null startActivity",Toast.LENGTH_LONG).show();
        }
    }

    public void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
        Toast.makeText(getApplicationContext(),"startActivityForResult",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getApplicationContext(),"onActivityResult",Toast.LENGTH_LONG).show();
        if (requestCode == RC_GOOGLE_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            processGoogleSignInResult(result);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processGoogleSignInResult(GoogleSignInResult signInResult) {
        if (signInResult.isSuccess()) {
            // Google Sign-In was successful, authenticate with Firebase
            GoogleSignInAccount account = signInResult.getSignInAccount();
            onSuccessGoogleSignIn(account);
        } else {
            // Google Sign-In failed

        }
    }

    public void onSuccessGoogleSignIn(GoogleSignInAccount signInAccount) {
        authenticateUserWithGoogleAccount(signInAccount);
    }

    public void authenticateUserWithGoogleAccount(final GoogleSignInAccount signInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {


                        } else {
                            currentLoginUser = auth.getCurrentUser();
                            addUserToDatabase(currentLoginUser);

                          ChatActivity.newIntent(getApplicationContext(),mtapedUser.uid,mtapedUser.name);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });
    }

    public void addUserToDatabase(final FirebaseUser firebaseUser) {
        firebaseUser.reload()
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        currentUser = new UserVO(null,firebaseUser.getDisplayName(),null,
                                firebaseUser.getUid(),
                                firebaseUser.getEmail());
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("users")
                                .child(firebaseUser.getUid())
                                .setValue(currentUser)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {
// successfully added user
                                            ChatActivity.newIntent(getApplicationContext(),mtapedUser.uid,mtapedUser.name);
                                        } else {
                                            Toast.makeText(getApplicationContext(),"addUserToDatabase unsuccessful",Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                    }});
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

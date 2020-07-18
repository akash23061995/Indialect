package com.aks.indialect;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.transition.Slide;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    SliderView sliderView;
    List<ImageSliderModel> imageSliderModelList;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    TextView userview;
    ImageView profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();

       imageSliderModelList = new ArrayList<>();
       sliderView=findViewById(R.id.imageSlider);
       imageSliderModelList.add(new ImageSliderModel(R.drawable.app_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.amer_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.golden_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.ganga_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.taj_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.chinar_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.jama_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.fort_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.khajuraho_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.matangeshwar_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.mauli_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.visrupeksha_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.hampi_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.hamdan_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.hunza_banner));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.hampi2_banner));

       sliderView.setSliderAdapter(new ImageSliderAdapter(this,imageSliderModelList));
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.startAutoCycle();

//        sliderView.setSliderAdapter(new ImageSliderAdapter(getActivity(),TotalCounts));
//        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    // user is signed in
                    onSignedInInitialize(user.getDisplayName());
                    profilepic = findViewById(R.id.accountpic);
                    Uri photouri = user.getPhotoUrl();
                    if(photouri!=null)
                        Picasso.with(MainActivity.this).load(photouri).into(profilepic);
                    else
                        profilepic.setImageResource(R.drawable.ic_icon_indialect);
                    if(mUsername!=null)
                      userview = findViewById(R.id.userview);
                    userview.setText(""+mUsername);
                    userview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                   Intent i = new Intent(MainActivity.this,Profile.class);
                   startActivity(i);
                        }
                    });
                    profilepic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MainActivity.this,Profile.class);
                            startActivity(i);
                          }
                    });
                }
                else
                {
             // user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.ic_icon_indialect)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        Toolbar toolbar = findViewById(R.id.toolbar); //get the actionbar
        setSupportActionBar(toolbar);
   //        action.setCustomView(R.layout.toolbar);//add the custom view
//        action.setDisplayShowTitleEnabled(false); //hide the title
//        TextView label = (TextView) action.getCustomView().findViewById(android.R.id.text1);

    }

    private void onSignedOutCleanup() {
      mUsername = ANONYMOUS;
    }

     private void onSignedInInitialize(String userName) {
    mUsername = userName;
    }

    @Override
    protected void onResume() {
        super.onResume();
      mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
     mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
                if(resultCode==RESULT_OK){
                        Toast.makeText(this,"Signed in",Toast.LENGTH_SHORT).show();
                }
                else
                {
                     Toast.makeText(this,"Sign in cancelled",Toast.LENGTH_SHORT).show();
                     finish();
                }
        }
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
        if (id == R.id.profile_menu) {
            // profile
            Intent i = new Intent(MainActivity.this,Profile.class);
            startActivity(i);
             return true;
        }
        if (id == R.id.sign_out_menu) {
            // sign out
            AuthUI.getInstance().signOut(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

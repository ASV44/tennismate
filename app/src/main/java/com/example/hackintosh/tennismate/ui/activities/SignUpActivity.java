package com.example.hackintosh.tennismate.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.hackintosh.tennismate.R;
import com.example.hackintosh.tennismate.dto.LevelEnum;
import com.example.hackintosh.tennismate.dto.User;
import com.example.hackintosh.tennismate.service.UserService;
import com.example.hackintosh.tennismate.ui.navigation.Navigator;
import com.example.hackintosh.tennismate.ui.presenters.SignUpPresenter;
import com.example.hackintosh.tennismate.ui.view.SingnUpView;
import com.example.hackintosh.tennismate.utils.CircleTransform;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by maxim on 11/11/17.
 */
public class SignUpActivity extends BaseActivity<SingnUpView, SignUpPresenter> implements SingnUpView {
    private List<String> levels = new ArrayList<>(Arrays.asList(LevelEnum.BEGINNER.getLevel(), LevelEnum.INTERMEDIATE.getLevel(), LevelEnum.ADVANCED.getLevel()));

    @BindView(R.id.level_spinner)
    public Spinner levelSpinner;

    @BindView(R.id.age_spinner)
    public Spinner ageSpinner;

    @BindView(R.id.full_name_editText)
    public EditText fullNameEditText;

    @BindView(R.id.signup_button)
    public Button button;

    @BindView(R.id.profile_imageView)
    public ImageView profileImageView;

    private Target target;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForExistingUser();
        setContentView(R.layout.activity_sign_up);
        setPresenter();
        ButterKnife.bind(this);
        populateView();
    }

    public void setPresenter() {
        super.setPresenter(new SignUpPresenter(new Navigator(this)));
        presenter.bind(this);
    }

    private void populateView() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fullNameEditText.setText(currentUser.getDisplayName());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, levels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(dataAdapter);

        List<Integer> ageList = new ArrayList<>();
        for(int i = 7; i < 60; i++) {
            ageList.add(i);
        }
        ArrayAdapter<Integer> ageAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, ageList);

        ageSpinner.setAdapter(ageAdapter);
        loadCircleImage(currentUser);
    }

    private void loadCircleImage(FirebaseUser currentUser) {
        Picasso.with(this).load(getUserPictureUrl(currentUser))
                .transform(new CircleTransform(true))
                .into(profileImageView);
    }

    private String getUserPictureUrl(FirebaseUser firebaseUser) {
       return firebaseUser.getPhotoUrl().toString().replaceAll("s\\d+\\-", "s600-");
    }

    private void checkForExistingUser() {
        UserService userService = new UserService();
        userService.getUserDetails(user -> {
            if(user != null) {
                this.presenter.navigator.openPlanGame();
            }
        }, s -> {});
    }

    private User populateUser() {
        User user = new User();
        user.setFullName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        user.setAge((Integer) ageSpinner.getSelectedItem());
        user.setImageUrl(getUserPictureUrl(FirebaseAuth.getInstance().getCurrentUser()));
        user.setLevel(LevelEnum.ADVANCED);
        user.setUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return user;
    }

    @OnClick(R.id.signup_button)
    public void registerUser() {
        this.presenter.registerUser(populateUser());
    }

    @Override
    public void onPostSuccess() {
        super.presenter.navigator.openPlanGame();
    }
}

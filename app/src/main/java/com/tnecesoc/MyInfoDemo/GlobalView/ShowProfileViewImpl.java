package com.tnecesoc.MyInfoDemo.GlobalView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tnecesoc.MyInfoDemo.Entity.Profile;
import com.tnecesoc.MyInfoDemo.GlobalModel.Local.LocalContactsHelper;
import com.tnecesoc.MyInfoDemo.GlobalModel.Local.SessionHelper;
import com.tnecesoc.MyInfoDemo.GlobalModel.Remote.Host;
import com.tnecesoc.MyInfoDemo.GlobalView.Interfaces.IFollowView;
import com.tnecesoc.MyInfoDemo.GlobalView.Interfaces.IShowProfileView;
import com.tnecesoc.MyInfoDemo.GlobalView.Tasks.FetchProfileTask;
import com.tnecesoc.MyInfoDemo.GlobalView.Tasks.FollowContactTask;
import com.tnecesoc.MyInfoDemo.GlobalView.Tasks.UnFollowContactTask;
import com.tnecesoc.MyInfoDemo.Modules.Homepage.Tasks.SyncAvatarTask;
import com.tnecesoc.MyInfoDemo.Modules.ProfileModule.Messages.PrivateChatActivity;
import com.tnecesoc.MyInfoDemo.Modules.ProfileModule.Messages.PrivateChatAdapter;
import com.tnecesoc.MyInfoDemo.R;

public class ShowProfileViewImpl extends AppCompatActivity implements IShowProfileView, IFollowView {

    public static final int RESULT_FOLLOW_STATE_CHANGED = 1;
    public static final int RESULT_FOLLOW_STATE_NOT_CHANGED = 0;

    private TextView lbl_username;
    private TextView lbl_nickname;
    private TextView lbl_motto;
    private SimpleDraweeView img_avatar;
    private ImageView img_gender;

    private TextView lbl_contacts_count, lbl_join_group_count, lbl_posts_count;

    private TextView lbl_community;
    private TextView lbl_phone;
    private TextView lbl_email;
    private TextView lbl_address;

    private Button btn_contact;

    private LocalContactsHelper helper;

    @Override
    public void showFollowSucceed() {
        refreshContactProfile();
        Toast.makeText(this, R.string.follow_succeed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFollowFailed() {

    }

    @Override
    public void showUnfollowSucceed() {
        refreshContactProfile();
        Toast.makeText(this, R.string.unfollow_succeed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUnfollowFailed() {

    }

    @Override
    public void showCommunicationData(int contacts, int messages, int posts) {
        lbl_contacts_count.setText(String.valueOf(contacts));
        lbl_join_group_count.setText(String.valueOf(messages));
        lbl_posts_count.setText(String.valueOf(posts));
    }

    @SuppressWarnings({"Duplicates", "ConstantConditions"})
    @Override
    public void showProfile(final Profile profile, Profile.Category category) {
        String displayUsername = "@" + profile.getUsername();
        lbl_username.setText(displayUsername);
        lbl_nickname.setText(profile.getNickname());
        lbl_motto.setText(profile.getMotto());

        Uri avatar_uri = Uri.parse(Host.findAvatarUrlByUsername(profile.getUsername()));

        new SyncAvatarTask(img_avatar, avatar_uri).execute();

        switch (profile.getGender()) {
            case "MALE":
                img_gender.setImageResource(R.drawable.male);
                break;
            case "FEMALE":
                img_gender.setImageResource(R.drawable.female);
                break;
            default:
                img_gender.setVisibility(View.GONE);
                break;
        }

        lbl_community.setText(profile.getCommunity());
        lbl_phone.setText(profile.getPhone());
        lbl_email.setText(profile.getEmail());
        lbl_address.setText(profile.getAddress());

        if (category == Profile.Category.FRIEND || category == Profile.Category.FOLLOW) {
            btn_contact.setText(getString(R.string.hint_unfollow_contact));
            btn_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doUnfollowContact();
                }
            });
        } else {
            btn_contact.setText(getString(R.string.hint_follow_contact));
            btn_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFollowContact();
                }
            });
        }

        findViewById(R.id.btn_contact_profile_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPrivateChatWith(profile);
            }
        });

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showFetchDataFailed() {
        Toast.makeText(this, R.string.hint_network_unavailable, Toast.LENGTH_SHORT).show();
    }

    private void doPrivateChatWith(Profile profile) {
        Intent intent = new Intent(ShowProfileViewImpl.this, PrivateChatActivity.class);
        intent.putExtra(PrivateChatActivity.KEY_CONTACT_PROFILE_TALK_WITH, profile);
        startActivity(intent);
    }

    private void onFollowContact() {
        String me = new SessionHelper(this).getSessionAttribute(SessionHelper.KEY_USERNAME);
        String other = lbl_username.getText().toString();
        other = other.substring(1);
        new FollowContactTask(this, helper).execute(me, other);
        setResult(RESULT_FOLLOW_STATE_CHANGED);
    }


    private void doUnfollowContact() {
        String me = new SessionHelper(this).getSessionAttribute(SessionHelper.KEY_USERNAME);
        String other = lbl_username.getText().toString();
        other = other.substring(1);
        new UnFollowContactTask(this, helper).execute(me, other);
        setResult(RESULT_FOLLOW_STATE_CHANGED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        initializeUI();

        refreshContactProfile();

        setResult(RESULT_FOLLOW_STATE_NOT_CHANGED);

        helper = new LocalContactsHelper(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    private void initializeUI() {
        lbl_username = (TextView) findViewById(R.id.lbl_profile_page_username);
        lbl_nickname = (TextView) findViewById(R.id.lbl_profile_page_nickname);
        lbl_motto = (TextView) findViewById(R.id.lbl_profile_page_motto);
        img_avatar = (SimpleDraweeView) findViewById(R.id.img_profile_page_avatar);
        img_gender = (ImageView) findViewById(R.id.img_profile_page_gender);
        lbl_community = (TextView) findViewById(R.id.lbl_contact_profile_community);
        lbl_phone = (TextView) findViewById(R.id.lbl_contact_profile_phone);
        lbl_email = (TextView) findViewById(R.id.lbl_contact_profile_email);
        lbl_address = (TextView) findViewById(R.id.lbl_contact_profile_address);

        lbl_contacts_count = (TextView) findViewById(R.id.lbl_profile_page_contacts_count);
        lbl_join_group_count = (TextView) findViewById(R.id.lbl_profile_page_join_groups);
        lbl_posts_count = (TextView) findViewById(R.id.lbl_profile_page_posts_count);

        btn_contact = (Button) findViewById(R.id.btn_contact_profile_contact);
    }

    private void refreshContactProfile() {

        String usernameWanted = getIntent().getExtras().getString("username", null);
        if (usernameWanted != null) {
            new FetchProfileTask(this).execute(usernameWanted);
        } else {
            finish();
        }

    }
}

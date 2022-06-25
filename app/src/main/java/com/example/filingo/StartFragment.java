package com.example.filingo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;

public class StartFragment extends Fragment {

    public View rootView;
    private Uri icnUri;
    private ShapeableImageView userImg;
    private EditText nameOfUser;
    private AppCompatButton goButton;
    private Context thiscontext;

    public static final String ICN_URI = "icnUri";
    public static final String USER_NAME = "userName";

    public StartFragment() { }

    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.start_app_layout, container, false);

        userImg = rootView.findViewById(R.id.start_app_user_icn);
        nameOfUser = rootView.findViewById(R.id.start_app_name_of_user);
        goButton = rootView.findViewById(R.id.start_app_go_button);
        thiscontext = container.getContext();



        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(MainActivity.SHARED_PREFS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(icnUri != null && !icnUri.toString().equals(""))
                    editor.putString("icnUri",icnUri.toString());
                else
                    editor.putString("icnUri","");

                if(nameOfUser == null || nameOfUser.getText().toString().equals(""))
                    editor.putString("userName","Andy");
                else
                    editor.putString("userName",nameOfUser.getText().toString());
                editor.apply();
                onDestroyView();

                ((MainActivity)getActivity()).displayMainInfoFragment();
            }
        });

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(StartFragment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShapeableImageView userImg = rootView.findViewById(R.id.start_app_user_icn);
        Uri uri = data.getData();
        icnUri = uri;
        userImg.setImageURI(uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
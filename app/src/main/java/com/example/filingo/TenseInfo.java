package com.example.filingo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;


public class TenseInfo extends Fragment{

        public View rootView;
        private Context thiscontext;
        public static Integer tenseNamePos;

        public static TenseInfo newInstance(Integer tenseNamePos) {
                TenseInfo.tenseNamePos = tenseNamePos;
                TenseInfo fragment = new TenseInfo();
                return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                rootView = inflater.inflate(R.layout.tense_info, container, false);
                TextView tenseInfoText = rootView.findViewById(R.id.top_tense_name);

                ShapeableImageView image = rootView.findViewById(R.id.info_tense_image);


                //Need to Add some info for each tense
                tenseInfoText.setText(TensesInfo.listOfTenses.get(tenseNamePos));
                //Uri imgUri = Uri.parse("file:///res/drawable/" + TensesInfo.listOfTenses.get(tenseNamePos) + ".jpg");
                Uri imgUri = Uri.parse("file:///data/data/assets/grammar_info/" + TensesInfo.listOfTenses.get(tenseNamePos) + ".jpg");

                // get input stream
                InputStream ims = null;
                try {
                        ims = getActivity().getAssets().open("grammar_info/"+ TensesInfo.listOfTenses.get(tenseNamePos) + ".jpg");
                        Drawable d = Drawable.createFromStream(ims, null);
                        image.setImageDrawable(d);
                } catch (IOException e) {
                        e.printStackTrace();
                }


                thiscontext = container.getContext();
                return rootView;
        }

        @Override
        public void onDestroyView() {
                super.onDestroyView();
        }

}
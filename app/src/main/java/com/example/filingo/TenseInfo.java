package com.example.filingo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


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

                //Need to Add some info for each tense
                tenseInfoText.setText(TensesInfo.listOfTenses.get(tenseNamePos));
                thiscontext = container.getContext();
                return rootView;
        }

        @Override
        public void onDestroyView() {
                super.onDestroyView();
        }

}
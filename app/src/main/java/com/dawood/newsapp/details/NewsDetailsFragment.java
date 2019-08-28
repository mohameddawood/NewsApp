package com.dawood.newsapp.details;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.dawood.newsapp.MainActivity;
import com.dawood.newsapp.R;
import com.dawood.newsapp.Utils;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailsFragment extends Fragment {


    @BindView(R.id.headText) TextView headText;
    @BindView(R.id.authorText) TextView authorText;
    @BindView(R.id.newsDetailsText) TextView newsDetailsText;
    @BindView(R.id.sourceUrl) TextView sourceUrl;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_details, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewsDetailsFragmentArgs args = NewsDetailsFragmentArgs.fromBundle(getArguments());
        if (!TextUtils.isEmpty(args.getDetails().getTitle()))
            headText.setText(args.getDetails().getTitle());
        else headText.setText("_");
        if (!TextUtils.isEmpty(args.getDetails().getAuthor()))
            authorText.setText("By:" + args.getDetails().getAuthor());
        else authorText.setText("-");
        newsDetailsText.setText(args.getDetails().getContent());
        sourceUrl.setText("Source : " + args.getDetails().getUrl());
        ((MainActivity) getActivity()).setActionBarTitle(args.getDetails().getSource().getName());

        sourceUrl.setOnClickListener(v -> Utils.goToBrowser(args.getDetails().getUrl(), getContext()));

    }


}

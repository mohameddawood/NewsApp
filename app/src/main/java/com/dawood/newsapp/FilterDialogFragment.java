package com.dawood.newsapp;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterDialogFragment extends DialogFragment {

    @BindView(R.id.imageView) ImageView close;
    @BindView(R.id.newsRadio) RadioButton newsSourceRadio;
    @BindView(R.id.countryRadio) RadioButton countryRadio;
    @BindView(R.id.countrySpinner) Spinner countrySpinner;
    @BindView(R.id.newsSpinner) Spinner newsSpinner;
    @BindView(R.id.filterBtn) Button filterBtn;
    @BindView(R.id.cancelBtn) Button cancelBtn;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    private List<String> sourcesNames;
    private List<String> sourcesIds;
    private FilterAction filterAction;
    private String item = "";
    private String sourceId = "";

    public FilterDialogFragment(List<String> sourcesNames, List<String> sourcesIds, FilterAction filterAction) {
        this.sourcesNames = sourcesNames;
        this.sourcesIds = sourcesIds;
        this.filterAction = filterAction;
    }

    public FilterDialogFragment() {

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_filter, container, false);
        ButterKnife.bind(this, v);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return v;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] arr = getActivity().getResources().getStringArray(R.array.countries);
        setSpinnerAdapter(countrySpinner, Arrays.asList(arr));
        setSpinnerAdapter(newsSpinner, sourcesNames);

        close.setOnClickListener(v -> getDialog().cancel());
        cancelBtn.setOnClickListener(v -> getDialog().cancel());

        doFilter();
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, data);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int checkedId = radioGroup.getCheckedRadioButtonId();
                switch (checkedId) {
                    case R.id.countryRadio:
                        item = parent.getSelectedItem().toString();
                        break;
                    case R.id.newsRadio:
                        try {
                            int d = radioGroup.getCheckedRadioButtonId();
                            if (d == R.id.newsRadio)
                                sourceId = sourcesIds.get(parent.getSelectedItemPosition());
                            else sourceId = "-1";
                        } catch (IndexOutOfBoundsException e) {
                            sourceId = "-1";
                        }
                        break;
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void doFilter() {
        filterBtn.setOnClickListener(v1 -> {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            switch (checkedId) {
                case R.id.countryRadio:
                    if (item.length() == 2) {
                        filterAction.onCountrySelected(item);
                        getDialog().cancel();
                    } else
                        Toast.makeText(getContext(), "please choose a country", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.newsRadio:
                    if (!sourceId.equals("-1")) {
                        filterAction.onNewsSelected(sourceId);
                        getDialog().cancel();
                    } else
                        Toast.makeText(getContext(), "please choose a news source", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public interface FilterAction {
        void onCountrySelected(String country);

        void onNewsSelected(String sourceId);
    }
}

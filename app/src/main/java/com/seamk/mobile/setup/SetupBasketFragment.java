package com.seamk.mobile.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;
import com.seamk.mobile.R;
import com.seamk.mobile.elasticsearch.Bucket;
import com.seamk.mobile.elasticsearch.ElasticStudentGroups;
import com.seamk.mobile.elasticsearch.ElasticTeachers;
import com.seamk.mobile.interfaces.callback.ElasticStudentGroupsCallback;
import com.seamk.mobile.interfaces.callback.ElasticTeachersCallback;
import com.seamk.mobile.objects.BasketSavedItems;
import com.seamk.mobile.objects.BasketStudentGroup;
import com.seamk.mobile.objects.BasketTeacher;
import com.seamk.mobile.objects.StudentGroup;
import com.seamk.mobile.objects.Teacher;
import com.seamk.mobile.retrofit.RetroFitters;
import com.seamk.mobile.util.Common;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class SetupBasketFragment extends Fragment implements SlidePolicy {

    @BindView(R.id.setup_basket_codes_names_autocomplete)
    AutoCompleteTextView codesNamesAutoComplete;
    @BindView(R.id.setup_basket_restaurants_spinner)
    Spinner restaurantsSpinner;
    @BindView(R.id.setup_basket_codes_names_textview)
    TextView codesNamesTextView;
    @BindView(R.id.setup_basket_retry_button)
    Button retryButton;
    @BindView(R.id.setup_basket_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.setup_basket_student_radiobutton)
    AppCompatRadioButton studentRadioButton;
    @BindView(R.id.setup_basket_teacher_radiobutton)
    AppCompatRadioButton teacherRadioButton;
    @BindView(R.id.setup_basket_skip_checkbox)
    AppCompatCheckBox skipCheckBox;

    List<StudentGroup> studentGroupCodesList = new ArrayList<>();
    List<Teacher> teacherNamesList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_basket_fragment, container, false);
        ButterKnife.bind(this, v);

        studentRadioButton.setOnClickListener(view -> {
            //TODO fill autocompletetextview with student codes
        });

        teacherRadioButton.setOnClickListener(view -> {
            //TODO fill autocompletetextview with teacher names
        });

        retryButton.setOnClickListener(view -> {
            beginFetch();
        });

        beginFetch();

        return v;
    }

    public void itemSelected(String item, boolean student) {
        BasketSavedItems basketSavedItems = new BasketSavedItems();

        List<BasketStudentGroup> studentGroupBaskets = new ArrayList<>();
        List<BasketTeacher> basketTeachers = new ArrayList<>();

        if (student) {
            studentGroupBaskets.add(new BasketStudentGroup(item, "0", new ArrayList<>()));
        } else {
            basketTeachers.add(new BasketTeacher(item, "", new ArrayList<>()));
        }

        basketSavedItems.setBasketStudentGroups(studentGroupBaskets);
        basketSavedItems.setBasketStudentGroupsRealizations(new ArrayList<>());
        basketSavedItems.setBasketRealizations(new ArrayList<>());
        basketSavedItems.setBasketTeachers(basketTeachers);
        basketSavedItems.setBasketTeacherRealizations(new ArrayList<>());

        Common.saveStudyBasket(basketSavedItems, getActivity());
    }

    void beginFetch() {
        try {
            RetroFitters.fetchElasticStudentGroups(getActivity(), 180, new ElasticStudentGroupsCallback() {
                @Override
                public void onSuccess(ElasticStudentGroups elasticStudentGroups) {
                    finishedFetching(elasticStudentGroups);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    finishedFetching(throwable);
                }
            });
            RetroFitters.fetchElasticTeachers(getActivity(), 180, new ElasticTeachersCallback() {
                @Override
                public void onSuccess(ElasticTeachers elasticTeachers) {
                    finishedFetching(elasticTeachers);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    finishedFetching(throwable);
                }
            });
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    void finishedFetching(Throwable throwable){
        //TODO Show error and retry button
    }

    void finishedFetching(ElasticStudentGroups elasticStudentGroups){
        for (Bucket bucket : elasticStudentGroups.getAggregations().getMostReservedStudentGroups().getBuckets() ) {
            studentGroupCodesList.add(new StudentGroup(bucket.getKey()));
        }
        Collections.sort(studentGroupCodesList, (Comparator<Object>) (obj1, obj2) -> ((StudentGroup)obj1).getStudentGroupCode().compareToIgnoreCase(((StudentGroup)obj2).getStudentGroupCode()));
    }

    void finishedFetching(ElasticTeachers elasticTeachers){
        for (Bucket bucket : elasticTeachers.getAggregations().getMostReservedTeachers().getBuckets() ) {
            teacherNamesList.add(new Teacher(bucket.getKey()));
        }
        Collections.sort(teacherNamesList, (Comparator<Object>) (obj1, obj2) -> ((Teacher)obj1).getName().compareToIgnoreCase(((Teacher)obj2).getName()));
    }

    @Override
    public boolean isPolicyRespected() {
        //TODO
        return skipCheckBox.isChecked();// || ;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        //TODO
        Toasty.info(getActivity(), getResources().getString(R.string.please_choose), Toast.LENGTH_SHORT, true).show();
    }
}
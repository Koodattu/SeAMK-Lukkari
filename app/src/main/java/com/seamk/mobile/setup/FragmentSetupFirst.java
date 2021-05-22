package com.seamk.mobile.setup;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlidePolicy;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.itemanimators.SlideUpAlphaAnimator;
import com.seamk.mobile.R;
import com.seamk.mobile.elasticsearch.Bucket;
import com.seamk.mobile.elasticsearch.ElasticStudentGroups;
import com.seamk.mobile.elasticsearch.ElasticTeachers;
import com.seamk.mobile.eventbusevents.NextSlideEvent;
import com.seamk.mobile.eventbusevents.StudentGroupEvent;
import com.seamk.mobile.interfaces.callback.ElasticStudentGroupsCallback;
import com.seamk.mobile.interfaces.callback.ElasticTeachersCallback;
import com.seamk.mobile.objects.BasketRealization;
import com.seamk.mobile.objects.BasketSavedItems;
import com.seamk.mobile.objects.BasketStudentGroup;
import com.seamk.mobile.objects.BasketTeacher;
import com.seamk.mobile.objects.StudentGroup;
import com.seamk.mobile.objects.Teacher;
import com.seamk.mobile.retrofit.RetroFitters;
import com.seamk.mobile.util.Common;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by Juha Ala-Rantala on 18.9.2017.
 */

// hakee ryhmien tunnukset tai opettajien nimet

public class FragmentSetupFirst extends Fragment implements ISlidePolicy, View.OnClickListener {

    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.setup_second_title) TextView titleText;
    @BindView(R.id.desc_text) TextView textView;
    @BindView(R.id.button_retry) Button bRetry;
    @BindView(R.id.progressbar) ProgressBar progressbar;
    @BindView(R.id.button_save_student_group) Button bSaveSG;
    @BindView(R.id.edit_text_view) EditText editText;
    @BindView(R.id.rb_student) AppCompatRadioButton radioButtonStudent;
    @BindView(R.id.rb_teacher) AppCompatRadioButton radioButtonTeacher;
    @BindView(R.id.linear_layout_setup_swipe) LinearLayout linearLayout;

    GridLayoutManager glm;

    private List<IItem> objectsListAll;
    //private List<IItem> objectsListShown;

    ItemAdapter<IItem> itemAdapter;
    FastAdapter<IItem> fastAdapter;

    int typeSelected; // oletuksena 0 eli opiskelijaryhmät
    boolean canContinue = false;
    int lastChecked;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setup_first, container, false);
        ButterKnife.bind(this, v);

        textView.setText(R.string.setup_first_text_desc);

        editText.setAlpha(0.5f);
        bSaveSG.setAlpha(0.5f);
        linearLayout.setAlpha(0);

        radioButtonStudent.setOnClickListener(this);
        radioButtonTeacher.setOnClickListener(this);

        glm = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(glm);
        recyclerView.setItemAnimator(new SlideUpAlphaAnimator());
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        itemAdapter.getItemFilter().withFilterPredicate(new IItemAdapter.Predicate<IItem>() {
            @Override
            public boolean filter(@NonNull IItem item, CharSequence constraint) {
                if (item instanceof StudentGroup) {
                    return ((StudentGroup)item).getStudentGroupCode().contains(String.valueOf(constraint).toUpperCase());
                } else if (item instanceof Teacher) {
                    return ((Teacher)item).getName().toUpperCase().contains(String.valueOf(constraint).toUpperCase());
                } else {
                    return false;
                }
            }
        });
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View view, @NonNull IAdapter<IItem> adapter, @NonNull IItem item, int position) {
                if (item instanceof StudentGroup){
                    itemSelected(((StudentGroup)item).getStudentGroupCode());
                } else {
                    itemSelected(((Teacher)item).getName());
                }
                Common.hideKeyboard((AppCompatActivity)getActivity());
                editText.clearFocus();
                return true;
            }
        });

        bRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bRetry.setVisibility(View.GONE);
                progressbar.setVisibility(View.VISIBLE);
                textView.setText(R.string.setup_second_wait_for_fetch);
                beginFetch();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemAdapter.filter(editable.toString());
            }
        });

        bSaveSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().length() == 0) {
                    //Toast.makeText(getContext(), R.string.text_box_is_empty, Toast.LENGTH_SHORT).show();
                    Toasty.info(getContext(), getResources().getString(R.string.text_box_is_empty), Toast.LENGTH_SHORT, true).show();
                } else {
                    Common.hideKeyboard((AppCompatActivity)getActivity());
                    editText.clearFocus();
                    itemSelected(editText.getText().toString().toUpperCase());
                    EventBus.getDefault().post(new NextSlideEvent());
                }
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        // turhaa ladataan uudelleen jos kyseinen rb on jo valittu

        if (view.getId() == R.id.rb_student && !(view.getId() == lastChecked) || view.getId() == R.id.rb_teacher && !(view.getId() == lastChecked)){
            typeSelected = radioButtonStudent.isChecked() ? 0 : 1;
            lastChecked = view.getId();
            glm.setSpanCount(typeSelected == 0 ? 3 : 2);
            editText.setAlpha(1f);
            bSaveSG.setAlpha(1f);
            editText.setEnabled(true);
            bSaveSG.setEnabled(true);
            editText.setText(null);
            linearLayout.setVisibility(View.GONE);
            linearLayout.setAlpha(0f);
            radioButtonStudent.setEnabled(false);
            radioButtonTeacher.setEnabled(false);
            progressbar.setVisibility(View.VISIBLE);
            beginFetch();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectsListAll = new ArrayList<>();
        //objectsListShown = new ArrayList<>();
    }

    public void itemSelected(String item) {
        BasketSavedItems basketSavedItems = new BasketSavedItems();

        List<BasketStudentGroup> studentGroupBaskets = new ArrayList<>();
        List<BasketTeacher> basketTeachers = new ArrayList<>();

        switch (typeSelected) {
            case 0:
                studentGroupBaskets.add(new BasketStudentGroup(item, "0", new ArrayList<BasketRealization>()));
                break;
            case 1:
                basketTeachers.add(new BasketTeacher(item, "", new ArrayList<BasketRealization>()));
                break;
        }

        basketSavedItems.setBasketStudentGroups(studentGroupBaskets);
        basketSavedItems.setBasketStudentGroupsRealizations(new ArrayList<BasketRealization>());
        basketSavedItems.setBasketRealizations(new ArrayList<BasketRealization>());
        basketSavedItems.setBasketTeachers(basketTeachers);
        basketSavedItems.setBasketTeacherRealizations(new ArrayList<BasketRealization>());

        Common.saveStudyBasket(basketSavedItems, getActivity());
        editText.setText(item);
        EventBus.getDefault().post(new StudentGroupEvent(item));
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.animate().alpha(1.0f).setDuration(500).start();
        canContinue = true;
    }

    void beginFetch(){
        objectsListAll = new ArrayList<>();
        itemAdapter.clear();

        try {
            //fetchElastic();
            switch (typeSelected) {
                case 0:
                    RetroFitters.fetchElasticStudentGroups(getActivity(), 180, new ElasticStudentGroupsCallback(){
                        @Override
                        public void onSuccess(ElasticStudentGroups elasticStudentGroups) {
                            finishedFetching(elasticStudentGroups);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            finishedFetching(throwable);
                        }
                    });
                    break;
                case 1:
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
                    break;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    void finishedFetching(Throwable throwable){
        textView.setText(R.string.setup_second_error_occurred);
        bRetry.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
        radioButtonStudent.setEnabled(true);
        radioButtonTeacher.setEnabled(true);
    }

    void finishedFetching(ElasticTeachers elasticTeachers){
        objectsListAll = new ArrayList<>();

        for (Bucket bucket : elasticTeachers.getAggregations().getMostReservedTeachers().getBuckets() ) {
            objectsListAll.add(new Teacher(bucket.getKey()));
        }

        Collections.sort(objectsListAll, new Comparator<Object>(){
            public int compare(Object obj1, Object obj2) {
                return ((Teacher)obj1).getName().compareToIgnoreCase(((Teacher)obj2).getName());
            }
        });

        //objectsListShown = new ArrayList<>(objectsListAll);
        //itemAdapter.add(objectsListShown);

        itemAdapter.add(objectsListAll);

        progressbar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        textView.setText(R.string.setup_second_help_text);

        radioButtonStudent.setEnabled(true);
        radioButtonTeacher.setEnabled(true);
    }

    void finishedFetching(ElasticStudentGroups elasticStudentGroups){

        objectsListAll = new ArrayList<>();

        for (Bucket bucket : elasticStudentGroups.getAggregations().getMostReservedStudentGroups().getBuckets() ) {
            objectsListAll.add(new StudentGroup(bucket.getKey()));
        }

        Collections.sort(objectsListAll, new Comparator<Object>(){
            public int compare(Object obj1, Object obj2) {
                return ((StudentGroup)obj1).getStudentGroupCode().compareToIgnoreCase(((StudentGroup)obj2).getStudentGroupCode());
            }
        });

        //objectsListShown = new ArrayList<>(objectsListAll);
        //itemAdapter.add(objectsListShown);

        itemAdapter.add(objectsListAll);

        progressbar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        textView.setText(R.string.setup_second_help_text);


        radioButtonStudent.setEnabled(true);
        radioButtonTeacher.setEnabled(true);
    }

    @Override
    public boolean isPolicyRespected() {
        return canContinue;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        // User illegally requested next slide
        //Toast.makeText(getContext(), getResources().getString(R.string.answer_both_questions), Toast.LENGTH_SHORT).show();
        Toasty.info(getActivity(), getResources().getString(R.string.please_choose), Toast.LENGTH_SHORT, true).show();
    }
}

package com.seamk.mobile.studybasket;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.seamk.mobile.R;
import com.seamk.mobile.adapters.StudyBasketSearchAdapter;
import com.seamk.mobile.elasticsearch.Bucket;
import com.seamk.mobile.elasticsearch.ElasticRealization;
import com.seamk.mobile.elasticsearch.ElasticStudentGroups;
import com.seamk.mobile.elasticsearch.ElasticTeachers;
import com.seamk.mobile.elasticsearch.HitRealization;
import com.seamk.mobile.elasticsearch.Teacher;
import com.seamk.mobile.eventbusevents.ItemAddedEvent;
import com.seamk.mobile.interfaces.callback.ElasticRealizationCallback;
import com.seamk.mobile.interfaces.callback.ElasticStudentGroupsCallback;
import com.seamk.mobile.interfaces.callback.ElasticTeachersCallback;
import com.seamk.mobile.objects.BasketRealization;
import com.seamk.mobile.objects.BasketSavedItems;
import com.seamk.mobile.objects.BasketStudentGroup;
import com.seamk.mobile.objects.BasketTeacher;
import com.seamk.mobile.retrofit.RetroFitters;
import com.seamk.mobile.util.Common;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by Juha Ala-Rantala on 18.3.2018.
 */

public class ActivityBasketAddItem extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.b_search) Button bSearch;
    @BindView(R.id.et_search) EditText editText;
    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.progressbar) ProgressBar progressBar;
    @BindView(R.id.empty_view) TextView emptyTextView;
    @BindView(R.id.first_text_view) TextView textViewFirst;
    @BindView(R.id.rg_type_of_search) RadioGroup radioGroup;

    private static final int SEARCH_TYPE_STUDENT_GROUP = 0;
    private static final int SEARCH_TYPE_TEACHER = 1;
    private static final int SEARCH_TYPE_REALIZATION = 2;

    int typeOfSearch = -1;
    boolean searchInProgress = false;

    List<Object> allItems = new ArrayList<>();
    BasketSavedItems basketSavedItems;
    StudyBasketSearchAdapter studyBasketSearchAdapter;

    List<BasketRealization> basketRealizations = new ArrayList<>();
    List<BasketStudentGroup> basketStudentGroups = new ArrayList<>();
    List<BasketRealization> basketStudentGroupRealizations = new ArrayList<>();
    List<BasketTeacher> basketTeachers = new ArrayList<>();
    List<BasketRealization> basketTeacherRealizations = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_search);
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.study_basket_search));

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateVisibilities(3, "");

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (NullPointerException e ){
                    e.printStackTrace();
                }
            }
        });




        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (NullPointerException e ){
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                int idx = group.indexOfChild(radioButton);
                if (idx == 0) {
                    typeOfSearch = SEARCH_TYPE_STUDENT_GROUP;
                } else if (idx == 1) {
                    typeOfSearch = SEARCH_TYPE_TEACHER;
                } else {
                    typeOfSearch = SEARCH_TYPE_REALIZATION;
                }
            }
        });
    }

    void updateVisibilities(int state, String error) {
        switch (state){
            case 0: // aloitetaan haku
                searchInProgress = true;
                textViewFirst.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                break;
            case 1: // haku onnistui
                searchInProgress = false;
                textViewFirst.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
                break;
            case 2: // haku epäonnistui, asetetaan syy
                searchInProgress = false;
                textViewFirst.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
                if (error.contains("SocketTimeoutException")) {
                    emptyTextView.setText(R.string.search_error_time_out);
                } else if (error.contains("UnknownHostException")) {
                    emptyTextView.setText(R.string.search_error_no_internet);
                } else {
                    emptyTextView.setText(getString(R.string.error_occurred) + error);
                }
                break;
            case 3: // activity käynnistyy
                searchInProgress = false;
                textViewFirst.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                break;
        }
    }

    void doSearch() {
        if (searchInProgress){
            Toasty.info(this, getString(R.string.basket_search_in_progress), Toast.LENGTH_SHORT, true).show();
        } else {
            editText.clearFocus();
            allItems = new ArrayList<>();
            String searchWord = editText.getText().toString();
            switch (typeOfSearch) {
                case SEARCH_TYPE_STUDENT_GROUP:
                    try {
                        updateVisibilities(0, "");
                        fetchStudentGroups(searchWord);
                    } catch (IOException | JSONException e) {
                        updateVisibilities(2, e.toString());
                    }
                    break;
                case SEARCH_TYPE_TEACHER:
                    try {
                        updateVisibilities(0, "");
                        fetchTeachers(searchWord);
                    } catch (IOException | JSONException e) {
                        updateVisibilities(2, e.toString());
                    }
                    break;
                case SEARCH_TYPE_REALIZATION:
                    try {
                        updateVisibilities(0, "");
                        fetchRealizations(searchWord);
                    } catch (IOException | JSONException e) {
                        updateVisibilities(2, e.toString());
                    }
                    break;
                default:
                    Toasty.info(this, getString(R.string.basket_search_choose_type), Toast.LENGTH_LONG, true).show();
                    break;
            }
        }
    }

    void fetchStudentGroups(final String searchWord) throws IOException, JSONException {
        RetroFitters.fetchElasticStudentGroups(getApplicationContext(), 180, new ElasticStudentGroupsCallback() {
            @Override
            public void onSuccess(ElasticStudentGroups elasticStudentGroups) {
                BasketSavedItems tmpBasketSavedItems = new BasketSavedItems();
                List<BasketStudentGroup> basketStudentGroups = new ArrayList<>();

                for (Bucket bucket : elasticStudentGroups.getAggregations().getMostReservedStudentGroups().getBuckets() ) {
                    if (bucket.getKey().toUpperCase().contains(searchWord.toUpperCase())){
                        basketStudentGroups.add(new BasketStudentGroup(bucket.getKey(), "0"));
                    }
                }

                Collections.sort(basketStudentGroups, new Comparator<Object>(){
                    public int compare(Object obj1, Object obj2) {
                        return ((BasketStudentGroup)obj1).getStudentGroupCode().compareToIgnoreCase(((BasketStudentGroup)obj2).getStudentGroupCode());
                    }
                });

                tmpBasketSavedItems.setBasketStudentGroups(basketStudentGroups);
                tmpBasketSavedItems.setBasketTeachers(new ArrayList<BasketTeacher>());
                tmpBasketSavedItems.setBasketRealizations(new ArrayList<BasketRealization>());
                try {
                    fetchStudentGroupRealizations(tmpBasketSavedItems);
                } catch (IOException | JSONException e) {
                    updateVisibilities(2, e.toString());
                }
            }
            @Override
            public void onFailure(Throwable throwable) {
                updateVisibilities(2, throwable.toString());
            }
        });
    }

    void fetchStudentGroupRealizations(final BasketSavedItems searchBasketSavedItems) throws IOException, JSONException {
        RetroFitters.fetchElasticRealizations(getApplicationContext(), new ElasticRealizationCallback() {
            @Override
            public void onSuccess(ElasticRealization elasticRealization) {
                if (elasticRealization.getHitsRealization().getTotal() > 100){
                    updateVisibilities(2, getString(R.string.basket_search_too_many_hits));
                } else if (elasticRealization.getHitsRealization().getTotal() == 0){
                    updateVisibilities(2, getString(R.string.basket_search_no_hits));
                } else {
                    List<HitRealization> realizations = new ArrayList<>(elasticRealization.getHitsRealization().getHitRealizations());
                    for (Iterator<HitRealization> iterator = realizations.iterator(); iterator.hasNext();) {
                        HitRealization realization = iterator.next();
                        if (realization.getSourceRealization().getCode().equals("") && realization.getSourceRealization().getLocalizedName().getValueFi().equals("")) {
                            iterator.remove();
                        }
                    }

                    List<BasketRealization> realizationSearchResults;

                    for (BasketStudentGroup studentGroupBasket : searchBasketSavedItems.getBasketStudentGroups()) {
                        realizationSearchResults = new ArrayList<>();
                        for (HitRealization realization : realizations) {
                            BasketRealization basketRealization = new BasketRealization(realization.getSourceRealization().getCode(), realization.getSourceRealization().getLocalizedName().getValueFi(), realization.getSourceRealization().getLocalizedName().getValueEn());
                            boolean alreadyAdded = false;
                            for (BasketRealization basketRealizationLoop : realizationSearchResults) {
                                if (basketRealization.getRealizationCode().equals(basketRealizationLoop.getRealizationCode())) {
                                    alreadyAdded = true;
                                }
                            }
                            if (!alreadyAdded) {
                                for (com.seamk.mobile.elasticsearch.StudentGroup studentGroup : realization.getSourceRealization().getStudentGroups()) {
                                    if (studentGroup.getCode().equals(studentGroupBasket.getStudentGroupCode())) {
                                        realizationSearchResults.add(basketRealization);
                                    }
                                }
                            }
                        }
                        Collections.sort(realizationSearchResults, new Comparator<BasketRealization>() {
                            public int compare(BasketRealization obj1, BasketRealization obj2) {
                                return obj1.getRealizationNameFi().compareToIgnoreCase(obj2.getRealizationNameFi());
                            }
                        });
                        studentGroupBasket.setBasketRealizations(realizationSearchResults);
                    }

                    for (BasketStudentGroup studentGroupBasketResult : searchBasketSavedItems.getBasketStudentGroups()){
                        for (BasketStudentGroup studentGroupBasketSaved : basketStudentGroups){
                            if (studentGroupBasketResult.getStudentGroupCode().equals(studentGroupBasketSaved.getStudentGroupCode())){
                                studentGroupBasketResult.setAdded(true);
                            }
                        }
                    }

                    for (BasketStudentGroup studentGroupBasketResult : searchBasketSavedItems.getBasketStudentGroups()) {
                        for (BasketRealization basketRealizationResult : studentGroupBasketResult.getBasketRealizations()) {
                            for (BasketRealization basketRealizationSaved : basketRealizations) {
                                if (basketRealizationResult.getRealizationCode().equals(basketRealizationSaved.getRealizationCode())) {
                                    basketRealizationResult.setAdded(true);
                                }
                            }
                        }
                    }

                    allItems = new ArrayList<Object>(searchBasketSavedItems.getBasketStudentGroups());
                    studyBasketSearchAdapter = new StudyBasketSearchAdapter(getApplicationContext(), allItems);
                    recyclerView.setAdapter(studyBasketSearchAdapter);

                    updateVisibilities(1, "");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                updateVisibilities(2, throwable.toString());
            }
        }, searchBasketSavedItems, 60);
    }



    void fetchTeachers(final String searchWord) throws IOException, JSONException {
        RetroFitters.fetchElasticTeachers(getApplicationContext(), 180, new ElasticTeachersCallback() {
            @Override
            public void onSuccess(ElasticTeachers elasticTeachers) {
                BasketSavedItems tmpBasketSavedItems = new BasketSavedItems();
                List<BasketTeacher> basketTeachers = new ArrayList<>();

                for (Bucket bucket : elasticTeachers.getAggregations().getMostReservedTeachers().getBuckets() ) {
                    if (bucket.getKey().toUpperCase().contains(searchWord.toUpperCase())){
                        basketTeachers.add(new BasketTeacher(bucket.getKey(), "0"));
                    }
                }

                Collections.sort(basketTeachers, new Comparator<Object>(){
                    public int compare(Object obj1, Object obj2) {
                        return ((BasketTeacher)obj1).getTeacherName().compareToIgnoreCase(((BasketTeacher)obj2).getTeacherName());
                    }
                });

                tmpBasketSavedItems.setBasketStudentGroups(new ArrayList<BasketStudentGroup>());
                tmpBasketSavedItems.setBasketTeachers(basketTeachers);
                tmpBasketSavedItems.setBasketRealizations(new ArrayList<BasketRealization>());
                try {
                    fetchTeacherRealizations(tmpBasketSavedItems);
                } catch (IOException | JSONException e) {
                    updateVisibilities(2, e.toString());
                    emptyTextView.setText(e.getMessage());
                }
            }
            @Override
            public void onFailure(Throwable throwable) {
                updateVisibilities(2, throwable.toString());
            }
        });
    }

    void fetchTeacherRealizations(final BasketSavedItems searchBasketSavedItems) throws IOException, JSONException {
        RetroFitters.fetchElasticRealizations(getApplicationContext(), new ElasticRealizationCallback() {
            @Override
            public void onSuccess(ElasticRealization elasticRealization) {
                if (elasticRealization.getHitsRealization().getTotal() > 100){
                    updateVisibilities(2, getString(R.string.basket_search_too_many_hits));
                } else if (elasticRealization.getHitsRealization().getTotal() == 0){
                    updateVisibilities(2, getString(R.string.basket_search_no_hits));
                } else {

                    List<HitRealization> realizations = new ArrayList<>(elasticRealization.getHitsRealization().getHitRealizations());
                    for (Iterator<HitRealization> iterator = realizations.iterator(); iterator.hasNext();) {
                        HitRealization realization = iterator.next();
                        if (realization.getSourceRealization().getCode().equals("") && realization.getSourceRealization().getLocalizedName().getValueFi().equals("")) {
                            iterator.remove();
                        }
                    }

                    List<BasketRealization> realizationSearchResults;

                    for (BasketTeacher basketTeacher : searchBasketSavedItems.getBasketTeachers()) {
                        realizationSearchResults = new ArrayList<>();
                        for (HitRealization realization : realizations) {
                            BasketRealization basketRealization = new BasketRealization(realization.getSourceRealization().getCode(), realization.getSourceRealization().getLocalizedName().getValueFi(), realization.getSourceRealization().getLocalizedName().getValueEn());
                            boolean alreadyAdded = false;
                            for (BasketRealization basketRealizationLoop : realizationSearchResults) {
                                if (basketRealization.getRealizationCode().equals(basketRealizationLoop.getRealizationCode())) {
                                    alreadyAdded = true;
                                }
                            }
                            if (!alreadyAdded) {
                                for (Teacher teacher : realization.getSourceRealization().getTeacher()) {
                                    if (teacher.getName().equals(basketTeacher.getTeacherName())) {
                                        realizationSearchResults.add(basketRealization);
                                    }
                                }
                            }
                        }
                        Collections.sort(realizationSearchResults, new Comparator<BasketRealization>() {
                            public int compare(BasketRealization obj1, BasketRealization obj2) {
                                return obj1.getRealizationNameFi().compareToIgnoreCase(obj2.getRealizationNameFi());
                            }
                        });
                        basketTeacher.setBasketRealizations(realizationSearchResults);
                    }

                    for (BasketTeacher teacherBasketResult : searchBasketSavedItems.getBasketTeachers()){
                        for (BasketTeacher teacherBasketSaved : basketTeachers){
                            if (teacherBasketResult.getTeacherName().equals(teacherBasketSaved.getTeacherName())){
                                teacherBasketResult.setAdded(true);
                            }
                        }
                    }

                    for (BasketTeacher teacherBasketResult : searchBasketSavedItems.getBasketTeachers()) {
                        for (BasketRealization basketRealizationResult : teacherBasketResult.getBasketRealizations()) {
                            for (BasketRealization basketRealizationSaved : basketRealizations) {
                                if (basketRealizationResult.getRealizationCode().equals(basketRealizationSaved.getRealizationCode())) {
                                    basketRealizationResult.setAdded(true);
                                }
                            }
                        }
                    }

                    allItems = new ArrayList<Object>(searchBasketSavedItems.getBasketTeachers());
                    studyBasketSearchAdapter = new StudyBasketSearchAdapter(getApplicationContext(), allItems);
                    recyclerView.setAdapter(studyBasketSearchAdapter);

                    updateVisibilities(1, "");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                updateVisibilities(2, throwable.toString());
            }
        }, searchBasketSavedItems, 60);
    }


    void fetchRealizations(final String searchWord) throws IOException, JSONException {
        RetroFitters.fetchElasticRealization(this, searchWord, new ElasticRealizationCallback() {
            @Override
            public void onSuccess(ElasticRealization elasticRealization) {
                if (elasticRealization.getHitsRealization().getTotal() > 100){
                    updateVisibilities(2, getString(R.string.basket_search_too_many_hits));
                } else if (elasticRealization.getHitsRealization().getTotal() == 0){
                    updateVisibilities(2, getString(R.string.basket_search_no_hits));
                } else {
                    for (HitRealization hitRealization : elasticRealization.getHitsRealization().getHitRealizations()){
                        if (!hitRealization.getSourceRealization().getCode().isEmpty() && hitRealization.getSourceRealization().getCode() != null){
                            allItems.add(new BasketRealization(hitRealization.getSourceRealization().getCode(), hitRealization.getSourceRealization().getLocalizedName().getValueFi(), hitRealization.getSourceRealization().getLocalizedName().getValueEn()));
                        }
                    }
                    studyBasketSearchAdapter = new StudyBasketSearchAdapter(getApplicationContext(), allItems);
                    recyclerView.setAdapter(studyBasketSearchAdapter);

                    updateVisibilities(1, "");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                updateVisibilities(2, throwable.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        basketSavedItems = Common.getStudyBasket(this);
        basketRealizations = basketSavedItems.getBasketRealizations();
        basketStudentGroups = basketSavedItems.getBasketStudentGroups();
        basketStudentGroupRealizations = basketSavedItems.getBasketStudentGroupsRealizations();
        basketTeachers = basketSavedItems.getBasketTeachers();
        basketTeacherRealizations = basketSavedItems.getBasketTeacherRealizations();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        basketSavedItems = new BasketSavedItems();
        for (BasketStudentGroup basketStudentGroup : basketStudentGroups){
            basketStudentGroup.setBasketRealizations(new ArrayList<BasketRealization>());
        }
        for (BasketTeacher basketTeacher : basketTeachers){
            basketTeacher.setBasketRealizations(new ArrayList<BasketRealization>());
        }
        basketSavedItems.setBasketRealizations(basketRealizations);
        basketSavedItems.setBasketStudentGroups(basketStudentGroups);
        basketSavedItems.setBasketStudentGroupsRealizations(basketStudentGroupRealizations);
        basketSavedItems.setBasketTeachers(basketTeachers);
        basketSavedItems.setBasketTeacherRealizations(basketTeacherRealizations);
        Common.saveStudyBasket(basketSavedItems, this);
        super.onPause();
    }

    @Subscribe
    public void onItemAdded(ItemAddedEvent event){
        if (event.getObject() instanceof BasketStudentGroup) {
            BasketStudentGroup studentGroupBasketEvent = (BasketStudentGroup)event.getObject();
            if (studentGroupBasketEvent.isAdded()){
                basketStudentGroups.add(studentGroupBasketEvent);
            } else {
                for (Iterator<BasketStudentGroup> iterator = basketStudentGroups.iterator(); iterator.hasNext();) {
                    BasketStudentGroup studentGroupBasket = iterator.next();
                    if (studentGroupBasket.getStudentGroupCode().equals(studentGroupBasketEvent.getStudentGroupCode())) {
                        iterator.remove();
                    }
                }
            }
        }
        if (event.getObject() instanceof BasketTeacher) {
            BasketTeacher basketTeacherEvent = (BasketTeacher)event.getObject();
            if (basketTeacherEvent.isAdded()){
                basketTeachers.add(basketTeacherEvent);
            } else {
                for (Iterator<BasketTeacher> iterator = basketTeachers.iterator(); iterator.hasNext();) {
                    BasketTeacher basketTeacher = iterator.next();
                    if (basketTeacher.getTeacherName().equals(basketTeacherEvent.getTeacherName())) {
                        iterator.remove();
                    }
                }
            }
        }
        if (event.getObject() instanceof BasketRealization) {
            BasketRealization basketRealizationEvent = (BasketRealization)event.getObject();
            if (basketRealizationEvent.isAdded()){
                basketRealizations.add(basketRealizationEvent);
            } else {
                for (Iterator<BasketRealization> iterator = basketRealizations.iterator(); iterator.hasNext();) {
                    BasketRealization basketRealizations = iterator.next();
                    if (basketRealizations.getRealizationCode().equals(basketRealizationEvent.getRealizationCode())) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}

package com.seamk.mobile.studybasket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.itemanimators.SlideInOutRightAnimator;
import com.seamk.mobile.R;
import com.seamk.mobile.UtilityFragment;
import com.seamk.mobile.adapters.StudyBasketAdapter;
import com.seamk.mobile.elasticsearch.ElasticRealization;
import com.seamk.mobile.elasticsearch.HitRealization;
import com.seamk.mobile.elasticsearch.StudentGroup;
import com.seamk.mobile.elasticsearch.Teacher;
import com.seamk.mobile.eventbusevents.DeleteEvent;
import com.seamk.mobile.interfaces.callback.ElasticRealizationCallback;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by Juha Ala-Rantala on 18.3.2018.
 */

public class FragmentStudyBasket extends UtilityFragment {

    @BindView(R.id.fab) FloatingActionButton floatingActionButton;
    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.empty_text_view) TextView emptyTextView;

    Snackbar snackbar;

    boolean canRefresh = false;
    StudyBasketAdapter studyBasketAdapter;
    List<Object> allItems = new ArrayList<>();
    BasketSavedItems basketSavedItems;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_study_basket, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle(getString(R.string.study_basket));

        setHasOptionsMenu(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new SlideInOutRightAnimator(recyclerView));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityBasketAddItem.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        basketSavedItems = Common.getStudyBasket(getContext());

        // Sets the same isShown value for all realizations with the same code, for student groups
        for (BasketRealization basketRealizations : basketSavedItems.getBasketRealizations()) {
            for (BasketRealization basketRealizationSaved : basketSavedItems.getBasketStudentGroupsRealizations()) {
                if (basketRealizations.getRealizationCode().equals(basketRealizationSaved.getRealizationCode())) {
                    basketRealizations.setShown(basketRealizationSaved.isShown());
                }
            }
        }

        // Sets the same isShown value for all realizations with the same code, for teachers
        for (BasketRealization basketRealizations : basketSavedItems.getBasketRealizations()) {
            for (BasketRealization basketRealizationSaved : basketSavedItems.getBasketTeacherRealizations()) {
                if (basketRealizations.getRealizationCode().equals(basketRealizationSaved.getRealizationCode())) {
                    basketRealizations.setShown(basketRealizationSaved.isShown());
                }
            }
        }

        allItems = new ArrayList<>();
        allItems.addAll(basketSavedItems.getBasketStudentGroups());
        allItems.addAll(basketSavedItems.getBasketTeachers());
        allItems.addAll(basketSavedItems.getBasketRealizations());
        studyBasketAdapter = new StudyBasketAdapter(getContext(), allItems);
        recyclerView.setAdapter(studyBasketAdapter);

        if (allItems.size() == 0){
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
        }

        if (allItems.size() > 0){
            try {
                fetchRealizations();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Toasty.error(getContext(), e.toString(), Toast.LENGTH_LONG, true).show();
            }
        }
    }

    @Override
    public void onPause(){
        if (snackbar != null){
            if (snackbar.isShown()){
                snackbar.dismiss();
            }
        }
        saveBasketItems();
        super.onPause();
    }


    void saveBasketItems() {
        List<Object> updatedList = studyBasketAdapter.getModifiedList();
        BasketSavedItems basketSavedItems = new BasketSavedItems();

        List<BasketRealization> basketRealizations = new ArrayList<>();
        List<BasketStudentGroup> basketStudentGroups = new ArrayList<>();
        List<BasketRealization> basketStudentGroupRealizations = new ArrayList<>();
        List<BasketTeacher> basketTeachers = new ArrayList<>();
        List<BasketRealization> basketTeacherRealizations = new ArrayList<>();
        Map<String, List<BasketRealization>> sgrMap = new HashMap<>();
        Map<String, List<BasketRealization>> trMap = new HashMap<>();

        for (Object object : updatedList){
            if (object instanceof BasketStudentGroup){
                basketStudentGroups.add((BasketStudentGroup) object);
            }
            if (object instanceof BasketRealization){
                basketRealizations.add((BasketRealization) object);
            }
            if (object instanceof BasketTeacher){
                basketTeachers.add((BasketTeacher) object);
            }
        }

        for (BasketStudentGroup studentGroupBasket : basketStudentGroups){
            for (BasketRealization basketRealization : studentGroupBasket.getBasketRealizations()){
                if (sgrMap.containsKey(basketRealization.getRealizationCode())){
                    sgrMap.get(basketRealization.getRealizationCode()).add(basketRealization);
                } else {
                    sgrMap.put(basketRealization.getRealizationCode(), new ArrayList<BasketRealization>());
                    sgrMap.get(basketRealization.getRealizationCode()).add(basketRealization);
                }
            }
            studentGroupBasket.setBasketRealizations(new ArrayList<BasketRealization>());
        }

        for (BasketRealization basketRealization : basketRealizations){
            if (sgrMap.containsKey(basketRealization.getRealizationCode())){
                sgrMap.get(basketRealization.getRealizationCode()).add(basketRealization);
            } else {
                sgrMap.put(basketRealization.getRealizationCode(), new ArrayList<BasketRealization>());
                sgrMap.get(basketRealization.getRealizationCode()).add(basketRealization);
            }
        }

        for (List<BasketRealization> entry : sgrMap.values()) {
            int index = 0;
            long max = 0;
            for (int i = 0; i < entry.size(); i++) {
                if(entry.get(i).getLastSavedTime() > max){
                    max = entry.get(i).getLastSavedTime();
                    index = i;
                }
            }
            basketStudentGroupRealizations.add(entry.get(index));
        }

        for (BasketTeacher basketTeacher : basketTeachers){
            for (BasketRealization basketRealization : basketTeacher.getBasketRealizations()){
                if (trMap.containsKey(basketRealization.getRealizationCode())){
                    trMap.get(basketRealization.getRealizationCode()).add(basketRealization);
                } else {
                    trMap.put(basketRealization.getRealizationCode(), new ArrayList<BasketRealization>());
                    trMap.get(basketRealization.getRealizationCode()).add(basketRealization);
                }
            }
            basketTeacher.setBasketRealizations(new ArrayList<BasketRealization>());
        }

        for (BasketRealization basketRealization : basketRealizations){
            if (trMap.containsKey(basketRealization.getRealizationCode())){
                trMap.get(basketRealization.getRealizationCode()).add(basketRealization);
            } else {
                trMap.put(basketRealization.getRealizationCode(), new ArrayList<BasketRealization>());
                trMap.get(basketRealization.getRealizationCode()).add(basketRealization);
            }
        }

        for (List<BasketRealization> entry : trMap.values()) {
            int index = 0;
            long max = 0;
            for (int i = 0; i < entry.size(); i++) {
                if(entry.get(i).getLastSavedTime() > max){
                    max = entry.get(i).getLastSavedTime();
                    index = i;
                }
            }
            basketTeacherRealizations.add(entry.get(index));
        }

        for (BasketRealization basketTeacherRealization : basketTeacherRealizations){
            for (BasketRealization basketStudentGroupRealization : basketStudentGroupRealizations) {
                if (basketTeacherRealization.getRealizationCode().equals(basketStudentGroupRealization.getRealizationCode())){
                    if (basketTeacherRealization.getLastSavedTime() > basketStudentGroupRealization.getLastSavedTime()) {
                        basketStudentGroupRealization.setShown(basketTeacherRealization.isShown());
                    } else {
                        basketTeacherRealization.setShown(basketStudentGroupRealization.isShown());
                    }
                }
            }
        }

        for (BasketRealization basketRealization : basketRealizations){
            basketRealization.setNoReservations(false);
            basketRealization.setDuplicateWarning(false);
        }

        basketSavedItems.setBasketRealizations(basketRealizations);
        basketSavedItems.setBasketStudentGroups(basketStudentGroups);
        basketSavedItems.setBasketStudentGroupsRealizations(basketStudentGroupRealizations);
        basketSavedItems.setBasketTeachers(basketTeachers);
        basketSavedItems.setBasketTeacherRealizations(basketTeacherRealizations);

        Common.saveStudyBasket(basketSavedItems, getContext());
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (canRefresh) {
                    if (allItems.size() > 0) {
                        try {
                            fetchRealizations();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Toasty.error(getContext(), e.toString(), Toast.LENGTH_LONG, true).show();
                        }
                    }
                } else {
                    Toasty.info(getContext(), getString(R.string.please_wait_a_moment), Toast.LENGTH_SHORT, true).show();
                }
                return true;
        }
        return false;
    }

    @Subscribe
    public void onDeleteEvent(DeleteEvent event){
        String string = "";
        if (event.getObject() instanceof BasketStudentGroup){
            string = ((BasketStudentGroup) event.getObject()).getStudentGroupCode();
        }
        if (event.getObject() instanceof BasketTeacher){
            string = ((BasketTeacher) event.getObject()).getTeacherName();
        }
        if (event.getObject() instanceof BasketRealization){
            string = ((BasketRealization) event.getObject()).getRealizationNameFi();
            string = string + " " +  ((BasketRealization) event.getObject()).getRealizationCode();
        }
        final String finalString = string;
        snackbar = Snackbar
                .make(coordinatorLayout, "'" + string + "' " + getString(R.string.was_deleted), 5000)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        studyBasketAdapter.undoLastRemove();
                        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "'" + finalString + "' " + getString(R.string.was_restored), Snackbar.LENGTH_LONG);
                        snackbar1.show();
                        if (studyBasketAdapter.getModifiedList().size() == 0){
                            emptyTextView.setVisibility(View.VISIBLE);
                        } else {
                            emptyTextView.setVisibility(View.GONE);
                        }
                    }
                });

        snackbar.show();

        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            @Override
            public void run(){
                if (studyBasketAdapter.getModifiedList().size() == 0){
                    emptyTextView.setVisibility(View.VISIBLE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                }
            }
        }, 10);
    }

    void fetchRealizations() throws IOException, JSONException {
        RetroFitters.fetchElasticRealizations(getContext(), new ElasticRealizationCallback() {
            @Override
            public void onSuccess(ElasticRealization elasticRealization) {
                updateBasket(elasticRealization);
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (throwable.toString().contains("SocketTimeoutException")) {
                    Toasty.error(getContext(), getString(R.string.error_basket_time_out), Toast.LENGTH_LONG, true).show();
                } else if (throwable.toString().contains("UnknownHostException")) {
                    Toasty.error(getContext(), getString(R.string.error_basket_no_internet), Toast.LENGTH_LONG, true).show();
                } else {
                    Toasty.error(getContext(), getString(R.string.error_basket_unknown), Toast.LENGTH_LONG, true).show();
                }
                canRefresh = true;
            }
        }, basketSavedItems, 60);
    }

    void updateBasket(ElasticRealization elasticRealization){
        if (elasticRealization.getHitsRealization().getTotal() <= 100){
            List<HitRealization> realizations = new ArrayList<>(elasticRealization.getHitsRealization().getHitRealizations());
            for (Iterator<HitRealization> iterator = realizations.iterator(); iterator.hasNext();) {
                HitRealization realization = iterator.next();
                if (realization.getSourceRealization().getCode().equals("") && realization.getSourceRealization().getLocalizedName().getValueFi().equals("")) {
                    iterator.remove();
                }
            }

            List<BasketRealization> realizationSearchResults;
            List<BasketRealization> basketRealizationsList = basketSavedItems.getBasketRealizations();
            List<BasketStudentGroup> basketStudentGroupsList = basketSavedItems.getBasketStudentGroups();
            List<BasketRealization> basketStudentGroupsRealizationsList = basketSavedItems.getBasketStudentGroupsRealizations();
            List<BasketTeacher> basketTeachersList = basketSavedItems.getBasketTeachers();
            List<BasketRealization> basketTeachersRealizationsList = basketSavedItems.getBasketTeacherRealizations();

            for (BasketStudentGroup studentGroupBasket : basketStudentGroupsList) {
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
                        for (StudentGroup studentGroup : realization.getSourceRealization().getStudentGroups()) {
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

            for (BasketStudentGroup studentGroupBasketResult : basketStudentGroupsList) {
                for (BasketRealization basketRealizationResult : studentGroupBasketResult.getBasketRealizations()) {
                    for (BasketRealization basketRealizationSaved : basketStudentGroupsRealizationsList) {
                        if (basketRealizationResult.getRealizationCode().equals(basketRealizationSaved.getRealizationCode())){
                            basketRealizationResult.setShown(basketRealizationSaved.isShown());
                        }
                    }
                }
            }

            for (BasketTeacher basketTeacher : basketTeachersList) {
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

            for (BasketTeacher teacherBasketResult : basketTeachersList) {
                for (BasketRealization basketRealizationResult : teacherBasketResult.getBasketRealizations()) {
                    for (BasketRealization basketRealizationSaved : basketTeachersRealizationsList) {
                        if (basketRealizationResult.getRealizationCode().equals(basketRealizationSaved.getRealizationCode())){
                            basketRealizationResult.setShown(basketRealizationSaved.isShown());
                        }
                    }
                }
            }

            for (int i = 0; i < basketRealizationsList.size(); i++) {
                boolean wasFound = false;
                int hitIndex = 0;
                for (int j = 0; j < realizations.size(); j++){
                    if (realizations.get(j).getSourceRealization().getCode().equals(basketRealizationsList.get(i).getRealizationCode())) {
                        wasFound = true;
                        hitIndex = j;
                    }
                }
                if (wasFound){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String tmpEndDate = realizations.get(hitIndex).getSourceRealization().getEndDate().replace("T", " ");
                    long dateEndDate = 0;
                    try { dateEndDate = simpleDateFormat.parse(tmpEndDate).getTime(); } catch (ParseException e) { e.printStackTrace(); }
                    Calendar calendar = Calendar.getInstance();
                    if (calendar.getTimeInMillis() > dateEndDate){
                        basketRealizationsList.get(i).setNoReservations(true);
                    } else {
                        basketRealizationsList.get(i).setNoReservations(false);
                    }
                } else {
                    basketRealizationsList.get(i).setNoReservations(true);
                }
            }

            for (BasketStudentGroup basketStudentGroup : basketStudentGroupsList){
                for (BasketRealization basketRealization : basketStudentGroup.getBasketRealizations()) {
                    for (BasketRealization realization : basketRealizationsList) {
                        if (realization.getRealizationCode().equals(basketRealization.getRealizationCode())) {
                            realization.setDuplicateWarning(true);
                        }
                    }
                }
            }

            for (BasketTeacher basketTeacher : basketTeachersList){
                for (BasketRealization basketRealization : basketTeacher.getBasketRealizations()) {
                    for (BasketRealization realization : basketRealizationsList) {
                        if (realization.getRealizationCode().equals(basketRealization.getRealizationCode())) {
                            realization.setDuplicateWarning(true);
                        }
                    }
                }
            }

            allItems = new ArrayList<>();
            allItems.addAll(basketStudentGroupsList);
            allItems.addAll(basketTeachersList);
            allItems.addAll(basketRealizationsList);
            studyBasketAdapter = new StudyBasketAdapter(getContext(), allItems);
            recyclerView.setAdapter(studyBasketAdapter);
            if (allItems.size() > 0) {
                Toasty.success(getContext(), getString(R.string.success_basket_fetch), Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.warning(getContext(), getString(R.string.warning_basket_too_large), Toast.LENGTH_LONG, true).show();
        }
    }
}

package com.seamk.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.seamk.mobile.elasticsearch.ElasticRealization;
import com.seamk.mobile.elasticsearch.HitRealization;
import com.seamk.mobile.elasticsearch.SourceRealization;
import com.seamk.mobile.interfaces.callback.ElasticRealizationCallback;
import com.seamk.mobile.objects.Realization;
import com.seamk.mobile.retrofit.RetroFitters;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class FragmentFindRealization extends Fragment {

    @BindView(R.id.et_search) EditText editText;
    @BindView(R.id.b_search) Button bSearch;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.text_view) TextView textView;

    ItemAdapter<IItem> itemAdapter;
    FastAdapter<IItem> fastAdapter;

    List<IItem> realizations;
    List<SourceRealization> hits;

    boolean searchInProgress = false;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_progress_edit_text, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);

        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<IItem> adapter, @NonNull IItem item, int position) {
                Intent intent;
                intent = new Intent(getContext(), ActivityRealizationDetails.class);

                for (SourceRealization sourceRealization : hits){
                    if (sourceRealization.getCode().equals(((Realization)item).getCode())){
                        intent.putExtra("realization", Parcels.wrap(sourceRealization));
                        break;
                    }
                }

                getContext().startActivity(intent);
                return true;
            }
        });

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();
                hideKeyboard();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    hideKeyboard();
                    return true;
                } else {
                    return false;
                }
            }
        });


        updateVisibilities(3, "");

        return v;
    }

    void doSearch() {
        if (searchInProgress){
            Toasty.info(getContext(), getString(R.string.basket_search_in_progress), Toast.LENGTH_SHORT, true).show();
        } else {
            editText.clearFocus();
            realizations = new ArrayList<>();
            hits = new ArrayList<>();
            itemAdapter.clear();
            String searchWord = editText.getText().toString();
            try {
                updateVisibilities(0, "");
                fetchRealizations(searchWord);
            } catch (IOException | JSONException e) {
                updateVisibilities(2, e.toString());
            }
        }
    }

    void hideKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e ){
            e.printStackTrace();
        }
    }

    void fetchRealizations(final String searchWord) throws IOException, JSONException {
        RetroFitters.fetchElasticRealizationFind(getContext(), searchWord, new ElasticRealizationCallback() {
            @Override
            public void onSuccess(ElasticRealization elasticRealization) {
                if (elasticRealization.getHitsRealization().getTotal() > 100){
                    updateVisibilities(2, getString(R.string.basket_search_too_many_hits));
                } else if (elasticRealization.getHitsRealization().getTotal() == 0){
                    updateVisibilities(2, getString(R.string.basket_search_no_hits));
                } else {
                    for (HitRealization hitRealization : elasticRealization.getHitsRealization().getHitRealizations()){
                        if (!hitRealization.getSourceRealization().getCode().isEmpty() && hitRealization.getSourceRealization().getCode() != null){
                            hits.add(hitRealization.getSourceRealization());
                            realizations.add(new Realization(hitRealization.getSourceRealization().getId(),
                                    hitRealization.getSourceRealization().getLocalizedName().getValueFi(),
                                    hitRealization.getSourceRealization().getCode(),
                                    hitRealization.getSourceRealization().getTeacher(),
                                    hitRealization.getSourceRealization().getStudentGroups(),
                                    hitRealization.getSourceRealization().getStartDate(),
                                    hitRealization.getSourceRealization().getEndDate()));
                        }
                    }
                    itemAdapter.add(realizations);
                    updateVisibilities(1, "");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                updateVisibilities(2, throwable.toString());
            }
        });
    }

    void updateVisibilities(int state, String error) {
        switch (state){
            case 0: // aloitetaan haku
                searchInProgress = true;
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                break;
            case 1: // haku onnistui
                searchInProgress = false;
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                break;
            case 2: // haku epäonnistui, asetetaan syy
                searchInProgress = false;
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                if (error.contains("SocketTimeoutException")) {
                    textView.setText(R.string.search_error_time_out);
                } else if (error.contains("UnknownHostException")) {
                    textView.setText(R.string.search_error_no_internet);
                } else {
                    textView.setText(getString(R.string.error_occurred) + error);
                }
                break;
            case 3: // activity käynnistyy
                searchInProgress = false;
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.find_search_realization_help);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                break;
        }
    }
}

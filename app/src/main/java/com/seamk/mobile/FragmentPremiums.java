package com.seamk.mobile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.seamk.mobile.objects.PremiumInfo;
import com.seamk.mobile.objects.PremiumProduct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 28.3.2018.
 */

public class FragmentPremiums extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    ItemAdapter<IItem> itemAdapter;
    FastAdapter<IItem> fastAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle(getString(R.string.support));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        itemAdapter.clear();
        itemAdapter.add(createPremiums());

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<IItem> adapter, @NonNull IItem item, int position) {

                switch (position){
                    case 1:

                        break;
                    case 2:

                        break;
                }

                return true;
            }
        });

        return v;
    }

    private List<IItem> createPremiums(){
        List<IItem> premiums = new ArrayList<>();

        premiums.add(new PremiumInfo());
        premiums.add(new PremiumProduct("Premium", "Hankkiudu eroon mainoksista ja tue sovelluksen kehitystä!", "1.99 €", "extrasGoldColorDark"));
        premiums.add(new PremiumProduct("Premium+", "Mainoksista eroon, yöteema sovellukseen ja iso kiitos kehittäjältä!", "4.99 €", "extrasGoldColor"));

        return premiums;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ((MainActivity)getActivity()).onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

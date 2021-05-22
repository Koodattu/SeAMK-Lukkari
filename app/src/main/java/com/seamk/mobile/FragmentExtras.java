package com.seamk.mobile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.seamk.mobile.objects.Extra;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 28.3.2018.
 */

public class FragmentExtras extends UtilityFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    ItemAdapter<IItem> itemAdapter;
    FastAdapter<IItem> fastAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle(getString(R.string.extra_features));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        itemAdapter.clear();
        itemAdapter.add(createExtras());

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<IItem> adapter, @NonNull IItem item, int position) {

                switch (position){
                    case 0:
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                        fragmentTransaction.replace(R.id.frame, new FragmentPremiums(), "PREMIUMS_FRAGMENT");
                        fragmentTransaction.addToBackStack("TAG");
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                        transaction.replace(R.id.frame, new FragmentLinks(), "LINKS_FRAGMENT");
                        transaction.addToBackStack("TAG");
                        transaction.commit();
                        break;
                    case 2:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto: juha.ala-rantala@seamk.fi"));
                        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_feedback)));
                        break;
                    case 3:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, getContext().getApplicationInfo());
                        String sAux = getResources().getString(R.string.download_app_message) + "\n";
                        sAux = sAux + "http://play.google.com/store/apps/details?id=" + getContext().getPackageName();
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, getResources().getString(R.string.choose_app)));
                        break;
                    case 4:
                        Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                        }
                        break;
                }

                return true;
            }
        });

        return v;
    }

    private List<IItem> createExtras(){
        List<IItem> extras = new ArrayList<>();

        extras.add(new Extra(getResources().getString(R.string.support), getString(R.string.support_developer), "€", "ic_thumb_up", "extrasGoldColor"));
        extras.add(new Extra(getResources().getString(R.string.links), getString(R.string.collection_useful_links), "", "ic_link_white", "colorPrimary"));
        extras.add(new Extra(getResources().getString(R.string.feedback), getString(R.string.send_feedback), "", "ic_email", "colorPrimary"));
        extras.add(new Extra(getResources().getString(R.string.share), getString(R.string.share_app), "", "ic_share", "colorPrimary"));
        extras.add(new Extra(getResources().getString(R.string.rate), getString(R.string.rate_app_on_gplay), "", "ic_star", "colorPrimary"));

        return extras;
    }
}

package com.seamk.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.seamk.mobile.activities.ActivityLinkWebView;
import com.seamk.mobile.objects.Link;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 28.3.2018.
 */

public class FragmentLinks extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    ItemAdapter<IItem> itemAdapter;
    FastAdapter<IItem> fastAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle(getString(R.string.links));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
        itemAdapter.clear();
        itemAdapter.add(createLinks());

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<IItem> adapter, @NonNull IItem item, int position) {
                Link link = (Link)item;

                if (link.getLinkURL().equals("https://tekniikka.seamk.fi/ruokajono/") || link.getLinkURL().equals("https://i.imgur.com/2Vw0uSz.jpg")) {
                    Intent intent = new Intent(getContext(), ActivityLinkWebView.class);
                    intent.putExtra("linkTitle", link.getLinkName());
                    intent.putExtra("linkUrl", link.getLinkURL());
                    startActivity(intent);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getLinkURL()));
                    startActivity(browserIntent);
                }

                return true;
            }
        });

        return v;
    }

    private List<IItem> createLinks(){
        List<IItem> links = new ArrayList<>();

        links.add(new Link("Frami F Ruokajono", "https://tekniikka.seamk.fi/ruokajono/"));
        links.add(new Link("SeAMK Yhteystiedot", "https://www.seamk.fi/seamk-info/yhteystiedot/"));
        links.add(new Link("Kampus Kartta", "https://i.imgur.com/2Vw0uSz.jpg"));
        links.add(new Link("Sähköposti", "https://mail.epedu.fi/owa"));
        links.add(new Link("WinhaWille", "https://wille.epedu.fi/"));
        links.add(new Link("Intra", "https://intra.seamk.fi/Etusivu"));
        links.add(new Link("Kirjasto ajat ja tiedot", "https://kirjasto.seamk.fi/aukiolot-ja-yhteystiedot/"));

        return links;
    }
}

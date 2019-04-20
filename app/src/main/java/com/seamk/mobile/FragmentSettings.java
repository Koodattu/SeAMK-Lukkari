package com.seamk.mobile;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.TextView;

import com.seamk.mobile.util.FragmentUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class FragmentSettings extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName("ApplicationPreferences");
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.settings);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_info, menu);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.sDisableFragmentAnimations) {
            Animation a = new Animation() {};
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle(getResources().getString(R.string.information));

                String[] names = {"Alanko-Luova Jermu", "Ansell", "Duppadiudiu", "Harjummikko", "Jukka Hakala", "Jäntin Mikke", "Kinuski", "Lepistö Juuso", "Matti3", "Matuuuuu", "Oscar Klop", "Rannon Petteri", "Räccis", "Sippolan Esa", "Superarde", "Unity Dev Toikka", "Perttulan näköne jätkä", "Viljaam", "Tumpe", "Vaaralline Ville"};
                List<String> strList = Arrays.asList(names);
                Collections.shuffle(strList);

                boolean badSize;

                while (true) {
                    Collections.shuffle(strList);
                    badSize = false;
                    for (int i = 0; i < strList.size(); i = i + 2) {
                        String testText = strList.get(i) + strList.get(i+1);
                        if (testText.length() > 30){
                            badSize = true;
                        }
                    }
                    if (!badSize){
                        break;
                    }
                }
                String allNames = "";
                for (int i = 0; i < strList.size(); i = i + 2){
                    allNames = allNames + strList.get(i);
                    allNames = allNames + " | ";
                    allNames = allNames + strList.get(i+1);
                    allNames = allNames + "\n";
                }

                alertDialogBuilder
                        .setMessage("SeAMK Lukkari\nVersio 1.3.3.7\n\nTekijä: Juha Ala-Rantala\n\nLaatuvastaava: Samu Kiviniitty\n\nHenkiset tuet:\n(Satunnaisjärjestys)\n\n" + allNames + "\n\n(Jos tunnet kuuluvasi listalle, pistä viestiä)")
                        .setCancelable(true)
                        .setPositiveButton("Sulje",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(16);
                TextView messageView = alertDialog.findViewById(android.R.id.message);
                messageView.setGravity(Gravity.CENTER);
                messageView.setTextSize(16);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

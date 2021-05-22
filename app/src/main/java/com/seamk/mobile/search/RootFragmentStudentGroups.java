package com.seamk.mobile.search;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.adapters.StudentGroupsAdapter;
import com.seamk.mobile.objects.StudentGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Juha Ala-Rantala on 26.4.2017.
 */

public class RootFragmentStudentGroups extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    StudentGroupsAdapter adapter;
    private List<StudentGroup> studentGroups;
    private List<StudentGroup> allstudentGroups;
    private List<StudentGroup> activestudentGroups;
    private List<StudentGroup> shownstudentgroups;
    private TextView emptyView;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_groups_grid, container, false);

        getActivity().setTitle(getResources().getString(R.string.group_grid));
        setHasOptionsMenu(true);

        preferences = v.getContext().getSharedPreferences(PREFS_NAME, 0);
        recyclerView = v.findViewById(R.id.rv);
        emptyView = v.findViewById(R.id.empty_view);

        GridLayoutManager glm = new GridLayoutManager(getContext() ,3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        shownstudentgroups = activestudentGroups;

        adapter = new StudentGroupsAdapter(shownstudentgroups, getContext());
        recyclerView.setAdapter(adapter);

        if (activestudentGroups.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        checkBox = v.findViewById(R.id.checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    shownstudentgroups = activestudentGroups;
                } else {
                    shownstudentgroups = allstudentGroups;
                }
                recyclerView.invalidate();
                adapter = new StudentGroupsAdapter(shownstudentgroups, getContext());
                recyclerView.setAdapter(adapter);
            }
        });

        return v;
    }


    public static final String PREFS_NAME = "ApplicationPreferences";
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activestudentGroups = new ArrayList<>();
        allstudentGroups = new ArrayList<>();
/*
        setContentView(R.layout.activity_student_groups);

        String path = getFilesDir().getAbsolutePath() + "/" + "studentgroupcodesjson.json";
        File file = new File(path);

        if (file.exists()){

            InputStream is;
            FileInputStream fis;
            String json = null;
            JSONObject jsonObject = null;
            try {
                is = openFileInput("studentgroupcodesjson.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
                jsonObject = new JSONObject(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Iterator<String> keys = jsonObject.keys();
            while( keys.hasNext() ){
                int key = Integer.parseInt(keys.next());
                String value = jsonObject.optString(Integer.toString(key));
                StudentGroup studentGroup = new StudentGroup(value, key);
                studentGroups.add(studentGroup);
            }
            Collections.sort(studentGroups, new Comparator<StudentGroup>(){
                public int compare(StudentGroup obj1, StudentGroup obj2) {
                    return obj1.getStudentGroupCode().compareToIgnoreCase(obj2.getStudentGroupCode());
                }
            });

            Toast.makeText(this, "Käytettiin olemassa olevaa tiedostoa.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            load_and_save_student_groups();

        }
*/
        activeStudentGroupsFromFile();
        allStudentGroupsFromFile();



    }

    public void activeStudentGroupsFromFile(){
        InputStream is;
        String json = null;
        JSONObject jsonObject = null;
        try {
            is = getActivity().getAssets().open("activestudentgroupsidcode");
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> keys = jsonObject.keys();
        while( keys.hasNext() ){
            int key = Integer.parseInt(keys.next());
            String value = jsonObject.optString(Integer.toString(key));
            StudentGroup studentGroup = new StudentGroup(value, key);
            activestudentGroups.add(studentGroup);
        }
        Collections.sort(activestudentGroups, new Comparator<StudentGroup>(){
            public int compare(StudentGroup obj1, StudentGroup obj2) {
                return obj1.getStudentGroupCode().compareToIgnoreCase(obj2.getStudentGroupCode());
            }
        });
    }

    public void allStudentGroupsFromFile(){
        InputStream is;
        String json = null;
        JSONObject jsonObject = null;
        try {
            is = getActivity().getAssets().open("allstudentgroupsidcode");
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> keys = jsonObject.keys();
        while( keys.hasNext() ){
            int key = Integer.parseInt(keys.next());
            String value = jsonObject.optString(Integer.toString(key));
            StudentGroup studentGroup = new StudentGroup(value, key);
            allstudentGroups.add(studentGroup);
        }
        Collections.sort(allstudentGroups, new Comparator<StudentGroup>(){
            public int compare(StudentGroup obj1, StudentGroup obj2) {
                return obj1.getStudentGroupCode().compareToIgnoreCase(obj2.getStudentGroupCode());
            }
        });
    }
    /*
        public void onRefreshAction(MenuItem menuItem) {
            if (canRefresh){
                canRefresh = false;

                fetchStudentGroups = new FetchStudentGroups();
                fetchStudentGroups.execute();

                Handler h = new Handler();
                h.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        canRefresh = true;
                    }
                },5000);
            }
        }

        boolean canRefresh = true;
    */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_student_groups, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return true;
    }
}

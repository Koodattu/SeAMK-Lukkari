package com.seamk.mobile.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.adapters.StudentGroupsAdapter;
import com.seamk.mobile.eventbusevents.StudentGroupEvent;
import com.seamk.mobile.objects.StudentGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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

public class RootFragmentSearchStudentGroup extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    StudentGroupsAdapter adapter;
    private List<StudentGroup> allstudentGroups;
    private List<StudentGroup> activestudentGroups;
    private List<StudentGroup> shownstudentgroups;
    private TextView emptyView;
    String studentGroup = null;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_student_group, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle(getResources().getString(R.string.timetable_search));
        recyclerView = v.findViewById(R.id.rv);
        emptyView = v.findViewById(R.id.empty_view);

        GridLayoutManager glm = new GridLayoutManager(getContext() ,3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);

        shownstudentgroups = activestudentGroups;

        adapter = new StudentGroupsAdapter(shownstudentgroups, getContext());
        recyclerView.setAdapter(adapter);

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
                adapter.filter(searchView.getQuery().toString());
            }
        });

        final List<String> studentGroupsStrings = new ArrayList<>();

        for (int i = 0; i < allstudentGroups.size(); i++){
            studentGroupsStrings.add(allstudentGroups.get(i).getStudentGroupCode());
        }

        final AutoCompleteTextView textView;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, studentGroupsStrings);
        textView = v.findViewById(R.id.autoCompleteTextView);
        textView.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

                studentGroupSelected();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                textView.clearFocus();
            }
        });

        Button button = v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (studentGroup != null && !studentGroup.equals(""))
                startTimetableActivity();
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activestudentGroups = new ArrayList<>();
        allstudentGroups = new ArrayList<>();
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

    SearchView searchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_student_groups, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Subscribe
    public void onStudentGroupEvent(StudentGroupEvent event) {
        studentGroup = event.studentGroupCode;
        startTimetableActivity();
    }


    public void studentGroupSelected() {
        AutoCompleteTextView textView = getActivity().findViewById(R.id.autoCompleteTextView);
        studentGroup = textView.getText().toString();
    }

    public void startTimetableActivity(){
        Intent intent = new Intent(getContext(), RootActivitySearchTimetable.class);
        intent.putExtra("studentGroupCode", studentGroup);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return true;
    }
}

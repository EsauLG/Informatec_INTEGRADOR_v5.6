package com.fcp.tec.informatecvdos.activities;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.adapters.ExpandableListAdapter;
import com.fcp.tec.informatecvdos.provider.ContractCalendario;
import com.fcp.tec.informatecvdos.provider.ContractExtraescolar;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment_Extraescolares extends Fragment
{

    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        // Inflate the layout for this fragment
        expandableListView = (view).findViewById(R.id.simple_expandable_listview);

        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        setItems();
        setListener();

        //Sincronizacion automaticamente
        USincronizacion sincronizacion = new USincronizacion();
        sincronizacion.sincronizarAutomaticamente(getContext(), ContractExtraescolar.AUTORIDAD, 1800);

        return view;
    }

    void setItems()
    {

        // Array list for header
        ArrayList<String> header = new ArrayList<String>();

        // Array list for child items
        List<String> child1 = new ArrayList<String>();
        List<String> child2 = new ArrayList<String>();
        List<String> child3 = new ArrayList<String>();


        // Hash map for both header and child
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor regDeportes = database.rawQuery("SELECT * FROM extraescolares WHERE tipo_extra=3", null);

        if (regDeportes.moveToFirst())
        {
            header.add("Deportivas");
            // Adding child data
            do
            {
                child1.add(regDeportes.getString(1));
            }
            while (regDeportes.moveToNext());
        }

        Cursor regCivicas = database.rawQuery("SELECT * FROM extraescolares WHERE tipo_extra=1", null);

        if (regCivicas.moveToFirst())
        {
            header.add("Cívicas");
            // Adding child data
            do
            {
                child2.add(regCivicas.getString(1));
            }
            while (regCivicas.moveToNext());
        }

        Cursor regCulturales = database.rawQuery("SELECT * FROM extraescolares WHERE tipo_extra=2", null);

        if (regCulturales.moveToFirst())
        {
            header.add("Culturales");
            // Adding child data
            do
            {
                child3.add(regCulturales.getString(1));
            }
            while (regCulturales.moveToNext());
        }

        database.close();
        // Adding header and childs to hash map
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);
        hashMap.put(header.get(2), child3);

        Context context = this.getContext();
        adapter = new ExpandableListAdapter(context, header, hashMap);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);
    }

    void setListener()
    {

        // This listener will show toast on group click
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id)
            {

                return false;
            }
        });

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {

            // Default position
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition)
            {
                if (groupPosition != previousGroup)

                    // Collapse the expanded group
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }

        });

        // This listener will show toast on child click
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        int groupPos, int childPos, long id)
            {

                return false;
            }
        });

    }
}
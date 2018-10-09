package com.fcp.tec.informatecvdos.activities;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.fcp.tec.informatecvdos.DatabaseHelper;
import com.fcp.tec.informatecvdos.R;
import com.fcp.tec.informatecvdos.adapters.ExpandableListAdapterPreguntas;
import com.fcp.tec.informatecvdos.provider.ContractPreguntasFrecuentes;
import com.fcp.tec.informatecvdos.utilidades.USincronizacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OneFragmentItem_Preguntas extends AppCompatActivity
{

    private ExpandableListAdapterPreguntas expandableListAdapter;
    private ExpandableListView expandableListView;
    SQLiteDatabase database;
    Cursor cSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_one_item_preguntas);

        USincronizacion uSincronizacion = new USincronizacion();
        uSincronizacion.sincronizarAutomaticamente(this, ContractPreguntasFrecuentes.AUTORIDAD, 1800);

        expandableListView = (ExpandableListView) findViewById(R.id.expandablelistview_preguntas);
        expandableListView.setGroupIndicator(null);
        setItems();
        setListener();

    }

    void setItems()
    {

        // Array list for header
        ArrayList<String> head = new ArrayList<>();

        // Array list for child items
        List<String> child1 = new ArrayList<>();
        List<String> child2 = new ArrayList<>();
        List<String> child3 = new ArrayList<>();
        List<String> child4 = new ArrayList<>();
        List<String> child5 = new ArrayList<>();
        List<String> child6 = new ArrayList<>();
        List<String> child7 = new ArrayList<>();
        List<String> child8 = new ArrayList<>();
        List<String> child9 = new ArrayList<>();


        // Hash map for both header and child
        HashMap<String, List<String>> hashMap = new HashMap<>();


        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        database = databaseHelper.getReadableDatabase();
        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 1", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child1.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }


        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 2", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child2.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }


        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 3", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child3.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }


        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 4", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child4.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }


        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 5", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child5.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }


        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 6", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child6.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }



        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 7", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child7.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }


        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 8", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child8.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }



        cSQL = database.rawQuery("SELECT titulo_pregunta, contenido_pregunta  FROM preguntas_frecuentes WHERE idpregunta = 9", null);

        if (cSQL.moveToFirst())
        {
            head.add(cSQL.getString(0));

            do
            {
                child9.add(cSQL.getString(1));
            }
            while (cSQL.moveToNext());

        }


        // Adding header and childs to hash map
        hashMap.put(head.get(0), child1);
        hashMap.put(head.get(1), child2);
        hashMap.put(head.get(2), child3);
        hashMap.put(head.get(3), child4);
        hashMap.put(head.get(4), child5);
        hashMap.put(head.get(5), child6);
        hashMap.put(head.get(6), child7);
        hashMap.put(head.get(7), child8);
        hashMap.put(head.get(8), child9);

        expandableListAdapter = new ExpandableListAdapterPreguntas(this, head, hashMap);
        expandableListView.setAdapter(expandableListAdapter);
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
        expandableListView
                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
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

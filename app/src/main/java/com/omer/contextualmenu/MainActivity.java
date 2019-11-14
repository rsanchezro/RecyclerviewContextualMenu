package com.omer.contextualmenu;

import android.content.DialogInterface;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Model> mData = new ArrayList<>();

    // action mode
    public static boolean isInActionMode = false;
    public static ArrayList<Model> selectionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Añadimos una barra de herramientas Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //Sirve para indicar que el tamaño del recyclerview no depende del contenido
        //del adaptador(true) y por lo tanto no cambia, es una cuestion de optimizacion
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Rellenamos los datos del arraylist
        String[] array = getResources().getStringArray(R.array.array_text);
        for (String text : array) {
            Model model = new Model(R.mipmap.ic_launcher, text);
            mData.add(model);
        }


        // adapter
        mAdapter = new MyAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflamos el menu principal
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void prepareToolbar(int position) {

        // prepare action mode
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action_mode);
        isInActionMode = true;
        mAdapter.notifyDataSetChanged();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prepareSelection(position);
    }


    public void prepareSelection(int position) {

        if (!selectionList.contains(mData.get(position))) {
            selectionList.add(mData.get(position));
        } else {
            selectionList.remove(mData.get(position));
        }

        updateViewCounter();
    }


    private void updateViewCounter() {
        int counter = selectionList.size();
        if (counter == 1) {
            // edit
            toolbar.getMenu().getItem(0).setVisible(true);
        } else {
            toolbar.getMenu().getItem(0).setVisible(false);
        }

        toolbar.setTitle(counter + " item(s) selected");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_edit) {

            if (selectionList.size() == 1) {
                final EditText editText = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("Edit")
                        .setView(editText)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Model model = selectionList.get(0);
                                model.setText(editText.getText().toString());
                                isInActionMode = false;

                                ((MyAdapter) mAdapter).changeDataItem(getCheckedLastPosition(), model);
                                clearActionMode();
                            }
                        })
                        .create().show();
            }

        } else if (item.getItemId() == R.id.item_delete) {
            isInActionMode = false;
            ((MyAdapter) mAdapter).removeData(selectionList);
            clearActionMode();
        } else if (item.getItemId() == android.R.id.home) {
            clearActionMode();
            mAdapter.notifyDataSetChanged();
        }

        return true;
    }

    public void clearActionMode() {
        isInActionMode = false;
        //Limpia el menu del toolbar
        toolbar.getMenu().clear();
        //Infla un nuevo menu, el de por defecto
        toolbar.inflateMenu(R.menu.menu_main);
        if (getSupportActionBar() != null) {
            //Desaparece la flecha
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        toolbar.setTitle(R.string.app_name);
        //Vacia la lista de elementos seleccionados
        selectionList.clear();
    }

    @Override
    public void onBackPressed() {
        if (isInActionMode) {
            clearActionMode();
            mAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    public int getCheckedLastPosition() {
        ArrayList<Model> dataSet = MyAdapter.getDataSet();
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i).equals(selectionList.get(0))) {
                return i;
            }
        }
        return 0;
    }

}
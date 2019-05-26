package com.sururiana.suratkesehatan2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sururiana.suratkesehatan2.helper.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView listsk;
    SwipeRefreshLayout swipe_refresh;

    SqliteHelper sqliteHelper;
    String query_sk;
    Cursor cursor;

    ArrayList<HashMap<String, String>> arussk = new ArrayList<>();

    public static String transaksi_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listsk     = (ListView) findViewById(R.id.list_sk);
        swipe_refresh     = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query_sk = "SELECT *, strftime('%d/%m/%Y', tanggal) AS tgl FROM transaksi ORDER BY transaksi_id DESC";
                Sk_adapter();
            }
        });

        sqliteHelper = new SqliteHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TambahActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        query_sk = "SELECT *, strftime('%d/%m/%Y', tanggal) AS tgl FROM transaksi ORDER BY transaksi_id DESC";

        Sk_adapter();
    }

    public void Sk_adapter(){
        swipe_refresh.setRefreshing(false);

        arussk.clear();listsk.setAdapter(null);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(query_sk, null);
        cursor.moveToFirst();

        for (int i=0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            Log.d("status",cursor.getString(1));

            HashMap<String, String> map = new HashMap<>();
            map.put("transaksi_id", cursor.getString(0));
            map.put("status", cursor.getString(1));
            map.put("nama", cursor.getString(2));
            map.put("berat", cursor.getString(3));
            map.put("tinggi", cursor.getString(4));
            map.put("tensi", cursor.getString(5));
            map.put("keterangan", cursor.getString(6));
            map.put("tanggal", cursor.getString(7));

            arussk.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arussk, R.layout.list_sk,
                new String[]{"transaksi_id", "status","nama","keterangan","tanggal"},
                new int [] {R.id.text_transaksi_id, R.id.text_status, R.id.text_nama, R.id.text_keterangan, R.id.text_tanggal} );
        listsk.setAdapter(simpleAdapter);
        listsk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                transaksi_id = ((TextView) view.findViewById(R.id.text_transaksi_id)).getText().toString();
                listMenu();
            }
        });
    }

    private void listMenu(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.list_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView text_tampil = (TextView)dialog.findViewById(R.id.text_tampil);
        TextView text_hapus = (TextView)dialog.findViewById(R.id.text_hapus);
        TextView text_edit = (TextView)dialog.findViewById(R.id.text_edit);

        text_tampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ViewActivity.class));
            }
        });
        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,EditActivity.class));
            }
        });

        text_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Hapus();
            }
        });
    }

    private void Hapus(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Yakin untuk menghapus?");
        builder.setPositiveButton(
                "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                        database.execSQL(
                                "DELETE FROM transaksi WHERE transaksi_id='" + transaksi_id + "'"
                        );
                        Toast.makeText(MainActivity.this,"Data berhasil dihapus",Toast.LENGTH_LONG).show();
                        Sk_adapter();
                    }
                }
        );
        builder.setNegativeButton(
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.sururiana.suratkesehatan2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sururiana.suratkesehatan2.helper.SqliteHelper;

public class ViewActivity extends AppCompatActivity {
    MainActivity M = new MainActivity();

    RadioGroup radio_status;
    RadioButton radio_sehat, radio_tdksehat;
    TextView view_nama, view_berat, view_tinggi, view_tensi, view_keterangan;
    Cursor cursor;

    SqliteHelper sqliteHelper;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        status = "";

        radio_status = (RadioGroup) findViewById(R.id.radio_status);
        radio_sehat = (RadioButton) findViewById(R.id.radio_sehat);
        radio_tdksehat = (RadioButton) findViewById(R.id.radio_tdksehat);

        view_nama = (TextView) findViewById(R.id.view_nama);
        view_berat = (TextView) findViewById(R.id.view_berat);
        view_tinggi = (TextView) findViewById(R.id.view_tinggi);
        view_tensi = (TextView) findViewById(R.id.view_tensi);
        view_keterangan = (TextView) findViewById(R.id.view_keterangan);

        sqliteHelper = new SqliteHelper(this);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT *, strftime('%d/%m/%Y', tanggal) AS tgl FROM transaksi WHERE transaksi_id='" + M.transaksi_id + "'",
                null);
        cursor.moveToFirst();

        status = cursor.getString(1);
        switch (status) {
            case "Sehat":
                radio_sehat.setChecked(true);
                break;
            case "Tidak Sehat":
                radio_tdksehat.setChecked(true);
                break;
        }
        view_nama.setText("Nama : "+(cursor.getString(2)));
        view_berat.setText("Berat : "+cursor.getString(3));
        view_tinggi.setText("Tinggi : "+cursor.getString(4));
        view_tensi.setText("Tekanan Darah : "+cursor.getString(5));
        view_keterangan.setText("Keterangan : "+cursor.getString(6));

        getSupportActionBar().setTitle("View Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp () {
        finish();
        return true;
    }

}

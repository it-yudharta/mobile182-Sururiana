package com.sururiana.suratkesehatan2;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.sururiana.suratkesehatan2.helper.SqliteHelper;

public class TambahActivity extends AppCompatActivity {

    EditText edit_nama, edit_berat, edit_tinggi, edit_tensi, edit_keterangan;
    Button btn_simpan;
    RippleView rip_simpan;
    RadioGroup radio_status;

    String status;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        status = "";
        sqliteHelper = new SqliteHelper(this);

        radio_status = (RadioGroup) findViewById(R.id.radio_status);
        edit_nama = (EditText) findViewById(R.id.edit_nama);
        edit_berat = (EditText) findViewById(R.id.edit_berat);
        edit_tinggi = (EditText) findViewById(R.id.edit_tinggi);
        edit_tensi = (EditText) findViewById(R.id.edit_tensi);
        edit_keterangan = (EditText) findViewById(R.id.edit_keterangan);

        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        rip_simpan = (RippleView) findViewById(R.id.rip_simpan);

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_sehat:
                        status = "Sehat";
                        break;
                    case R.id.radio_tdksehat:
                        status = "Tidak Sehat";
                        break;
                }
                Log.d("Log Status", status);
            }
        });

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (status.equals("") || edit_nama.getText().toString().equals("") || edit_berat.getText().toString().equals("") ||
                        edit_tinggi.getText().toString().equals("") || edit_tensi.getText().toString().equals("") || edit_keterangan.getText().toString().equals("")) {

                    Toast.makeText(TambahActivity.this, "Isi data dengan lengkap", Toast.LENGTH_LONG).show();
                } else {
                    SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                    database.execSQL(
                            "INSERT INTO transaksi (status, nama, berat, tinggi, tensi, keterangan) VALUES ('" +
                                    status + "','" + edit_nama.getText().toString() + "','" + edit_berat.getText().toString() + "'," +
                                    "'" + edit_tinggi.getText().toString() + "','" + edit_tensi.getText().toString() + "','" + edit_keterangan.getText().toString() + "')"
                    );
                    Toast.makeText(TambahActivity.this, "Data berhasil di simpan", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        getSupportActionBar().setTitle("Tambah Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

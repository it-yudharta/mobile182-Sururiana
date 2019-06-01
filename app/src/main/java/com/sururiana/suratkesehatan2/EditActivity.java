package com.sururiana.suratkesehatan2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.sururiana.suratkesehatan2.helper.SqliteHelper;

public class EditActivity extends AppCompatActivity {
    MainActivity M = new MainActivity();

    RadioGroup radio_status;
    RadioButton radio_sehat, radio_tdksehat;
    EditText edit_nama, edit_berat, edit_tinggi, edit_tensi, edit_keterangan;
    Cursor cursor;
    Button btn_simpan;
    RippleView rip_simpan;

    SqliteHelper sqliteHelper;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        status = "";

        radio_status = (RadioGroup) findViewById(R.id.radio_status);
        radio_sehat = (RadioButton) findViewById(R.id.radio_sehat);
        radio_tdksehat = (RadioButton) findViewById(R.id.radio_tdksehat);

        edit_nama = (EditText) findViewById(R.id.edit_nama);
        edit_berat = (EditText) findViewById(R.id.edit_berat);
        edit_tinggi = (EditText) findViewById(R.id.edit_tinggi);
        edit_tensi = (EditText) findViewById(R.id.edit_tensi);
        edit_keterangan = (EditText) findViewById(R.id.edit_keterangan);

        rip_simpan = (RippleView) findViewById(R.id.rip_simpan);

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
        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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

        edit_nama.setText("Nama : "+(cursor.getString(2)));
        edit_berat.setText("Berat : "+cursor.getString(3));
        edit_tinggi.setText("Tinggi : "+cursor.getString(4));
        edit_tensi.setText("Tekanan Darah : "+cursor.getString(5));
        edit_keterangan.setText("Keterangan : "+cursor.getString(6));

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (status.equals("") || edit_nama.getText().toString().equals("") || edit_berat.getText().toString().equals("") ||
                        edit_tinggi.getText().toString().equals("") || edit_tensi.getText().toString().equals("") || edit_keterangan.getText().toString().equals("")) {

                    Toast.makeText(EditActivity.this, "Isi data dengan lengkap", Toast.LENGTH_LONG).show();
                } else {
                    SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                    database.execSQL(
                            "UPDATE transaksi SET status='" + status + "',nama='" + edit_nama.getText().toString() + "',berat='" + edit_berat.getText().toString() + "'," +
                                    "tinggi='" + edit_tinggi.getText().toString() + "',tensi='" + edit_tensi.getText().toString() + "',keterangan='" + edit_keterangan.getText().toString() + "' WHERE transaksi_id='" + M.transaksi_id + "')"
                    );
                    Toast.makeText(EditActivity.this, "Data berhasil di simpan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        getSupportActionBar().setTitle("Edit Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

        @Override
        public boolean onSupportNavigateUp () {
            finish();
            return true;
        }
}


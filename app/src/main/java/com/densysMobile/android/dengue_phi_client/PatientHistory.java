package com.densysMobile.android.dengue_phi_client;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.densysMobile.android.dengue_phi_client.Network.NetworkCore;
import com.densysMobile.android.dengue_phi_client.Model.Record;
import com.densysMobile.android.dengue_phi_client.Model.VisitDetail;

import java.util.Comparator;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class PatientHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        String patientNo = getIntent().getStringExtra("KEY_PATIENT_NO");
        String patientType = getIntent().getStringExtra("KEY_PATIENT_TYPE");
        SortableTableView sortableTableView = (SortableTableView) findViewById(R.id.historyTable);
        sortableTableView.setHeaderBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        sortableTableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, "Fever Type", "Inspect Date", "Action Taken", "Comment", "Visit Id"));
        NetworkCore.getPatientVisitHistory(this, patientNo,patientType, sortableTableView);
//        sortableTableView.setDataAdapter(new PatientVisitHistoryDataAdapter(PatientHistory.this, Util.getVisitDetails(), sortableTableView));
        sortableTableView.setColumnComparator(4, new VisitIdComparator());
        sortableTableView.setColumnComparator(0, new FeverTypeComparator());
        sortableTableView.setEmptyDataIndicatorView(findViewById(R.id.empty_data_indicator));
    }

    private class VisitIdComparator implements Comparator<VisitDetail> {
        @Override
        public int compare(VisitDetail o1, VisitDetail o2) {
            return o1.getpVisitId().compareTo(o2.getpVisitId());
        }
    }

    private class FeverTypeComparator implements Comparator<VisitDetail> {

        @Override
        public int compare(VisitDetail o1, VisitDetail o2) {
            return o1.getpTypeFever().compareTo(o2.getpTypeFever());
        }
    }

    private class PatientDateAddedComparator implements Comparator<Record> {

        @Override
        public int compare(Record o1, Record o2) {
            return o1.getPtDateAdd().getDate().compareTo(o2.getPtDateAdd().getDate());
        }
    }
}

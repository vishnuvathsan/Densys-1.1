package com.densysMobile.android.dengue_phi_client;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.densysMobile.android.dengue_phi_client.Network.NetworkCore;
import com.densysMobile.android.dengue_phi_client.Model.Record;
import com.densysMobile.android.dengue_phi_client.Model.RecordView;
import com.densysMobile.android.dengue_phi_client.Model.SearchParam;

import java.util.Comparator;
import java.util.Iterator;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ViewPatientList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_list);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        final FloatingActionButton infoButton = (FloatingActionButton) findViewById(R.id.patientListInfoButton);
        final SearchParam searchParam = (SearchParam) getIntent().getSerializableExtra("KEY");
        final SortableTableView<RecordView> sortableTableView = (SortableTableView<RecordView>) findViewById(R.id.tableView);

        NetworkCore.getRecordList(sortableTableView, ViewPatientList.this, searchParam);
        sortableTableView.setHeaderBackgroundColor(ContextCompat.getColor(ViewPatientList.this, R.color.colorPrimary));
        sortableTableView.setHeaderAdapter(new SimpleTableHeaderAdapter(ViewPatientList.this, "ID", "Name", "BHT"
                , "Hospital", "PHI Area", "Date Added", "Visits"));
        sortableTableView.setSwipeToRefreshEnabled(true);
        sortableTableView.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
//                Toast.makeText(ViewPatientList.this, "Fetching Data.. Please Wait.", Toast.LENGTH_SHORT).show();
                NetworkCore.getRecordList(sortableTableView, ViewPatientList.this, searchParam);
                if (refreshIndicator.isVisible()) {
                    refreshIndicator.hide();
                }
            }
        });

        sortableTableView.addDataClickListener(new TableDataClickListener<RecordView>() {
            @Override
            public void onDataClicked(int rowIndex, RecordView clickedData) {
                Intent intent = new Intent(ViewPatientList.this, PatientVisit.class);
                String recId = clickedData.getRecid();
                Record clickedRecord = PatientDetails.getRecord(recId);
                intent.putExtra("KEY", clickedRecord);
                startActivityForResult(intent, 0);
            }
        });
        sortableTableView.setColumnComparator(0, new RecIdComparator());
        sortableTableView.setColumnComparator(5, new PatientDateAddedComparator());
        sortableTableView.setColumnComparator(6, new VisitComparator());
        sortableTableView.setDataRowBackgroundProvider(new RecordViewColorProvider());

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(v.getContext())
                        .title("Patient List Info")
                        .customView(R.layout.custom_patient_list_info, true)
                        .autoDismiss(false)
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                dialog.show();

                TextView totalItemsTV = (TextView) dialog.getCustomView().findViewById(R.id.itemCount);
                TextView visitedTV = (TextView) dialog.getCustomView().findViewById(R.id.visited);
                TableDataAdapter<RecordView> dataAdapter = sortableTableView.getDataAdapter();
                int itemCount = 0;
                int visitedCount = 0;
                Iterator<RecordView> iterator = dataAdapter.getData().iterator();
                while (iterator.hasNext()) {
                    itemCount++;
                    if (Integer.parseInt(iterator.next().getVisitCount()) > 0) {
                        visitedCount++;
                    }
                }
                totalItemsTV.setText("Total number of cases reported - " + itemCount);
                visitedTV.setText("Number of visited cases - " + visitedCount);
            }
        });
        sortableTableView.setLongClickable(false);
        sortableTableView.setSaveEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private class RecIdComparator implements Comparator<RecordView> {
        @Override
        public int compare(RecordView o1, RecordView o2) {
            return o1.getRecid().compareTo(o2.getRecid());
        }
    }

    private class PatientDateAddedComparator implements Comparator<RecordView> {
        @Override
        public int compare(RecordView o1, RecordView o2) {
            return o1.getPtDateAdd().getDate().compareTo(o2.getPtDateAdd().getDate());
        }
    }


    private class VisitComparator implements Comparator<RecordView> {
        @Override
        public int compare(RecordView o1, RecordView o2) {
            return o1.getVisitCount().compareTo(o2.getVisitCount());
        }
    }

    private class RecordViewColorProvider implements TableDataRowBackgroundProvider<RecordView> {

        @Override
        public Drawable getRowBackground(int rowIndex, RecordView rowData) {
            int color;
            if (Integer.parseInt(rowData.getVisitCount()) == 1) {
                color = ContextCompat.getColor(ViewPatientList.this, R.color.yellow);
            } else if (Integer.parseInt(rowData.getVisitCount()) >= 2) {
                color = ContextCompat.getColor(ViewPatientList.this, R.color.orange);
            } else {
                color = ContextCompat.getColor(ViewPatientList.this, R.color.white);
            }
            return new ColorDrawable(color);
        }
    }
}

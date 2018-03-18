package com.densysMobile.android.dengue_phi_client.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.densysMobile.android.dengue_phi_client.Model.RecordView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

/**
 * Created by Sineth on 2/2/2017.
 */

public class RecordTableDataAdapter extends TableDataAdapter<RecordView> {
    private static final int TEXT_SIZE = 14;

    public RecordTableDataAdapter(Context context, List<RecordView> data) {
        super(context, data);
    }

    private View renderVisitCount(RecordView record) {
        return renderString(record.getVisitCount());
    }


    private View renderphi(RecordView record) {
        return renderString(record.getPhiDesc());
    }



    private View renderHospital(RecordView record) {
        return renderString(record.getHospitalName());
    }

    private View renderBHT(RecordView record){
        return renderString(record.getBHT());
    }

    private View renderPatientDateAdd(RecordView record) {
        SimpleDateFormat outputDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat inputDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedString = null;
        try {
            Date parse = inputDateFormat.parse(record.getPtDateAdd().getDate());
            formattedString = outputDateFormat.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return renderString(formattedString);
//        return renderString(record.getPtDateAdd().getDate());
    }

    private View renderPatientName(RecordView record) {
        return renderString(record.getPtName());
    }



    private View renderrecid(RecordView record) {
        return renderString(record.getRecid());
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        RecordView record = getRowData(rowIndex);
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderrecid(record);
                break;
            case 1:
                renderedView = renderPatientName(record);
                break;
            case 2:
                renderedView = renderBHT(record);

                break;
            case 3:
                renderedView = renderHospital(record);
                break;
            case 4:
                renderedView = renderphi(record);
                break;
            case 5:
                renderedView = renderPatientDateAdd(record);
                break;
            case 6:
                renderedView = renderVisitCount(record);
        }
        return renderedView;
    }
}

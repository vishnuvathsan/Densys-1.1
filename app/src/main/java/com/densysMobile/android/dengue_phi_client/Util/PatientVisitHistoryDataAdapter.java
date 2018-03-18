package com.densysMobile.android.dengue_phi_client.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.densysMobile.android.dengue_phi_client.Model.VisitDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

/**
 * Created by Sineth on 2/3/2017.
 */

public class PatientVisitHistoryDataAdapter extends LongPressAwareTableDataAdapter<VisitDetail> {

    private static final float TEXT_SIZE = 14;

    /**
     * Creates a new {@link LongPressAwareTableDataAdapter} with the given paramters.
     *
     * @param context   The context that shall be used.
     * @param data      The data that shall be displayed.
     * @param tableView The table to listen for long presses by the user.
     */
    public PatientVisitHistoryDataAdapter(Context context, List<VisitDetail> data, TableView<VisitDetail> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        VisitDetail visitDetail = getRowData(rowIndex);
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderFeverType(visitDetail);
                break;
            case 1:
                renderedView = renderInspectionDate(visitDetail);
                break;
            case 2:
                renderedView = renderActionTaken(visitDetail);
                break;
            case 3:
                renderedView = renderComment(visitDetail);
                break;
            case 4:
                renderedView = renderVisitId(visitDetail);
                break;
        }
        return renderedView;
    }

    private View renderVisitId(VisitDetail visitDetail) {
        return renderString(visitDetail.getpVisitId());
    }

    private View renderComment(VisitDetail visitDetail) {
        return renderString(visitDetail.getpComment());
    }

    private View renderActionTaken(VisitDetail visitDetail) {
        return renderString(visitDetail.getpActionTaken());
    }

    private View renderInspectionDate(VisitDetail visitDetail) {
        SimpleDateFormat outputDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat inputDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedString = null;
        try {
            Date parse = inputDateFormat.parse(visitDetail.getpDateInspect().getDate());
            formattedString = outputDateFormat.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return renderString(formattedString);
    }

    private View renderFeverType(VisitDetail visitDetail) {
        return renderString(visitDetail.getpTypeFever());
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        return null;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }
}

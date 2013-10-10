package com.voskalenko.foursquretracker.views;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.voskalenko.foursquretracker.R;

/**
 * Created with IntelliJ IDEA.
 * User: Vitaly
 * Date: 10.10.13
 * Time: 11:34
 * To change this template use File | Settings | File Templates.
 */
@EViewGroup(R.layout.view_proposed_venue_group)
public class ProposedVenueGroupView extends RelativeLayout {

    @ViewById(R.id.txt_header)
    TextView txtHeader;

    public ProposedVenueGroupView(Context context) {
        super(context);
    }

    public TextView getTxtHeader() {
        return txtHeader;
    }

    public void setTxtHeader(TextView txtHeader) {
        this.txtHeader = txtHeader;
    }

}

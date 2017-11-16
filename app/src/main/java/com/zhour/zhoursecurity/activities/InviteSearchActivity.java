package com.zhour.zhoursecurity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Constants;
import com.zhour.zhoursecurity.adapters.InviteSearchAdapter;
import com.zhour.zhoursecurity.models.VisitorListModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteSearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @BindView(R.id.list_invite_search)
    ListView list_invite_search;

    private Intent intent;
    private VisitorListModel visitorListModel;
    private InviteSearchAdapter inviteSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_search);
        ButterKnife.bind(this);
        getIntentData();
    }

    /**
     * This method is used to get the intent data
     */
    private void getIntentData() {
        intent = getIntent();
        if (intent.hasExtra(Constants.VISITOR_MODEL)) {
            visitorListModel = (VisitorListModel) intent.getSerializableExtra(Constants.VISITOR_MODEL);
        }

        if (visitorListModel != null && visitorListModel.getVisitorModels() != null && visitorListModel.getVisitorModels().size() > 0) {
            inviteSearchAdapter = new InviteSearchAdapter(InviteSearchActivity.this, visitorListModel.getVisitorModels());
            list_invite_search.setAdapter(inviteSearchAdapter);
        }

        list_invite_search.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent detailsIntent = new Intent(InviteSearchActivity.this, DetailsActivity.class);
        detailsIntent.putExtra(Constants.VISITOR_MODEL, visitorListModel.getVisitorModels().get(i));
        startActivity(detailsIntent);
    }
}

package young.tze.appstat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MainActivity extends Activity {

    private EditText mEditText;
    private LoaderTask mLoaderTask;
    private RecyclerView mRecyclerView;
    private View mProgressBarContainer;
    private View mIndexBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mIndexBar = findViewById(R.id.index_bar);
        mProgressBarContainer = findViewById(R.id.progress_bar_container);
        mEditText = (EditText) findViewById(R.id.edittext);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        startQuery("");
    }


    private RecyclerViewDecoration mRecyclerViewDecoration;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerViewAdapter mAdapter;

    void updateData(ArrayList<ItemInfo> itemInfos) {
        mProgressBarContainer.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mIndexBar.setVisibility(View.VISIBLE);
        if (mRecyclerViewDecoration == null) {
            mRecyclerViewDecoration = new RecyclerViewDecoration(this, itemInfos);
            mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            //mLinearLayoutManager.setStackFromEnd(true);
            mLinearLayoutManager.setReverseLayout(true);
            mAdapter = new RecyclerViewAdapter(this, itemInfos);
            mRecyclerView.addItemDecoration(mRecyclerViewDecoration);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mRecyclerViewDecoration.setDataList(itemInfos);
            mAdapter.setDataList(itemInfos);
            mAdapter.notifyDataSetChanged();
            mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
        }
        mProgressBarContainer.setVisibility(View.INVISIBLE);

    }

    private void startQuery(String input) {
        if (mLoaderTask != null)
            mLoaderTask.cancel(true);
        mProgressBarContainer.setVisibility(View.VISIBLE);
        mLoaderTask = new LoaderTask(this);
        mLoaderTask.execute(input);
    }
}

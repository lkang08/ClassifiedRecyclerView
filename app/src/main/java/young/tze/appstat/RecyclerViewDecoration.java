package young.tze.appstat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Yangzhi on 2017-06-30.
 */

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
    private ArrayList<ItemInfo> mItemInfos;
    private int mItemMargin;
    private int mItemLabelHeight;
    private int mItemDividerHeight;
    private int mItemLabelTextSize;
    private int mItemLabelTextTopPadding;
    private int mItemGapHeight;
    private int mLabelTextColor;
    private int mFloatCircleColor;
    private int mFloatCircleRadius;
    private boolean mReverse = true;
    private int mStatusBarHeight;
    private int mEditTextHeight;

    private Paint mLabelTextPaint;
    private Paint mFloatCirclePaint;
    private Paint mLabelAreaPaint;
    private Paint mFloatCircleTextPaint;

    private Drawable mItemDivider;
    private Drawable mItemGap;

    public RecyclerViewDecoration(Context context, ArrayList<ItemInfo> itemInfos) {
        this.mItemInfos = itemInfos;

        mItemMargin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
        mItemLabelHeight = context.getResources().getDimensionPixelSize(R.dimen.item_label_height);
        mItemDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.item_divider_height);
        mItemLabelTextTopPadding = context.getResources().getDimensionPixelSize(R.dimen.item_label_text_top_padding);
        mItemGapHeight = context.getResources().getDimensionPixelSize(R.dimen.item_gap_height);

        mItemLabelTextSize = context.getResources().getDimensionPixelSize(R.dimen.item_label_text_size);
        mLabelTextColor = context.getResources().getColor(R.color.labelTextColor);
        mFloatCircleColor = context.getResources().getColor(R.color.itemGapColor);

        mFloatCircleRadius = 3 * mItemLabelTextSize / 2;

        mLabelTextPaint = new Paint();
        mLabelTextPaint.setColor(mLabelTextColor);
        mLabelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mLabelTextPaint.setTextSize(mItemLabelTextSize);

        mLabelAreaPaint = new Paint();
        mLabelAreaPaint.setColor(Color.WHITE);

        mFloatCirclePaint = new Paint();
        mFloatCirclePaint.setColor(mFloatCircleColor);


        mFloatCircleTextPaint = new Paint();
        mFloatCircleTextPaint.setColor(Color.WHITE);
        mFloatCircleTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mFloatCircleTextPaint.setTextSize(2 * mItemLabelTextSize);

        mEditTextHeight = context.getResources().getDimensionPixelSize(R.dimen.edittext_height);
        mStatusBarHeight = Utilities.getStatusbarHeight(context);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            mItemDivider = activity.getResources().getDrawable(R.drawable.recycler_view_divider, null);
            mItemGap = activity.getResources().getDrawable(R.drawable.recycler_view_gap, null);
        }
    }

    public void setDataList(ArrayList<ItemInfo> itemInfos) {
        mItemInfos = itemInfos;
    }

    private void drawGroupViewReverse(Canvas c, RecyclerView parent) {
        if (mItemInfos.size() == 0)
            return;
        int childCount = parent.getChildCount();
        int itemCount = mItemInfos.size();
        //Log.d("lk", "drawGroupView childCount =  " + childCount);
        for (int i = 0; i < childCount; i++) {
            View itemView = parent.getChildAt(i);
            int j = parent.getChildAdapterPosition(itemView);
            ItemInfo itemInfo = mItemInfos.get(j);
            //第0个位置不需要绘制
            if (j == 0) {
                /*mItemGap.setBounds(0, itemView.getBottom() + mItemLabelHeight + mItemDividerHeight,
                        parent.getWidth(), itemView.getBottom() + mItemLabelHeight + mItemDividerHeight + mItemGapHeight);
                mItemGap.draw(c);*/
                mLabelTextPaint.setAntiAlias(true);
                char labelTitle = Character.toUpperCase(itemInfo.getAppNameInPinyin().charAt(0));
                if (!Character.isLetter(labelTitle)) {
                    labelTitle = '#';
                }
                c.drawText("" + labelTitle, mItemMargin,
                        itemView.getBottom() + mItemDividerHeight + mItemLabelTextTopPadding, mLabelTextPaint);
                mLabelTextPaint.setAntiAlias(false);
                mItemDivider.setBounds(mItemMargin, itemView.getBottom(), itemView.getWidth() + mItemMargin, itemView.getBottom() + mItemDividerHeight);
                mItemDivider.draw(c);
            } else if (j < itemCount) {
                ItemInfo preItemInfo = mItemInfos.get(j - 1);
                if (preItemInfo.getAppNameInPinyin().charAt(0) != itemInfo.getAppNameInPinyin().charAt(0)) {
                    //需要绘item上方item gap、label title及divider分隔线
                    if ((preItemInfo.getAppNameInPinyin().charAt(0) >= 'a' && preItemInfo.getAppNameInPinyin().charAt(0) <= 'z')
                            || (itemInfo.getAppNameInPinyin().charAt(0) >= 'a' && itemInfo.getAppNameInPinyin().charAt(0) <= 'z')) {
                        mItemGap.setBounds(0, itemView.getBottom() + mItemLabelHeight + mItemDividerHeight,
                                parent.getWidth(), itemView.getBottom() + mItemLabelHeight + mItemDividerHeight + mItemGapHeight);
                        mItemGap.draw(c);
                        mLabelTextPaint.setAntiAlias(true);
                        char labelTitle = Character.toUpperCase(itemInfo.getAppNameInPinyin().charAt(0));
                        if (!Character.isLetter(labelTitle)) {
                            labelTitle = '#';
                        }
                        c.drawText("" + labelTitle, mItemMargin,
                                itemView.getBottom() + mItemDividerHeight + mItemLabelTextTopPadding, mLabelTextPaint);
                        mLabelTextPaint.setAntiAlias(false);
                        mItemDivider.setBounds(mItemMargin, itemView.getBottom(), itemView.getWidth() + mItemMargin, itemView.getBottom() + mItemDividerHeight);
                        mItemDivider.draw(c);
                    }
                } else {
                    //仅绘item下方的divider分隔线
                    mItemDivider.setBounds(mItemMargin, itemView.getBottom(), itemView.getWidth() + mItemMargin, itemView.getBottom() + mItemDividerHeight);
                    mItemDivider.draw(c);
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawGroupViewReverse(c, parent);
    }

    private void drawSuspendView(Canvas c, RecyclerView parent) {
        if (mItemInfos.size() == 0) {
            return;
        }
        //int lastChildIndex = parent.getChildCount() - 1;
        View lastChild = parent.getChildAt(0);
        ItemInfo lastItemInfo = mItemInfos.get(parent.getChildAdapterPosition(lastChild));
        char floatLabelTitle = Character.toUpperCase(lastItemInfo.getAppNameInPinyin().charAt(0));
        int floatLabelTranslationY = 0;
        int screenHeight = Utilities.getDisplayMetrics(parent.getContext()).heightPixels - mEditTextHeight;
        char floatCircleTitle = lastItemInfo.getAppNameInDefault().charAt(0);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(childView);
            if (adapterPosition == 0) {
                continue;
            }
            //找到当前recyclerView中最后一个带有Label的Item
            if (parent.findChildViewUnder(mItemMargin + childView.getWidth() / 2,
                    childView.getBottom() + mItemDividerHeight + mItemLabelHeight / 2) == null) {
                if ((childView.getBottom() > screenHeight - mStatusBarHeight - 2 * mItemLabelHeight - mItemDividerHeight)
                        && childView.getBottom() < screenHeight - mStatusBarHeight - mItemLabelHeight - mItemDividerHeight) {
                    floatLabelTitle = Character.toUpperCase(mItemInfos.get(adapterPosition - 1).getAppNameInPinyin().charAt(0));
                    floatLabelTranslationY = mItemLabelHeight - ((screenHeight - mStatusBarHeight - childView.getBottom() - mItemDividerHeight - mItemLabelHeight));
                    break;
                } else {
                    floatLabelTranslationY = 0;
                }
            }
        }
        c.drawRect(new Rect(0, screenHeight - mItemLabelHeight - mStatusBarHeight + floatLabelTranslationY,
                parent.getWidth(), screenHeight - mStatusBarHeight + floatLabelTranslationY), mLabelAreaPaint);
        mItemDivider.setBounds(0, screenHeight - mItemLabelHeight - mItemDividerHeight - mStatusBarHeight + floatLabelTranslationY,
                parent.getWidth(), screenHeight - mItemLabelHeight - mStatusBarHeight);
        mItemDivider.draw(c);
        mLabelTextPaint.setAntiAlias(true);
        if (!Character.isLetter(floatLabelTitle)) {
            floatLabelTitle = '#';
        }
        c.drawText("" + floatLabelTitle, mItemMargin, mItemLabelTextTopPadding + screenHeight - mItemLabelHeight - mStatusBarHeight + floatLabelTranslationY, mLabelTextPaint);
        mLabelTextPaint.setAntiAlias(false);

        if (parent.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
            mFloatCirclePaint.setAntiAlias(true);
            c.drawCircle(Utilities.getDisplayMetrics(parent.getContext()).widthPixels / 2,
                    Utilities.getDisplayMetrics(parent.getContext()).heightPixels / 2, mFloatCircleRadius, mFloatCirclePaint);
            mFloatCirclePaint.setAntiAlias(false);
            mFloatCircleTextPaint.setAntiAlias(true);
            //设置文本水平左右居中
            mFloatCircleTextPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fm = mFloatCircleTextPaint.getFontMetrics();
            float y = Utilities.getDisplayMetrics(parent.getContext()).heightPixels / 2 - fm.descent + (fm.bottom - fm.top) / 2;
            c.drawText("" + floatCircleTitle, Utilities.getDisplayMetrics(parent.getContext()).widthPixels / 2,
                    y, mFloatCircleTextPaint);
            mFloatCircleTextPaint.setAntiAlias(false);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        drawSuspendView(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int count = mItemInfos.size();
        int childPosition = parent.getChildAdapterPosition(view);
        ItemInfo itemInfo = mItemInfos.get(childPosition);
        if (childPosition == 0) {
            outRect.set(0, 0, 0, mItemLabelHeight + mItemDividerHeight);
        } else {
            ItemInfo nextItemInfo = mItemInfos.get(childPosition - 1);

            if (nextItemInfo.getAppNameInPinyin().charAt(0) != itemInfo.getAppNameInPinyin().charAt(0)) {
                if ((nextItemInfo.getAppNameInPinyin().charAt(0) >= 'a' && nextItemInfo.getAppNameInPinyin().charAt(0) <= 'z')
                        || (itemInfo.getAppNameInPinyin().charAt(0) >= 'a' && itemInfo.getAppNameInPinyin().charAt(0) <= 'z')) {
                    outRect.set(0, 0, 0, mItemGapHeight + mItemLabelHeight + mItemDividerHeight);
                }
            } else {
                outRect.set(0, 0, 0, mItemDividerHeight);
            }
        }
    }
}

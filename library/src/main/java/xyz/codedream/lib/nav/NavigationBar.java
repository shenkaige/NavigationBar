package xyz.codedream.lib.nav;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 公用导航
 *
 * @author skg
 */
public class NavigationBar extends FrameLayout implements OnClickListener {

    private LinearLayout leftBox;
    private LinearLayout rightBox;
    private ViewGroup titleViewBlock;
    private TextView titleView;

    public interface NavItem {

        public enum ResType {
            layout, drawable, string
        }

        public int getResId();

        public String getTitle();

        public ResType getResType();

        public int getId();
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.template_nav_bar, this, true);
        titleViewBlock = (ViewGroup) findViewById(R.id.template_nav_title_block);
        titleView = (TextView) findViewById(R.id.template_nav_title);
        leftBox = (LinearLayout) findViewById(R.id.template_nav_left_box);
        rightBox = (LinearLayout) findViewById(R.id.template_nav_right_box);
    }

    public View findViewByNavItem(NavItem item) {
        return findViewById(item.getId());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int leftOffset = 0;
        int rightOffset = 0;
        View v = findSizeableViewFromEnd(leftBox);
        if (v != null) {
            leftOffset = v.getLeft() + v.getMeasuredWidth();
            v = null;
        }
        v = rightBox.getChildAt(0);
        v = findSizeableViewFromHeader(rightBox);
        if (v != null) {
            rightOffset = getMeasuredWidth() - rightBox.getLeft() - v.getLeft();
        }
        int minOffset = Math.max(leftOffset, rightOffset);
        titleViewBlock.setPadding(minOffset, 0, minOffset, 0);
        titleViewBlock.invalidate();
    }

    private View findSizeableViewFromHeader(ViewGroup vg) {
        int childcount;
        if (vg == null || (childcount = vg.getChildCount()) <= 0) {
            return null;
        }
        for (int i = 0; i < childcount; i++) {
            View v = vg.getChildAt(i);
            if (v.getVisibility() == View.VISIBLE) {
                return v;
            }
        }
        return null;
    }

    private View findSizeableViewFromEnd(ViewGroup vg) {
        int childcount;
        if (vg == null || (childcount = vg.getChildCount()) <= 0) {
            return null;
        }
        for (int i = childcount - 1; i > -1; i--) {
            View v = vg.getChildAt(i);
            if (v.getVisibility() == View.VISIBLE) {
                return v;
            }
        }
        return null;
    }

    /**
     * 设置title
     *
     * @param title
     */
    public void setTitle(String title) {
        titleView.setText(title);
    }

    /**
     * 通过 string res id 设置title
     *
     * @param titleResId
     */
    public void setTitle(int titleResId) {
        String titleStr = null;
        try {
            titleStr = getResources().getString(titleResId);
        } catch (Exception e) {
        }
        setTitle(titleStr);
    }

    /**
     * 获取标题View
     *
     * @return
     */
    public TextView getTitleView() {
        return titleView;
    }

    public void addTopLayer(View layerView) {
        changeNavVisibility(View.GONE);
        addView(layerView);
    }

    public void removeTopLayer(View layerView) {
        removeView(layerView);
        changeNavVisibility(View.VISIBLE);
    }

    private void changeNavVisibility(int visibility) {
        leftBox.setVisibility(visibility);
        rightBox.setVisibility(visibility);
        titleViewBlock.setVisibility(visibility);
        titleView.setVisibility(visibility);
    }

    /**
     * 使用自定义的View替换title view
     *
     * @param titleViewLayoutId
     */
    public View replaceTitleView(int titleViewLayoutId) {
        View v = LayoutInflater.from(getContext()).inflate(titleViewLayoutId, this, false);
        replaceTitleView(v);
        return v;
    }

    /**
     * 使用自定义的View替换title view
     *
     * @param titleView
     */
    public void replaceTitleView(View titleView) {
        titleViewBlock.removeAllViews();
        if (titleView != null) {
            titleViewBlock.addView(titleView);
        }
    }

    public void setTitleTextViewDrawable(Drawable l, Drawable t, Drawable r, Drawable b) {
        setDrawableBound(l);
        setDrawableBound(t);
        setDrawableBound(r);
        setDrawableBound(b);
        titleView.setCompoundDrawables(l, t, r, b);
        // 解决低版本sdk不能生效的问
        titleViewBlock.requestLayout();
        titleViewBlock.invalidate();
    }

    private void setDrawableBound(Drawable d) {
        if (d != null) {
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }
    }

    /**
     * 添加的资源的类型
     */
    public enum ResIdType {
        layout, drawable, text
    }

    /**
     * @see #addFromLeft(NavItem, boolean)
     */
    public void addFromLeft(NavItem item) {
        addFromLeft(item, true);
    }

    /**
     * 从左边开始添加
     *
     * @param item
     * @param defaultShow 默认是显示还是隐藏
     */
    public void addFromLeft(NavItem item, boolean defaultShow) {
        addFromLeft(item, getDefaultVisibleFlag(defaultShow));
    }

    /**
     * @param item
     * @param visibale View的可见属性Gone，Visible，invisible
     */
    public void addFromLeft(NavItem item, int visibale) {
        View v = createItem(item, leftBox);
        leftBox.addView(v);
        //
        innerChangeNavChildViewVisiable(v, visibale);
        perItemAdded(v);
    }

    /**
     * @see #addFromRight(NavItem, boolean)
     */
    public void addFromRight(NavItem item) {
        addFromRight(item, true);
    }

    /**
     * 从右边开始添加
     *
     * @param item
     * @param defaultShow 默认是显示还是隐藏
     */
    public void addFromRight(NavItem item, boolean defaultShow) {
        addFromRight(item, getDefaultVisibleFlag(defaultShow));
    }

    /**
     * @param item
     * @param visibale View的可见属性Gone，Visible，invisible
     */
    public void addFromRight(NavItem item, int visibale) {
        View v = createItem(item, rightBox);
        //
        innerChangeNavChildViewVisiable(v, visibale);
        rightBox.addView(v);
        perItemAdded(v);
    }

    private void perItemAdded(View item) {
        // if (item != null) {
        // ViewGroup.LayoutParams lp = item.getLayoutParams();
        // if (lp == null) {
        // lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.MATCH_PARENT);
        // } else {
        // lp.width = LayoutParams.WRAP_CONTENT;
        // lp.height = LayoutParams.WRAP_CONTENT;
        // }
        // item.setBackgroundColor(Color.BLUE);
        // item.setLayoutParams(lp);
        // }
    }

    public boolean isHaveNavItem(NavItem nav) {
        return findViewById(nav.getId()) != null;
    }

    /**
     * 修改NAV Item的可见性
     *
     * @param childId
     * @param show
     */
    public void changeNavChildVisiable(int childId, boolean show) {
        innerChangeNavChildViewVisiable(findViewById(childId), show);
    }

    public void changeNavChildVisiable(int childId, int visible) {
        innerChangeNavChildViewVisiable(findViewById(childId), visible);
    }

    private void innerChangeNavChildViewVisiable(View v, boolean show) {
        if (v == null) {
            return;
        }
        int targetVisible = getDefaultVisibleFlag(show);
        innerChangeNavChildViewVisiable(v, targetVisible);
    }

    private int getDefaultVisibleFlag(boolean show) {
        return show ? View.VISIBLE : View.INVISIBLE;
    }

    private void innerChangeNavChildViewVisiable(View v, int visible) {
        if (v == null) {
            return;
        }
        checkViewVisible(v, visible);
        if (v.getTag() instanceof View) {
            checkViewVisible((View) v.getTag(), visible);
        }
    }

    private void checkViewVisible(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    /**
     * 状态监听
     *
     * @author skg
     */
    public interface NavgationListener {
        public void onNavigationClick(View v, NavItem item, NavigationBar nav);
    }

    private NavgationListener mNavListener;

    public void setNavgationListener(NavgationListener l) {
        mNavListener = l;
    }

    public void dismis() {
        if (isShown()) {
            setVisibility(View.GONE);
        }
    }

    public void show() {
        if (!isShown()) {
            setVisibility(View.VISIBLE);
        }
    }

    private static final int TAG_KEY_ITEM = R.id.tag_nav_item_key;

    private View createItem(NavItem item, ViewGroup parent) {
        LayoutInflater f = LayoutInflater.from(getContext());
        View v = null;
        switch (item.getResType()) {
            case layout:
                v = f.inflate(item.getResId(), parent, false);
                break;
            case string:
                v = f.inflate(R.layout.template_nav_item_txt, parent, false);
                if (item.getResId() == 0) {
                    ((TextView) v).setText(item.getTitle());
                } else {
                    ((TextView) v).setText(item.getResId());
                }
                break;
            case drawable:
                v = f.inflate(R.layout.template_nav_item_img, parent, false);
                ((ImageView) v).setImageResource(item.getResId());
                break;
            default:
                throw new IllegalArgumentException("bad Item resType");
        }
        if (v != null) {
            v.setOnClickListener(this);
            v.setId(item.getId());
            v.setTag(TAG_KEY_ITEM, item);
        }
        return v;
    }

    private View lastClickItemView;

    @Override
    public void onClick(View v) {
        lastClickItemView = v;
        this.post(OnItemClickRunnable);
    }

    private final Runnable OnItemClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mNavListener != null && lastClickItemView != null) {
                mNavListener.onNavigationClick(lastClickItemView, (NavItem) lastClickItemView.getTag(TAG_KEY_ITEM),
                        NavigationBar.this);
            }
        }
    };

    /**
     * 设置自定义的图片，必须NavItem已经添加成功了才能生效
     *
     * @param navItem
     * @param faceResId
     */
    public void setNavItemFace(View navItem, int faceResId) {
        if (navItem instanceof ImageView) {
            ((ImageView) navItem).setImageResource(faceResId);
        } else if (navItem != null) {
            navItem.setBackgroundResource(faceResId);
        }
    }

    /**
     * 设置自定义的图片，必须NavItem已经添加成功了才能生效
     *
     * @param navItemId
     * @param faceResId
     */
    public void setNavItemFace(int navItemId, int faceResId) {
        setNavItemFace(findViewById(navItemId), faceResId);
    }

    public boolean replaceItem(NavItem old, NavItem newItem) {
        View itemView = findViewById(old.getId());
        if (itemView == null) {
            return false;
        }
        ViewGroup itemViewParent = (ViewGroup) itemView.getParent();
        int oldItemIndex = itemViewParent.indexOfChild(itemView);
        View newItemView = createItem(newItem, itemViewParent);
        itemViewParent.removeViewAt(oldItemIndex);
        itemViewParent.addView(newItemView, oldItemIndex);
        invalidate();
        requestLayout();
        return true;
    }

    public void reset() {
        leftBox.removeAllViews();
        rightBox.removeAllViews();
        setTitle(null);
    }

    public void commit() {
        this.forceLayout();
        this.requestLayout();
        this.invalidate();
    }
}
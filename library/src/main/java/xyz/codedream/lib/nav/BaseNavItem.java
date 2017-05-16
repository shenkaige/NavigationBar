package xyz.codedream.lib.nav;

import xyz.codedream.lib.nav.NavigationBar.NavItem;

/**
 * @author sky
 */
public class BaseNavItem implements NavItem {
    private int resId;
    private int id;
    private String title;
    private ResType resType;

    public BaseNavItem(int resId, ResType resType) {
        this.resId = resId;
        this.id = resId;
        this.resType = resType;
    }

    public BaseNavItem(String title) {
        this.title = title;
        this.resId = 0;
        this.id = title.hashCode();
        this.resType = ResType.string;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public int getResId() {
        return resId;
    }

    public ResType getResType() {
        return resType;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + resId;
        result = prime * result + ((resType == null) ? 0 : resType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseNavItem other = (BaseNavItem) obj;
        return id == other.id;
    }


    public static NavItem text(int stringResId) {
        return new BaseNavItem(stringResId, ResType.string);
    }

    public static NavItem text(String title) {
        return new BaseNavItem(title);
    }

    public static NavItem img(int drawableResId) {
        return new BaseNavItem(drawableResId, ResType.drawable);
    }

    public static NavItem view(int layoutResId) {
        return new BaseNavItem(layoutResId, ResType.layout);
    }
}
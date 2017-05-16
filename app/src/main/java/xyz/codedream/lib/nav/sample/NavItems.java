package xyz.codedream.lib.nav.sample;

import xyz.codedream.lib.nav.BaseNavItem;
import xyz.codedream.lib.nav.NavigationBar.NavItem;

/**
 * 导航条目
 *
 * @author skg
 */
public class NavItems {
    public static final NavItem search_img = BaseNavItem.img(R.drawable.nav_icon_search);
    public static final NavItem search_txt = BaseNavItem.text("Search");
    public static final NavItem save = BaseNavItem.text("Save");
    public static final NavItem messgae_count_view = BaseNavItem.view(R.layout.nav_item_msg_count);
}

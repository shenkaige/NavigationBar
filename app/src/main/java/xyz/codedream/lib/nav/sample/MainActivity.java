package xyz.codedream.lib.nav.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import xyz.codedream.lib.nav.NavigationBar;
import xyz.codedream.lib.nav.NavigationBar.NavItem;
import xyz.codedream.lib.nav.NavigationBar.NavgationListener;
import xyz.codedream.lib.nav.SearchBar;
import xyz.codedream.lib.nav.SearchBar.SearchBarListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        NavigationBar navBar1 = (NavigationBar) findViewById(R.id.nav_bar_1);
        navBar1.setTitle("Nav1");//中间标题
        navBar1.addFromLeft(NavItems.search_img);//左侧添加可多个
        navBar1.addFromRight(NavItems.search_txt);//右侧添加可多个
        navBar1.setNavgationListener(navgationListener);
        //
        NavigationBar navBar2 = (NavigationBar) findViewById(R.id.nav_bar_2);
        navBar2.setTitle("Nav2");
        navBar2.addFromLeft(NavItems.search_img);//左侧1
        navBar2.addFromLeft(NavItems.save);//左侧2
        navBar2.addFromRight(NavItems.messgae_count_view);//右侧自定义View
        navBar2.setNavgationListener(navgationListener);
        //
        NavigationBar navBar3 = (NavigationBar) findViewById(R.id.nav_bar_3);
        navBar3.setTitle("View Title");
        SearchBar mSearchBar = createSearchBar();//自定义View
        navBar3.replaceTitleView(mSearchBar);//替换到标题位置
        //navBar3.addFromLeft(NavItems.save);//同时支持左右添加
        navBar3.setNavgationListener(navgationListener);


        //-----其他常用函数-----
        //添加整个导航层View
        //navBar3.addTopLayer(mSearchBar);

        //设置title的drawable
        //navBar3.setTitleTextViewDrawable(left,top,right,bottom);

        //使用Layout Id直接替换title
        //navBar3.replaceTitleView(R.layout.xxx);

        //通过res设置title
        //navBar3.setTitle(R.string.app_name);

        //设置item是否可见
        //navBar3.changeNavChildVisiable(itemId,visiable);
    }

    private SearchBar createSearchBar() {
        SearchBar searchBar = new SearchBar(this);
        searchBar.setSearchHint("搜索你喜欢的商品");
        searchBar.setSearchBarListener(new SearchBarListener() {
            @Override
            public void onRequestHideSearchBar(SearchBar bar) {
                toast("onRequestHideSearchBar");
            }

            @Override
            public void onRequestDoSearch(SearchBar bar, String keyword) {
                toast("doSearch():" + keyword);
            }
        });
        searchBar.setActionButtonVisiblity(false);
        NavigationBar.LayoutParams lp = new NavigationBar.LayoutParams(NavigationBar.LayoutParams.MATCH_PARENT, NavigationBar.LayoutParams.MATCH_PARENT);
        lp.leftMargin = (int) (12 * getResources().getDisplayMetrics().density);
        lp.rightMargin = lp.leftMargin;
        searchBar.setLayoutParams(lp);
        return searchBar;
    }

    private NavgationListener navgationListener = new NavgationListener() {
        @Override
        public void onNavigationClick(View v, NavItem item, NavigationBar nav) {
            if (item.getId() == NavItems.search_img.getId()) {
                toast("img(Search)");
            } else if (item.getId() == NavItems.search_txt.getId()) {
                toast("txt(Search)");
            } else if (item.getId() == NavItems.messgae_count_view.getId()) {
                toast("view(MsgCount)");
            } else if (item.getId() == NavItems.save.getId()) {
                toast("txt(Save)");
            }
        }
    };

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}

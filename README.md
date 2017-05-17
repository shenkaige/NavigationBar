 目前很多APP的头部导航都是非导航相似的，为了简化开发，降低耦合，所以一直在使用封装之后的NavigationBar。

### NavigationBar目前支持功能
1，从左侧添加menu，支持多个。（menu可以是string，drawable，layout）   
2，从右侧添加menu，支持多个。（menu可以是string，drawable，layout）   
3，设置中间的文字title。   
4，给title设置 drewable left,top,right,bottom。   
5，用自定义View替换title view。   
6，添加/移除 top layer view。   
7，统一坚挺menu点击事件。   

### 代码用例

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
```
menu item监听

```java
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
```
menu item定义

```java
public class NavItems {
    public static final NavItem search_img = BaseNavItem.img(R.drawable.nav_icon_search);
    public static final NavItem search_txt = BaseNavItem.text("Search");
    public static final NavItem save = BaseNavItem.text("Save");
    public static final NavItem messgae_count_view = BaseNavItem.view(R.layout.nav_item_msg_count);
}

```

通常引入NavigationBar之后只需要定义项目需要用到的Item即可。

### 效果图(三种简单组合)
![image](https://github.com/shenkaige/NavigationBar/blob/master/screenshot/demo.png)

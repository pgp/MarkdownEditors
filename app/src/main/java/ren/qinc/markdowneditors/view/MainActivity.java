/*
 * Copyright 2016. SHENQINCI(沈钦赐)<dev@qinc.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ren.qinc.markdowneditors.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import ren.qinc.markdowneditors.AppContext;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.base.BaseDrawerLayoutActivity;
import ren.qinc.markdowneditors.base.BaseFragment;
import ren.qinc.markdowneditors.utils.Toast;

/**
 * The type Main activity.
 */
public class MainActivity extends BaseDrawerLayoutActivity {
    private BaseFragment mCurrentFragment;


    private int currentMenuId;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    //阴影的高度
    @Override
    protected float getElevation() {
        return 0;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            setDefaultFragment(R.id.content_fragment_container);
        }
    }

    private void setDefaultFragment(@IdRes int fragmentId) {
        mCurrentFragment = new FolderManagerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentId, mCurrentFragment)
                .commit();
    }

    @Override
    public void initData() {
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.localhost) {//|| id == R.id.other
            if (id == currentMenuId) {
                return false;
            }
            currentMenuId = id;
            getDrawerLayout().closeDrawer(GravityCompat.START);
            return true;
        }

        if (onOptionsItemSelected(item)) {
            getDrawerLayout().closeDrawer(GravityCompat.START);
        }
        return false;
    }

    @Override
    protected int getDefaultMenuItemId() {
        currentMenuId = R.id.localhost;
        return currentMenuId;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_helper:
                CommonMarkdownActivity.startHelper(this);
                return true;
            case R.id.menu_about:
                AboutActivity.startAboutActivity(this);
                return true;
            case R.id.menu_setting:
                return true;
//            case R.id.menu_update:
//                AppContext.showSnackbar(getWindow().getDecorView(), "initUpdate logic has been removed");
//                return true;
//            case R.id.other:
//                AppContext.showSnackbar(getWindow().getDecorView(), "敬请期待");
//                return true;
        }
        return super.onOptionsItemSelected(item);// || mCurrentFragment.onOptionsItemSelected(item);
    }

    private long customTime = 0;

    @Override
    public void onBackPressed() {//返回按钮
        if (getDrawerLayout().isDrawerOpen(GravityCompat.START)) {//侧滑菜单打开，关闭菜单
            getDrawerLayout().closeDrawer(GravityCompat.START);
            return;
        }

        if (mCurrentFragment != null && mCurrentFragment.onBackPressed()) {//如果Fragment有处理，则不据需执行
            return;
        }

        //没有东西可以返回了，剩下软件退出逻辑
        if (Math.abs(customTime - System.currentTimeMillis()) < 2000) {
            finish();
        } else {// 提示用户退出
            customTime = System.currentTimeMillis();
            Toast.showShort(mContext, "Press once more to exit");
        }
    }
}

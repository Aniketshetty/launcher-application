package com.launcher.application

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.launcher.application.fragments.AppDrawerFragment
import com.launcher.application.fragments.HomescreenFragment
import com.layoutxml.applistmanagerlibrary.AppList
import com.layoutxml.applistmanagerlibrary.interfaces.ActivityListener
import com.layoutxml.applistmanagerlibrary.interfaces.NewActivityListener
import com.layoutxml.applistmanagerlibrary.interfaces.SortListener
import com.layoutxml.applistmanagerlibrary.interfaces.UninstalledActivityListener
import com.layoutxml.applistmanagerlibrary.objects.AppData

class MainAct : AppCompatActivity(), ActivityListener, NewActivityListener,
    UninstalledActivityListener, SortListener {

    companion object
    {  val TAG = "MainAct"
        val appDataList: MutableList<AppData>? = ArrayList()

    }


    override fun onBackPressed() {}

    var mainIntent = Intent()
    var appdrawerFragment: AppDrawerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppList.registerListeners(
            null,
            this@MainAct,
            null,
            this@MainAct,
            null,
            this@MainAct,
            this@MainAct
        )
        appdrawerFragment = AppDrawerFragment()
        mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        AppList.getAllActivities(getApplicationContext(), mainIntent, 0)
        if (appdrawerFragment!!.progressBar != null) appdrawerFragment!!.progressBar!!.visibility =
            View.VISIBLE
        val navigationView: BottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigationView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
                val transaction: FragmentTransaction =
                    getSupportFragmentManager().beginTransaction()
                when (menuItem.itemId) {
                    R.id.drawerItem -> {
                        appdrawerFragment = AppDrawerFragment()
                        appdrawerFragment!!.arguments = getIntent().getExtras()
                        transaction.replace(R.id.fragment_container, appdrawerFragment!!)
                        transaction.commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.homeItem -> {
                        val homescreenFragmentFragment = HomescreenFragment()
                        homescreenFragmentFragment.arguments = getIntent().getExtras()
                        transaction.replace(R.id.fragment_container, homescreenFragmentFragment)
                        transaction.commit()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        AppList.destroy()
    }

    override fun onResume() {
        super.onResume()
        if (appDataList != null && appDataList.size != 0) {
            AppList.getAllNewActivities(getApplicationContext(), appDataList, mainIntent, 1)
            AppList.getAllUninstalledActivities(getApplicationContext(), appDataList, mainIntent, 2)
        }
    }

    override fun activityListener(
        list: List<AppData?>?,
        intent: Intent?,
        integer: Int?,
        integer1: Int?,
        aBoolean: Boolean?,
        integer2: Int?
    ) {
        AppList.sort(list, AppList.BY_APPNAME_IGNORE_CASE, AppList.IN_ASCENDING, integer2)
    }

    override fun newActivityListener(
        list: MutableList<AppData?>,
        intent: Intent?,
        integer: Int?,
        integer1: Int?,
        aBoolean: Boolean?,
        aBoolean1: Boolean,
        integer2: Int?
    ) {
        appDataList?.let { list.addAll(it) }
        if (aBoolean1) AppList.getAllNewActivities(
            getApplicationContext(),
            appDataList,
            mainIntent,
            1
        ) else AppList.sort(list, AppList.BY_APPNAME_IGNORE_CASE, AppList.IN_ASCENDING, integer2)
    }

    override fun uninstalledActivityListener(
        list: List<AppData?>?,
        intent: Intent?,
        integer: Int?,
        integer1: Int?,
        aBoolean: Boolean?,
        aBoolean1: Boolean?,
        integer2: Int?
    ) {
        appDataList?.removeAll(list!!)
    }

    override fun sortListener(
        list: List<AppData?>?,
        integer: Int?,
        integer1: Int?,
        integer2: Int?
    ) {
        if (appdrawerFragment!!.progressBar != null) appdrawerFragment!!.progressBar!!.visibility =
            View.GONE
        appDataList?.clear()
        appDataList?.addAll(list as Collection<AppData>)
        val itself = AppData()
        itself.packageName = getPackageName()
        appDataList?.remove(itself)
        if (appdrawerFragment!!.appListAdapter != null) appdrawerFragment!!.appListAdapter!!.notifyDataSetChanged()
    }
}
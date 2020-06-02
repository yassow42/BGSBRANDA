package com.creativeoffice.bgsbranda.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.creativeoffice.bgsbranda.BottomNavigationViewHelper
import com.creativeoffice.bgsbranda.R
import kotlinx.android.synthetic.main.activity_montaj.*


class MontajActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_montaj)
        setupNavigationView()

    }
    /*private fun setupRecyclerView() {
        rcSiparisler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = SiparisAdapter(this, siparislerList,userID)
        rcSiparisler.adapter = adapter
        rcSiparisler.setHasFixedSize(true)
    }
*/

    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu: Menu = bottomNav.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}
package com.creativeoffice.bgsbranda

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.creativeoffice.bgsbranda.Activity.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

class BottomNavigationViewHelper {


    companion object {

        fun setupBottomNavigationView(bottomNavigationViewEx: BottomNavigationViewEx) {

            bottomNavigationViewEx.enableAnimation(true)
            bottomNavigationViewEx.enableItemShiftingMode(true)
            bottomNavigationViewEx.enableShiftingMode(false)
            bottomNavigationViewEx.setTextVisibility(true)


        }

        fun setupNavigation(context: Context, bottomNavigationViewEx: BottomNavigationViewEx) {

            bottomNavigationViewEx.onNavigationItemSelectedListener =
                object : BottomNavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {

                        when (item.itemId) {

                            R.id.ic_siparisler -> {

                                val intent = Intent(context, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                                context.startActivity(intent)

                            }

                            R.id.ic_teklif->{
                                val intent = Intent(context, TeklifActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                            }
                            R.id.ic_uretim->{
                                val intent = Intent(context, UretimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                            }
                            R.id.ic_montaj->{
                                val intent = Intent(context, MontajActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                            }

                            R.id.ic_musteri -> {

                                val intent = Intent(context, MusterilerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                            }


                        }
                        return false
                    }
                }
        }
    }
}
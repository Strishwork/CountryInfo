package com.example.countryinfo

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_tablet.*

class MainActivity : AppCompatActivity(R.layout.main_layout),
    CountryPreviewFragment.ItemClickedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientation()
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CountryPreviewFragment.newInstance())
                .commit()
        }
    }

    override fun itemClicked(id: String) {
        if (fragment_container_tablet == null) {
            openDetailsFragmentPhone(id)
        } else {
            openDetailsFragmentTablet(id)
        }
    }

    private fun openDetailsFragmentPhone(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CountryDetailsFragment.newInstance(id))
            .addToBackStack(null)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun openDetailsFragmentTablet(id: String) {
        tablet_details_bg_placeholder.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_tablet, TabletCountryDetailsFragment.newInstance(id))
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setOrientation() {
        requestedOrientation = if (resources.getBoolean(R.bool.is_tablet)) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
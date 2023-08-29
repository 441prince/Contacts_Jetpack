package com.prince.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.prince.contacts.view.ContactFragment
import com.prince.contacts.view.FavoriteFragment
import com.prince.contacts.view.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar    // creating object of ToolBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set the references of the declared objects above
        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)
        bar = findViewById(R.id.toolbar)

        // To make our toolbar show the application
        // we need to give it to the ActionBar
        setSupportActionBar(bar)

        // Initializing the ViewPagerAdapter
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // add fragment to the list
        adapter.addFragment(ProfileFragment(), "Profiles")
        adapter.addFragment(ContactFragment(), "Contacts")
        adapter.addFragment(FavoriteFragment(), "Favorites")

        // Adding the Adapter to the ViewPager
        pager.adapter = adapter

        // bind the viewPager with the TabLayout.
        tab.setupWithViewPager(pager)
    }
}
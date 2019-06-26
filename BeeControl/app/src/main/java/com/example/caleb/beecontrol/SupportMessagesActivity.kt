package com.example.caleb.beecontrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SupportMessagesActivity : AppCompatActivity() {

    private lateinit var adapter: SupportAdapter
    private val db = FirebaseFirestore.getInstance()
    private val supportMRef = db.collection("SupportM")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_messages)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query = supportMRef.orderBy("id", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Support>()
                .setQuery(query, Support::class.java)
                .build()

        adapter = SupportAdapter(options)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewSupportMessages)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    fun back(view: View) {
        onBackPressed()
    }
}
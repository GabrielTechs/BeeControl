package com.example.caleb.beecontrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SupportMessagesActivity : AppCompatActivity() {

    private var adapter: AssistanceAdapter? = null
    private val db = FirebaseFirestore.getInstance()
    private val SupportMReF = db.collection("SupportM")
    var userRef = db.collection("user")
    lateinit var txtAssistanceDate: TextView
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_messages)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query = SupportMReF.orderBy("status", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Assistance>()
                .setQuery(query, Assistance::class.java)
                .build()

        adapter = AssistanceAdapter(options)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewSupportMessages)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }



    fun back(view: View){
        onBackPressed()
    }
}

package com.langcab.app

import GsonRequest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                user?.getIdToken(true)?.addOnCompleteListener { task ->
                    val idToken: String? = task.result?.token
                    idToken?.let { loadWords(idToken) }

                }
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private fun loadWords(idToken: String) {
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        val hostName = "https://www.langcab.com/api/word"
        val url = "${hostName}/?search=&language=Chinese&page=0&size=10&sort=timeCreated,DESC"
        val gsonRequest = GsonRequest(url, Pageable::class.java, mutableMapOf("Authorization" to idToken),
                Response.Listener { response ->
                    println(response.content.size)
                    renderList(response)
                },
                Response.ErrorListener { error ->  println(error) })
        requestQueue.add(gsonRequest)
    }

    private fun renderList(pageable: Pageable) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val adapter: RecyclerView.Adapter<WordAdapter.ViewHolder> = WordAdapter((pageable.content))
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                1)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}

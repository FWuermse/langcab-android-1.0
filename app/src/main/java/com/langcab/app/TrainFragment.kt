package com.langcab.app

import GsonRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [TrainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrainFragment : Fragment() {

    lateinit var token: String
    lateinit var currentLanguage: String

    private val hostName: String = "https://www.langcab.com/api"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(true)?.addOnCompleteListener { task ->
            val idToken: String? = task.result?.token
            idToken?.let {
                token = idToken
                getCurrentLanguage(idToken)
            }
        }

        return inflater.inflate(R.layout.fragment_train, container, false)
    }

    private fun getCurrentLanguage(idToken: String) {
        val queue = Volley.newRequestQueue(activity)
        val url = "${hostName}/language/last"
        val gsonRequest = GsonRequest(url,
            Request.Method.GET,
            String::class.java,
            mutableMapOf("Authorization" to idToken),
            { language ->
                currentLanguage = language
                loadWords(idToken, language)
            },
            { error -> println(error) })
        queue.add(gsonRequest)
    }

    private fun loadWords(idToken: String, language: String) {
        val queue = Volley.newRequestQueue(activity)
        val url = "${hostName}/learn/?language=${language}"
        val gsonRequest = GsonRequest(url,
            Request.Method.GET,
            ArrayList<String>()::class.java,
            mutableMapOf("Authorization" to idToken),
            { response ->
                println(response)
            },
            { error -> println(error) })
        queue.add(gsonRequest)
    }
}

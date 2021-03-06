package com.langcab.app

import GsonRequest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class AddWordFragment : Fragment() {

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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_create).setOnClickListener {
            createVocabulary(token, currentLanguage)
            findNavController().navigate(R.id.to_list)
        }
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
            },
            { error -> println(error) })
        queue.add(gsonRequest)
    }

    private fun createVocabulary(idToken: String, language: String) {
        val queue = Volley.newRequestQueue(activity)
        val url = "${hostName}/word/"
        val newWord: TextInputLayout? = view?.findViewById(R.id.text_field1)
        val translateWord: TextInputLayout? = view?.findViewById(R.id.text_field2)
        val pronounceWord: TextInputLayout? = view?.findViewById(R.id.text_field3)
        val languageWord: TextInputLayout? = view?.findViewById(R.id.text_field4)

        val gsonRequest = GsonRequest(url,
            Request.Method.POST,
            Pageable::class.java,
            mutableMapOf("Authorization" to idToken, "Content-Type" to "application/json"),
            //body
            Word(newWord?.editText?.text.toString(),
                 translateWord?.editText?.text.toString(),
                 pronounceWord?.editText?.text.toString(),
                 languageWord?.editText?.text.toString()),
            { response -> println(response) },
            { error -> println(error) })
        queue.add(gsonRequest)
    }
}

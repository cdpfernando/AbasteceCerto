package br.com.conradowho.abastececerto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_AbasteceCerto)

        super.onCreate(savedInstanceState)
    }
}
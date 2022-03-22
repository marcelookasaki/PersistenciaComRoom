package br.com.aavol.convidados.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.fourvrstudios.convidados.R

/*
Aprimoramentos do Aluno
Aprimoramento 1 → Ordenar a lista de convidados (aprimorar query de listar convidados em ordem alfabética)
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
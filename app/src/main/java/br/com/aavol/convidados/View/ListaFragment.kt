package br.com.aavol.convidados.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.aavol.convidados.MyViewModel
import br.com.fourvrstudios.convidados.R
import br.com.aavol.convidados.ViewModelFactory
import br.com.fourvrstudios.convidados.databinding.FragmentListaBinding
import br.com.aavol.convidados.db.MyDatabase
import br.com.aavol.convidados.db.Repositorio

class ListaFragment : Fragment() {
    private lateinit var binding: FragmentListaBinding
    lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lista, container, false)
        // Inicialização do Banco de dados e obtenção do DAO
        val dao = MyDatabase.getInstance(requireContext()).convidadoDAO
        // Inicialização Repositório
        val repositorio = Repositorio(dao)
        // Inicialização do Factory
        val factory = ViewModelFactory(repositorio)
        // Instancia um ViewModel usando a "Factory"
        viewModel = ViewModelProvider(requireActivity(), factory).get(MyViewModel::class.java)
        // Ao usa a activity como contexto requireActivity(), todos os fragmentos podem compartilhar o ViewModel e o seus respectivos dados
        binding.viewModelList = viewModel


        viewModel.convidados.observe(viewLifecycleOwner, Observer {
            var strListaConvidados = ""
            var somaConvidados = 0 //it.size
            it.forEach {
                somaConvidados++
                strListaConvidados += "${somaConvidados.toString()} - ${it.name.toString()} - ${it.email.toString()} \n"
            }
            binding.textView.text =
                strListaConvidados + "\n" + "Total de Convidados: $somaConvidados"
        })


        return binding.root
    }
}
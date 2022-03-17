package br.com.aavol.convidados.View

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.aavol.convidados.MyViewModel
import br.com.fourvrstudios.convidados.R
import br.com.aavol.convidados.ViewModelFactory
import br.com.fourvrstudios.convidados.databinding.FragmentCrudBinding
import br.com.aavol.convidados.db.MyDatabase
import br.com.aavol.convidados.db.Repositorio

class CrudFragment : Fragment() {
    private lateinit var binding: FragmentCrudBinding

    lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_crud, container, false)

        // Inicialização do Banco de Dados e obtenção do DAO
        val dao = MyDatabase.getInstance(requireContext()).convidadoDAO
        // Inicialização do Repositório
        val repositorio = Repositorio(dao)
        // Inicialização da Factory
        val factory = ViewModelFactory(repositorio)
        // Instanciar o ViewModel usando a "Factory"
        viewModel = ViewModelProvider(requireActivity(), factory).get(MyViewModel::class.java)


        binding.viewModelTag = viewModel // Vincula o ViewModel a tag <data>
        binding.lifecycleOwner = activity


        viewModel.onClear.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                alertaLimparLista()
            }
        })
        binding.btnVerLista.setOnClickListener {
            findNavController().navigate(R.id.action_crudFragment_to_listaFragment)
        }

        return binding.root
    }


    // Solicitar confirmação do usuário para excluir tudo
    fun alertaLimparLista() {
        val alert = AlertDialog.Builder(context)
        alert.setTitle("Tem Certeza que deseja limpar a lista de convidados?")
        alert.setMessage("Toda a lista de convidados será perdida")
        alert.setCancelable(false)
        alert.setPositiveButton(
            "Confirmar"
        ) { dialog, which ->
            viewModel.clearAll()
            Toast.makeText(context, "Todos os convidados foram removidos.", Toast.LENGTH_LONG).show();
            viewModel.setOnClearState(false)
        }
        alert.setNegativeButton(
            "Cancelar"
        ) { dialog, which ->
            viewModel.setOnClearState(false)
        }
        val alertDialog = alert.create()
        alertDialog.show()
    }
}
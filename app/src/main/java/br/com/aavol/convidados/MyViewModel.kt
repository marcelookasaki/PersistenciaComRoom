package br.com.aavol.convidados

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.aavol.convidados.db.Convidado
import br.com.aavol.convidados.db.Repositorio
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MyViewModel(private val repositorio: Repositorio) : ViewModel() {


    val convidados = repositorio.listaConvidados

    private var convidadoUpdateDelete: Convidado?

    val inputNome = MutableLiveData<String?>()
    val inputEmail = MutableLiveData<String?>()
    val inputBusca = MutableLiveData<String?>()

    val onClear = MutableLiveData<Boolean>()
    private var isUpdateOrDelete = false

    val saveUpdateBtnTxt =
        MutableLiveData<String>() // Texto do botão da esquerda save_or_update_button
    val clearDeleteBtnTxt =
        MutableLiveData<String>() // Texto do botão da direita clear clear_all_or_delete_button

    init {
        convidadoUpdateDelete = null
        saveUpdateBtnTxt.value = "Salvar"
        clearDeleteBtnTxt.value = "Limpar"
        onClear.value = false
    }

    fun insert(convidado: Convidado): Job =
        viewModelScope.launch {
            repositorio.insert(convidado)
        }

    fun clearAll(): Job =
        viewModelScope.launch {
            repositorio.clearAll()
        }

    fun update(convidado: Convidado): Job =
        viewModelScope.launch {
            repositorio.update(convidado)
            finishUpdateDelete()
        }

    fun delete(convidado: Convidado): Job =
        viewModelScope.launch {
            repositorio.delete(convidado)
            finishUpdateDelete()
        }

    fun search(): Job =
        viewModelScope.launch {
            // Corrotina iniciada em escopo de viewModel
            var convidadoEncontrado = repositorio.selectConvidado(inputBusca.value.toString())
            if (convidadoEncontrado != null) {
                startUpdateDelete(convidadoEncontrado)
            }
        }


    // 1 - MODIFICADOR DE ESTADO
    fun startUpdateDelete(convidado: Convidado) {
        inputNome.value = convidado.name
        inputEmail.value = convidado.email
        convidadoUpdateDelete = convidado
        saveUpdateBtnTxt.value = "Alterar"
        clearDeleteBtnTxt.value = "Deletar"
        isUpdateOrDelete = true
    }

    // MODIFICADOR DE ESTADO
    fun finishUpdateDelete() {
        inputNome.value = null
        inputEmail.value = null
        saveUpdateBtnTxt.value = "Salvar"
        clearDeleteBtnTxt.value = "Limpar"
        isUpdateOrDelete = false
        convidadoUpdateDelete = null
    }


    // Método chamada pelo botão da esquerda
    fun saveUpdate() {
        if (isUpdateOrDelete) {
            // UPDATE
            convidadoUpdateDelete?.name = inputNome.value!!
            convidadoUpdateDelete?.email = inputEmail.value!!
            update(convidadoUpdateDelete!!)
        } else {
            // SALVAR - INSERT
            val nome = inputNome.value!!
            val email = inputEmail.value!!
            insert(Convidado(0, nome, email))
            inputNome.value = null
            inputEmail.value = null
        }
    }

    // Método chamada pelo botão da direita
    fun clearOrDelete() {
        if (isUpdateOrDelete) {
            // DELETAR 1 REGISTRO
            delete(convidadoUpdateDelete!!)
        } else {
            // DELETAR TUDO
            setOnClearState(true)
            //clearAll()
        }
    }

    fun setOnClearState(state: Boolean) {
        onClear.value = state
    }

}


// ViewModelFactory :  Uma classe que cria ViewModels (BOILERPLATE)
class ViewModelFactory(private val repositorio: Repositorio) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(repositorio) as T // Retorna a ViewModel
        }
        throw IllegalArgumentException("ViewModel Desconhecida")
    }
}
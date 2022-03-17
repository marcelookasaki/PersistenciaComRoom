package br.com.aavol.convidados.db

//Repositório: uma classe criada para gerenciar várias fontes de dados.
class Repositorio(private val dao: ConvidadoDAO) {
//Somente o DAO precisa ser passado ao construtor do repositório (não é necessário expor o banco de dados)já que ele contém todos os métodos de leitura/gravação do BD.

    //bter a lista de convidados e aramazernar em uma variável
    val listaConvidados = dao.listarConvidados()

    suspend fun insert(convidado: Convidado) {
        dao.inserirConvidado(convidado)
    }

    suspend fun update(convidado: Convidado) {
        dao.updateConvidado(convidado)
    }

    suspend fun delete(convidado: Convidado) {
        dao.deleteConvidado(convidado)
    }

    suspend fun selectConvidado(busca: String): Convidado? {
        return dao.buscarConvidado(busca)
    }

    suspend fun clearAll() {
        dao.deleteAll()
    }



// Funções de suspensão: O modificador suspend informa ao compilador que esta função precisa ser chamada por uma corrotina ou outra função de suspensão.

}
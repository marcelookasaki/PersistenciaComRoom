package br.com.aavol.convidados.db

import androidx.lifecycle.LiveData
import androidx.room.*

// DAO - Data Access Object
@Dao
interface ConvidadoDAO {
    //@Insert Annotation - Metadado para informar ao Room que esta é uma função de Inserção
    @Insert(onConflict = OnConflictStrategy.REPLACE) // OnConflict: Define o que fazer caso exista um registro com o mesmo ID
    suspend fun inserirConvidado(convidado: Convidado)
    // Update
    @Update
    suspend fun updateConvidado(convidado: Convidado)
    //Delete
    @Delete
    suspend fun deleteConvidado(convidado: Convidado)

    @Query("DELETE FROM convidado_data_table")
    suspend fun deleteAll()

    // Query que realiza buscas na lista à partir de um nome digitado pelo usuário
    @Query("SELECT * FROM convidado_data_table WHERE convidado_name LIKE '%' || :search || '%'")
    suspend fun buscarConvidado(search: String) : Convidado?

    //Query busca toodos os usuários
    @Query("SELECT * FROM convidado_data_table")
    fun listarConvidados(): LiveData<List<Convidado>>

    // ************* OUTROS EXEMPLOS DE QUERYS *******************/
/*
    @Query("SELECT * FROM convidado_data_table ORDER BY _id DESC")
    fun listarConvidadosDesc(): LiveData<List<Convidado>>


    @Query("SELECT * FROM convidado_data_table ORDER BY convidado_name ASC")
    fun listarConvidadosAsc(): Flow<List<Convidado>>   // Retorna uma lista em ordem alfabética

    // O "Flow" sempre mantém / armazena em cache a versão mais recente dos dados e notifica seus observadores quando os dados foram alterados.

    @Query("SELECT * FROM  convidado_data_table ORDER BY _id DESC LIMIT 1") // Retorna o último convidado incluido
    fun getUltimoConvidado(): Convidado?*/

}
import java.util.*

class Monitoramento {

    fun main() {
        println("Bem-vindo!")

        var opcao: Int

        do {
            println("\nEscolha uma opção:")
            println("1. Fazer login e começar o monitoramento")
            println("2. Sair")

            opcao = readLine()?.toIntOrNull() ?: 0

            when (opcao) {
                1 -> fazerLoginEIniciarMonitoramento()
                2 -> ScriptPadraoPython.pararScript()

                else -> println("Opção inválida. Tente novamente.")
            }

        } while (opcao != 2)
    }

    fun fazerLoginEIniciarMonitoramento() {
        val scanner = Scanner(System.`in`)

        println("\nDigite seu e-mail:")
        val email = scanner.nextLine()

        println("Digite sua senha:")
        val senha = scanner.nextLine()

        // Lógica de validação no SQL Server
        val conexaoSql = Conexao.jdbcTemplateServer

        val query = """
        SELECT COUNT(*) FROM Funcionario
        WHERE email = '$email' AND senha = '$senha'
        """
        val resultado = conexaoSql!!.queryForObject(query, Int::class.java)

        if (resultado == 1) {
            println("\nLogin bem-sucedido! Esquentando as máquinas...")
            Thread.sleep(2000)

            println("Começando o monitoramento...")
            Thread.sleep(2000)

            val (arquivo1, arquivo2) = ScriptPadraoPython.criarScript()

            ScriptPadraoPython.executarScript(arquivo1, arquivo2)

            println("Monitoramento iniciado...")
        } else {
            println("\nLogin inválido. Tente novamente.")
        }
    }
}
import java.io.File
import com.github.britooo.looca.api.core.Looca

object ScriptPadraoPython {

    var pythonProcesses: List<Process> = listOf()

    fun criarScript(): Pair<String, String> {

        val codigoPythonDefaultHard = """
import psutil
import time
import pymssql
from mysql.connector import connect
import threading

# Configurações de conexão e tokens
mysql_cnx = connect(user='root', password='urubu100', host='localhost', database='centrix')
sql_server_cnx = pymssql.connect(server='44.197.21.59', database='centrix', user='sa', password='centrix')

# Função para monitorar informações do sistema
def monitorar_info_sistema():
    while True:
        cpu_cores = psutil.cpu_count(logical=False)
        ram_total = round(psutil.virtual_memory().total / (1024**3), 3)
        disco_total = round(psutil.disk_usage('/').total / (1024**3), 3)

        # Inserir no MySQL
        bdLocal_cursor = mysql_cnx.cursor()
        add_info_sistema = (
            "INSERT INTO info_sistema (cpu_cores, ram_total, disco_total, fkMaquina) "
            "VALUES (%s, %s, %s, %s)"
        )
        bdLocal_cursor.execute(add_info_sistema, (cpu_cores, ram_total, disco_total, 7))
        bdLocal_cursor.close()
        mysql_cnx.commit()

        # Inserir no SQL Server
        bdServer_cursor = sql_server_cnx.cursor()
        bdServer_cursor.execute(add_info_sistema, (cpu_cores, ram_total, disco_total, 7))
        bdServer_cursor.close()
        sql_server_cnx.commit()

        time.sleep(10)

# Criar thread para a função de monitoramento de info_sistema
thread_info_sistema = threading.Thread(target=monitorar_info_sistema)

# Iniciar a thread
thread_info_sistema.start()

# Não é necessário aguardar até que a thread termine

            
    """.trimIndent()

        val codigoPythonDefaultRede = """
import psutil
import time
import pymssql
from mysql.connector import connect
from slack_sdk import WebClient
import threading

# Configurações de conexão e tokens
mysql_cnx = connect(user='root', password='urubu100', host='localhost', database='centrix')
sql_server_cnx = pymssql.connect(server='44.197.21.59', database='centrix', user='sa', password='centrix')
slack_token = 'xoxb-5806834878417-6181633164562-oINkxKK5LUXZXD3Xgwq7QNdi'
slack_channel = '#notificacao-ruan'
slack_client = WebClient(token=slack_token)

# Limites de notificação
limite_cpu = 89
limite_ram = 89
limite_disco = 89

def enviar_notificacao(mensagem):
    try:
        slack_client.chat_postMessage(channel=slack_channel, text=mensagem)
    except Exception as e:
        print(f"Erro ao enviar mensagem para o Slack: {e}")

# Função para monitorar métricas em tempo real
def monitorar_metricas_tempo_real():
    while True:
        cpu_percent = round(psutil.cpu_percent(), 2)
        ram_percent = round(psutil.virtual_memory().percent, 2)
        disco_percent = round(psutil.disk_usage('/').percent, 2)

        # Verificar se ultrapassou os limites e enviar notificação
        if cpu_percent > limite_cpu:
            mensagem_cpu = f"Aviso: Uso de CPU acima do limite! ({cpu_percent}%)"
            enviar_notificacao(mensagem_cpu)

        if ram_percent > limite_ram:
            mensagem_ram = f"Aviso: Uso de RAM acima do limite! ({ram_percent}%)"
            enviar_notificacao(mensagem_ram)

        if disco_percent > limite_disco:
            mensagem_disco = f"Aviso: Uso de Disco acima do limite! ({disco_percent}%)"
            enviar_notificacao(mensagem_disco)

        # Inserir no MySQL
        bdLocal_cursor = mysql_cnx.cursor()
        add_metricas_tempo_real = (
            "INSERT INTO metricas_tempo_real (cpu_percent, ram_percent, disco_percent, fkMaquina) "
            "VALUES (%s, %s, %s, %s)"
        )
        bdLocal_cursor.execute(add_metricas_tempo_real, (cpu_percent, ram_percent, disco_percent, 7))
        bdLocal_cursor.close()
        mysql_cnx.commit()

        # Inserir no SQL Server
        bdServer_cursor = sql_server_cnx.cursor()
        bdServer_cursor.execute(add_metricas_tempo_real, (cpu_percent, ram_percent, disco_percent, 7))
        bdServer_cursor.close()
        sql_server_cnx.commit()

        time.sleep(3)

# Criar thread para a função de monitoramento de métricas em tempo real
thread_metricas_tempo_real = threading.Thread(target=monitorar_metricas_tempo_real)

# Iniciar a thread
thread_metricas_tempo_real.start()

# Não é necessário aguardar até que a thread termine


    """.trimIndent()


        val nomeArquivoPyDefaultHard = "script-python-info-sistema.py"
        File(nomeArquivoPyDefaultHard).writeText(codigoPythonDefaultHard)

        Thread.sleep(2 * 1000L)

        val nomeArquivoPyDefaultRede = "script-python-metricas-tempo-real.py"
        File(nomeArquivoPyDefaultRede).writeText(codigoPythonDefaultRede)

        return Pair(nomeArquivoPyDefaultHard, nomeArquivoPyDefaultRede)

    }

    private val looca = Looca()
    private val so = looca.sistema.sistemaOperacional
    private val executor = if (so.contains("Win")) {
        "py"
    } else {
        "python3"
    }
    fun executarScript(arquivo1: String, arquivo2: String) {
        val pythonProcess1: Process
        val pythonProcess2: Process

        if (so.contains("Win")) {
            pythonProcess1 = Runtime.getRuntime().exec("$executor $arquivo1")
            pythonProcess2 = Runtime.getRuntime().exec("$executor $arquivo2")
        } else {
            pythonProcess1 = Runtime.getRuntime().exec("python3 script-python-info-sistema.py")
            pythonProcess2 = Runtime.getRuntime().exec("python3 script-python-metricas-tempo-real.py")
        }

        pythonProcesses = listOf(pythonProcess1, pythonProcess2)
    }

    fun pararScript() {
        println("Até logo!")
        for (process in pythonProcesses) {
            process.destroyForcibly()
        }
    }
}
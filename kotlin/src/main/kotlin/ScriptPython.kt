import java.io.File

object scriptPadraoPython {

    var pythonProcesses: List<Process> = listOf()

    fun criarScript(tempo: Int, idMaquinaDado: Int, idEmpresaDado: Int): String {

        val codigoPythonDefaultHard = """
import psutil
import time
import pymssql
from slack_sdk import WebClient
from slack_sdk.errors import SlackApiError
from datetime import datetime
import mysql.connector
from threading import Thread

# Função para se conectar ao MySQL
def conectar_mysql():
    return mysql.connector.connect(
        user='root',
        password='363776',
        host='localhost',
        database='centrix'
    )

# Função para inserir dados na tabela info_sistema
def inserir_info_sistema(cursor, bdLocal_cursor, bdServer_cursor):
    while True:
        cpu_cores = psutil.cpu_count(logical=False)
        ram_total = round(psutil.virtual_memory().total / (1024 ** 3), 3)  # Convertendo para GB
        disco_total = round(psutil.disk_usage('/').total / (1024 ** 3), 3)  # Convertendo para GB

        add_info_sistema = (
            "INSERT INTO info_sistema"
            "(cpu_cores, ram_total, disco_total, fkMaquina)"
            "VALUES (%s, %s, %s, %s)"
        )

        bdLocal_cursor.execute(add_info_sistema, (cpu_cores, ram_total, disco_total, 1))
        cursor.commit()

        bdServer_cursor.execute(add_info_sistema, (cpu_cores, ram_total, disco_total, 1))
        cursor.commit()

        time.sleep(10)

# Função para inserir dados na tabela metricas_tempo_real
def inserir_metricas_tempo_real(cursor, bdLocal_cursor, bdServer_cursor, slack_client, slack_channel, limite_cpu, limite_ram, limite_disco):
    while True:
        cpu_percent = psutil.cpu_percent(interval=1)
        ram_percent = psutil.virtual_memory().percent
        disco_percent = psutil.disk_usage('/').percent

        if cpu_percent > limite_cpu:
            message = f"Aviso: Uso de CPU acima do limite! ({cpu_percent}%)"
            slack_client.chat_postMessage(channel=slack_channel, text=message)

        if ram_percent > limite_ram:
            message = f"Aviso: Uso de RAM acima do limite! ({ram_percent} GB)"
            slack_client.chat_postMessage(channel=slack_channel, text=message)

        if disco_percent > limite_disco:
            message = f"Aviso: Uso de Disco acima do limite! ({disco_percent} GB)"
            slack_client.chat_postMessage(channel=slack_channel, text=message)

        add_metricas_tempo_real = (
            "INSERT INTO metricas_tempo_real"
            "(cpu_percent, ram_percent, disco_percent, fkMaquina)"
            "VALUES (%s, %s, %s, %s)"
        )

        bdLocal_cursor.execute(add_metricas_tempo_real, (cpu_percent, ram_percent, disco_percent, 1))
        cursor.commit()

        bdServer_cursor.execute(add_metricas_tempo_real, (cpu_percent, ram_percent, disco_percent, 1))
        cursor.commit()

        time.sleep(3)

# Função para criar e iniciar threads
def iniciar_threads():
    mysql_cnx = conectar_mysql()
    bdLocal_cursor = mysql_cnx.cursor()

    slack_token = 'xoxb-5806834878417-6181633164562-enQr1lEpjFZEjBJLMbyCyFz5'
    slack_channel = '#notificação-ruan'
    slack_client = WebClient(token=slack_token)

    limite_cpu = 90
    limite_ram = 90
    limite_disco = 90

    # Conexão com o SQL Server
    sql_server_cnx = pymssql.connect(server='44.197.21.59', database='centrix', user='sa', password='centrix')
    bdServer_cursor = sql_server_cnx.cursor()

    # Thread para inserir dados na tabela info_sistema
    thread_info_sistema = Thread(target=inserir_info_sistema, args=(mysql_cnx, bdLocal_cursor, bdServer_cursor))

    # Thread para inserir dados na tabela metricas_tempo_real
    thread_metricas_tempo_real = Thread(target=inserir_metricas_tempo_real, args=(mysql_cnx, bdLocal_cursor, bdServer_cursor, slack_client, slack_channel, limite_cpu, limite_ram, limite_disco))

    # Iniciando as threads
    thread_info_sistema.start()
    thread_metricas_tempo_real.start()

    # Aguardando o término das threads (o que não acontecerá, pois elas são loops infinitos)
    thread_info_sistema.join()
    thread_metricas_tempo_real.join()

if __name__ == "__main__":
    iniciar_threads()
    """.trimIndent()

        val nomeArquivoPyDefaultHard = "script-python.py"
        File(nomeArquivoPyDefaultHard).writeText(codigoPythonDefaultHard)

        Thread.sleep(2 * 1000L)

        return nomeArquivoPyDefaultHard

    }

    fun executarScript(arquivo1: String) {
        val pythonProcess1 = Runtime.getRuntime().exec("py $arquivo1")
        pythonProcesses = listOf(pythonProcess1)
    }

    fun pararScript() {
        for (process in pythonProcesses) {
            process.destroyForcibly()
        }
    }
}
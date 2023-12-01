# Instale o pacote 'odbc' se ainda não estiver instalado
if (!requireNamespace("odbc", quietly = TRUE)) {
  install.packages("odbc")
}

# Carregue o pacote
library(odbc)

# Defina os parâmetros da conexão para o SQL Server
conexao <- dbConnect(
  odbc(),
  driver = "ODBC Driver 17 for SQL Server",  # ou o driver correspondente que você está usando
  server = "44.197.21.59",
  database = "Centrix",
  uid = "sa",
  pwd = "centrix"
)

# Execute uma consulta de exemplo
resultado <- dbGetQuery(conexao, "SELECT * FROM metricas_tempo_real")

# Visualize os resultados
print(resultado)

# Feche a conexão quando terminar
dbDisconnect(conexao)

# Supondo que 'resultado' contenha os dados da tabela 'metricas_tempo_real'
# (Certifique-se de executar o script de conexão do SQL Server primeiro)

# Calcule a média de cada métrica
media_cpu <- mean(resultado$cpu_percent)
media_ram <- mean(resultado$ram_percent)
media_disco <- mean(resultado$disco_percent)

# Calcule o máximo de cada métrica
max_cpu <- max(resultado$cpu_percent)
max_ram <- max(resultado$ram_percent)
max_disco <- max(resultado$disco_percent)

# Calcule o mínimo de cada métrica
min_cpu <- min(resultado$cpu_percent)
min_ram <- min(resultado$ram_percent)
min_disco <- min(resultado$disco_percent)

# Crie um resumo
resumo <- data.frame(
  Métrica = c("CPU", "RAM", "Disco"),
  Média = c(media_cpu, media_ram, media_disco),
  Máximo = c(max_cpu, max_ram, max_disco),
  Mínimo = c(min_cpu, min_ram, min_disco)
)

# Exiba o resumo
print(resumo)

# Visualize as estatísticas descritivas
summary(resultado)

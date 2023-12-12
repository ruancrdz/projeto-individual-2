# Instale e carregue as bibliotecas necessárias
install.packages("odbc")
install.packages("dplyr")

library(odbc)
library(dplyr)

# Configuração da conexão
con <- dbConnect(odbc::odbc(),
                 .connection_string = "Driver={ODBC Driver 17 for SQL Server};Server=44.197.21.59;Database=centrix;Uid=sa;Pwd=centrix;")

# Consultar dados da tabela
query <- "SELECT data_hora, cpu_percent, ram_percent, disco_percent FROM metricas_tempo_real"
dados <- dbGetQuery(con, query)

summary(dados)

# Boxplot para cada métrica
boxplot(dados$cpu_percent, main = "Boxplot - CPU Percent", ylab = "CPU Percent")
boxplot(dados$ram_percent, main = "Boxplot - RAM Percent", ylab = "RAM Percent")
boxplot(dados$disco_percent, main = "Boxplot - Disco Percent", ylab = "Disco Percent")

# Histograma para cada métrica
hist(dados$cpu_percent, main = "Histograma - CPU Percent", xlab = "CPU Percent", col = "lightblue")
hist(dados$ram_percent, main = "Histograma - RAM Percent", xlab = "RAM Percent", col = "lightgreen")
hist(dados$disco_percent, main = "Histograma - Disco Percent", xlab = "Disco Percent", col = "lightcoral")

# Exemplo de gráfico de linha usando ggplot2
library(ggplot2)

ggplot(dados, aes(x = data_hora)) +
  geom_line(aes(y = cpu_percent, color = "CPU")) +
  geom_line(aes(y = ram_percent, color = "RAM")) +
  geom_line(aes(y = disco_percent, color = "Disco")) +
  labs(title = "Métricas ao longo do Tempo", y = "Percentagem")

# Fechar a conexão
dbDisconnect(con)

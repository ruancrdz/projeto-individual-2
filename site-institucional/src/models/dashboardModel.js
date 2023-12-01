// Importa a configuração do banco de dados
var database = require("../database/config");

// Função para calcular e retornar informações do sistema
function calcular() {
    // Define a instrução SQL para selecionar todas as colunas da tabela info_sistema onde id é 1
    var instrucao = `SELECT * FROM info_sistema WHERE id = 1;`;

    // Exibe no console a instrução SQL a ser executada
    console.log("Executando a instrução SQL: \n" + instrucao);

    // Chama a função de execução do banco de dados com a instrução SQL
    return database.executar(instrucao);
}

// Função para obter métricas em tempo real (últimas 5 entradas)
function obterMetricasTempoReal() {
    // Define a instrução SQL para selecionar todas as colunas da tabela metricas_tempo_real, ordenadas pela data_hora em ordem decrescente, limitando a 5 entradas
    var instrucao = `SELECT * FROM metricas_tempo_real ORDER BY data_hora DESC LIMIT 5;`;

    // Exibe no console a instrução SQL a ser executada
    console.log("Executando a instrução SQL: \n" + instrucao);

    // Chama a função de execução do banco de dados com a instrução SQL
    return database.executar(instrucao);
}

// Exporta as funções do modelo para serem utilizadas no controlador
module.exports = {
    calcular,
    obterMetricasTempoReal
}
// Importa o modelo do dashboard que contém as funções de acesso ao banco de dados
var dashboardModel = require("../models/dashboardModel");

// Função para calcular e retornar dados do dashboard
function calcular(req, res) {
    // Chama a função correspondente do modelo para calcular os dados
    dashboardModel.calcular()
        .then(function (resultado) {
            // Verifica se a consulta retornou resultados
            if (resultado.length > 0) {
                // Extrai os dados do primeiro resultado (assumindo que a consulta retorna apenas uma linha)
                const dados = resultado[0];

                // Retorna os dados relevantes no formato JSON
                res.status(200).json({
                    CPU: dados.cpu_cores,
                    RAM: dados.ram_total,
                    DISCO: dados.disco_total
                });
            } else {
                // Retorna um status 204 (Sem Conteúdo) se nenhum resultado for encontrado
                res.status(204).send("Nenhum resultado encontrado!");
            }
        })
        .catch(function (erro) {
            // Trata erros, imprime no console e retorna uma resposta de erro
            console.log(erro);
            console.log("Houve um erro ao realizar a consulta! Erro: ", erro.sqlMessage);
            res.status(500).json(erro.sqlMessage);
        });
}

// Função para obter métricas em tempo real do dashboard
function obterMetricasTempoReal(req, res) {
    // Chama a função correspondente do modelo para obter métricas em tempo real
    dashboardModel.obterMetricasTempoReal()
        .then(function (resultado) {
            // Verifica se a consulta retornou resultados
            if (resultado.length > 0) {
                // Extrai os dados do primeiro resultado (assumindo que a consulta retorna apenas uma linha)
                const dados = resultado[0];

                // Retorna as métricas em tempo real no formato JSON
                res.status(200).json({
                    cpu_percent: dados.cpu_percent,
                    ram_percent: dados.ram_percent,
                    disco_percent: dados.disco_percent
                });
            } else {
                // Retorna um status 204 (Sem Conteúdo) se nenhum resultado for encontrado
                res.status(204).send("Nenhum resultado encontrado!");
            }
        })
        .catch(function (erro) {
            // Trata erros, imprime no console e retorna uma resposta de erro
            console.log(erro);
            console.log("Houve um erro ao realizar a consulta! Erro: ", erro.sqlMessage);
            res.status(500).json(erro.sqlMessage);
        });
}

// Exporta as funções do controlador para serem utilizadas nas rotas
module.exports = {
    calcular,
    obterMetricasTempoReal
}
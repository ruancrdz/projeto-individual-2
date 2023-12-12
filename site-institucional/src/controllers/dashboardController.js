var dashRuanModel = require("../models/dashRuanModel");

function calcular(req, res) {
    dashRuanModel.calcular()
        .then(function (resultado) {
            if (resultado.length > 0) {
                const dados = resultado[0];

                res.status(200).json({
                    CPU: dados.cpu_cores,
                    RAM: dados.ram_total,
                    DISCO: dados.disco_total
                });
            } else {
                res.status(204).send("Nenhum resultado encontrado!");
            }
        })
        .catch(function (erro) {
            console.log(erro);
            console.log("Houve um erro ao realizar a consulta! Erro: ", erro.sqlMessage);
            res.status(500).json(erro.sqlMessage);
        });
}

function obterMetricasTempoReal(req, res) {
    dashRuanModel.obterMetricasTempoReal()
        .then(function (resultado) {
            if (resultado.length > 0) {
                const dados = resultado[0];

                res.status(200).json({
                    cpu_percent: dados.cpu_percent,
                    ram_percent: dados.ram_percent,
                    disco_percent: dados.disco_percent
                });
            } else {
                res.status(204).send("Nenhum resultado encontrado!");
            }
        })
        .catch(function (erro) {
            console.log(erro);
            console.log("Houve um erro ao realizar a consulta! Erro: ", erro.sqlMessage);
            res.status(500).json(erro.sqlMessage);
        });
}

module.exports = {
    calcular,
    obterMetricasTempoReal
}
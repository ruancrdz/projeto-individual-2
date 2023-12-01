var mysql = require("mysql2");
var sql = require('mssql');

// // CONEXÃO DO SQL SERVER - AZURE (NUVEM)  
// var sqlServerConfig = {
//     port: parseInt("1433", 10),
//     server: "44.197.21.59",
//     user: "sa",
//     password: "centrix",
//     database: "Centrix",
//     options: {
//         encrypt: false,
//         trustServerCertificate: true,
//         stream: false
//     }
// }

// CONEXÃO DO MYSQL WORKBENCH
var mySqlConfig = {
    host: "localhost",
    database: "projeto",
    user: "root",
    password: "363776",
};

/**
 * Função para executar instruções SQL no MySQL ou SQL Server com base no ambiente configurado.
 * @param {string} instrucao - A instrução SQL a ser executada.
 * @returns {Promise} - Uma Promise que resolve com os resultados da consulta ou rejeita com um erro.
 */
function executar(instrucao) {
    // VERIFICA A VARIÁVEL DE AMBIENTE SETADA EM app.js
    if (process.env.AMBIENTE_PROCESSO == "producao") {
        // Conexão e consulta no SQL Server
        return new Promise(function (resolve, reject) {
            sql.connect(sqlServerConfig).then(function () {
                return sql.query(instrucao);
            }).then(function (resultados) {
                console.log(resultados);
                resolve(resultados.recordset);
            }).catch(function (erro) {
                reject(erro);
                console.log('ERRO: ', erro);
            });
            sql.on('error', function (erro) {
                console.log("ERRO NO SQL SERVER (Azure): ", erro);
            });
        });
    } else if (process.env.AMBIENTE_PROCESSO == "desenvolvimento") {
        // Conexão e consulta no MySQL
        return new Promise(function (resolve, reject) {
            var conexao = mysql.createConnection(mySqlConfig);
            conexao.connect();
            conexao.query(instrucao, function (erro, resultados) {
                conexao.end();
                if (erro) {
                    reject(erro);
                }
                console.log(resultados);
                resolve(resultados);
            });
            conexao.on('error', function (erro) {
                console.log("ERRO NO MySQL WORKBENCH: ", erro.sqlMessage);
            });
        });
    } else {
        // Ambiente não configurado
        return new Promise(function (resolve, reject) {
            console.log("\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n");
            reject("AMBIENTE NÃO CONFIGURADO EM app.js")
        });
    }
}

module.exports = {
    executar
}
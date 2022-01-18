// Created by E.S. Olthof
// Creation date 12/15/2021

package com.example.thevault.domain.mapping.dao;

import com.example.thevault.domain.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JDBCTransactieDAO implements TransactieDAO {
    JdbcTemplate jdbcTemplate;

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(JDBCKlantDAO.class);

    @Autowired
    public JDBCTransactieDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JDBCTransactieDAO");
    }

    //TODO bedrag in prijs veranderen in database
    private PreparedStatement slaTransactieOpStatement(Transactie transactie, Connection connection) throws SQLException {
        String sql = "INSERT INTO transactie (aantal, momentTransactie, koperGebruikerId, cryptomuntId, bedrag," +
                " verkoperGebruikerId, bankFee) values (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setDouble(1, transactie.getAantal());
        System.out.println("*** Dit is de datum: " + transactie.getMomentTransactie());
        ps.setTimestamp(2, Timestamp.valueOf(transactie.getMomentTransactie()));
        ps.setInt(3, transactie.getKoper().getGebruikerId());
        ps.setInt(4, transactie.getCryptomunt().getId());
        ps.setDouble(5, transactie.getPrijs());
        ps.setInt(6, transactie.getVerkoper().getGebruikerId());
        ps.setDouble(7, transactie.getBankFee());
        return ps;
    }


    @Override
    public Transactie slaTransactieOp(Transactie transactie) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> slaTransactieOpStatement(transactie, connection), keyHolder);
        int transactieId = keyHolder.getKey().intValue();
        transactie.setTransactieId(transactieId);
        return transactie;
    }

    @Override
    public List<Transactie> geefAlleTransacties(){
        String sql = "SELECT * FROM transactie;";
        return jdbcTemplate.query(sql, new TransactieRowMapper());
    }

    @Override
    public List<Transactie> geefTransactiesVanGebruiker(Gebruiker gebruiker) {
        String sql = "SELECT * FROM transactie WHERE verkoperGebruikerId = ? OR koperGebruikerId = ?;";
        List<Transactie> transactiesGebruiker = null;
        try {
            transactiesGebruiker = jdbcTemplate.query(sql, new TransactieRowMapper()
                    , gebruiker.getGebruikerId(), gebruiker.getGebruikerId());
        } catch (EmptyResultDataAccessException exception){
            System.out.println("Geen data gevonden, exceptie: " + exception);
        }
        return transactiesGebruiker;
    }

    @Override
    public List<Transactie> geefTransactiesVanGebruikerInPeriode(Gebruiker gebruiker, Timestamp startDatum, Timestamp eindDatum) {
        String sql = "SELECT * FROM transactie WHERE (verkoperGebruikerId = ? OR koperGebruikerId = ?) AND momentTransactie BETWEEN ? AND ?;";
        List<Transactie> transactiesGebruiker = null;
        try {
            transactiesGebruiker = jdbcTemplate.query(sql, new TransactieRowMapper()
                    , gebruiker.getGebruikerId(), gebruiker.getGebruikerId()
                    , startDatum, eindDatum);
        } catch (EmptyResultDataAccessException exception){
            System.out.println("Geen data gevonden, exceptie: " + exception);
        }
        return transactiesGebruiker;
    }

    @Override
    public List<Transactie> geefAlleTransactiesInPeriode(Timestamp startDatum, Timestamp eindDatum) {
        String sql = "SELECT * FROM transactie WHERE momentTransactie BETWEEN ? AND ?;";
        List<Transactie> transactiesInPeriode = null;
        try {
            transactiesInPeriode = jdbcTemplate.query(sql, new TransactieRowMapper()
                    , startDatum, eindDatum);
        } catch (EmptyResultDataAccessException exception){
            System.out.println("Geen data gevonden, exceptie: " + exception);
        }
        return transactiesInPeriode;
    }

    @Override
    public List<Transactie> geefTransactiesVanGebruikerMetCryptomunt(Gebruiker gebruiker, Cryptomunt cryptomunt) {
        String sql = "SELECT * FROM transactie WHERE (verkoperGebruikerId = ? OR koperGebruikerId = ?) AND cryptomuntId = ?;";

        List<Transactie> transactiesGebruiker = null;
        try {
            transactiesGebruiker = jdbcTemplate.query(sql, new TransactieRowMapper()
                    , gebruiker.getGebruikerId(), gebruiker.getGebruikerId()
                    , cryptomunt.getId());
        } catch (EmptyResultDataAccessException exception){
            System.out.println("Geen data gevonden, exceptie: " + exception);
        }
        return transactiesGebruiker;
    }

    @Override
    public Transactie verwijderTransactie(Transactie transactie) {
        String sql = "DELETE FROM transactie WHERE transactieId = ?";

        int amountOfRows = jdbcTemplate.update(sql, transactie.getTransactieId());
        if(amountOfRows == 0){
            // TODO bedenk exception
        }
        return transactie;

    }

    private static class TransactieRowMapper implements RowMapper<Transactie> {
        @Override
        public Transactie mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            LocalDateTime dateTime = resultSet.getObject("momentTransactie", LocalDateTime.class);
            Gebruiker koper = new Klant();
            Gebruiker verkoper = new Klant();
            Cryptomunt cryptomunt = new Cryptomunt(resultSet.getInt("cryptomuntId"));
            koper.setGebruikerId(resultSet.getInt("koperGebruikerId"));
            verkoper.setGebruikerId(resultSet.getInt("verkoperGebruikerId"));
            Trigger triggerKoper = new TriggerKoper(koper, cryptomunt, resultSet.getDouble("bedrag"),resultSet.getDouble("aantal"));
            Trigger triggerVerkoper = new TriggerVerkoper(verkoper, cryptomunt, resultSet.getDouble("bedrag"),resultSet.getDouble("aantal"));
            Transactie transactie = new Transactie(dateTime, triggerKoper,  triggerVerkoper);
            transactie.setTransactieId(resultSet.getInt("transactieId"));
            transactie.setBankFee(resultSet.getDouble("bankFee"));
            return transactie;
        }
    }
}

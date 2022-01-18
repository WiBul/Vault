package com.example.thevault;

import com.example.thevault.domain.mapping.dao.JDBCAdresDAO;
import com.example.thevault.domain.mapping.dao.JDBCKlantDAO;
import com.example.thevault.domain.model.Adres;
import net.minidev.json.JSONUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TheVaultApplication {



    public static void main(String[] args) {
        SpringApplication.run(TheVaultApplication.class, args);

    }


}

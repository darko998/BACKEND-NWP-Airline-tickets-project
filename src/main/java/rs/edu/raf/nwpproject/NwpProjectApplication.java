package rs.edu.raf.nwpproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import rs.edu.raf.nwpproject.models.User;

import java.util.List;

@SpringBootApplication
public class NwpProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NwpProjectApplication.class, args);
    }

}

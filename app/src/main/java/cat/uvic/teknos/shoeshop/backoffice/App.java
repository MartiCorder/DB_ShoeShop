package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.JdbcModelFactory;
import cat.uvic.teknos.shoeshop.domain.jdbc.repositories.JdbcRepositoryFactory;
import cat.uvic.teknos.shoeshop.domain.jpa.models.JpaModelFactory;
import cat.uvic.teknos.shoeshop.domain.jpa.repositories.JpaRepositoryFactory;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {

        //App per JDBC
        RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
        ModelFactory modelFactory = new JdbcModelFactory();

        //App per JPA
        //RepositoryFactory repositoryFactory = new JpaRepositoryFactory();
        //ModelFactory modelFactory = new JpaModelFactory();

        var backOffice = new BackOffice(System.in, System.out, repositoryFactory, modelFactory);

        backOffice.start();
    }
}
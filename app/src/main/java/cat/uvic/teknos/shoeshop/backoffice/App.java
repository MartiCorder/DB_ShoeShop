package cat.uvic.teknos.shoeshop.backoffice;

import cat.uvic.teknos.shoeshop.domain.jdbc.models.JdbcModelFactory;
import cat.uvic.teknos.shoeshop.domain.jdbc.repositories.JdbcRepositoryFactory;
import cat.uvic.teknos.shoeshop.models.ModelFactory;
import cat.uvic.teknos.shoeshop.repositories.RepositoryFactory;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {

        RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
        ModelFactory modelFactory = new JdbcModelFactory();

        var backOffice = new BackOffice(System.in, System.out, repositoryFactory, modelFactory);

        backOffice.start();
    }
}
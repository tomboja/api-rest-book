package cs599.edu.miu.boja.qr.api.books.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.sql.DataSource;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/4/25
 */
@Path("/testdb")
@Produces(MediaType.APPLICATION_JSON)
public class DbConnectionTest {

    @Inject
    DataSource dataSource;

    @GET
    @Path("/connection")
    public String testConnection() {
        try (var connection = dataSource.getConnection()) {
            if (connection != null) {
                return "Database connection is successful!";
            } else {
                return "Failed to connect to the database.";
            }
        } catch (Exception e) {
            return "Error connecting to the database: " + e.getMessage();
        }
    }
}

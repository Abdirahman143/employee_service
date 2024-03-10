package com.ems.employee_service.utils;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestContainerManager {
private static final Logger logger = LoggerFactory.getLogger(TestContainerManager.class);
    private static final String POSTGRES_VERSION = "14"; // Specify a specific version of PostgreSQL
    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:" + POSTGRES_VERSION);
    private static final String DEFAULT_DATABASE_NAME = "test";
    private static final String PASSWORD="test";

    private static PostgreSQLContainer<?> container;

    /**
     * Starts the PostgreSQL container if needed and executes database migration using Flyway.
     */
    public static synchronized void startContainerIfNeeded() {
        if (container == null || !container.isRunning()) {
            container = new PostgreSQLContainer<>(POSTGRES_IMAGE)
                    .withDatabaseName(DEFAULT_DATABASE_NAME)
                    .withPassword(PASSWORD)
                   // .withEnv("POSTGRES_PASSWORD", "test") // Using environment variables for configuration
                    .withLogConsumer(new Slf4jLogConsumer(logger)); // Log container output

            container.start();
            container.waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 1)); // Wait for database to be ready
            migrateDatabase();
        }
    }

    /**
     * Migrates the database using Flyway.
     */
    private static void migrateDatabase() {
        Path migrationPath = Paths.get("src/test/resources/db/migration");
        Flyway flyway = Flyway.configure()
                .dataSource(getJdbcUrl(), "test", getPassword()) // Using environment variables for configuration
                .locations("filesystem:" + migrationPath.toAbsolutePath().toString())
                .load();
        flyway.migrate();
    }

    /**
     * Starts the PostgreSQL container if needed.
     */
    public static void startContainer() {
        startContainerIfNeeded();
    }

    /**
     * Stops the PostgreSQL container if running.
     */
    public static void stopContainer() {
        if (container != null && container.isRunning()) {
            container.stop();
        }
    }

    /**
     * Retrieves the JDBC URL of the PostgreSQL container.
     *
     * @return JDBC URL
     */
    public static String getJdbcUrl() {
        startContainerIfNeeded();
        return container.getJdbcUrl();
    }

    /**
     * Retrieves the database name used in the container.
     *
     * @return database name
     */
    public static String getDatabaseName() {
        return DEFAULT_DATABASE_NAME;
    }

    /**
     * Retrieves the database username.
     *
     * @return database username
     */
    public static String getUsername() {
        return "test";
    }

    /**
     * Retrieves the database password.
     *
     * @return database password
     */
    public static String getPassword() {
        return PASSWORD; // Returning empty string instead of the actual password
    }
}
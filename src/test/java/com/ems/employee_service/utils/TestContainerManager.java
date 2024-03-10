package com.ems.employee_service.utils;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class TestContainerManager {

    private  static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:latest");
    private static final String DATABASE_NAME ="test";
    private static final String USER_NAME ="test";
    private static final String PASSWORD ="test";

    private static PostgreSQLContainer<?>container;

    private static synchronized  void startContainerIfNeeded(){
        if(container==null || !container.isRunning()){
            container = new PostgreSQLContainer<>(POSTGRES_IMAGE).
                    withDatabaseName(DATABASE_NAME).
                    withUsername(USER_NAME).
                    withPassword(PASSWORD);

            container.start();
            container.waitingFor(Wait.forListeningPort());

        }
    }
    public static void startContainer(){
        startContainerIfNeeded();
    }
    public static void stopContainer(){
        if(container==null && container.isRunning()){
            container.stop();
        }
    }
    public static String getJdbcUrl(){
        startContainerIfNeeded();
        return container.getJdbcUrl();
    }

    public static String getDatabaseName(){
        return container.getDatabaseName();
    }

    public static String getUserName(){
        return container.getUsername();
    }

    public static String getPassword(){
        return container.getPassword();
    }



}

package ru.vsu.strelnikov_m_i.console;

import ru.vsu.strelnikov_m_i.context.AppContext;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.factories.Factory;
import ru.vsu.strelnikov_m_i.services.AuthService;

import java.util.Scanner;

public class ConsoleInput {
    Scanner scanner;
    ErrorConsuming consuming;
    AuthService authorizer;
    Factory commandFactory;

    public ConsoleInput(Scanner scanner, ErrorConsuming consuming, AuthService authorizer, Factory commandFactory) {
        //todo: think about InputStream
        this.scanner = scanner;
        this.consuming = consuming;
        this.authorizer = authorizer;
        this.commandFactory = commandFactory;
    }

    public ResponseType inputLogin() {
        String line = scanner.nextLine();
        if (line.contains("exit")) {
            return ResponseType.EXIT;
        }
        int id;
        try {
            id = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            consuming.processError("Please enter a valid ID");
            return ResponseType.ERROR;
        }
        String password = scanner.nextLine();

        try {
            authorizer.authorization(id, password);
        } catch (RuntimeException e) {
            consuming.processError(e.getMessage());
            return ResponseType.ERROR;
        }
        return ResponseType.OK;
    }

    public ResponseType inputCommand() {
        String[] line = scanner.nextLine().split(" ");
        if (line[0].equals("exit")) {
            authorizer.quit();
            return ResponseType.EXIT;
        }

        ResponseType response;

        try {
            response = commandFactory.getCommand(line[0] + " " + line[1]).execute();
        } catch (ObjectNotFoundException e) {
            consuming.processError(e.getMessage());
            return ResponseType.ERROR;
        }
        if (response == ResponseType.EXIT) {
            return ResponseType.OK;
        }
        return response;
    }
}

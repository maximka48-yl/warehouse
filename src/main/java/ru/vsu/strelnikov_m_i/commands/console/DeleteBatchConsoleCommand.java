package ru.vsu.strelnikov_m_i.commands.console;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.services.BatchService;
import ru.vsu.strelnikov_m_i.services.EntryService;

import java.util.Scanner;

@Setter
public class DeleteBatchConsoleCommand implements Command {
    private BatchService batchService;
    private Scanner scanner;

    @Override
    public ResponseType execute() throws NumberFormatException {
        System.out.println("Enter batch's id");
        System.out.println("To exit the deletion operation enter: exit");
        String line = scanner.nextLine();
        if (line.equals("exit")) {
            return ResponseType.EXIT;
        }

        int id = Integer.parseInt(line);

        batchService.delete(id);
        return ResponseType.OK;
    }
}

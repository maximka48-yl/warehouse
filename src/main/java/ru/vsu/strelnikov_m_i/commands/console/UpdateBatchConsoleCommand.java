package ru.vsu.strelnikov_m_i.commands.console;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.exceptions.WrongFormatException;
import ru.vsu.strelnikov_m_i.services.BatchService;

import java.sql.Date;
import java.util.Scanner;

@Setter
public class UpdateBatchConsoleCommand implements Command {
    private BatchService batchService;
    private Scanner scanner;
    @Override
    public ResponseType execute() {
        System.out.println("Enter batch id you want to update");
        System.out.println("To exit the addition operation enter: exit");
        String string = scanner.nextLine();
        if (string.equals("exit")) {
            return ResponseType.EXIT;
        }

        int id = Integer.parseInt(string);
        Batch batch = batchService.getById(id);
        System.out.println(batch.toString());

        System.out.println("Enter batch's properties:\ndate\tsample\nDate\tstring");
        String[] line = scanner.nextLine().split(" ");
        if (line[0].equals("exit")) {
            return ResponseType.EXIT;
        }
        Date date;
        try {
            date = Date.valueOf(line[0]);
        } catch (IllegalArgumentException e) {
            throw new WrongFormatException("Wrong date format");
        }
        String sample = line[1];
        batchService.update(batch.getId(), date, sample);
        return ResponseType.OK;
    }

}

package ru.vsu.strelnikov_m_i.commands.console;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.exceptions.WrongFormatException;
import ru.vsu.strelnikov_m_i.services.BatchService;

import java.sql.Date;
import java.util.Scanner;

@Setter
public class AddBatchConsoleCommand implements Command {
    private Scanner scanner;
    private BatchService batchService;

    @Override
    public ResponseType execute() {
        System.out.println("Enter batch's properties:\ndate\tsample\nDate\tstring");
        System.out.println("To exit the addition operation enter: exit");
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
        batchService.add(date, sample);
        return ResponseType.OK;
    }
}

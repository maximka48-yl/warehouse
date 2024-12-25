package ru.vsu.strelnikov_m_i.commands.console;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.console.ErrorConsuming;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.context.AppContext;
import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.services.EntryService;

import java.sql.Date;
import java.util.Arrays;
import java.util.Scanner;

@Setter
public class AddEntryConsoleCommand implements Command {
    private EntryService entryService;
    private Scanner scanner;
    private ErrorConsuming consuming;

    @Override
    public ResponseType execute() {
        System.out.println("Enter entry's properties:\nentryType\tbatch\tdate\tamount\nEntryType\tint \tDate\tint");
        System.out.println("To exit the addition operation enter: exit");
        String[] line = scanner.nextLine().split(" ");
        if (line[0].equals("exit")) {
            return ResponseType.EXIT;
        }
        if (line.length < 4) {
            consuming.processError("Lesser amount of attributes were written");
            return ResponseType.ERROR;
        }
        EntryType entryType;
        try {
            entryType = EntryType.valueOf(line[0]);
        } catch (IllegalArgumentException e) {
            consuming.processError("Entry type " + line[0] + " is invalid\nValid entry types are: " + Arrays.toString(Arrays.copyOfRange(EntryType.values(), 0, EntryType.values().length)));
            return ResponseType.ERROR;
        }
        int batch;
        try {
            batch = Integer.parseInt(line[1]);
        } catch (NumberFormatException e) {
            consuming.processError("Invalid batch number " + line[1]);
            return ResponseType.ERROR;
        }
        Date date;
        try {
            date = Date.valueOf(line[2]);
        } catch (IllegalArgumentException e) {
            consuming.processError("Invalid date or date format" + line[2] + "\nValid format is: yyyy-MM-dd");
            return ResponseType.ERROR;
        }
        int amount;
        try {
            amount = Integer.parseInt(line[3]);
        } catch (NumberFormatException e) {
            consuming.processError("Invalid amount " + line[3]);
            return ResponseType.ERROR;
        }

        try {
             entryService.add(entryType, batch, date, amount, AppContext.getUser());
        } catch (ObjectNotFoundException e) {
            consuming.processError(String.format("Failed to create entry: %s", e.getMessage()));
            return ResponseType.ERROR;
        }
        return ResponseType.OK;
    }
}

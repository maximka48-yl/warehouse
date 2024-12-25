package ru.vsu.strelnikov_m_i.commands.console;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.context.AppContext;
import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.exceptions.NotEnoughAttributes;
import ru.vsu.strelnikov_m_i.exceptions.WrongFormatException;
import ru.vsu.strelnikov_m_i.services.EntryService;

import java.sql.Date;
import java.util.Arrays;
import java.util.Scanner;

@Setter
public class UpdateEntryConsoleCommand implements Command {
    private EntryService entryService;
    private Scanner scanner;

    @Override
    public ResponseType execute() throws NumberFormatException {
        System.out.println("Enter entry id you want to update");
        System.out.println("To exit the addition operation enter: exit");
        String string = scanner.nextLine();
        if (string.equals("exit")) {
            return ResponseType.EXIT;
        }
        int id = Integer.parseInt(string);
        Entry entry = entryService.getById(id, AppContext.getUser());
        System.out.println(entry.toString());

        System.out.println("Enter entry's properties:\nentryType\tsample\tbatch\tdate\tamount\nEntryType\tString\tint \tDate\tint");
        String[] line = scanner.nextLine().split(" ");
        if (line[0].equals("exit")) {
            return ResponseType.EXIT;
        }
        if (line.length < 4) {
            throw new NotEnoughAttributes("Lesser amount of attributes were written");
        }
        EntryType entryType;
        try {
            entryType = EntryType.valueOf(line[0]);
        } catch (IllegalArgumentException e) {
            throw new WrongFormatException("Entry type " + line[0] + " is invalid\nValid entry types are: " + Arrays.toString(Arrays.copyOfRange(EntryType.values(), 1, EntryType.values().length)));
        }
        int batch;
        try {
            batch = Integer.parseInt(line[1]);
        } catch (NumberFormatException e) {
            throw new WrongFormatException("Invalid batch number " + line[1]);
        }
        Date date;
        try {
            date = Date.valueOf(line[2]);
        } catch (IllegalArgumentException e) {
            throw new WrongFormatException("Invalid date or date format" + line[2] + "\nValid format is: yyyy-MM-dd");
        }
        int amount;
        try {
            amount = Integer.parseInt(line[3]);
        } catch (NumberFormatException e) {
            throw new WrongFormatException("Invalid amount " + line[3]);
        }

        entryService.update(entry.getId(), entryType, batch, date, amount, AppContext.getUser());
        return ResponseType.OK;
    }
}

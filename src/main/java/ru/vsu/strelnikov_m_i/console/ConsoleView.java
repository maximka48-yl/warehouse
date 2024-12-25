package ru.vsu.strelnikov_m_i.console;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.commands.console.AddEntryConsoleCommand;
import ru.vsu.strelnikov_m_i.commands.console.ViewEntryConsoleCommand;
import ru.vsu.strelnikov_m_i.context.AppContext;
import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.factories.CommandFactoryConsole;
import ru.vsu.strelnikov_m_i.repositories.in_memory.BatchInMemoryRepository;
import ru.vsu.strelnikov_m_i.repositories.in_memory.EntryInMemoryRepository;
import ru.vsu.strelnikov_m_i.repositories.in_memory.UserInMemoryRepository;
import ru.vsu.strelnikov_m_i.services.AuthService;
import ru.vsu.strelnikov_m_i.services.EntryService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

@Setter
public class ConsoleView implements ErrorConsuming {
    //todo: think about userId for creating new entities
    private ConsoleInput input;

    //todo: generalize addWindow
//    private ResponseType openAddEntryWindow() {
//        System.out.println("Enter entry's properties:\nentryType\tsample\tbatch\tdate\tamount\nEntryType\tString\tint \tDate\tint");
//        System.out.println("To exit the addition operation enter: exit");
//        return input.inputEntry();
//    }

    public void logIn() {
        System.out.println("Login");
        System.out.println("Enter user id and password or exit to quit");
        ResponseType response;
        do {
            response = input.inputLogin();
        }
        while (response == ResponseType.ERROR);
        if (response == ResponseType.EXIT) {
            return;
        }
        showStartWindow();
    }

    //todo: make it do its purpose - call user-chosen methods
    private void showStartWindow() {
        System.out.println(AppContext.getUser().toString());

        //todo: role-depending command input
        ResponseType response;
        do {
            System.out.println("List of available commands:");
            System.out.println(AppContext.getACTIONS().toString());
            System.out.println("List of available entities:");
            System.out.println(AppContext.getENTITIES().get(AppContext.getUser().getRole()).toString());
            System.out.println("Expecting input like: <command> <entity>");

            response = input.inputCommand();
        }
        while (response != ResponseType.EXIT);
    }

    @Override
    public void processError(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        EntryInMemoryRepository repository = new EntryInMemoryRepository();
        BatchInMemoryRepository batchRepository = new BatchInMemoryRepository();
        UserInMemoryRepository userInMemoryRepository = new UserInMemoryRepository();

        userInMemoryRepository.add(new User(0, "Вася Пупкин", "123".hashCode(), RoleType.MANAGER, "mylo@soap.com", "454545"));
        repository.add(new Entry(0, EntryType.RECEIVED, 0, 1, new Date(0),1));
        System.out.println(repository.getAll());
        batchRepository.add(new Batch(0, new Date(0), "Конфетка"));

        AuthService authService = new AuthService(userInMemoryRepository);
        CommandFactoryConsole factory = new CommandFactoryConsole();

        ViewEntryConsoleCommand viewEntryConsoleCommand = new ViewEntryConsoleCommand();
        AddEntryConsoleCommand addEntryConsoleCommand = new AddEntryConsoleCommand();

        EntryService entryService = new EntryService(repository, batchRepository);

        viewEntryConsoleCommand.setEntryService(entryService);
        addEntryConsoleCommand.setEntryService(entryService);

        factory.addManagerCommand("view Entry", viewEntryConsoleCommand);
        factory.addManagerCommand("add Entry", addEntryConsoleCommand);

        AppContext.getACTIONS().add("view");
        AppContext.getACTIONS().add("add");
        ArrayList<String> arrayList = new ArrayList<>();
        AppContext.getENTITIES().put(RoleType.MANAGER, arrayList);

        arrayList.add(Entry.class.getSimpleName());

        ConsoleView consoleView = new ConsoleView();
        Scanner scanner = new Scanner(System.in);
        ConsoleInput consoleInput = new ConsoleInput(scanner, consoleView, authService, factory);

        addEntryConsoleCommand.setConsuming(consoleView);
        addEntryConsoleCommand.setScanner(scanner);

        consoleView.setInput(consoleInput);
        consoleView.logIn();
    }
}

package ru.vsu.strelnikov_m_i.commands.console;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.context.AppContext;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.services.EntryService;

@Setter
public class ViewEntryConsoleCommand implements Command {
    EntryService entryService;

    @Override
    public ResponseType execute() {
        System.out.println(entryService.getByAuthor(AppContext.getUser().getId()).toString());
        return ResponseType.OK;
    }
}

package ru.vsu.strelnikov_m_i.commands.console;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.enums.ResponseType;
import ru.vsu.strelnikov_m_i.services.BatchService;


@Setter
public class ViewBatchConsoleCommand implements Command {
    private BatchService service;

    @Override
    public ResponseType execute() {
        System.out.println(service.getAll().toString());
        return ResponseType.OK;
    }
}

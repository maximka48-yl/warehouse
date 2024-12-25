package ru.vsu.strelnikov_m_i.context;

import lombok.Getter;
import lombok.Setter;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.RoleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppContext {
    @Setter
    @Getter
    private static User user;
    @Getter
    private final static List<String> ACTIONS = new ArrayList<>();
    @Getter
    private final static Map<RoleType, List<String>> ENTITIES = new HashMap<>();

    public static  List<String> getAvailableEntities() {
        return ENTITIES.get(user.getRole());
    }
}

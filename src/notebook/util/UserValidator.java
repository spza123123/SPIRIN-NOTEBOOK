package notebook.util;

public class UserValidator {
    public String isNameValid(String name) {
        if(name.isEmpty()) {
            throw new RuntimeException("Пусто!Не введены данные!");
        }
        return name = name.trim().replaceAll(" ", "");

    }
    public boolean isIdEmpty(Long id) {
        if (id == null) throw new RuntimeException("Id = null!");
        return String.valueOf(id).isEmpty();
    }
}

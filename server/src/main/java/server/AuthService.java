package server;
/**
 *метод получения никнейма по логину и паролю
 * @return null если учетка не найдена
 * @return nickname если учетка не найдена
 */
public interface AuthService {
    String getNicknameByLoginAndPassword(String login,String password);


    }


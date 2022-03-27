package server;

public class SQLAuthService implements AuthService{
    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return SQLUserDataBase.getNicknameByLoginAndPassword(login, password);
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        return SQLUserDataBase.registration(login, password, nickname);
    }

    @Override
    public boolean changeNick(String oldNickname, String newNickname) {
        return SQLUserDataBase.changeNick(oldNickname, newNickname);
    }
}

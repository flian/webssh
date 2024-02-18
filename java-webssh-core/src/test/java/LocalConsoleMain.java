import com.jcraft.jsch.*;

import java.nio.charset.StandardCharsets;
import java.util.Hashtable;

/**
 * <h3>javaWebSSH</h3>
 * <p>test webssh in local</p>
 *
 * @author : foy
 * @date : 2024-02-18 09:51
 **/
public class LocalConsoleMain {
    public static void main(String[] args) throws JSchException {
        JSch jsch = new JSch();
        String userName="root";
        String password="Admin123";
        String ipaddress="192.168.59.66";
        int port = 22;
        Hashtable<String, String> config = new Hashtable();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        jsch.setConfig(config);
        Session session = jsch.getSession(userName, ipaddress, port);

        session.setPassword(password);
        session.connect(30 * 1000);
        //session.connect();
        Channel channel = session.openChannel("shell");
        channel.setInputStream(System.in);
        channel.setOutputStream(System.out);
        //((ChannelShell)channel).setPtyType("xterm");
        ((ChannelShell)channel).setPty(true);
        //com.jcraft.jsch.JSchSessionDisconnectException: SSH_MSG_DISCONNECT: 2 Packet integrity error.
        ((ChannelShell)channel).setTerminalMode(composeTerminalModes());
        //((ChannelShell)channel).setTerminalMode("ECHO".getBytes(StandardCharsets.UTF_8));
        channel.connect(30 * 1000);
        //channel.connect();
    }

    private static byte[] composeTerminalModes(){
        byte[] terminalModes = {
                0,0,0,
                //ECHO 53
                53,
                1,
               // 1,
               /* // TTY_OP_ISPEED 128
                (byte)0x80,
                // 14400 = 00008ca0
                0, 0, (byte)0x36, (byte)0xb0,
                // TTY_OP_OSPEED 129
                (byte)0x81,
                // 14400 again
                0, 0, (byte)0x36, (byte)0xb0,*/
                // TTY_OP_END
                0,
        };
        return terminalModes;
    }
}

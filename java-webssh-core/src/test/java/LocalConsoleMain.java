import com.jcraft.jsch.*;
import org.lotus.carp.webssh.config.service.impl.JschLogger;

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
        JSch.setLogger(new JschLogger());
        JSch jsch = new JSch();
        String userName="root";
        String password="Admin123";
        String ipaddress="192.168.202.66";
        int port = 22;
        Hashtable<String, String> config = new Hashtable();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        jsch.setConfig(config);
        Session session = jsch.getSession(userName, ipaddress, port);
        session.setOutputStream(System.out);
        session.setPassword(password);
        session.connect(30 * 1000);
        //session.connect();
        Channel channel = session.openChannel("shell");
        channel.setInputStream(System.in);
        channel.setOutputStream(System.out);

        ((ChannelShell)channel).setPtyType("xterm");
        ((ChannelShell)channel).setPty(true);
        //com.jcraft.jsch.JSchSessionDisconnectException: SSH_MSG_DISCONNECT: 2 Packet integrity error.
        //java.io.IOException: End of IO Stream Read
        ((ChannelShell)channel).setTerminalMode(composeTerminalModes());
        //((ChannelShell)channel).setTerminalMode("ECHO".getBytes(StandardCharsets.UTF_8));
        channel.connect(30 * 1000);
        //channel.connect();
    }

    private static byte[] composeTerminalModes(){
        //https://stackoverflow.com/questions/24623170/an-example-of-how-to-specify-terminal-modes-pty-req-string-for-ssh-client?rq=1
        byte[] terminalModes = {
                //Translate uppercase characters to lowercase.
                37,
                0,0,0,1,
                //0,0,0,
                //ECHO 53
                53,
                0,0,0,1,
                //ECHOE Visually erase chars.
                54,
                0,0,0,1,
                //ECHOK Kill character discards current line.
                55,
                0,0,0,1,
                //ECHONL Echo NL even if ECHO is off
                56,
                0,0,0,1,
               //ECHOCTL Echo control characters as ^(Char).
                60,
                0,0,0,1,
               // 1,
               // TTY_OP_ISPEED 128
                (byte)0x80,
                // 14400 = 00003840
                0, 0, (byte)0x38, (byte)0x40,
                // TTY_OP_OSPEED 129
                (byte)0x81,
                // 14400 again
                0, 0, (byte)0x38, (byte)0x40,
                // TTY_OP_END
                0,
        };
        return terminalModes;
    }
}

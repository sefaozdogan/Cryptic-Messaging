import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class Client {
    private static byte key_to_bytes[];
    private static byte iv_to_bytes[];
    private static boolean mark;
    private static String name;

    private PrintWriter outputWrt;

    private JFrame frame = new JFrame("Cyrpt Messenger");
    private JButton btnConnect = new JButton("Connect");
    private JButton btnDisconnect = new JButton("Disconnect");
    private JButton btnEncyript = new JButton("Encyript");
    private JButton btnSend = new JButton("Send");
    private JRadioButton radioAes = new JRadioButton("Aes");
    private JRadioButton radioDes= new JRadioButton("Des");
    private ButtonGroup grp1 = new ButtonGroup();
    private JRadioButton radioCbc = new JRadioButton("Cbc");
    private JRadioButton radioOfb= new JRadioButton("Ofb");
    private ButtonGroup grp2 = new ButtonGroup();
    private JTextField inputMessage = new JTextField(1);
    private JTextArea cyripted = new JTextArea(1, 2);
    private JTextArea messageArea = new JTextArea(30, 40);

    private GridBagConstraints setGrid(int gridwidth, int gridx, int gridy){
        GridBagConstraints gridProperties = new GridBagConstraints();
        gridProperties.fill = GridBagConstraints.HORIZONTAL;
        gridProperties.ipady = 10;
        gridProperties.gridwidth = gridwidth;
        gridProperties.weightx = 1;
        gridProperties.weighty = 1;
        gridProperties.gridx = gridx;
        gridProperties.gridy = gridy;
        return gridProperties;
    }

    private void setLayout(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        grp1.add(radioAes);
        grp1.add(radioDes);
        grp2.add(radioCbc);
        grp2.add(radioOfb);
        messageArea.setEditable(false);
        cyripted.setEditable(false);
        inputMessage.setEditable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        panel.add(btnConnect, setGrid(1,0,0));
        panel.add(btnDisconnect, setGrid(1,1,0));
        panel.add(radioAes, setGrid(1,2,0));
        panel.add(radioDes, setGrid(1,3,0));
        panel.add(radioCbc, setGrid(1,4,0));
        panel.add(radioOfb, setGrid(1,5,0));
        panel.add(messageArea, setGrid(10,0,1));
        panel.add(inputMessage, setGrid(5,0,2));
        panel.add(btnEncyript, setGrid(1,5,2));
        panel.add(cyripted, setGrid(5,0,3));
        panel.add(btnSend, setGrid(1,5,3));

        frame.add(panel);
        frame.pack();
        frame.setSize(600,650);
        frame.setVisible(true);
    }

    private void runClient() throws Exception {

        BufferedReader inputBuf;

        btnConnect.setEnabled(false);
        inputMessage.setEditable(true);

        inputMessage.addActionListener(e -> {
            outputWrt.println(sendEncyript());
            inputMessage.setText("");
        });

        Socket socket = new Socket("localhost", 1414);
        inputBuf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputWrt = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String input = inputBuf.readLine();
            if (input.startsWith("_UserName_"))
                outputWrt.println(name);
            else if (input.startsWith("_Exit_")) {
                System.out.println(name+" is disconnected.");
                socket.close();
                System.exit(0);
            }else  {
                messageArea.append(input+ "\n");
                messageArea.append(decrypt(input,getType(),getMode())+ "\n");
            }
        }
    }

    private Client(){
        setLayout();

        btnConnect.addActionListener(e -> {
            name=getName();
            mark=true;
            radioDes.setSelected(true);
            radioCbc.setSelected(true);
        });

        btnDisconnect.addActionListener(e -> outputWrt.println("_Exit_"));

        btnEncyript.addActionListener(e -> {
            if(inputMessage.getText().length()>0)
                sendEncyript();
        });

        btnSend.addActionListener(e -> {
            if(inputMessage.getText().length()>0){
                outputWrt.println(sendEncyript());
                inputMessage.setText("");
            }
        });
    }

    private String sendEncyript(){
        String message= inputMessage.getText();
        message=name+">"+message;

        try {
            byte[] encrypted = encrypt(message,getType(),getMode());
            message = Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e1) {e1.printStackTrace();}
        cyripted.setText(message);

        return message;
    }

    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter user name:",
            "Input username",
            JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args)  throws Exception{

        mark=false;
        Client client = new Client();
        while(true){
            Thread.sleep(20);
            if(mark)
                break;
        }
        client.runClient();
    }

    private static byte[] encrypt(String plainText, String type, String mode) throws Exception {
        byte[] clean = plainText.getBytes();
        Cipher cipher = Cipher.getInstance(type+"/"+mode+"/PKCS5Padding");
        SecretKeySpec seckey = new SecretKeySpec(key_to_bytes, type);
        cipher.init(Cipher.ENCRYPT_MODE, seckey,new IvParameterSpec(iv_to_bytes));

        return cipher.doFinal(clean);
    }

    private static String decrypt(String cipherText, String type, String mode) throws Exception {
        Cipher cipher = Cipher.getInstance(type+"/"+mode+"/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(key_to_bytes, type);
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(iv_to_bytes));
        byte[] decryptedText = cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes()));

        return new String(decryptedText);
    }

    private String getMode(){
        if (radioCbc.isSelected())
            return "CBC";
        else
            return "OFB";
    }

    private String getType(){
        if (radioAes.isSelected()){
            key_to_bytes = "0ur5h4r3dK3y_Aes".getBytes();
            iv_to_bytes = "initi4liz4v3ct0r".getBytes();
            return "AES";
        }
        else{
            key_to_bytes = "MyD3sK3y".getBytes();
            iv_to_bytes = "MyD3sV3c".getBytes();
            return "DES";
        }
    }
}
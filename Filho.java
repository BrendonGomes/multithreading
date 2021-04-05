import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;


public class Filho extends JFrame implements ActionListener, KeyListener{
    
    private static final long serialVersionUID = 1L;
    private JTextField txtIP;
    private JTextField txtPorta;
    private JTextField txtNome;
    private JButton btnCair;
    private JButton btnChorar;
    private JButton btnFome;
    private JButton btnSair;
    private JTextField txtMsg;
    private JTextArea texto;
    private JLabel lblHistorico;
    private JPanel pnlContent;
    //variaveis de conexão
    private Socket socket;
    private OutputStream ou;
    private Writer ouw;
    private BufferedWriter bfw;
    public static void main(String []args) throws IOException{
        Filho app = new Filho();
        app.conectar();
        app.escutar();
    }

    public Filho() throws IOException{
        JLabel lblMenssage = new JLabel("Cadastrar Filho");
        txtIP = new JTextField("127.0.0.1");
        txtPorta = new JTextField("5000");
        txtNome = new JTextField("NOME DO FILHO");
        Object[] texts = {lblMenssage,txtIP,txtPorta,txtNome};
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel();
        texto              = new JTextArea(10,20);
        texto.setEditable(false);
        texto.setBackground(new Color(240,240,240));
        txtMsg        = new JTextField(20);
        lblHistorico  = new JLabel("HISTORICO");
        btnCair       = new JButton("CAIR");
        btnChorar       = new JButton("CHORO");
        btnFome       = new JButton("FOME");
        btnSair       = new JButton("Sair");
        btnSair.setToolTipText("Sair");
        btnCair.addActionListener(this);
        btnChorar.addActionListener(this);
        btnFome.addActionListener(this);
        btnSair.addActionListener(this);
        btnCair.addKeyListener(this);
        btnFome.addKeyListener(this);
        btnChorar.addKeyListener(this);
        btnFome.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(texto);
        texto.setLineWrap(true);
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll);

        pnlContent.add(btnSair);
        pnlContent.add(btnFome);
        pnlContent.add(btnChorar);
        pnlContent.add(btnCair);
        pnlContent.setBackground(Color.LIGHT_GRAY);
        texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
        txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        setTitle(txtNome.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250,300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void sair() throws IOException {

        enviarMensagem("Sair");
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if (e.getActionCommand().equals(btnCair.getActionCommand()))
                enviarMensagem("HOUVE UMA QUEDA");
            else if (e.getActionCommand().equals(btnSair.getActionCommand()))
                sair();
            else if (e.getActionCommand().equals(btnChorar.getActionCommand()))
                enviarMensagem("ESTA CHORANDO");
            else if (e.getActionCommand().equals(btnFome.getActionCommand()))
                enviarMensagem("ESTA COM FOME");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

//        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//        try {
//                
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
       
    }

    
    public void escutar() throws IOException {

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while (!"Sair".equalsIgnoreCase(msg))

            if (bfr.ready()) {
                msg = bfr.readLine();
                if (msg.equals("Sair"))
                    texto.append("Servidor caiu! \r\n");
                else
                    texto.append(msg + "\r\n");
            }
    }

    public void enviarMensagem(String msg) throws IOException {

        if (msg.equals("Sair")) {
            bfw.write("Desconectado \r\n");
            texto.append("Desconectado \r\n");
        } else {
            bfw.write(msg + "\r\n");
            texto.append(txtNome.getText() + " -> " + msg  + "\r\n");
        }
        bfw.flush();
        txtMsg.setText("");
    }

    public void conectar() throws IOException {

        socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
        ou = socket.getOutputStream();
        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);
        bfw.write(txtNome.getText() + "\r\n");
        bfw.flush();
    }


}

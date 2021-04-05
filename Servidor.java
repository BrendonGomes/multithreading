
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread{

    private static ArrayList<BufferedWriter>clieChat;
    private String nome;
    private static ServerSocket sktServ;
    private Socket conectServ;
    private InputStream inputStream;
    private InputStreamReader inputStrReader;
    private BufferedReader bufferReader;

    public static void main(String []args) {

        try{
          //Cria os objetos necessário para instânciar o servidor
          JLabel lblMessage = new JLabel("Porta do Servidor:");
          JTextField txtPorta = new JTextField("5000");
          Object[] texts = {lblMessage, txtPorta };
          JOptionPane.showMessageDialog(null, texts);
          sktServ = new ServerSocket(Integer.parseInt(txtPorta.getText()));
          clieChat = new ArrayList<BufferedWriter>();
          JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+
          txtPorta.getText());
      
           while(true){
             System.out.println("Aguardando conexão...");
             Socket con = sktServ.accept();
             System.out.println("Cliente conectado...");
             Thread t = new Servidor(con);
              t.start();
          }
        }catch (Exception e) {
      
          e.printStackTrace();
        }
       }

    public Servidor(Socket conectServ)
	{
        this.conectServ = conectServ;
        try{
            inputStream = conectServ.getInputStream();
            inputStrReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputStrReader);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){

        try {
            String mensagem;
            OutputStream outStream = this.conectServ.getOutputStream();
            OutputStreamWriter outStremWriter = new OutputStreamWriter(outStream);
            BufferedWriter bufferWriter = new BufferedWriter(outStremWriter);
            clieChat.add(bufferWriter);
            nome = mensagem = bufferReader.readLine();
            while(!"Sair".equalsIgnoreCase(mensagem) && mensagem != null){
                mensagem = bufferReader.readLine();
                sendToAll(bufferWriter, mensagem);
                System.out.println(mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToAll(BufferedWriter bufferWriter, String mensagem) throws IOException{
        BufferedWriter bwS;

        for(BufferedWriter bw : clieChat){
            bwS = (BufferedWriter)bw;
            if(!(bufferWriter == bwS)){
                bw.write(nome + "->" + mensagem+ "\r\n");
                bw.flush();
            }
        }
    }
    

}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client {

    Socket socket;
    BufferedReader br;
    PrintWriter pw;
    public Client(){
        try {
            System.out.println("Ready to send request to server");
            socket=new Socket("127.0.0.1",7777);
            System.out.println("Connection done between client and server");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     //thread- which is going read data from client
     public void startReading(){
        Runnable r1=()->{
            System.out.println("reader started...");
            while(!socket.isClosed()){
                String msg="";
                try {
                    msg = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(msg.equals("exit")){
                    System.out.println("Server want to stop this chat!");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                System.out.println("Server : "+ msg);
            }
        };
        new Thread(r1).start();

    }
    //thread- which is going write data to client
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("writer started...");
            while(true){
                try {
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    pw.println(content);
                    pw.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        //start thread
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("client is running...");
        new Client();
    }
}

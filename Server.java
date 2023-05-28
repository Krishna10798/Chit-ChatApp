import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
class Server{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter pw;
    public Server(){
        try {
            server=new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket=server.accept();

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
                    System.out.println("Client want to stop this chat!");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                System.out.println("Client : "+ msg);
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
        System.out.println("server is running...");
        new Server();
    }
}
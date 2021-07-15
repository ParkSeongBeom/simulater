package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

class DemoApplicationTests1 {
    public static void main(String[] args) {
        Logger log = (Logger) LoggerFactory.getLogger(DemoApplicationTests1.class);
        try{
            Socket socket = new Socket("localhost", 31181);
            socket.setSoTimeout(5000);
            log.info("server : {}, connected : {}",socket.getRemoteSocketAddress(),socket.isConnected());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),Charset.forName("MS949")));
            log.info("length : {}","1152IF_COP_101_COP1118_HFG9000              COP1118HFG900097096V100COP111820210630155242COP1118202106309474090982                         ONL00000                                                                                                              080081COP1118             3TESTbNRXNf6O+sfhUpyqbRoO/p9wHI1+fNL+vPbCIkh3ZFrZQ+K+E1wMZD+Z+nA0aQHCQ1EO7C92ieyc+UZ46g==20210630155242           0000000000000000000000000000                                                            00000000          0000500000정상처리 되었습니다.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ".length());
            log.info("1");
            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Charset.forName("MS949")));
            log.info("2");
            String msg = "1152IF_COP_101_COP1118_HFG9000              COP1118HFG900097096V100COP111820210630155242COP1118202106309474090982                         ONL00000                                                                                                              080081COP1118             3TESTbNRXNf6O+sfhUpyqbRoO/p9wHI1+fNL+vPbCIkh3ZFrZQ+K+E1wMZD+Z+nA0aQHCQ1EO7C92ieyc+UZ46g==20210630155242           0000000000000000000000000000                                                            00000000          0000500000정상처리 되었습니다.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ";
//            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write(msg);
            log.info("3");
            pw.flush();
//            socket.getOutputStream().write("1152IF_COP_101_COP1118_HFG9000              COP1118HFG900097096V100COP111820210630155242COP1118202106309474090982                         ONL00000                                                                                                              080081COP1118             3TESTbNRXNf6O+sfhUpyqbRoO/p9wHI1+fNL+vPbCIkh3ZFrZQ+K+E1wMZD+Z+nA0aQHCQ1EO7C92ieyc+UZ46g==20210630155242           0000000000000000000000000000                                                            00000000          0000500000정상처리 되었습니다.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ".getBytes(Charset.forName("MS949")));
//            pw.println("1152IF_COP_101_COP1118_HFG9000              COP1118HFG900097096V100COP111820210630155242COP1118202106309474090982                         ONL00000                                                                                                              080081COP1118             3TESTbNRXNf6O+sfhUpyqbRoO/p9wHI1+fNL+vPbCIkh3ZFrZQ+K+E1wMZD+Z+nA0aQHCQ1EO7C92ieyc+UZ46g==20210630155242           0000000000000000000000000000                                                            00000000          0000500000정상처리 되었습니다.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ");
            log.info("4");
            log.info("5");
            log.info(br.readLine());
            log.info("6");
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

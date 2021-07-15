package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.Socket;

@SpringBootTest
class DemoApplicationTests {
    Logger log = (Logger) LoggerFactory.getLogger(DemoApplicationTests.class);

    @Test
    void contextLoads() {
        try{
            Socket socket = new Socket("localhost", 8092);
            socket.setSoTimeout(5000);
            log.info("server : {}, connected : {}",socket.getRemoteSocketAddress(),socket.isConnected());

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            log.info("1");
            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            log.info("2");
//            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write("1152IF_COP_101_COP1118_HFG9000              COP1118HFG900097096V100COP111820210630155242COP1118202106309474090982                         ONL00000                                                                                                              080081COP1118             3TESTbNRXNf6O+sfhUpyqbRoO/p9wHI1+fNL+vPbCIkh3ZFrZQ+K+E1wMZD+Z+nA0aQHCQ1EO7C92ieyc+UZ46g==20210630155242           0000000000000000000000000000                                                            00000000          0000500000정상처리 되었습니다.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ");
//            pw.println("1152IF_COP_101_COP1118_HFG9000              COP1118HFG900097096V100COP111820210630155242COP1118202106309474090982                         ONL00000                                                                                                              080081COP1118             3TESTbNRXNf6O+sfhUpyqbRoO/p9wHI1+fNL+vPbCIkh3ZFrZQ+K+E1wMZD+Z+nA0aQHCQ1EO7C92ieyc+UZ46g==20210630155242           0000000000000000000000000000                                                            00000000          0000500000정상처리 되었습니다.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ");
            log.info("3");
            pw.flush();
            log.info(br.readLine());
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

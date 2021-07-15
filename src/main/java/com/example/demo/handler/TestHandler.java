package com.example.demo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.Objects;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class TestHandler extends ChannelInboundHandlerAdapter {
    @Value("${tcp.server.info_code}")
    private String info_code;

    @Value("${tcp.server.import_code}")
    private String import_code;

    private ByteBuf buff;

    // 핸들러가 생성될 때 호출되는 메소드
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buff = ctx.alloc().buffer(1147);
    }

    // 핸들러가 제거될 때 호출되는 메소드
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buff = null;
    }

    // 클라이언트와 연결되어 트래픽을 생성할 준비가 되었을 때 호출되는 메소드
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("Remote Address: " + remoteAddress);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf mBuf = (ByteBuf) msg;
        buff.writeBytes(mBuf);  // 클라이언트에서 보내는 데이터가 축적됨
        mBuf.release();

        String resultCode = "00000";
        String resultMessage = "정상처리 되었습니다.                                                                                                                                           ";
        String responseString = buff.toString(Charset.forName("MS949"));
        String gram_tp_dv = responseString.substring(256, 262);
        String point = "0";
        if (Objects.equals(gram_tp_dv, "080081")) {
            resultCode = info_code;
            point = paddingNZoro("100000", 10);
        } else if (Objects.equals(gram_tp_dv, "030031")) {
            resultCode = import_code;
            point = paddingNZoro(responseString.substring(396, 406), 10);
        } else {
            point = paddingNZoro("0", 10);
        }
        log.info("[하나멤버스] req message : {}",responseString);
        log.info("[하나멤버스] gram_tp_dv : {}, resultCode: {}",gram_tp_dv, resultCode);

        if (Objects.equals(resultCode, "06028")) {
            resultMessage = "일_한도_횟수를 초과 하였습니다.                                                                                                                                    ";
        } else if (Objects.equals(resultCode, "06029")) {
            resultMessage = "일_한도_머니를 초과 하였습니다.                                                                                                                                    ";
        } else if (Objects.equals(resultCode, "06030")) {
            resultMessage = "월_한도_횟수를 초과 하였습니다.                                                                                                                                    ";
        } else if (Objects.equals(resultCode, "06031")) {
            resultMessage = "월_한도_머니를 초과 하였습니다.                                                                                                                                    ";
        } else if (Objects.equals(resultCode, "01903")) {
            resultMessage = "23:50 ~ 00:10 서비스 점검 중입니다. 이용에 불편 드려 죄송합니다.                                                                                                           ";
        } else if (!Objects.equals(resultCode, "00000")) {
            resultMessage = "오류가 발생하였습니다.                                                                                                                                          ";
        }

        String resultStr = "1500IF_COP_101_COP1118_HFG9000              COP1118HFG900097096V100COP111820210630155242COP1118202106309474090982                         ONL"+resultCode+"                                                                                                              "+gram_tp_dv+"COP1118             3TESTbNRXNf6O+sfhUpyqbRoO/p9wHI1+fNL+vPbCIkh3ZFrZQ+K+E1wMZD+Z+nA0aQHCQ1EO7C92ieyc+UZ46g==20210630155242           "+point+point+"00000000                                                            00000000          "+point+resultMessage+"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         012345670123456701234567";
        log.info("[resultStr] resultStr : [{}]",resultStr);
        ByteBuf rBuf = Unpooled.directBuffer();;
        byte[] temp = resultStr.getBytes(Charset.forName("MS949"));
        rBuf.writeBytes(temp);
        log.info("[getBytes] getBytes : [{}]",temp.length);
        final ChannelFuture f = ctx.writeAndFlush(rBuf);
        f.addListener(ChannelFutureListener.CLOSE);
    }

    private String paddingNZoro(@Nullable String source, int length) {
        int sourceLength = source != null ? source.length() : 0;
        int paddingSize = length - sourceLength;
        if (paddingSize <= 0) {
            return source;
        }

        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < paddingSize; i++) {
            builder.append("0");
        }
        builder.append(source);

        return builder.toString();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        ctx.close();
        cause.printStackTrace();
    }

}
package com.dingjianjun.basetech.nio;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/28
 */
@ChannelHandler.Sharable
@Slf4j
public class FlowControlChannelHandler extends ChannelInboundHandlerAdapter {
    private BizThreadGroup bizThreadGroup;
    public FlowControlChannelHandler(BizThreadGroup bizThreadGroup) {
        this.bizThreadGroup = bizThreadGroup;
    }
    private static final int FLOW_CONTROL_THRESHOLD = 2;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端的并发连接数
        int curConnNum = bizThreadGroup.curConnNum() + 1;
        if (curConnNum >= FLOW_CONTROL_THRESHOLD) {
            log.warn("当前客户端的并发数连接数：{}, 已达到流控阈值：{}, 关闭连接channelId:{}", curConnNum, FLOW_CONTROL_THRESHOLD, ctx.channel().id());
            ctx.close();
            return;
        }

        ctx.fireChannelActive();
    }
}

/**
 * Copyright (c) Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.shared.protocol.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Used to make sure any stray exceptions that make it back to the socket get logged.
 */
@Slf4j
public class ExceptionLoggingHandler extends ChannelDuplexHandler {

    private final String connectionName;

    public ExceptionLoggingHandler(String connectionName) {
        this.connectionName = connectionName;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Uncaught exception on connection " + connectionName, cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            super.channelRead(ctx, msg);
        } catch (Exception e) {
            log.error("Uncaught exception observed during channel read on connection {}", connectionName, e);
            throw e;
        }
    }

}

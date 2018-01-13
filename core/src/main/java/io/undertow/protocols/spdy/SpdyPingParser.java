/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.undertow.protocols.spdy;


import java.nio.ByteBuffer;

/**
 * Parser for SPDY ping frames.
 *
 * @author Stuart Douglas
 */
class SpdyPingParser extends SpdyPushBackParser {

    private int id;

    SpdyPingParser(int frameLength) {
        super(frameLength);
    }

    @Override
    protected void handleData(ByteBuffer resource) {
        if (resource.remaining() < 4) {
            return;
        }
        id = SpdyProtocolUtils.readInt(resource);
    }

    public int getId() {
        return id;
    }
}

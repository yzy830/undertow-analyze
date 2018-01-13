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

package io.undertow.util;


import org.junit.Assert;
import org.junit.Test;


public class FlexBase64TestCase {

    @Test
    public void testReadStopsAtTerminator() throws Exception {
        String source = "ZWxsbw===";
        byte[] target = new byte[1024];
        final FlexBase64.Decoder decoder = FlexBase64.createDecoder();
        int read = decoder.decode(source, 0, source.length(), target, 0, target.length);
        Assert.assertEquals(4, read);
        Assert.assertEquals("ello", new String(target, 0, read));
        Assert.assertEquals(8, decoder.getLastInputPosition());

    }
}
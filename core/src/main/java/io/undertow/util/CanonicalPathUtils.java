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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stuart Douglas
 */
public class CanonicalPathUtils {


    public static String canonicalize(final String path) {
        int state = START;
        for (int i = path.length() - 1; i >= 0; --i) {
            final char c = path.charAt(i);
            switch (c) {
                case '/':
                    if (state == FIRST_SLASH) {
                        return realCanonicalize(path, i + 1, FIRST_SLASH);
                    } else if (state == ONE_DOT) {
                        return realCanonicalize(path, i + 2, FIRST_SLASH);
                    } else if (state == TWO_DOT) {
                        return realCanonicalize(path, i + 3, FIRST_SLASH);
                    }
                    state = FIRST_SLASH;
                    break;
                case '.':
                    if (state == FIRST_SLASH || state == START) {
                        state = ONE_DOT;
                    } else if(state == ONE_DOT) {
                        state = TWO_DOT;
                    } else {
                        state = NORMAL;
                    }
                    break;
                default:
                    state  = NORMAL;
                    break;
            }
        }
        return path;
    }

    static final int START = -1;
    static final int NORMAL = 0;
    static final int FIRST_SLASH = 1;
    static final int ONE_DOT = 2;
    static final int TWO_DOT = 3;


    private static String realCanonicalize(final String path, final int lastDot, final int initialState) {
        int state = initialState;
        int eatCount = 0;
        int tokenEnd = path.length();
        final List<String> parts = new ArrayList<>();
        for (int i = lastDot - 1; i >= 0; --i) {
            final char c = path.charAt(i);
            switch (state) {
                case NORMAL: {
                    if (c == '/') {
                        state = FIRST_SLASH;
                        if (eatCount > 0) {
                            --eatCount;
                            tokenEnd = i;
                        }
                    }
                    break;
                }
                case FIRST_SLASH: {
                    if (c == '.') {
                        state = ONE_DOT;
                    } else if (c == '/') {
                        if (eatCount > 0) {
                            --eatCount;
                            tokenEnd = i;
                        } else {
                            parts.add(path.substring(i + 1, tokenEnd));
                            tokenEnd = i;
                        }
                    } else {
                        state = NORMAL;
                    }
                    break;
                }
                case ONE_DOT: {
                    if (c == '.') {
                        state = TWO_DOT;
                    } else if (c == '/') {
                        if (i + 2 != tokenEnd) {
                            parts.add(path.substring(i + 2, tokenEnd));
                        }
                        tokenEnd = i;
                        state = FIRST_SLASH;
                    } else {
                        state = NORMAL;
                    }
                    break;
                }
                case TWO_DOT: {
                    if (c == '/') {
                        if (i + 3 != tokenEnd) {
                            parts.add(path.substring(i + 3, tokenEnd));
                        }
                        tokenEnd = i;
                        eatCount++;
                        state = FIRST_SLASH;
                    } else {
                        state = NORMAL;
                    }
                }
            }
        }
        final StringBuilder result = new StringBuilder();
        if (tokenEnd != 0) {
            result.append(path.substring(0, tokenEnd));
        }
        for (int i = parts.size() - 1; i >= 0; --i) {
            result.append(parts.get(i));
        }
        if(result.length() == 0) {
            return "/";
        }
        return result.toString();
    }

    private CanonicalPathUtils() {

    }
}

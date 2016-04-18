/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.dsl.processor.model;

import com.oracle.truffle.dsl.processor.Log;
import com.oracle.truffle.dsl.processor.ProcessorContext;
import com.oracle.truffle.dsl.processor.java.ElementUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

public abstract class MessageContainer implements Iterable<MessageContainer> {

    private final List<Message> messages = new ArrayList<>();

    public final void addWarning(String text, Object... params) {
        getMessages().add(new Message(null, null, this, String.format(text, params), Kind.WARNING));
    }

    public final void addWarning(AnnotationValue value, String text, Object... params) {
        getMessages().add(new Message(null, value, this, String.format(text, params), Kind.WARNING));
    }

    public final void addError(String text, Object... params) {
        addError(null, text, params);
    }

    public final void addError(AnnotationValue value, String text, Object... params) {
        getMessages().add(new Message(null, value, this, String.format(text, params), Kind.ERROR));
    }

    public final void addError(AnnotationMirror mirror, AnnotationValue value, String text, Object... params) {
        getMessages().add(new Message(mirror, value, this, String.format(text, params), Kind.ERROR));
    }

    protected List<MessageContainer> findChildContainers() {
        return Collections.emptyList();
    }

    public abstract Element getMessageElement();

    public MessageContainer getBaseContainer() {
        return null;
    }

    public Iterator<MessageContainer> iterator() {
        return findChildContainers().iterator();
    }

    public final void emitMessages(ProcessorContext context, Log log) {
        emitMessagesImpl(context, log, new HashSet<MessageContainer>(), null);
    }

    private void emitMessagesImpl(ProcessorContext context, Log log, Set<MessageContainer> visitedSinks, List<Message> verifiedMessages) {
        List<Message> childMessages;
        if (verifiedMessages == null) {
            childMessages = collectMessagesWithElementChildren(new HashSet<MessageContainer>(), getMessageElement());
        } else {
            childMessages = verifiedMessages;
        }
        verifyExpectedMessages(context, log, childMessages);

        for (int i = getMessages().size() - 1; i >= 0; i--) {
            emitDefault(context, log, getMessages().get(i));
        }

        for (MessageContainer sink : findChildContainers()) {
            if (visitedSinks.contains(sink)) {
                continue;
            }

            visitedSinks.add(sink);
            if (sink.getMessageElement() == this.getMessageElement()) {
                sink.emitMessagesImpl(context, log, visitedSinks, childMessages);
            } else {
                sink.emitMessagesImpl(context, log, visitedSinks, null);
            }
        }
    }

    private List<Message> collectMessagesWithElementChildren(Set<MessageContainer> visitedSinks, Element e) {
        if (visitedSinks.contains(this)) {
            return Collections.emptyList();
        }
        visitedSinks.add(this);

        List<Message> foundMessages = new ArrayList<>();
        if (getMessageElement() != null && ElementUtils.typeEquals(getMessageElement().asType(), e.asType())) {
            foundMessages.addAll(getMessages());
        }
        for (MessageContainer sink : findChildContainers()) {
            foundMessages.addAll(sink.collectMessagesWithElementChildren(visitedSinks, e));
        }
        return foundMessages;
    }

    private void verifyExpectedMessages(ProcessorContext context, Log log, List<Message> msgs) {
        TypeElement expectError = context.getTruffleTypes().getExpectError();
        if (expectError != null) {
            Element element = getMessageElement();
            if (element != null) {
                AnnotationMirror mirror = ElementUtils.findAnnotationMirror(element.getAnnotationMirrors(), expectError.asType());
                if (mirror != null) {
                    List<String> values = ElementUtils.getAnnotationValueList(String.class, mirror, "value");
                    if (values == null) {
                        values = Collections.emptyList();
                    }
                    if (values.size() != msgs.size()) {
                        log.message(Kind.ERROR, element, mirror, ElementUtils.getAnnotationValue(mirror, "value"), String.format("Error count expected %s but was %s.", values.size(), msgs.size()));
                    }
                }
            }
        }
    }

    private void emitDefault(ProcessorContext context, Log log, Message message) {
        Kind kind = message.getKind();

        Element messageElement = getMessageElement();
        AnnotationMirror messageAnnotation = getMessageAnnotation();
        AnnotationValue messageValue = getMessageAnnotationValue();
        if (message.getAnnotationValue() != null) {
            messageValue = message.getAnnotationValue();
        }
        if (message.getAnnotationMirror() != null) {
            messageAnnotation = message.getAnnotationMirror();
        }

        String text = message.getText();

        TypeElement expectError = context.getTruffleTypes().getExpectError();
        if (expectError != null) {
            AnnotationMirror mirror = ElementUtils.findAnnotationMirror(messageElement.getAnnotationMirrors(), expectError.asType());
            if (mirror != null) {
                List<String> expectedTexts = ElementUtils.getAnnotationValueList(String.class, mirror, "value");
                boolean found = false;
                for (String expectedText : expectedTexts) {
                    if (expectedText.endsWith("%") && text.startsWith(expectedText.substring(0, expectedText.length() - 1))) {
                        found = true;
                        break;
                    } else if (text.equals(expectedText)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    log.message(kind, messageElement, mirror, ElementUtils.getAnnotationValue(mirror, "value"), "Message expected one of '%s' but was '%s'.", expectedTexts, text);
                } else {
                    return;
                }

            }
        }

        log.message(kind, messageElement, messageAnnotation, messageValue, text);
    }

    public AnnotationMirror getMessageAnnotation() {
        return null;
    }

    public AnnotationValue getMessageAnnotationValue() {
        return null;
    }

    public final boolean hasErrors() {
        return hasErrorsImpl(new HashSet<MessageContainer>());
    }

    public final List<Message> collectMessages() {
        List<Message> collectedMessages = new ArrayList<>();
        collectMessagesImpl(collectedMessages, new HashSet<MessageContainer>());
        return collectedMessages;
    }

    private void collectMessagesImpl(List<Message> collectedMessages, Set<MessageContainer> visitedSinks) {
        collectedMessages.addAll(getMessages());
        for (MessageContainer sink : findChildContainers()) {
            if (visitedSinks.contains(sink)) {
                return;
            }

            visitedSinks.add(sink);
            sink.collectMessagesImpl(collectedMessages, visitedSinks);
        }
    }

    private boolean hasErrorsImpl(Set<MessageContainer> visitedSinks) {
        for (Message msg : getMessages()) {
            if (msg.getKind() == Kind.ERROR) {
                return true;
            }
        }
        for (MessageContainer sink : findChildContainers()) {
            if (visitedSinks.contains(sink)) {
                continue;
            }

            visitedSinks.add(sink);

            if (sink.hasErrorsImpl(visitedSinks)) {
                return true;
            }
        }
        return false;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public static final class Message {

        private final MessageContainer originalContainer;
        private final AnnotationMirror annotationMirror;
        private final AnnotationValue annotationValue;
        private final String text;
        private final Kind kind;

        public Message(AnnotationMirror annotationMirror, AnnotationValue annotationValue, MessageContainer originalContainer, String text, Kind kind) {
            this.annotationMirror = annotationMirror;
            this.annotationValue = annotationValue;
            this.originalContainer = originalContainer;
            this.text = text;
            this.kind = kind;
        }

        public AnnotationMirror getAnnotationMirror() {
            return annotationMirror;
        }

        public AnnotationValue getAnnotationValue() {
            return annotationValue;
        }

        public MessageContainer getOriginalContainer() {
            return originalContainer;
        }

        public String getText() {
            return text;
        }

        public Kind getKind() {
            return kind;
        }

        @Override
        public String toString() {
            return kind + ": " + text;
        }

    }

}

package com.demo.protobuf;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Common {

    public static class RpcExt {

        OutInType inputType;

        OutInType outType;

        public OutInType getInputType() {
            return inputType;
        }

        public void setInputType(OutInType inputType) {
            this.inputType = inputType;
        }

        public OutInType getOutType() {
            return outType;
        }

        public void setOutType(OutInType outType) {
            this.outType = outType;
        }
    }

    public static class OutInType {

        private String name;

        Set<MessageType> messageTypes;

        Set<MessageType> enumTypes;

        public Set<MessageType> getMessageTypes() {
            return messageTypes;
        }

        public void setMessageTypes(Set<MessageType> messageTypes) {
            this.messageTypes = messageTypes;
        }

        public Set<MessageType> getEnumTypes() {
            return enumTypes;
        }

        public void setEnumTypes(Set<MessageType> enumTypes) {
            this.enumTypes = enumTypes;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class MessageType {
        private String fullName;
        private List<Field> fields;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public List<Field> getFields() {
            return fields;
        }

        public void setFields(List<Field> fields) {
            this.fields = fields;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MessageType that = (MessageType) o;
            return Objects.equals(fullName, that.fullName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fullName);
        }
    }

    public static class Field {
        private String name;
        private int number;
        private int type;
        private String typeName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

}

package com.demo.protobuf;

import com.alibaba.fastjson.JSON;
import com.demo.protobuf.Common.Field;
import com.demo.protobuf.Common.MessageType;
import com.demo.protobuf.Common.RpcExt;
import com.google.protobuf.Descriptors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SourceBuilder {

    /**
     * 构建json串
     * @param inputType
     * @param outType
     * @return
     */
    private static String buildRpcExt(final Descriptors.Descriptor inputType,
                                      final Descriptors.Descriptor outType) {
        RpcExt rpcExt = new RpcExt();
        rpcExt.setInputType(buildMessageTypes("request", inputType));
        rpcExt.setOutType(buildMessageTypes("response", outType));
        return JSON.toJSONString(rpcExt);
    }

    private static Common.OutInType buildMessageTypes(String name, Descriptors.Descriptor descriptor) {
        // 记录所有的messageType
        Set<MessageType> messageTypes = new HashSet<>();
        // 记录所有的enumType
        Set<MessageType> enumTypes = new HashSet<>();
        // 构建inputType的所有field，同时记录field依赖的messageType
        List<Field> fields = buildFields(descriptor, messageTypes, enumTypes);
        // 构建inputType的messageType
        MessageType messageType = new MessageType();
        // 名称
        messageType.setFullName(name);
        // 字段
        messageType.setFields(fields);
        // 添加进messageType集合
        messageTypes.add(messageType);
        // 返回两种集合
        Common.OutInType outInType = new Common.OutInType();
        outInType.setEnumTypes(enumTypes);
        outInType.setMessageTypes(messageTypes);
        outInType.setName(name);
        return outInType;
    }

    private static List<Field> buildFields(Descriptors.Descriptor descriptor, Set<MessageType> messageTypes, Set<MessageType> enumTypes) {
        List<Field> fields = new ArrayList<>();
        for (Descriptors.FieldDescriptor item : descriptor.getFields()) {
            Field field = new Field();
            field.setName(item.toProto().getName());
            field.setType(item.toProto().getType().getNumber());
            field.setNumber(item.toProto().getNumber());
            fields.add(field);
            if (item.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE && item.getMessageType() != null) {
                field.setTypeName(item.getMessageType().getFullName().replaceAll("\\.", ""));
                messageTypes.add(buildMessageType(item.getMessageType(), messageTypes, enumTypes));
            }
            if (item.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM && item.getEnumType() != null) {
                field.setTypeName(item.getEnumType().getFullName().replaceAll("\\.", ""));
                enumTypes.add(buildEnumType(item.getEnumType()));
            }
        }
        return fields;
    }

    private static MessageType buildMessageType(Descriptors.Descriptor descriptor, Set<MessageType> messageTypes, Set<MessageType> enumTypes) {
        MessageType messageType = new MessageType();
        messageType.setFullName(descriptor.getFullName().replaceAll("\\.", ""));
        messageType.setFields(buildFields(descriptor, messageTypes, enumTypes));
        return messageType;
    }

    private static MessageType buildEnumType(Descriptors.EnumDescriptor descriptor) {
        MessageType messageType = new MessageType();
        messageType.setFullName(descriptor.getFullName().replaceAll("\\.", ""));
        messageType.setFields(buildValues(descriptor));
        return messageType;
    }

    private static List<Field> buildValues(Descriptors.EnumDescriptor descriptor) {
        List<Field> fields = new ArrayList<>();
        for (Descriptors.EnumValueDescriptor item : descriptor.getValues()) {
            Field field = new Field();
            field.setName(item.toProto().getName());
            field.setNumber(item.toProto().getNumber());
            fields.add(field);
        }
        return fields;
    }

}

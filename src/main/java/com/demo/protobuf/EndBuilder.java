package com.demo.protobuf;

import com.demo.protobuf.Common.MessageType;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import java.util.Set;

public class EndBuilder {


    /**
     * 构建 Descriptor
     * @param name
     * @param messageTypes
     * @param enumTypes
     * @return
     * @throws Descriptors.DescriptorValidationException
     */
    public Descriptors.Descriptor buildDescriptors(String name, Set<MessageType> messageTypes, Set<MessageType> enumTypes)
            throws Descriptors.DescriptorValidationException {
        DescriptorProtos.FileDescriptorProto.Builder protoBuilder = DescriptorProtos.FileDescriptorProto.newBuilder();
        // messageType
        for (MessageType item : messageTypes) {
            DescriptorProtos.DescriptorProto.Builder messageType = DescriptorProtos.DescriptorProto.newBuilder();
            messageType.setName(item.getFullName());
            item.getFields().forEach(loop -> {
                        DescriptorProtos.FieldDescriptorProto.Builder builder =
                                messageType.addFieldBuilder()
                                        .setName(loop.getName())
                                        .setType(DescriptorProtos.FieldDescriptorProto.Type.forNumber(loop.getType()))
                                        .setNumber(loop.getNumber());
                        if (loop.getTypeName() != null) {
                            builder.setTypeName(loop.getTypeName());
                        }
                    }
            );
            protoBuilder.addMessageType(messageType);
        }
        // enumType
        for (MessageType item : enumTypes) {
            DescriptorProtos.EnumDescriptorProto.Builder enumType = DescriptorProtos.EnumDescriptorProto.newBuilder()
                    .setName(item.getFullName());
            item.getFields().forEach(loop -> {
                enumType.addValue(DescriptorProtos.EnumValueDescriptorProto.newBuilder().setName(loop.getName())
                        .setNumber(loop.getNumber()).build());
            });
            protoBuilder.addEnumType(enumType);
        }
        DescriptorProtos.FileDescriptorProto fileDescriptorProto = protoBuilder.build();
        Descriptors.FileDescriptor fileDescriptor =
                Descriptors.FileDescriptor.buildFrom(fileDescriptorProto, new Descriptors.FileDescriptor[0]);
        Descriptors.Descriptor descriptor = fileDescriptor.findMessageTypeByName(name);
        return descriptor;
    }


    /**
     * 构建 DynamicMessage
     * @param name
     * @param messageTypes
     * @param enumTypes
     * @param data
     * @return
     * @throws Descriptors.DescriptorValidationException
     * @throws InvalidProtocolBufferException
     */
    public DynamicMessage buildDynamicMessage(String name, Set<MessageType> messageTypes, Set<MessageType> enumTypes,
                                               String data)
            throws Descriptors.DescriptorValidationException, InvalidProtocolBufferException {
        Descriptors.Descriptor descriptor = this.buildDescriptors(name, messageTypes, enumTypes);
        DynamicMessage.Builder messageBuilder = DynamicMessage.newBuilder(descriptor);
        if (data != null && !"".equals(data)) {
            JsonFormat.parser().ignoringUnknownFields().merge(data, messageBuilder);
        }
        DynamicMessage dynamicMessage = messageBuilder.build();
        return dynamicMessage;
    }

}

package javelin.service;

import javelin.AbstractITTest;
import javelin.dto.NewCommunicationRequest;
import javelin.entity.Receiver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static javelin.entity.Communication.Status.ACCEPTED;
import static javelin.entity.Communication.Status.CANCELLED;
import static javelin.entity.Communication.Status.CREATED;
import static javelin.entity.Communication.Type.PHOTO;
import static javelin.entity.Communication.Type.TEXT;
import static javelin.entity.Communication.Type.VIDEO;
import static org.junit.jupiter.api.Assertions.*;

class CommunicationServiceTest extends AbstractITTest {

    @Autowired
    private CommunicationService communicationService;

    @Test
    void When_ValidPhotoRequest_Then_ExpectCorrectlyCreatedCommunication() {
        var r = new NewCommunicationRequest(
            "objectId1",
            PHOTO,
            "txt",
            1L,
            Receiver.ALL
        );

        var communication = communicationService.createNew(r);

        assertEquals(r.objectId(), communication.getObjectId());
        assertEquals(r.type(), communication.getType());
        assertEquals(r.caption(), communication.getTxt());
        assertEquals(r.sender(), communication.getSender());
        assertEquals(r.receiver(), communication.getReceiver());
        assertEquals(CREATED, communication.getStatus());
        assertNotNull(communication.getCreated());
        assertNotNull(communication.getUpdated());
        assertNotNull(communication.getId());
        assertNull(communication.getObjectUrl());
    }

    @Test
    void When_ValidVideoRequest_Then_ExpectCorrectlyCreatedCommunication() {
        var r = new NewCommunicationRequest(
            "objectId1",
            VIDEO,
            "txt",
            1L,
            Receiver.ALL
        );

        var communication = communicationService.createNew(r);

        assertEquals(r.objectId(), communication.getObjectId());
        assertEquals(r.type(), communication.getType());
        assertEquals(r.caption(), communication.getTxt());
        assertEquals(r.sender(), communication.getSender());
        assertEquals(r.receiver(), communication.getReceiver());
        assertEquals(CREATED, communication.getStatus());
        assertNotNull(communication.getCreated());
        assertNotNull(communication.getUpdated());
        assertNotNull(communication.getId());
        assertNull(communication.getObjectUrl());
    }

    @Test
    void When_ValidNullCaptionRequest_Then_ExpectCorrectlyCreatedCommunication() {
        var r = new NewCommunicationRequest(
            "objectId1",
            PHOTO,
            null,
            1L,
            Receiver.ALL
        );

        var communication = communicationService.createNew(r);

        assertEquals(r.objectId(), communication.getObjectId());
        assertEquals(r.type(), communication.getType());
        assertEquals(r.sender(), communication.getSender());
        assertEquals(r.receiver(), communication.getReceiver());
        assertEquals(CREATED, communication.getStatus());
        assertNotNull(communication.getCreated());
        assertNotNull(communication.getUpdated());
        assertNotNull(communication.getId());
        assertNull(communication.getObjectUrl());
        assertNull(communication.getTxt());
    }

    @Test
    void When_ValidTextRequest_Then_ExpectCorrectlyCreatedCommunication() {
        var r = new NewCommunicationRequest(
            "objectId1",
            TEXT,
            null,
            1L,
            Receiver.ALL
        );

        var communication = communicationService.createNew(r);

        assertEquals(r.objectId(), communication.getObjectId());
        assertEquals(r.type(), communication.getType());
        assertEquals(r.sender(), communication.getSender());
        assertEquals(r.receiver(), communication.getReceiver());
        assertEquals(CREATED, communication.getStatus());
        assertNotNull(communication.getCreated());
        assertNotNull(communication.getUpdated());
        assertNotNull(communication.getId());
        assertNull(communication.getObjectUrl());
        assertNull(communication.getTxt());
    }

    @Test
    void When_InvalidNullObjectIdRequest_Then_AnError() {
        var r = new NewCommunicationRequest(
            null,
            null,
            null,
            1L,
            Receiver.ALL
        );

        assertThrows(DataIntegrityViolationException.class, () -> communicationService.createNew(r));
    }

    @Test
    void When_AcceptPhotoCommunication_Then_ExpectedFieldsAreUpdated() {
        var r = new NewCommunicationRequest(
            "objectId1",
            PHOTO,
            "txt",
            1L,
            Receiver.ALL
        );

        var communication = communicationService.createNew(r);

        var accepted = communicationService.accept(communication.getId());

        assertEquals(communication.getId(), accepted.getId());
        assertEquals(CREATED, communication.getStatus());
        assertEquals(ACCEPTED, accepted.getStatus());
        assertNotEquals(communication.getUpdated(), accepted.getUpdated());
        assertNull(communication.getObjectUrl());
    }

    @Test
    void When_CancelPhotoCommunication_Then_ExpectedFieldsAreUpdated() {
        var r = new NewCommunicationRequest(
            "objectId1",
            PHOTO,
            "txt",
            1L,
            Receiver.ALL
        );

        var communication = communicationService.createNew(r);

        var cancelled = communicationService.cancel(communication.getId());

        assertEquals(communication.getId(), cancelled.getId());
        assertEquals(CREATED, communication.getStatus());
        assertEquals(CANCELLED, cancelled.getStatus());
        assertNotEquals(communication.getUpdated(), cancelled.getUpdated());
        assertNull(communication.getObjectUrl());
    }

    @Test
    void When_CancelAcceptedCommunication_Then_ExpectedItCannotBeCancelled() {
        var r = new NewCommunicationRequest(
            "objectId1",
            PHOTO,
            "txt",
            1L,
            Receiver.ALL
        );

        var communication = communicationService.createNew(r);

        var accept = communicationService.accept(communication.getId());

        assertEquals(communication.getId(), accept.getId());
        assertEquals(CREATED, communication.getStatus());
        assertEquals(ACCEPTED, accept.getStatus());

        var cancelled = communicationService.cancel(communication.getId());

        assertEquals(communication.getId(), cancelled.getId());
        assertEquals(ACCEPTED, cancelled.getStatus());
    }

    @Test
    void When_AcceptCancelledCommunication_Then_ExpectedItCanBeAccepted() {
        var r = new NewCommunicationRequest(
            "objectId1",
            PHOTO,
            "txt",
            1L,
            Receiver.ALL
        );

        var communication = communicationService.createNew(r);

        var cancelled = communicationService.cancel(communication.getId());

        assertEquals(communication.getId(), cancelled.getId());
        assertEquals(CREATED, communication.getStatus());
        assertEquals(CANCELLED, cancelled.getStatus());

        var accept = communicationService.accept(communication.getId());

        assertEquals(communication.getId(), cancelled.getId());
        assertEquals(ACCEPTED, accept.getStatus());
    }
}
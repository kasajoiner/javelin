package javelin.dto;

import javelin.entity.Communication;
import javelin.entity.Receiver;

public record NewCommunicationRequest(String objectId, Communication.Type type, String caption, Long sender, Receiver receiver) {
}

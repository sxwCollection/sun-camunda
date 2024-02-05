package camu.openapi.client.message;

public class MessageFactory {

    public static LottoMessage getMessageInstance(MessageType type, String... args) {
        LottoMessage out;
        final String msgName = type.getValue();
        switch (type) {
            case NEW_CUSTOMER -> {
                MessageHead head = new MessageHead(msgName, args[0] + "-" + System.currentTimeMillis());
                out = new CustomerMessage(head, args[0]);
                break;
            }
            case WIN -> {
                final String msg = "Congratulations " + args[0] + ", " + args[1];
                out = new CheckingResultMessage(new MessageHead(msgName, null), msg);
                break;
            }
            case LOSE, PUNISH -> {
                out = new CheckingResultMessage(new MessageHead(msgName, null), args[0]);
                break;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return out;
    }
}

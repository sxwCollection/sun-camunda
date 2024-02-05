package camu.openapi.client.message;

public enum MessageType {
    NEW_CUSTOMER("customerBuyLotto"), WIN("WinMsg"), LOSE("NoWinMsg"), PUNISH("PunishMsg");
    private final String value;

    MessageType(String val) {
        this.value = val;
    }

    public String getValue() {
        return this.value;
    }
}

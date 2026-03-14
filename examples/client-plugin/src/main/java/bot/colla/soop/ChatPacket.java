package bot.colla.soop;

public record ChatPacket(ChatPacket.Type type, String nickname, int count) {
    public enum Type {
        BALLOON,
        ADBALLOON,
        BATTLE_MISSION,
        CHALLENGE_MISSION,
        VIDEO_BALLOON
    }
}

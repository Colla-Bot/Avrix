package bot.colla.soop.chat;

import io.netty.buffer.ByteBuf;

public interface SoopPacket {
    void toBytes(ByteBuf buf);
}

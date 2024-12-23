package client;

import common.BaseBullet;
import processing.core.PApplet;
import util.SerializingInputStream;

public class ClientBullet extends BaseBullet implements Drawable {
    public ClientBullet(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
    }

    @Override
    public void draw(PApplet g) {

    }
}

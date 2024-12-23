package common;

import util.SerializingOutputStream;

public interface Sendable {
    void getData(SerializingOutputStream out);
}

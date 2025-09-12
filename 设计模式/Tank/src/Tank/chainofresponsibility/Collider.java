package Tank.chainofresponsibility;

import Tank.AbstractGameObject;

import java.io.Serializable;

public interface Collider extends Serializable {

    public boolean collide(AbstractGameObject go1,AbstractGameObject go2);
}

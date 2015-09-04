package fr.lyrgard.hexScape;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;

/**
 *
 * @author kobayasi
 */
public class CartoonEdgeProcessor implements SceneProcessor {

    RenderManager rm;
    ViewPort vp;

    public CartoonEdgeProcessor() {
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
        this.rm = rm;
        this.vp = vp;
    }

    @Override
    public void reshape(ViewPort vp, int w, int h) {
    }

    @Override
    public boolean isInitialized() {
        if (rm != null) {
            return true;
        }
        return false;
    }

    @Override
    public void preFrame(float tpf) {
    }

    @Override
    public void postQueue(RenderQueue rq) {
        rm.setForcedTechnique("CartoonEdge");
        rm.renderViewPortQueues(vp, false);
        rm.setForcedTechnique(null);
    }

    @Override
    public void postFrame(FrameBuffer out) {
    }

    @Override
    public void cleanup() {
    }
}
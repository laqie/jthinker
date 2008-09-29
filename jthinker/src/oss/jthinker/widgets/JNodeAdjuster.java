package oss.jthinker.widgets;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import oss.jthinker.util.MixedQuadripoleTrigger;
import oss.jthinker.util.QuadripoleTrigger;
import oss.jthinker.util.Mixer;
import oss.jthinker.util.MixerInvertor;

/**
 * Helper class for adjusting coordinates of lines to make it align
 * properly to custom node borders.
 * 
 * @author iappel
 */
public class JNodeAdjuster {
    /**
     * Creates a new {@link QuadripoleTrigger} that recalculates positions
     * of the nodes into coordinates of line edges.
     * 
     * @param a node to be used as a beginning of the line
     * @param b node to be used as a end of the line
     * @return a new {@link QuadripoleTrigger} that correctly recalculates
     * positions
     */
    public static QuadripoleTrigger<Point> makeTrigger(JNode a, JNode b) {
        ComponentTrigger cta = new ComponentTrigger(a);
        ComponentTrigger ctb = new ComponentTrigger(b);
        Mixer<Point> mixa = makeMixer(a);
        Mixer<Point> mixb = makeMixer(b);

        MixerInvertor<Point> invmixb = new MixerInvertor<Point>(mixb);
        return new MixedQuadripoleTrigger<Point>(cta, ctb, mixa, invmixb);
    }

    private static abstract class ComponentMixer implements Mixer<Point> {
        private final Component component;

        public ComponentMixer(Component component) {
            this.component = component;
        }

        protected abstract Point compute(Rectangle bounds, Point outer);

        public Point mix(Point first, Point second) {
            return compute(component.getBounds(), second);
        }
    }

    private static Mixer<Point> makeMixer(JNode node) {
        switch (node.getNodeSpec().getBorderType()) {
            case ELLIPSE:
                return new ComponentMixer(node) {
                    @Override
                    protected Point compute(Rectangle bounds, Point outer) {
                        return GeometryUtils.ellipseIntersection(
                                bounds, outer);
                    }
                };
            case ROUND_RECT:
                return new ComponentMixer(node) {
                    @Override
                    protected Point compute(Rectangle bounds, Point outer) {
                        return GeometryUtils.roundRectangleIntersection(
                                bounds, outer);
                    }
                };
            case SHARP_RECT:
            case HEXAGON:
               return new ComponentMixer(node) {
                    @Override
                    protected Point compute(Rectangle bounds, Point outer) {
                        return GeometryUtils.rectangleIntersection(
                                bounds, outer);
                    }
                };
            default:
                throw new RuntimeException();
        }
    }
}

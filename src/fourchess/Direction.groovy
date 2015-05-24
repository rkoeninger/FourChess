package fourchess
import java.awt.Point

enum Direction {
	E( 1,  0),
    N( 0, -1),
    W(-1,  0),
    S( 0,  1)

    private Direction(dx, dy) {
        this.dx = dx
        this.dy = dy
    }

    final int dx
    final int dy

	Direction getOpposite() {
        switch (this) {
            case E: return W
            case N: return S
            case W: return E
            case S: return N
            default: throw new IllegalArgumentException("Undefined Direction")
        }
    }

    Direction getLeft() {
        switch (this) {
            case E: return N
            case N: return W
            case W: return S
            case S: return E
            default: throw new IllegalArgumentException("Undefined Direction")
        }
    }

    Direction getRight() {
        switch (this) {
            case E: return S
            case N: return E
            case W: return N
            case S: return W
            default: throw new IllegalArgumentException("Undefined Direction")
        }
    }
	
	Point shiftForward(Point p) { new Point(p.x.intValue() + dx, p.y.intValue() + dy) }
	
	Point shiftForwardLeft(Point p) { left.shiftForward(shiftForward(p)) }
	
	Point shiftForwardRight(Point p) { right.shiftForward(shiftForward(p)) }
}

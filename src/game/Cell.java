package game;

/**
 * Kelas yang merepresentasikan satu sel pada papan permainan. Tujuan dibuat
 * kelas baru (dan tidak langsung menggunakan integer saja) adalah untuk
 * memungkinan pass by reference terhadap sel di papan.
 *
 * @author Jiang Han
 */
public class Cell {

    /**
     * Nilai dari tile pada cell ini. Nilai 0 berarti cell ini kosong
     */
    int value;

    Cell() {
    }

    Cell(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        return this.value == other.value;
    }
}

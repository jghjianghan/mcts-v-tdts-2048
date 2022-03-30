package model;

/**
 * Represents a single cell in a board
 * @author Jiang Han
 */
public class Cell {
    int value;
    Cell(){}
    Cell(int value){this.value = value;}

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
        if (this.value != other.value) {
            return false;
        }
        return true;
    }
    
}

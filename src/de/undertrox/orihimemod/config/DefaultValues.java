package de.undertrox.orihimemod.config;

public class DefaultValues {
    public int undoSteps = 50;
    public int foldedModelUndoSteps = 20;
    public int auxLineUndoSteps = 50;
    public int lineThickness = 1;
    public int auxLineThickness = 1;
    public int pointSize = 1;
    public double gridAngle = 90;
    public boolean showHelp = true;
    public int gridSize = 8;
    public int gridDivSize = 4;
    public GridMode gridMode  = GridMode.GRID_ON_SQUARE;
    public boolean gridAssist = false;
    public boolean antialiasing = false;
    public boolean foldedModelAntiAliasing = false;

    public enum GridMode {
        NO_GRID(0), GRID_ON_SQUARE(1), GRID_EVERYWHERE(2);
        int id;

        GridMode(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static GridMode fromId(int id) {
            switch (id) {
                case 1: return GRID_ON_SQUARE;
                case 2: return GRID_EVERYWHERE;
                case 0:
                default:
                    return NO_GRID;
            }
        }
    }
}

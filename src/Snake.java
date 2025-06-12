package src;

public class Snake {
    // Determine the snake's position
    public int[] snakexLength = new int[750];
    public int[] snakeyLength = new int[750];

    // Snake's length and whether it has moved or not
    int lengthOfSnake;
    int moves;

    // Direction of the snake
    boolean left;
    boolean right;
    boolean up;
    boolean down;

    // Whether the snake is dead or not
    boolean death;

    // Constructor
    public Snake(){
        this.left = false;
        this.right = false;
        this.up = false;
        this.down = false;
        this.death = false;
        this.lengthOfSnake = 5;
        this.moves = 0;
    }

    // Move to the right
    public void moveRight(){
        if (this.moves != 0 && !this.death) {
            this.moves++;
            if (!this.left) {
                this.right = true;
            } else {
                this.right = false;
                this.left = true;
            }
            this.up = false;
            this.down = false;
        }
    }

    // Move to the left
    public void moveLeft(){
        if (this.moves != 0 && !this.death) {
            this.moves++;
            if (!this.right) {
                this.left = true;
            } else {
                this.left = false;
                this.right = true;
            }
            this.up = false;
            this.down = false;
        }
    }

    // Move up
    public void moveUp(){
        if (this.moves != 0 && !this.death) {
            this.moves++;
            if (!this.down) {
                this.up = true;
            } else {
                this.up = false;
                this.down = true;
            }
            this.left = false;
            this.right = false;
        }
    }

    // Move down
    public void moveDown(){
        if (this.moves != 0 && !this.death) {
            this.moves++;
            if (!this.up) {
                this.down = true;
            } else {
                this.down = false;
                this.up = true;
            }
            this.left = false;
            this.right = false;
        }
    }

    // Function to handle snake's death, to avoid repeating the code
    public void dead() {
        // Make the snake unable to move
        this.right = false;
        this.left = false;
        this.up = false;
        this.down = false;
        this.death = true;
    }

    // Snake's movement to the right
    public void movementRight(){
        // Move the head to the next index
        for (int i = this.lengthOfSnake - 1; i >= 0; i--) {
            // Move the snakeyLength position
            this.snakeyLength[i + 1] = this.snakeyLength[i];
        }
        for (int i = this.lengthOfSnake - 1; i >= 0; i--) {
            // Move the snakexLength position
            if (i == 0) {
                this.snakexLength[i] = this.snakexLength[i] + 6;
            } else {
                this.snakexLength[i] = this.snakexLength[i - 1];
            }
            // If the snake has passed the right edge
            if (this.snakexLength[0] > 637) {
                // Move the head back inside the board
                this.snakexLength[0] -= 6;
                // Snake dies
                dead();
            }
        }
    }

    // Snake's movement to the left
    public void movementLeft(){
        // Move the head to the next index
        for (int i = this.lengthOfSnake - 1; i >= 0; i--) {
            // Move the snakeyLength position
            this.snakeyLength[i + 1] = this.snakeyLength[i];
        }
        for (int i = this.lengthOfSnake - 1; i >= 0; i--) {
            // Move the snakexLength position
            if (i == 0) {
                this.snakexLength[i] = this.snakexLength[i] - 6;
            } else {
                this.snakexLength[i] = this.snakexLength[i - 1];
            }
            // If the snake has passed the left edge
            if (this.snakexLength[0] < 25) {
                // Move the head back inside the board
                this.snakexLength[0] += 6;
                // Snake dies
                dead();
            }
        }
    }

    // Snake's movement upward
    public void movementUp(){
        // Move the head to the next index
        for (int i = this.lengthOfSnake - 1; i >= 0; i--) {
            // Move the snakexLength position
            this.snakexLength[i + 1] = this.snakexLength[i];
        }
        for (int i = this.lengthOfSnake - 1; i >= 0; i--) {
            // Move the snakeyLength position
            if (i == 0) {
                this.snakeyLength[i] = this.snakeyLength[i] - 6;
            } else {
                this.snakeyLength[i] = this.snakeyLength[i - 1];
            }
            // If the snake has passed the top edge
            if (this.snakeyLength[0] < 73) {
                // Move the head back inside the board
                this.snakeyLength[0] += 6;
                // Snake dies
                dead();
            }
        }
    }

    // Snake's movement downward
    public void movementDown(){
        // Move the head to the next index
        for (int i = this.lengthOfSnake - 1; i >= 0; i--) {
            // Move the snakexLength position
            this.snakexLength[i + 1] = this.snakexLength[i];
        }
        for (int i = this.lengthOfSnake - 1; i >= 0; i--) {
            // Move the snakeyLength position
            if (i == 0) {
                this.snakeyLength[i] = this.snakeyLength[i] + 6;
            } else {
                this.snakeyLength[i] = this.snakeyLength[i - 1];
            }
            // If the snake has passed the bottom edge
            if (this.snakeyLength[0] > 679) {
                // Move the head back inside the board
                this.snakeyLength[0] -= 6;
                // Snake dies
                dead();
            }
        }
    }
}

class Bomb extends Bullet {

  Bomb(float x, float y, float speedY)
  {
    super(x, y, speedY);
  }

  void draw() //bomb graphics
  {
    stroke(0);
    fill(255);
    rect(x, y, 5, 10);
  }

  void move() 
  {
    y = y + speedY;
    if (this.y > height || this.y < 0) // if bullet outside of screen remove and stop
    { 
      bombs.remove(this);
      this.speedY = 0;
    }
  }

  void update() // When called in the main game class this method performs the other methods within this class
  {
    move();
    draw();
  }
}


package engine;

public final class Interval {
  private double min;
  private double max;
  public Interval(double min, double max){
    this.min = min;
    this.max = max;
  }

  public boolean overlap(Interval other){
    return (this.min <= other.max && other.min <= this.max);
  }

  public boolean overlap(double val){
    return(this.min <= val && this.max >= val);
  }

  public double getMax(){return this.max;}

  public double getMin(){return this.min;}


}

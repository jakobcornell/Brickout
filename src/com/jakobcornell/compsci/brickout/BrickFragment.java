package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public final class BrickFragment {
  private static Random random = new Random();
  
  private double x, y;
  private final double xv, yv;
  
  private final Shape clip;
  
  private int longevity = 45 + random.nextInt(50);
  public boolean broken = false;
  
  private BrickFragment(final double x, final double y, final double xv, final double yv, final Shape clip) {
    this.x=x;
    this.y=y;
    this.xv=xv;
    this.yv=yv;
    this.clip = clip;
  }
  
  public static Collection<BrickFragment> explode(final Brick b, final double ix, final double iy) {
    final double[][] xbreak = new double[2][5], ybreak = new double[2][3];
    xbreak[0][0] = xbreak[1][0] = 0;
    xbreak[0][4] = xbreak[1][4] = 4;
    for(int x=1;x<=3;x++) {
      xbreak[0][x] = -0.5+random.nextDouble()+x;
      xbreak[1][x] = -0.5+random.nextDouble()+x;
    }
    ybreak[0][0] = ybreak[1][0] = 0;
    ybreak[0][2] = ybreak[1][2] = 2;
    ybreak[0][1] = 0.5+random.nextDouble();
    ybreak[1][1] = 0.5+random.nextDouble();
    final Area[] vregions = new Area[4];
    for(int x=0;x<=3;x++) {
      final Path2D.Double path = new Path2D.Double();
      path.moveTo(xbreak[0][x], 0);
      path.lineTo(xbreak[1][x], 2);
      path.lineTo(xbreak[1][x+1], 2);
      path.lineTo(xbreak[0][x+1], 0);
      vregions[x] = new Area(path);
    }
    final Path2D.Double[] hregions = new Path2D.Double[2];
    for(int y=0;y<=1;y++) {
      final Path2D.Double path = new Path2D.Double();
      path.moveTo(0, ybreak[0][y]);
      path.lineTo(4, ybreak[1][y]);
      path.lineTo(4, ybreak[1][y+1]);
      path.lineTo(0, ybreak[0][y+1]);
      hregions[y] = path;
    }
    final BrickFragment[] fragments = new BrickFragment[4*2];
    for(int x=0;x<=3;x++) for(int y=0;y<=1;y++) {
      final Area a = new Area(hregions[y]);
      a.intersect(vregions[x]);
      fragments[4*y+x] = new BrickFragment(b.x, b.y, 0.05*(ix+x-2.0+(random.nextDouble()*2-1)), 0.05*(iy+y-1.0+(random.nextDouble()*2-1)), a);
    }
    return Arrays.asList(fragments);
  }
  
  public void tick() {
    x += xv;
    y += yv;
    if(longevity > 0)
      longevity--;
    else
      broken = true;
  }
  
  public void paint(Graphics2D g) {
    g = (Graphics2D) g.create();
    g.translate(x, y);
    final AffineTransform t = AffineTransform.getScaleInstance(4.0/Brick.image.getWidth(), 2.0/Brick.image.getHeight());
    final Shape oldClip = g.getClip();
    g.clip(clip);
    g.drawImage(Brick.image, t, null);
    g.setClip(oldClip);
  }
}

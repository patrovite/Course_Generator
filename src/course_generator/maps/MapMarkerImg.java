/*
 * Course Generator
 * Copyright (C) 2016 Pierre Delore
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package course_generator.maps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapObjectImpl;
import static org.openstreetmap.gui.jmapviewer.MapObjectImpl.getDefaultFont;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 *
 * @author pierre.delore
 */

public class MapMarkerImg extends MapObjectImpl implements MapMarker {

    Coordinate coord;
    MapMarker.STYLE markerStyle;
    Image img;

    public MapMarkerImg(Coordinate coord, Image img) {
        this(null, null, coord, img);
    }
    
    public MapMarkerImg(Layer layer, String name, Coordinate coord, Image img) {
        this(layer, name, coord, img, MapMarker.STYLE.VARIABLE, getDefaultStyle());
    }
    
    public MapMarkerImg(Layer layer, String name, Coordinate coord, Image img, MapMarker.STYLE markerStyle, Style style) {
        super(layer, name, style);
        this.markerStyle = markerStyle;
        this.coord = coord;
        this.img=img;
    }

    public Coordinate getCoordinate(){
        return coord;
    }
    public double getLat() {
        return coord.getLat();
    }

    public double getLon() {
        return coord.getLon();
    }

    public Image getImg() {
        return img;
    }
    
    public MapMarker.STYLE getMarkerStyle() {
        return markerStyle;
    }

    public double getRadius() {
        return 0;
    }
    
    public void paint(Graphics g, Point position, int radio) {
        //int size_h = radio;
        //int size = img. size_h * 2;
        
        
        if (g instanceof Graphics2D && getBackColor()!=null) {
            Graphics2D g2 = (Graphics2D) g;
            Composite oldComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2.setPaint(getBackColor());
            g.drawImage(img, position.x-img.getWidth(null)/2, position.y-img.getHeight(null)/2, null);
            //g.fillOval(position.x - size_h, position.y - size_h, size, size);
            g2.setComposite(oldComposite);
        }
        g.setColor(getColor());
        g.drawImage(img, position.x-img.getWidth(null)/2, position.y-img.getHeight(null)/2, null);
        //g.drawOval(position.x - size_h, position.y - size_h, size, size);

        if(getLayer()==null||getLayer().isVisibleTexts()) paintText(g, position);
    }

    public static Style getDefaultStyle(){
        return new Style(Color.ORANGE, new Color(200,200,200,200), null, getDefaultFont());
    }
    @Override
    public String toString() {
        return "MapMarkerImg at " + getLat() + " " + getLon();
    }
    
    @Override
    public void setLat(double lat) {
        if(coord==null) coord = new Coordinate(lat,0);
        else coord.setLat(lat);
    }
    
    @Override
    public void setLon(double lon) {
        if(coord==null) coord = new Coordinate(0,lon);
        else coord.setLon(lon);
    }
    
    public void setImg(Image img) {
        this.img=img;
    }
}

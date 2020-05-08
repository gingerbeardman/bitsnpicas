package com.kreative.bitsnpicas.exporter;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import com.kreative.bitsnpicas.BitmapFont;
import com.kreative.bitsnpicas.BitmapFontExporter;
import com.kreative.bitsnpicas.BitmapFontGlyph;

public class PlaydateBitmapFontExporter implements BitmapFontExporter {
	public byte[] exportFontToBytes(BitmapFont font) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		RenderedImage r = exportFontToImage(font);
		ImageIO.write(r, "png", b);
		return b.toByteArray();
	}

	public void exportFontToStream(BitmapFont font, OutputStream os) throws IOException {
		RenderedImage r = exportFontToImage(font);
		ImageIO.write(r, "png", os);
	}

	public void exportFontToFile(BitmapFont font, File file) throws IOException {
		RenderedImage ri = exportFontToImage(font);
		ImageIO.write(ri, "png", file);
	}
	
	public RenderedImage exportFontToImage(BitmapFont font) {
		BitmapFontGlyph[] g = new BitmapFontGlyph[95];
		int[] w = new int[95];
		int maxw = 0;
		for (int i = 0; i < 95; i++) {
			g[i] = font.getCharacter(i+32);
			if (g[i] == null) g[i] = new BitmapFontGlyph(new byte[1][1], 0, 1, 1);
			w[i] = g[i].getCharacterWidth();
			if (w[i] < 1) w[i] = 1;
			maxw = (w[i] > maxw) ? w[i] : maxw;
		}
		int width = 0;
		for (int i = 0; i < 95; i++) {
			width += maxw;
		}
		int height = 1+font.getLineAscent()+font.getLineDescent()+font.getLineGap();
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0, i = 0; i < 95; x += maxw, i++) {
			byte[][] gg = g[i].getGlyph();
			for (int gy = 0, iy = 1+font.getLineAscent()-g[i].getGlyphAscent(); gy < gg.length; gy++, iy++) {
				if (iy > 0 && iy < height) {
					for (int gx = 0, ix = x+g[i].getGlyphOffset(); gx < gg[gy].length; gx++, ix++) {
						bi.setRGB(ix, iy, ((gg[gy][gx] & 0xFF) << 24) | 0x000000);
					}
				}
			}
		}
		return bi;
	}
}

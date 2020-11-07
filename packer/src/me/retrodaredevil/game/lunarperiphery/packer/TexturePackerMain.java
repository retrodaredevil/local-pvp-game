package me.retrodaredevil.game.lunarperiphery.packer;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;

public class TexturePackerMain {

	public static void main(String[] args) {
		System.out.println("Working directory: " + new File(".").getAbsolutePath());
		TexturePacker.process("assetsraw/textures", "assets/skins/main", "skin");
	}
}

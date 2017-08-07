package elucent.gravelores.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import elucent.gravelores.ConfigManager;
import elucent.gravelores.GravelOres;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenGravelOres implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.getDimensionType() == DimensionType.OVERWORLD){
			if (random.nextInt(ConfigManager.orePileChance) == 0){
				IBlockState ore = GravelOres.spawns.get(random.nextInt(GravelOres.spawns.size())).getDefaultState();
				int xx = chunkX*16 + 8 + random.nextInt(16);
				int zz = chunkZ*16 + 8 + random.nextInt(16);
				BlockPos top = world.getTopSolidOrLiquidBlock(new BlockPos(xx,64,zz));
				IBlockState state = world.getBlockState(top);
				if (state.getBlock() != Blocks.WATER){
					if (state.getBlock() == Blocks.AIR || state.getBlock().isReplaceable(world, top)){
						int tries = ConfigManager.orePileMinSize+random.nextInt(ConfigManager.orePileMaxSize-ConfigManager.orePileMinSize);
						List<BlockPos> blocks = new ArrayList<BlockPos>();
						blocks.add(top);
						for (int i = 0; i < tries; i ++){
							BlockPos pos = blocks.get(random.nextInt(blocks.size()));
							EnumFacing face = EnumFacing.getFront(random.nextInt(6));
							BlockPos replace = pos.offset(face);
							IBlockState state2 = world.getBlockState(replace);
							if (state2.getBlock() == Blocks.AIR || state2.getBlock().isReplaceable(world, replace)){
								blocks.add(replace);
							}
						}
						for (int i = 0; i < blocks.size(); i ++){
							world.setBlockState(blocks.get(i), ore);
						}
					}
				}
			}
		}
	}

}

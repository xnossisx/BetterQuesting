package betterquesting.client.gui.rewards;

import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import betterquesting.client.gui.GuiQuesting;
import betterquesting.client.gui.misc.GuiEmbedded;
import betterquesting.client.themes.ThemeRegistry;
import betterquesting.quests.rewards.RewardItem;
import betterquesting.utils.BigItemStack;
import betterquesting.utils.RenderUtils;

public class GuiRewardItem extends GuiEmbedded
{
	RewardItem reward;
	int scroll = 0;
	
	public GuiRewardItem(RewardItem reward, GuiQuesting screen, int posX, int posY, int sizeX, int sizeY)
	{
		super(screen, posX, posY, sizeX, sizeY);
		this.reward = reward;
	}

	@Override
	public void drawTask(int mx, int my, float partialTick)
	{
		int rowLMax = (sizeX - 40)/18;
		int rowL = Math.min(reward.items.size(), rowLMax);
		
		if(rowLMax < reward.items.size())
		{
			scroll = MathHelper.clamp_int(scroll, 0, reward.items.size() - rowLMax);
			RenderUtils.DrawFakeButton(screen, posX, posY, 20, 20, "<", screen.isWithin(mx, my, posX, posY, 20, 20, false)? 2 : 1);
			RenderUtils.DrawFakeButton(screen, posX + 20 + 18*rowL, posY, 20, 20, ">", screen.isWithin(mx, my, posX + 20 + 18*rowL, posY, 20, 20, false)? 2 : 1);
		} else
		{
			scroll = 0;
		}
		
		BigItemStack ttStack = null; // Reset
		
		for(int i = 0; i < rowL; i++)
		{
			BigItemStack stack = reward.items.get(i + scroll);
			screen.mc.renderEngine.bindTexture(ThemeRegistry.curTheme().guiTexture());
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			screen.drawTexturedModalRect(posX + (i * 18) + 20, posY + 1, 0, 48, 18, 18);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderUtils.RenderItemStack(screen.mc, stack.getBaseStack(), posX + (i * 18) + 21, posY + 2, stack != null && stack.stackSize > 1? "" + stack.stackSize : "");
			
			if(screen.isWithin(mx, my, posX + (i * 18) + 20, posY + 1, 16, 16, false))
			{
				ttStack = stack;
			}
		}
		
		if(ttStack != null)
		{
			screen.DrawTooltip(ttStack.getBaseStack().getTooltip(screen.mc.thePlayer, screen.mc.gameSettings.advancedItemTooltips), mx, my);
		}
	}
	
	@Override
	public void mouseClick(int mx, int my, int button)
	{
		if(button != 0)
		{
			return;
		}
		
		int rowLMax = (sizeX - 40)/18;
		int rowL = Math.min(reward.items.size(), rowLMax);
		
		if(screen.isWithin(mx, my, posX, posY, 20, 20, false))
		{
			scroll = MathHelper.clamp_int(scroll - 1, 0, reward.items.size() - rowLMax);
		} else if(screen.isWithin(mx, my, posX + 20 + 18*rowL, posY, 20, 20, false))
		{
			scroll = MathHelper.clamp_int(scroll + 1, 0, reward.items.size() - rowLMax);
		}
	}
}

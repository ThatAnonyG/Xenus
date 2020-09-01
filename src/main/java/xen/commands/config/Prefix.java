package xen.commands.config;

import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import xen.lib.command.Command;
import xen.lib.command.CommandContext;
import xen.lib.mongodb.guild.GuildModel;
import xen.lib.utils.Utils;

public class Prefix extends Command {
  private GuildModel guildModel;

  public Prefix() {
    super("prefix");
    setCategory(Categories.CONFIG);
    setDescription("Lets you to change the bot prefix.");
    setUsage("<New Value>");
    setPerms(new Permission[]{Permission.ADMINISTRATOR});
  }

  @Override
  public void run(@NotNull CommandContext ctx) {
    guildModel = (GuildModel) ctx
            .getClient()
            .getDbManager()
            .find(ctx.getEvent().getGuild());

    String prefix = ctx.getArgs().get(0);
    if (prefix.length() > 5) {
      Utils.sendEm(
              ctx.getEvent().getChannel(),
              ctx.getClient().getCross() + " Prefix must have less than 6 letters!",
              Utils.Embeds.ERROR
      ).queue();
      return;
    }

    guildModel.setPrefix(prefix);
    guildModel = (GuildModel) ctx.getClient().getDbManager().save(guildModel);

    Utils.sendConfigLog(
            ctx.getEvent(),
            guildModel,
            "Prefix Changed",
            "Prefix changed to `" + prefix + "`"
    );

    Utils.sendEm(
            ctx.getEvent().getChannel(),
            ctx.getClient().getTick() +
                    " Prefix has been changed to `" +
                    guildModel.getPrefix() +
                    "`!",
            Utils.Embeds.SUCCESS
    ).queue();
  }
}
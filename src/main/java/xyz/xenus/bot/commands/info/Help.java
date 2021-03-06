package xyz.xenus.bot.commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.xenus.lib.Utils;
import xyz.xenus.lib.command.Command;
import xyz.xenus.lib.command.CommandContext;
import xyz.xenus.lib.mongodb.guild.GuildModel;

import java.util.Arrays;

public class Help extends Command {
    public Help() {
        super("help");
        setAliases(new String[]{"h"});
        setCd(5000);
        setDescription("Shows you help and info for a command.");
        setUsage("[Command Name]");
    }

    @Override
    public void run(@NotNull CommandContext ctx) {
        GuildModel guildModel = ctx.getClient().getDbManager().find(
                ctx.getEvent().getGuild()
        );

        if (ctx.getArgs().isEmpty()) {
            EmbedBuilder embed = Utils.embed()
                    .setTitle(ctx.getEvent().getJDA().getSelfUser().getName() + " Help")
                    .setColor(Utils.getHex())
                    .setDescription(
                            "The bot prefix for **" + ctx.getEvent().getGuild().getName() +
                                    "** is `" + guildModel.getPrefix() + "`"
                    )
                    .addField(
                            "Commands List",
                            "[View All Commands](https://xenus.xyz/commands/config)",
                            false
                    )
                    .addField(
                            "Other Links",
                            String.join(
                                    "\n",
                                    "[Invite Xenus](https://xenuz.xyz/invite)",
                                    "[Support Server](https://xenuz.xyz/discord)",
                                    "[Donate via PayPal](https://paypal.me/ratul003)"
                            ),
                            false
                    );

            ctx.getEvent().getAuthor().openPrivateChannel().queue(
                    (c) -> c.sendMessage(embed.build()).queue(
                            (m) -> Utils.sendEm(
                                    ctx.getEvent().getChannel(),
                                    ctx.getClient().getTick() + " Please check your DMs!",
                                    Utils.Embeds.SUCCESS
                            ).queue(),
                            (e) -> ctx.getEvent().getChannel().sendMessage(embed.build()).queue()
                    )
            );
            return;
        }

        String name = ctx.getArgs().get(0).toLowerCase();
        Command cmd = ctx.getClient().getCommand(name);
        if (cmd == null || cmd.getCategory().equals(Categories.DEV)) {
            Utils.sendEm(
                    ctx.getEvent().getChannel(),
                    ctx.getClient().getCross() + " No command found with name: **" + name + "**",
                    Utils.Embeds.ERROR
            ).queue();
            return;
        }

        String builder = "**Name:** " + (cmd.getName().substring(0, 1).toUpperCase() + cmd.getName().substring(1)) +
                "\n**Description:** " + cmd.getDescription();
        if (cmd.getAliases().length > 0)
            builder += "\n**Aliases:** " + String.join(
                    " | ",
                    Arrays.stream(cmd.getAliases())
                            .map((a) -> "`" + a + "`")
                            .toArray(String[]::new)
            );
        if (cmd.getCd() > 0) builder += "\n**Cool Down:** " + cmd.getCd() / 1000 + " seconds(s)";

        String perms = String.join(
                "\n",
                "**User Perms:** " +
                        (cmd.getPerms().length > 0 ?
                                String.join(" | ",
                                        Arrays.stream(cmd.getPerms())
                                                .map((p) -> "`" + p.getName() + "`")
                                                .toArray(String[]::new)
                                )
                                :
                                "`None`"),
                "**Bot Perms:** " +
                        (cmd.getBotPerms().length > 0 ?
                                String.join(" | ",
                                        Arrays.stream(cmd.getBotPerms())
                                                .map((p) -> "`" + p.getName() + "`")
                                                .toArray(String[]::new)
                                )
                                :
                                "`None`")
        );

        EmbedBuilder embed = Utils.embed()
                .setTitle("Command Help and Info")
                .setColor(Utils.getHex())
                .addField("General Info", builder.trim(), false)
                .addField("Permissions Needed", perms.trim(), false)
                .addField("Usage", "```" + cmd.getUsage() + "```", false)
                .setFooter(
                        "Syntax: <> = Required | [] = Optional",
                        ctx.getEvent().getJDA().getSelfUser().getAvatarUrl()
                );

        ctx.getEvent().getChannel().sendMessage(embed.build()).queue();
    }
}

package xyz.xenus.commands.dev;

import net.dv8tion.jda.api.entities.Member;
import xyz.xenus.lib.Utils;
import xyz.xenus.lib.command.Command;
import xyz.xenus.lib.command.CommandContext;
import xyz.xenus.lib.mongodb.user.UserModel;

import java.util.Optional;

// TODO - Fix the getMember function

public class Blacklist extends Command {
  public Blacklist() {
    super("blacklist");
    setCategory(Categories.DEV);
  }

  @Override
  public void run(CommandContext ctx) {
    UserModel authorDB = (UserModel) ctx.getClient().getDbManager().find(ctx.getEvent().getAuthor());
    if (
            !authorDB.getBadges().isDeveloper() &&
                    !ctx.getEvent().getAuthor().getId().equals("543452691863437312")
    )
      return;

    Optional<Member> optionalMember = Utils.getMember(ctx.getEvent().getMessage(), ctx.getArgs());
    if (optionalMember.isEmpty()) {
      Utils.sendEm(
              ctx.getEvent().getChannel(),
              ctx.getClient().getCross() + " No user found with the given info!",
              Utils.Embeds.ERROR
      ).queue();
      return;
    }

    Member member = optionalMember.get();
    UserModel userModel = (UserModel) ctx.getClient().getDbManager().find(member.getUser());
    userModel.getBadges().setBlacklisted(!userModel.getBadges().isBlacklisted());
    userModel = (UserModel) ctx.getClient().getDbManager().save(userModel);

    String msg = userModel.getBadges().isBlacklisted() ?
            " has been blacklisted!" :
            " has been whitelisted!";
    Utils.sendEm(
            ctx.getEvent().getChannel(),
            ctx.getClient().getTick() + member.getAsMention() + msg,
            Utils.Embeds.SUCCESS
    ).queue();
  }
}

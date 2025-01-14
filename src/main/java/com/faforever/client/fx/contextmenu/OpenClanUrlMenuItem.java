package com.faforever.client.fx.contextmenu;

import com.faforever.client.clan.ClanService;
import com.faforever.client.domain.ClanBean;
import com.faforever.client.domain.PlayerBean;
import com.faforever.client.fx.PlatformService;
import com.faforever.client.i18n.I18n;
import com.faforever.client.util.Assert;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class OpenClanUrlMenuItem extends AbstractMenuItem<PlayerBean> {

  private final I18n i18n;
  private final PlatformService platformService;
  private final ClanService clanService;

  @Override
  protected void onClicked() {
    Assert.checkNullIllegalState(object, "no player has been set");

    clanService.getClanByTag(object.getClan())
        .thenAccept(possibleClan -> possibleClan.map(ClanBean::getWebsiteUrl).ifPresent(platformService::showDocument));
  }

  @Override
  protected boolean isDisplayed() {
    return object != null && StringUtils.isNotBlank(object.getClan());
  }

  @Override
  protected String getItemText() {
    return i18n.get("clan.visitPage");
  }
}
